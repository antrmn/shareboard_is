package common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Classe di utilità contenente funzioni per la stampa di date nella view
 */
public final class DateUtils {
    /**
     * <p>Questa funzione restituisce un messaggio indicante il tempo trascorso tra il tempo di invocazione del metodo al tempo
     * passato come parametro.</p>
     * <p>Ad esempio, se il tempo passato come parametro corrisponde al 05/03/2022 16:00:00Z e il metodo è stato invocato alle
     * 16:00:05 dello stesso giorno, il metodo restituirà una stringa contenente "5 secondi fa"</p>.
     * @param then Il tempo di riferimento
     * @return Il messaggio "x secondi/minuti/ore/giorni/mesi/anni fa"
     */
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
            int thenYear = then.atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            n = Math.abs(thenYear - LocalDate.now().getYear());
            return n == 1 ? n + " anno" : n + " anni";
        }
    }
}