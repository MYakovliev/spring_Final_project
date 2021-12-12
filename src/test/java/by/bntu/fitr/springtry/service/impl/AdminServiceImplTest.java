package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.*;
import by.bntu.fitr.springtry.service.AdminService;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class AdminServiceImplTest {
    private Calendar calendar = Calendar.getInstance();
    private Lot lot1 = new Lot(1, "name1", "desc1", null, null, new BigDecimal("20.21"), null, new ArrayList<>());
    private Lot lot2 = new Lot(2, "name2", "desc2", null, null, new BigDecimal("20.45"), null, new ArrayList<>());
    private static final User buyer = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
            "/img/default_image.png", false, "login1");

    @Autowired
    private AdminService adminService;
    @Autowired
    private LotService lotService;

    @BeforeEach
    void setUp() {
        calendar.set(2020, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp start = new Timestamp(calendar.getTime().getTime());
        start.setNanos(0);
        lot1.setStartTime(start);
        lot2.setStartTime(start);
        calendar.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Timestamp finish = new Timestamp(calendar.getTime().getTime());
        finish.setNanos(0);
        lot1.setFinishTime(finish);
        lot2.setFinishTime(finish);
        User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
                "/img/default_image.png", false, "login2");
        lot1.setSeller(seller);
        lot2.setSeller(seller);
        List<Bid> bidHistory = new ArrayList<>();
        lot2.setBidHistory(bidHistory);
        Bid bid1 = new Bid(1, buyer, new BigDecimal("20.45"), Status.LOSE);
        Bid bid2 = new Bid(2, buyer, new BigDecimal("21.50"), Status.WINING);
        bidHistory.add(bid1);
        bidHistory.add(bid2);
        lot1.setBidHistory(bidHistory);
    }

    @Test
    @DirtiesContext
    void banPositive() {
        User banned = adminService.ban(1);
        assertTrue(banned.isBanned());
    }

    @Test
    void banNegative() {
        assertThrows(ServiceException.class, () -> adminService.ban(1000));
    }

    @Test
    @DirtiesContext
    void unbanPositive() {
        User unbanned = adminService.unban(1);
        assertFalse(unbanned.isBanned());
    }

    @Test
    void unbanNegative() {
        assertThrows(ServiceException.class, () -> adminService.unban(1000));
    }

    @Test
    @DirtiesContext
    void submitWinnerPositive() {
        lot1.setFinishTime(Timestamp.valueOf("2021-01-01 12:01:01"));
        System.out.println();
        Lot lot = adminService.submitWinner(lot1);
        assertTrue(lotService.isLotSubmitted(lot.getId()));
    }

    @Test
    void submitWinnerNegative() {
        assertThrows(ServiceException.class, () -> adminService.submitWinner(lot1));
    }
}