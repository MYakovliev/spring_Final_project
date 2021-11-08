package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.entity.Bid;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.Status;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.repository.LotRepository;
import by.bntu.fitr.springtry.repository.UserRepository;
import by.bntu.fitr.springtry.service.AdminService;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.util.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LotRepository lotRepository;

    @Override
    public void ban(long userId) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        user.setBanned(true);
        userRepository.save(user);
    }

    @Override
    public void unban(long userId) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        user.setBanned(false);
        userRepository.save(user);
    }

    @Override
    public void submitWinner(Lot lot) throws ServiceException {
        List<Bid> bidHistory = lot.getBidHistory();
        Date finishTime = lot.getFinishTime();
        if (!finishTime.after(new Date())){
            throw new ServiceException(ErrorMessage.UNABLE_TO_SUBMIT);
        }
        bidHistory.forEach(bid -> {if (bid.getStatus()== Status.WINING) bid.setStatus(Status.WON);});
        lot.setBidHistory(bidHistory);
        lotRepository.save(lot);
    }
}
