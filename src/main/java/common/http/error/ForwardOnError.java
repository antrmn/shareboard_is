package common.http.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>L'annotazione consente di impostare in modo dichiarativo il percorso su cui una servlet effettua
 * il dispatch in caso di errore.</p>
 * <p>Affinché funzioni, è necessario che la servlet su cui applicare l'annotazione estenda {@link common.http.interceptor.InterceptableServlet}</p>
 * <p>È possibile applicare l'annotazione ai singoli metodi "doX" implementati (es. doGet). Applicare l'annotazione
 * direttamente alla classe equivale ad applicarla su tutti i metodi "doX" implementati.</p>
 * <p>Quando un metodo "doX" annotato con {@link ForwardOnError} lancia una particolare eccezione
 * (elencate in basso), si verificano le seguenti azioni: </p>
 * <ul>
 *     <li>Viene catturata l'eccezione allo scopo di recuperare i messaggi di errore rilevanti
 *     per inserirli nell'attributo di richiesta <pre>errors</pre></li>
 *     <li>Viene impostato lo status code 400 BAD REQUEST</li>
 *     <li>Viene passato il controllo (mediante metodo <pre>forward</pre> alla servlet al percorso specificato)</li>
 * </ul>
 * <p>Le eccezioni catturate sono le seguenti:  </p>
 * <table>
 *     <tr>
 *         <td>Eccezione</td><td>Messaggi di errore</td>
 *     </tr>
 *     <tr>
 *         <td>{@link javax.validation.ConstraintViolationException}</td><td>I messaggi degli oggetti {@link javax.validation.ConstraintViolation}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link IllegalArgumentException}</td><td>{@link IllegalArgumentException#getMessage()}</td>
 *     </tr>
 * </table>
 * @see ForwardOnErrorInterceptor
 * @see common.http.interceptor.InterceptableServlet
 * @see common.http.interceptor.ServletInterceptor
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForwardOnError {
    /**
     * @return L'URL pattern dove effettuare il forward della richiesta
     */
    String value();
}
