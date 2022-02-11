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
        assertTrue(timeSince.equals("2 secondi"));
    }

    @Test
    void timeInMinutes(){
        Instant then = Instant.now().plus(3,ChronoUnit.MINUTES);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.equals("2 minuti"));
    }

    @Test
    void timeInHour(){
        Instant then = Instant.now().plus(3,ChronoUnit.HOURS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.equals("2 ore"));
    }

    @Test
    void timeInDays(){
        Instant then = Instant.now().plus(3,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.equals("2 giorni"));
    }

    @Test
    void timeInMonth(){
        Instant then = Instant.now().plus(90,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.equals("2 mesi"));
    }

    @Test
    void timeInYears(){
        Instant then = Instant.now().plus(900,ChronoUnit.DAYS);
        String timeSince = DateUtils.printTimeSince(then);
        System.out.println(timeSince);
        assertTrue(timeSince.equals("2 anni"));
    }

}
