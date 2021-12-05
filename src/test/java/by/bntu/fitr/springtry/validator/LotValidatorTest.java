package by.bntu.fitr.springtry.validator;

import by.bntu.fitr.springtry.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class LotValidatorTest {

    @ParameterizedTest
    @MethodSource("provideValidNames")
    public void testIsValidName(String name) {
        boolean actual = LotValidator.isValidName(name);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNames")
    public void testIsInvalidName(String name) {
        boolean actual = LotValidator.isValidName(name);
        assertFalse(actual);
    }

    @Test
    public void testIsValidDescription() {
        String description = "Just typical description. Can contain anything to describe product";
        boolean actual = LotValidator.isValidDescription(description);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidTimes")
    public void testIsValidTime(Timestamp startTime, Timestamp finishTime) {
        boolean actual= LotValidator.isValidTime(startTime, finishTime);
        assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTimes")
    public void testIsInvalidTime(Timestamp startTime, Timestamp finishTime) {
        boolean actual= LotValidator.isValidTime(startTime, finishTime);
        assertFalse(actual);
    }

    public static Stream<Arguments> provideValidNames(){
        return Stream.of(
                Arguments.of("name"),
                Arguments.of("Actually valid name."),
                Arguments.of("This: 'also' name (valid)"),
                Arguments.of("Just \"name\" = -34"),
                Arguments.of("1234567890123456789012345678901234567890"));
    }

    public static Stream<Arguments> provideInvalidNames(){
        return Stream.of(
                Arguments.of(""),
                Arguments.of("non"),
                Arguments.of("<!-- non !-->"),
                Arguments.of("<c:if/>"),
                Arguments.of("12345678901234567890123456789012345678901"),
                Arguments.of("3/4*5/12"));
    }


    public static Stream<Arguments> provideValidTimes(){
        return Stream.of(
                Arguments.of(Timestamp.valueOf("2022-10-11 12:00:00"),Timestamp.valueOf("2022-11-12 14:00:00")),
                Arguments.of(Timestamp.valueOf("2022-09-01 14:00:00"), Timestamp.valueOf("2024-01-09 14:00:00")),
                Arguments.of(null, Timestamp.valueOf("2022-01-03 14:00:00"))
        );
    }


    public static Stream<Arguments> provideInvalidTimes(){
        return Stream.of(
                Arguments.of(Timestamp.valueOf("2020-01-03 22:01:01"), Timestamp.valueOf("2022-01-01 12:00:00")),
                Arguments.of(Timestamp.valueOf("2022-03-04 15:00:00"), Timestamp.valueOf("2019-01-02 12:00:00"))
        );
    }


}