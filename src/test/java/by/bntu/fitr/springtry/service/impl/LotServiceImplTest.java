package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.service.LotService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class LotServiceImplTest {
    private Calendar calendar = Calendar.getInstance();
    private Lot lot1 = new Lot(1, "name1", "desc1", null, null, new BigDecimal("20.21"), null, new ArrayList<>());
    private Lot lot2 = new Lot(2, "name2", "desc2", null, null, new BigDecimal("20.45"), null, new ArrayList<>());
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
        User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER, null, false, "login2");
        lot1.setSeller(seller);
        lot2.setSeller(seller);
    }

    @Test
    void createNewLotPositive() {
    }

    @Test
    void findLotByIdPositive() {
        Lot expected = lot2;
        Lot actual = lotService.findLotById(2);
        assertEquals(expected, actual);
    }

    @Test
    void findLotByNamePositive() {
    }

    @Test
    void findLotByBuyerIdPositive() {
    }

    @Test
    void findLotBySellerIdPositive() {
    }

    @Test
    void findActivePositive() {
    }

    @Test
    void findAllPositive() {

    }

    @Test
    void isLotSubmittedPositive() {
    }
}