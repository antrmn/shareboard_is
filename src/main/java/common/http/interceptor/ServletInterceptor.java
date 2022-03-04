package common.http.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;


/**
 * Classe astratta rappresentante un interceptor invocabile da un {@link InterceptableServlet} prima di eseguire
 * un metodo "doGet", "doPost", "doX"... <br/>
 *
 * Per definire un interceptor occorre:
 * <ul>
 *     <li>Estendere questa classe (Specificando l'annotazione a cui la nuova sottoclasse sarà associata)</li>
 *     <li>Definire il comportamento dell'interceptor sovrascrivendo i metodi {@link ServletInterceptor#handle(HttpServletRequest, HttpServletResponse, HttpServletBiConsumer)}
 *     e {@link this#init(Annotation)}</li>
 * </ul>
 *
 * @param <A> Il tipo di annotazione a cui l'interceptor sarà associato
 */
public abstract class ServletInterceptor<A extends Annotation>{


    /**
     * Chiamato dal factory per inizializzare l'interceptor dopo averlo istanziato.
     * @param annotation L'istanza di annotazione associata all'interceptor, con eventuali parametri necessari per la configurazione
     */
    protected abstract void init (A annotation);

    /**
     * Metodo contenete la logica dell'interceptor, che si occupa di processare la richiesta e la risposta passati come parametro
     * e di passare eventualmente il controllo eventualmente al prossimo metodo specificato come parametro
     * @param req La richiesta da processare
     * @param resp La risposta da processare
     * @param next Il riferimento al metodo a cui poter passare il controllo
     * @throws ServletException
     * @throws IOException
     */
    public abstract void handle(HttpServletRequest req, HttpServletResponse resp, HttpServletBiConsumer next)
            throws ServletException, IOException;

    /**
     * Definisce l'ordine in cui l'interceptor deve essere eseguito rispetto agli altri interceptor.
     * Un numero più basso implica una maggiore priorità. Il valore di default (Se il metodo non è sovrascritto) è Integer.MAX_VALUE
     * @return La priorità dell'interceptor
     */
    public int priority(){
        return Integer.MAX_VALUE/2;
    }
}