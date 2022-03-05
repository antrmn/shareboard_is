package common.http.error;

import usecase.auth.BannedUserException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>L'annotazione consente di impostare in modo dichiarativo se inviare eventuali errori in formato JSON nel corpo della risposta</p>
 * <p>Affinché funzioni, è necessario che la servlet su cui applicare l'annotazione estenda {@link common.http.interceptor.InterceptableServlet}</p>
 * <p>È possibile applicare l'annotazione ai singoli metodi "doX" implementati (es. doGet). Applicare l'annotazione
 * direttamente alla classe equivale ad applicarla su tutti i metodi "doX" implementati.</p>
 * <p>Quando un metodo "doX" annotato con {@link JSONError} lancia una particolare eccezione
 * (elencata in basso), si verificano le seguenti azioni: </p>
 * <ul>
 *     <li>Viene catturata l'eccezione allo scopo di recuperare i messaggi di errore rilevanti
 *     per inserirli nell'attributo di richiesta <pre>errors</pre></li>
 *     <li>I messaggi raccolti vengono inclusi in un array JSON con chiave <pre>errors</pre></li>
 *     <li>Viene impostato l'apposito status code a seconda dell'eccezione catturata</li>
 *     <li>Il JSON risultante viene scritto sulla risposta</li>
 * </ul>
 * <p>Le eccezioni catturate sono le seguenti:  </p>
 * <table>
 *     <tr>
 *         <td>Eccezione</td><td>Messaggi di errore</td><td>Status code</td>
 *     </tr>
 *     <tr>
 *         <td>{@link javax.validation.ConstraintViolationException}</td><td>I messaggi degli oggetti {@link javax.validation.ConstraintViolation}</td><td>400</td>
 *     </tr>
 *     <tr>
 *         <td>{@link IllegalArgumentException}</td><td>{@link IllegalArgumentException#getMessage()}</td><td>400</td>
 *     </tr>
 *     <tr>
 *         <td>{@link usecase.auth.AuthenticationRequiredException}</td><td>{@link usecase.auth.AuthenticationRequiredException#getMessage()}</td><td>401</td>
 *     </tr>
 *     <tr>
 *         <td>{@link usecase.auth.BannedUserException}</td><td>La durata del ban, contenuta in {@link BannedUserException#getDuration()}</td><td>403</td>
 *     </tr>
 *     <tr>
 *         <td>{@link usecase.auth.AuthorizationException}</td><td>{@link usecase.auth.AuthorizationException#getMessage()}</td><td>403</td>
 *     </tr>
 *     <tr>
 *         <td>{@link RuntimeException}</td><td>{{@link RuntimeException#getMessage()}}</td><td>4500</td>
 *     </tr>
 *     <tr>
 *         <td>{@link javax.servlet.ServletException}</td><td>{{@link javax.servlet.ServletException#getMessage()}}</td><td>500</td>
 *     </tr>
 *     <tr>
 *         <td>{@link java.io.IOException}</td><td>{{@link java.io.IOException#getMessage()}}</td><td>500</td>
 *     </tr>
 * </table>
 * @see ForwardOnErrorInterceptor
 * @see common.http.interceptor.InterceptableServlet
 * @see common.http.interceptor.ServletInterceptor
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONError {
}
