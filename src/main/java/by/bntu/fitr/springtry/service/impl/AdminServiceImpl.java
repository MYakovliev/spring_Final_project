package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.entity.Bid;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.Status;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.repository.BidRepository;
import by.bntu.fitr.springtry.repository.LotRepository;
import by.bntu.fitr.springtry.repository.UserRepository;
import by.bntu.fitr.springtry.service.AdminService;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.util.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private BidRepository bidRepository;

    @Override
    public User ban(long userId) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        user.setBanned(true);
        return userRepository.save(user);
    }

    @Override
    public User unban(long userId) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ServiceException(ErrorMessage.UNKNOWN_USER)
        );
        user.setBanned(false);
        return userRepository.save(user);
    }

    @Override
    public Lot submitWinner(Lot lot) throws ServiceException {
        List<Bid> bidHistory = bidRepository.findByIdLot(lot.getId());
        Date finishTime = lot.getFinishTime();
        if (!(finishTime.before(new Date()) || (bidHistory == null || bidHistory.isEmpty()))){
            throw new ServiceException(ErrorMessage.UNABLE_TO_SUBMIT);
        }
        bidHistory.forEach(bid -> {if (bid.getStatus()== Status.WINING) bid.setStatus(Status.WON);});
        lot.setBidHistory(bidHistory);
        bidRepository.saveAll(bidHistory);
        return lotRepository.save(lot);
    }
}
