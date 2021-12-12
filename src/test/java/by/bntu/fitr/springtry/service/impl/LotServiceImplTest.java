package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.*;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class LotServiceImplTest {
    private Calendar calendar = Calendar.getInstance();
    private Lot lot1 = new Lot(1, "name1", "desc1", null, null, new BigDecimal("20.21"), null, new ArrayList<>());
    private Lot lot2 = new Lot(2, "name2", "desc2", null, null, new BigDecimal("20.45"), null, new ArrayList<>());
    private static final User seller = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
            "/img/default_image.png", false, "login2");
    private static final User buyer = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
            "/img/default_image.png", false, "login1");

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
    void createNewLotPositive() {
        long expected = 3;
        Lot lotName = lotService.createNewLot("lotName", null, "20.1", null,
                Timestamp.valueOf("2022-05-01 20:14:10"), seller,Arrays.asList("one", "two"));
        assertEquals(expected, lotName.getId());
    }

    @ParameterizedTest
    @MethodSource("provideCreateNewLotNegative")
    void createNewLotNegative(String name, String description, String startBid, Timestamp startTime,
                              Timestamp finishTime, User seller, List<String> images){
        assertThrows(ServiceException.class, ()->lotService.createNewLot(name, description, startBid, startTime, finishTime, seller, images));
    }

    private static Stream<Arguments> provideCreateNewLotNegative() {
        return Stream.of(
                Arguments.of("<!-- non !-->", null, "20.1", null, Timestamp.valueOf("2022-05-01 20:14:10"), seller, new ArrayList<>()),
                Arguments.of("valid name", null, "-20.1", null, Timestamp.valueOf("2022-05-01 20:14:10"), seller, new ArrayList<>()),
                Arguments.of("valid name", null, "20.1", null, Timestamp.valueOf("2020-05-01 20:14:10"), seller, new ArrayList<>()),
                Arguments.of("<!-- non !-->", null, "20.1", null, Timestamp.valueOf("2022-05-01 20:14:10"), buyer, new ArrayList<>())
        );
    }

    @Test
    void findLotByIdPositive() {
        Lot expected = lot2;
        Lot actual = lotService.findLotById(2);
        assertEquals(expected, actual);
    }


    @Test
    void findLotByIdNegative(){
        assertThrows(ServiceException.class, ()->lotService.findLotById(1000));
    }

    @Test
    void findLotByNamePositive() {
        List<Lot> expected = Arrays.asList(lot1, lot2);
        Page<Lot> actual = lotService.findLotByName("name", 1, Integer.MAX_VALUE);
        assertEquals(expected, actual.getContent());
    }


    @ParameterizedTest
    @MethodSource("provideFindLotByNameNegative")
    void findLotByNameNegative(String name, int page, int amount){
        assertThrows(IllegalArgumentException.class, ()->lotService.findLotByName(name, page, amount));
    }

    private static Stream<Arguments> provideFindLotByNameNegative() {
        return Stream.of(
                Arguments.of("name", 0, 0),
                Arguments.of("name", 1, -1),
                Arguments.of("name", 1, 0)
        );
    }

    @Test
    void findLotByBuyerIdPositive() {
        List<Lot> expected = List.of(lot1);
        Page<Lot> actual = lotService.findLotByBuyerId(buyer, 1, Integer.MAX_VALUE);
        assertEquals(expected, actual.getContent());
    }


    @ParameterizedTest
    @MethodSource("provideFindLotByBuyerIdNegative")
    void findLotByBuyerIdNegative(User buyerId, int page, int amount){
        assertThrows(IllegalArgumentException.class, ()->lotService.findLotByBuyerId(buyerId, page, amount));
    }

    private static Stream<Arguments> provideFindLotByBuyerIdNegative() {
        return Stream.of(
                Arguments.of(buyer, 0, 0),
                Arguments.of(seller, 1, -1),
                Arguments.of(buyer, 1, 0)
        );
    }

    @Test
    void findLotBySellerIdPositive() {
        List<Lot> expected = Arrays.asList(lot1, lot2);
        Page<Lot> actual = lotService.findLotBySellerId(seller, 1, Integer.MAX_VALUE);
        assertEquals(expected, actual.getContent());
    }


    @ParameterizedTest
    @MethodSource("provideFindLotBySellerIdNegative")
    void findLotBySellerIdNegative(User sellerId, int page, int amount){
        assertThrows(IllegalArgumentException.class, ()->lotService.findLotBySellerId(sellerId, page, amount));
    }

    private static Stream<Arguments> provideFindLotBySellerIdNegative() {
        return Stream.of(
                Arguments.of(buyer, 0, 0),
                Arguments.of(seller, 1, -1),
                Arguments.of(buyer, 1, 0)
        );
    }

    @Test
    void findActivePositive() {
        List<Lot> expected = Arrays.asList(lot1, lot2);
        Page<Lot> actual = lotService.findActive(1, Integer.MAX_VALUE);
        assertEquals(expected, actual.getContent());
    }


    @ParameterizedTest
    @MethodSource("provideFindActiveNegative")
    void findActiveNegative(int page, int amount){
        assertThrows(IllegalArgumentException.class, ()->lotService.findActive(page, amount));
    }

    private static Stream<Arguments> provideFindActiveNegative() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(1, -1),
                Arguments.of(1, 0)
        );
    }

    @Test
    void findAllPositive() {
        List<Lot> expected = Arrays.asList(lot1, lot2);
        Page<Lot> actual = lotService.findAll(1, Integer.MAX_VALUE);
        assertEquals(expected, actual.getContent());
    }


    @ParameterizedTest
    @MethodSource("provideFindAllNegative")
    void findAllNegative(int page, int amount){
        assertThrows(IllegalArgumentException.class, ()->lotService.findAll(page, amount));
    }

    private static Stream<Arguments> provideFindAllNegative() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(1, -1),
                Arguments.of(1, 0)
        );
    }

    @Test
    void isLotSubmittedPositive() {
        assertFalse(lotService.isLotSubmitted(1));
    }

    @Test
    void isLotSubmittedNoLot(){
        assertThrows(ServiceException.class, ()->lotService.isLotSubmitted(1000));
    }
}