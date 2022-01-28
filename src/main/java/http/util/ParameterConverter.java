package http.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Classe di utilità che permette di accedere a parametri di un {@link HttpServletRequest} nel tipo desiderato
 */
@ApplicationScoped
public class ParameterConverter {
    private HttpServletRequest request;

    protected ParameterConverter(){}

    /**
     * Costruttore unico dell'oggetto. Se l'oggetto istanziato in un contesto CDI, la dipendenza sarà
     * soddisfatta automaticamente
     * @param request L'oggetto {@link HttpServletRequest} rappresentante la richiesta in corso
     */
    @Inject
    public ParameterConverter(HttpServletRequest request){
        this.request = request;
    }


    /**
     * Restituisce il valore di un parametro di richiesta come intero
     * @param parameterKey Una stringa che specifica il nome del parametro
     * @return Un {@link OptionalInt} contenente l'intero; {@link OptionalInt#empty()} se il parametro
     * non è stato trovato o se il parsing ha avuto esito negativo
     * @see Integer#parseInt(String)
     */
    public OptionalInt getIntParameter(String parameterKey){
        String parameter = request.getParameter(parameterKey);
        if(parameter == null || parameter.isBlank())
            return OptionalInt.empty();
        try{
            return OptionalInt.of(Integer.parseInt(parameter.trim()));
        } catch (NumberFormatException e){
            return OptionalInt.empty();
        }
    }

    /**
     * Restituisce il valore di un parametro di richiesta come double
     * @param parameterKey Una stringa che specifica il nome del parametro
     * @return Un {@link OptionalDouble} contenente il double; {@link OptionalDouble#empty()} se il parametro
     * non è stato trovato o se il parsing ha avuto esito negativo
     * @see Double#parseDouble(String)
     */
    public OptionalDouble getDoubleParameter(String parameterKey){
        String parameter = request.getParameter(parameterKey);
        if(parameter == null || parameter.isBlank())
            return OptionalDouble.empty();
        try{
            return OptionalDouble.of(Double.parseDouble(parameter.trim()));
        } catch (NumberFormatException e){
            return OptionalDouble.empty();
        }
    }

    /**
     * Restituisce il valore di un parametro di richiesta come long
     * @param parameterKey Una stringa che specifica il nome del parametro
     * @return Un {@link OptionalLong} contenente il long; {@link OptionalLong#empty()} se il parametro
     * non è stato trovato o se il parsing ha avuto esito negativo
     * @see Long#parseLong(String)
     */
    public OptionalLong getLongParameter(String parameterKey) {
        String parameter = request.getParameter(parameterKey);
        if (parameter == null || parameter.isBlank())
            return OptionalLong.empty();
        try {
            return OptionalLong.of(Long.parseLong(parameter.trim()));
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }

    /**
     * Restituisce il valore di un parametro di richiesta come {@link LocalDate}. La conversione segue il formato "YYYY-mm-DD"
     * @param parameterKey Una stringa che specifica il nome del parametro
     * @return Un {@link Optional<LocalDate>} contenente l'oggetto; {@link Optional#empty()} se il parametro
     * non è stato trovato o se il parsing ha avuto esito negativo
     * @see LocalDate#parse(CharSequence)
     */
    public Optional<LocalDate> getDateParameter(String parameterKey){
        String parameter = request.getParameter(parameterKey);
        if (parameter == null || parameter.isBlank())
            return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(parameter.trim()));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
