package by.bntu.fitr.springtry.validator;

import by.bntu.fitr.springtry.config.TestConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class UserValidatorTest {

    @ParameterizedTest
    @MethodSource("provideValidLogin")
    public void testIsValidLogin(String login) {
        boolean actual = UserValidator.isValidLogin(login);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLogin")
    public void testIsInvalidLogin(String login) {
        boolean actual = UserValidator.isValidLogin(login);
        assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidName")
    public void testIsValidName(String name) {
        boolean actual = UserValidator.isValidName(name);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidName")
    public void testIsInvalidName(String name) {
        boolean actual = UserValidator.isValidName(name);
        assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidMail")
    public void testIsValidMail(String mail) {
        boolean actual = UserValidator.isValidMail(mail);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMail")
    public void testIsInvalidMail(String mail) {
        boolean actual = UserValidator.isValidMail(mail);
        assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidPassword")
    public void testIsValidPassword(String password) {
        boolean actual = UserValidator.isValidPassword(password);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPassword")
    public void testIsInvalidPassword(String password) {
        boolean actual = UserValidator.isValidPassword(password);
        assertFalse(actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidBid")
    public void testIsValidBid(String bid) {
        boolean actual = UserValidator.isValidBid(bid);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBid")
    public void testIsInvalidBid(String bid) {
        boolean actual = UserValidator.isValidBid(bid);
        assertFalse(actual);
    }

    public static Stream<Arguments> provideValidName() {
        return Stream.of(
                Arguments.of("Q"), Arguments.of("justName"), Arguments.of("name"), Arguments.of("Царь"),
                Arguments.of("qwertyuiopqwertyuiopqwertyuiopqwert"));
    }

    public static Stream<Arguments> provideInvalidName() {
        return Stream.of(Arguments.of("name.com"), Arguments.of("invalid name"),
                Arguments.of("123"), Arguments.of(""),
                Arguments.of("qwertyuiopqwertyuiopqwertyuiopqwerty"));
    }

    public static Stream<Arguments> provideValidMail() {
        return Stream.of(Arguments.of("email@rumbler.tv"), Arguments.of("my-mail@bntu.by"),
                Arguments.of("mail.my.company@company.by"), Arguments.of("12345678901234567890@qwertyuio.ru"),
                Arguments.of("mail@ma.ma"));
    }

    public static Stream<Arguments> provideInvalidMail() {
        return Stream.of(Arguments.of("name.com"), Arguments.of("!@mail@invalid.mail"), Arguments.of("23${12}@in.valid"),
                Arguments.of("mai@m.ma"), Arguments.of("qwertyuiopqwertyuiop@wertyuiop.wertyuiopq"), Arguments.of("1234@12.12"));
    }

    public static Stream<Arguments> provideValidPassword() {
        return Stream.of(Arguments.of("!@#$%^&*()#$%^&"), Arguments.of("passwordTypicalMain"),
                Arguments.of("password"), Arguments.of("12345678901234567890123456789012345"));
    }

    public static Stream<Arguments> provideValidBid() {
        return Stream.of(
                Arguments.of("1234567890.12"), Arguments.of("1"), Arguments.of("14"), Arguments.of("12.5"),
                Arguments.of("45.56"), Arguments.of("0.03"), Arguments.of("12.6"));
    }

    public static Stream<Arguments> provideInvalidBid() {
        return Stream.of(Arguments.of("mabid"), Arguments.of("-1, 45"), Arguments.of("-9.5"),
                Arguments.of("12.556"), Arguments.of(""), Arguments.of("1234567890.123"), Arguments.of("12345678912"));
    }

    public static Stream<Arguments> provideInvalidPassword() {
        return Stream.of(Arguments.of("name.co"), Arguments.of(""), Arguments.of("123456789012345678901234567890123456"),
                Arguments.of("1234567"));
    }

    public static Stream<Arguments> provideValidLogin() {
        return Stream.of(Arguments.of("login"), Arguments.of("login45"), Arguments.of("43212"), Arguments.of("Царь"),
                Arguments.of("12345678901234567890123456789012345"));
    }

    public static Stream<Arguments> provideInvalidLogin() {
        return Stream.of(Arguments.of("дщпшт-тщ"), Arguments.of("<loyih>"),
                Arguments.of("123"), Arguments.of("123456789012345678901234567890123456"));
    }
}