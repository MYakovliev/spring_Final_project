package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.entity.*;
import by.bntu.fitr.springtry.repository.LotRepository;
import by.bntu.fitr.springtry.repository.UserRepository;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.service.UserService;
import by.bntu.fitr.springtry.util.ErrorMessage;
import by.bntu.fitr.springtry.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String ANY_SQL_SYMBOL = "%";
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LotRepository lotRepository;

    @Override
    public User login(String login, String password) throws ServiceException {
        if (!(UserValidator.isValidLogin(login) && UserValidator.isValidPassword(password))) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        User userByLogin = userRepository.findByLogin(login).orElseThrow(
                () ->
                        new ServiceException(ErrorMessage.INVALID_LOGIN_OR_PASSWORD)

        );
        String encodedPassword = userByLogin.getPassword();
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        if (!matches) {
            throw new ServiceException(ErrorMessage.INVALID_LOGIN_OR_PASSWORD);
        }
        return userByLogin;
    }

    @Override
    public User register(String name, String mail, String login, String password, UserRole role) throws ServiceException {
        if (!(UserValidator.isValidLogin(login) && UserValidator.isValidPassword(password)
                && UserValidator.isValidMail(mail) && UserValidator.isValidName(name) &&
                (role == UserRole.BUYER || role == UserRole.SELLER))
                ) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        password = passwordEncoder.encode(password);
        User user = new User(0, name, mail, new BigDecimal("0.00"), role, null, false);
        if (isTaken(login)) {
            throw new ServiceException(ErrorMessage.INVALID_LOGIN_OR_PASSWORD);
        }
        user.setLogin(login);
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public User findUserById(long id) throws ServiceException {
        return userRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
    }

    @Override
    public User changeUserData(long userId, String avatar, String name, String mail) throws ServiceException {
        if (!(UserValidator.isValidMail(mail) && UserValidator.isValidName(name))) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        user.setAvatar(avatar);
        user.setName(name);
        user.setMail(mail);
        return userRepository.save(user);
    }

    @Override
    public User changeUserPassword(long userId, String oldPassword, String newPassword) throws ServiceException {
        if (!(UserValidator.isValidPassword(oldPassword) && UserValidator.isValidPassword(newPassword))) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        String oldEncodedPassword = user.getPassword();
        boolean matches = passwordEncoder.matches(oldPassword, oldEncodedPassword);
        if (!matches) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public User addBalance(long userId, String payment) throws ServiceException {
        if (!UserValidator.isValidBid(payment)) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        BigDecimal paymentDecimal = new BigDecimal(payment);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        BigDecimal balance = user.getBalance();
        user.setBalance(balance.add(paymentDecimal));
        return userRepository.save(user);
    }

    @Override
    public List<User> findUserByName(String name, int pageNumber, int amountPerPage) throws ServiceException {
        if (pageNumber < 1 || amountPerPage < 1) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        return userRepository.findByNameLike(ANY_SQL_SYMBOL + name + ANY_SQL_SYMBOL,
                PageRequest.of(pageNumber - 1, amountPerPage));
    }


    @Override
    public List<User> findAll(int pageNumber, int amountPerPage) throws ServiceException {
        if (pageNumber < 1 || amountPerPage < 1) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        return userRepository.findAll(PageRequest.of(pageNumber - 1, amountPerPage)).getContent();
    }

    private boolean isTaken(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public Lot makeBid(User buyer, String stringBid, Lot lot) throws ServiceException {
        if (!lot.getFinishTime().after(new Date())) {
            throw new ServiceException(ErrorMessage.AUCTION_IS_CLOSED);
        }
        if (!UserValidator.isValidBid(stringBid)) {
            throw new ServiceException(ErrorMessage.INCORRECT_BID);
        }
        BigDecimal bid = new BigDecimal(stringBid);
        if (!(lot.getCurrentCost().compareTo(bid) < 0)) {
            throw new ServiceException(ErrorMessage.SMALL_BID);
        }
        if (!(buyer.getBalance().compareTo(bid) > 0)) {
            throw new ServiceException(ErrorMessage.NOT_ENOUGH_MONEY);
        }
        List<Bid> bidHistory = lot.getBidHistory();
        for (Bid bid1 : bidHistory) {
            if (bid1.getStatus() == Status.WINING) {
                bid1.setStatus(Status.LOSE);
                User buyer1 = bid1.getBuyer();
                buyer1.setBalance(buyer1.getBalance().add(bid1.getBid()));
                userRepository.save(buyer1);
                break;
            }
        }
        Bid newBid = new Bid(0, buyer, bid, Status.WINING);
        bidHistory.add(newBid);
        return lotRepository.save(lot);
    }


}
