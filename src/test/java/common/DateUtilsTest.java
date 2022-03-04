package common;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DateUtilsTest {

    private Instant now;

    @BeforeAll
    void getNow(){
        now = Instant.now();
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3})
    void timeInSecond(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.SECONDS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("secondi") >= 0 || timeSince.indexOf("secondo") >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    void timeInMinutes(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.MINUTES);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("minuti") >= 0 || timeSince.indexOf("minuto") >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    void timeInHour(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.HOURS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("ore") >= 0 || timeSince.indexOf("ora") >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    void timeInDays(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("giorni") >= 0 || timeSince.indexOf("giorno") >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {90,30,31,32})
    void timeInMonth(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("mesi") >= 0 || timeSince.indexOf("mese") >= 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {400,900,1200})
    void timeInYears(int unit){
        Instant then = Instant.now().minus(unit,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("anni") >= 0 || timeSince.indexOf("anno") >= 0);
    }

}
