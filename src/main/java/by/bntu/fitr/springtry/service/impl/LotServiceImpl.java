package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.entity.Bid;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.Status;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.repository.BidRepository;
import by.bntu.fitr.springtry.repository.LotRepository;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.util.ErrorMessage;
import by.bntu.fitr.springtry.validator.LotValidator;
import by.bntu.fitr.springtry.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class LotServiceImpl implements LotService {
    private static final String ANY_SQL_SYMBOL = "%";
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    private LotRepository lotRepository;
    @Autowired
    private BidRepository bidRepository;

    @Override
    public Lot createNewLot(String name, String description, String startBid, Timestamp startTime, Timestamp finishTime,
                            User seller, List<String> images) {
        logger.debug("{}, {}, {}, {}, {}, {}, {}", name, description, startBid, startTime, finishTime, seller, images);
        if (!(LotValidator.isValidName(name) && LotValidator.isValidTime(startTime, finishTime)
                && LotValidator.isValidDescription(description) && UserValidator.isValidBid(startBid))) {
            throw new ServiceException(ErrorMessage.INVALID_DATA_FORMAT);
        }
        if (startTime == null) {
            startTime = new Timestamp(System.currentTimeMillis());
        }
        BigDecimal startBidDecimal = new BigDecimal(startBid);

        Lot lot = new Lot(0, name, description, startTime, finishTime, startBidDecimal, seller, images);
        lot = lotRepository.save(lot);
        return lot;
    }

    @Override
    public Lot findLotById(long id) {
        Lot lot = lotRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorMessage.UNKNOWN_LOT));
        List<Bid> bids = bidRepository.findByIdLot(lot.getId());
        lot.setBidHistory(bids);
        return lot;
    }

    @Override
    public Page<Lot> findLotByName(String name, int pageNumber, int amountPerPage) {
        Page<Lot> lotPage = lotRepository.findByNameLike(ANY_SQL_SYMBOL + name + ANY_SQL_SYMBOL,
                PageRequest.of(pageNumber - 1, amountPerPage));
        lotPage.forEach(lot -> {
            lot.setBidHistory(bidRepository.findByIdLot(lot.getId()));
        });
        return lotPage;
    }

    @Override
    public List<Lot> findLotByBuyerId(User buyer, int pageNumber, int amountPerPage) {
        List<Lot> lots = lotRepository.findByBuyer(buyer, PageRequest.of(pageNumber - 1, amountPerPage));
        lots.forEach(lot -> {
            lot.setBidHistory(bidRepository.findByIdLot(lot.getId()));
        });
        return lots;
    }

    @Override
    public List<Lot> findLotBySellerId(User seller, int pageNumber, int amountPerPage) {
        List<Lot> lots = lotRepository.findBySeller(seller, PageRequest.of(pageNumber - 1, amountPerPage));
        lots.forEach(lot -> {
            lot.setBidHistory(bidRepository.findByIdLot(lot.getId()));
        });
        return lots;
    }

    @Override
    public List<Lot> findActive(int pageNumber, int amountPerPage) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Lot> lots = lotRepository.findByFinishTimeAfter(now, PageRequest.of(pageNumber - 1, amountPerPage));
        lots.forEach(lot -> {
            lot.setBidHistory(bidRepository.findByIdLot(lot.getId()));
        });
        return lots;
    }

    @Override
    public List<Lot> findAll(int pageNumber, int amountPerPage) {
        List<Lot> lots = lotRepository.findAll(PageRequest.of(pageNumber - 1, amountPerPage)).getContent();
        lots.forEach(lot -> {
            lot.setBidHistory(bidRepository.findByIdLot(lot.getId()));
        });
        return lots;
    }

    @Override
    public boolean isLotSubmitted(long lotId) {
        lotRepository.findById(lotId).orElseThrow(() -> new ServiceException(ErrorMessage.UNKNOWN_LOT));
        List<Bid> bidHistory = bidRepository.findByIdLot(lotId);
        bidHistory.removeIf(bid -> bid.getStatus()!= Status.WON);
        Optional<Bid> submittedWinner = bidHistory.stream().findAny();
        return submittedWinner.isPresent();
    }
}
