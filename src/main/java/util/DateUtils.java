package util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class DateUtils {
    public static String printTimeSince(Instant then){
        long n;
        if ((n = Math.abs(Instant.now().until(then, ChronoUnit.SECONDS))) < 60){
            return n == 1 ? n + " secondo" : n + " secondi";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.MINUTES))) < 60){
            return n == 1 ? n + " minuto" : n + " minuti";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.HOURS))) < 24){
            return n == 1 ? n + " ora" : n + " ore";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.DAYS))) < 30) {
            return n == 1 ? n + " giorno" : n + " giorni";
        } else if ((n = Math.abs(Instant.now().until(then, ChronoUnit.DAYS))) < 365) {
            n /= 30;
            return n == 1 ? n + " mese" : n + " mesi";
        } else {
            n = Math.abs(Instant.now().until(then, ChronoUnit.YEARS));
            return n == 1 ? n + " anno" : n + " anni";
        }
    }
}