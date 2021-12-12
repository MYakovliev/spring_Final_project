package by.bntu.fitr.springtry.service.impl;

import by.bntu.fitr.springtry.config.TestConfig;
import by.bntu.fitr.springtry.entity.Lot;
import by.bntu.fitr.springtry.entity.User;
import by.bntu.fitr.springtry.entity.UserRole;
import by.bntu.fitr.springtry.repository.UserRepository;
import by.bntu.fitr.springtry.service.LotService;
import by.bntu.fitr.springtry.service.ServiceException;
import by.bntu.fitr.springtry.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.doubleThat;
import static org.mockito.Mockito.when;
//todo make same with mock
@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class UserServiceImplTest {
    private static final User user1 = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
            "/img/default_image.png", false, "login1");

    @Autowired
    private LotService lotService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DirtiesContext
    void makeBidPositive() {
        user1.setPassword("$2a$12$swOvQiIkAIu8.OapWlPJkeRkwhEl9gZM60b8M9DKOAB7fI3Sm4Kxa");
        Lot lotById = lotService.findLotById(1);
        user1.setBalance(new BigDecimal("40"));
        Lot lot = userService.makeBid(user1, "30.01", lotById);
        BigDecimal currentCost = lot.getCurrentCost();
        assertEquals(new BigDecimal("30.01"), currentCost);
    }

    @Test
    @DirtiesContext
    void makeBidNegative() {
        Lot lotById = lotService.findLotById(1);
        user1.setBalance(new BigDecimal("40"));
        assertThrows(ServiceException.class, () -> userService.makeBid(user1, "p", lotById));
    }

    @Test
    void loginPositive() {
        User expected = user1;
        User actual = userService.login("login1", "password");
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForLogin")
    void loginNegative(String login, String password) {
        assertThrows(ServiceException.class, () -> userService.login(login, password));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForRegister")
    void registerNegative(String name, String mail, String login, String password, UserRole role) {
        assertThrows(ServiceException.class, () -> userService.register(name, mail, login, password, role));
    }

    @Test
    @DirtiesContext
    void registerPositive() {
        User expected = new User(3, "name", "mail3@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
                "/img/default_image.png", false, "login3");
        User actual = userService.register("name", "mail3@mail.ma", "login3", "password", UserRole.BUYER);
        assertEquals(expected, actual);
    }

    @Test
    void findUserByIdPositive() {
        User expected = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
                "/img/default_image.png", false, "login1");
        User actual = userService.findUserById(1);
        assertEquals(expected, actual);
    }

    @Test
    void findUserByIdNegative() {
        int idNotInDb = 1000;
        assertThrows(ServiceException.class, () -> userService.findUserById(idNotInDb));
    }

    @Test
    @DirtiesContext
    void changeUserDataPositive() {
        User expected = new User(1, "oldName", "mail@mymail.ma", new BigDecimal("0.00"), UserRole.BUYER,
                "hello.jpg", false, "login1");
        User actual = userService.changeUserData(1, "hello.jpg", "oldName", "mail@mymail.ma");
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForChangeUserData")
    void changeUserDataNegative(long userId, String name, String mail, String avatar) {
        assertThrows(ServiceException.class, () -> userService.changeUserData(userId, avatar, name, mail));
    }

    @Test
    @DirtiesContext
    void changeUserPasswordPositive() {
        User user = userService.changeUserPassword(1, "password", "actuallyPassword");
        assertTrue(passwordEncoder.matches("actuallyPassword", user.getPassword()));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForChangeUserPassword")
    void changeUserPasswordNegative(long userId, String oldPassword, String newPassword) {
        assertThrows(ServiceException.class, () -> userService.changeUserPassword(userId, oldPassword, newPassword));
    }

    @Test
    @DirtiesContext
    void addBalancePositiveBuyer() {
        User expected = new User(1, "name1", "mail1@mail.ma", new BigDecimal("12.01"), UserRole.BUYER,
                "/img/default_image.png", false, "login1");
        User actual = userService.addBalance(1, "12.01");

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForAddBalance")
    void addBalanceNegative(long userId, String payment) {
        assertThrows(ServiceException.class, () -> userService.addBalance(userId, payment));
    }

    @Test
    void findUserByNamePositive() {
        User user1 = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
                "/img/default_image.png", false, "login1");
        User user2 = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
                "/img/default_image.png", false, "login2");
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        List<User> actual = userService.findUserByName("name", 1, 20).getContent();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForFindUserByName")
    void findUserByNameNegative(String name, int page, int amount) {
        assertThrows(ServiceException.class, () -> userService.findUserByName(name, page, amount));
    }

    @Test
    void findAllPositive() {
        User user1 = new User(1, "name1", "mail1@mail.ma", new BigDecimal("0.00"), UserRole.BUYER,
                "/img/default_image.png", false, "login1");
        User user2 = new User(2, "name2", "mail2@mail.ma", new BigDecimal("0.00"), UserRole.SELLER,
                "/img/default_image.png", false, "login2");
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        List<User> actual = userService.findAll(1, 20).getContent();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDataForFindAll")
    void findAllNegative(int page, int amount) {
        assertThrows(ServiceException.class, () -> userService.findAll(page, amount));
    }

    private static Stream<Arguments> provideInvalidDataForLogin() {
        return Stream.of(
                Arguments.of("/.gt", "password"),
                Arguments.of("goodlogin", "bad"),
                Arguments.of("loginNotInBd", "password"),
                Arguments.of("login1", "passwordwrong")
        );
    }

    private static Stream<Arguments> provideInvalidDataForRegister() {
        return Stream.of(
                Arguments.of("name", "invalidmail", "login", "password", UserRole.BUYER),
                Arguments.of("name", "validmail@mail.na", "login invalid", "password", UserRole.SELLER),
                Arguments.of("name", "mail@ma.ma", "login", "password", UserRole.GUEST),
                Arguments.of("name", "imail@ma.ma", "login", "pass", UserRole.BUYER),
                Arguments.of("invalidNaMES Indeed", "email@ma.ma", "login", "password", UserRole.BUYER),
                Arguments.of("name", "inva@ma.ma", "login1", "password", UserRole.BUYER)
        );
    }

    public static Stream<Arguments> provideInvalidDataForChangeUserData() {
        return Stream.of(
                Arguments.of(1000, "avatar.jpg", "name", "mail@mail.ma"),
                Arguments.of(1, "avatar.jpg", "invalid name./", "mail@mail.ma"),
                Arguments.of(1, "avatar.jpg", "name", "invalid mail")
        );
    }

    public static Stream<Arguments> provideInvalidDataForChangeUserPassword() {
        return Stream.of(
                Arguments.of(1000, "password", "passwordnew"),
                Arguments.of(1, "pasw", "password"),
                Arguments.of(1, "password", "name"),
                Arguments.of(1, "passwordnotmatch", "password")
        );
    }

    public static Stream<Arguments> provideInvalidDataForAddBalance() {
        return Stream.of(
                Arguments.of(1000, "12.01"),
                Arguments.of(1, "pasw")
        );
    }

    public static Stream<Arguments> provideInvalidDataForFindUserByName() {
        return Stream.of(
                Arguments.of("name", 0, 1),
                Arguments.of("name", 1, 0)
        );
    }

    public static Stream<Arguments> provideInvalidDataForFindAll() {
        return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(1, 0)
        );
    }
}