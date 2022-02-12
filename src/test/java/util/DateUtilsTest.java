package util;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateUtilsTest {

    @Test
    void timeInSecond(){
        Instant then = Instant.now().plus(3,ChronoUnit.SECONDS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("secondi") >= 0 || timeSince.indexOf("secondo") >= 0);
    }

    @Test
    void timeInMinutes(){
        Instant then = Instant.now().plus(3,ChronoUnit.MINUTES);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("minuti") >= 0 || timeSince.indexOf("minuto") >= 0);
    }

    @Test
    void timeInHour(){
        Instant then = Instant.now().plus(3,ChronoUnit.HOURS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("ore") >= 0 || timeSince.indexOf("ora") >= 0);
    }

    @Test
    void timeInDays(){
        Instant then = Instant.now().plus(3,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("giorni") >= 0 || timeSince.indexOf("giorno") >= 0);
    }

    @Test
    void timeInMonth(){
        Instant then = Instant.now().plus(90,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("mesi") >= 0 || timeSince.indexOf("mese") >= 0);
    }

    @Test
    void timeInYears(){
        Instant then = Instant.now().plus(900,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.indexOf("anni") >= 0 || timeSince.indexOf("anno") >= 0);
    }

}
