package media;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Classe che incapsula la logica per il ritrovo di media (immagini) dal filesystem
 */
public class MediaRepository implements Serializable{

    private Path uploadRoot = Path.of(System.getProperty("openejb.home"), "uploads");
    private final long sizeLimit = 5*1024*1024; //todo: parametrizza nel metodo insert

    /**
     *  Costruttore vuoto
     * @return nuova istanza di MediaRepository
     */
    public MediaRepository(){}

    /**
     *  Costruttore con parametro per settare uploadRoot
     * @param uploadRoot oggetto Path per settare uploadRoot
     * @return nuova istanza di MediaRepository
     */
    public MediaRepository(Path uploadRoot){
        this.uploadRoot = uploadRoot;
    }

    /**
     *  Salva un file nel filesystem e ne restituisce il nome
     * @param stream stream di dati
     * @param filename nome del file da salvare
     * @throws ReadLimitExceededException se il file supera i 5MB
     * @throws IOException
     * @return nuova istanza di MediaRepository
     */
    public String insert(InputStream stream, String filename) throws IOException {
        Files.createDirectories(uploadRoot);
        File file = new File(uploadRoot.toFile(), filename);
        Files.copy(new LimitedInputStream(stream,sizeLimit), file.toPath());
        return filename;
    }

    /**
     *  Salva un file nel filesystem e ne restituisce un nome univoco generato in maniera casuale
     * @param stream stream di dati
     * @throws IOException
     * @return nuova istanza di MediaRepository
     */
    public String insert(InputStream stream) throws IOException {
        return insert(stream, UUID.randomUUID().toString());
    }

    /**
     *  Rimuove un file dal filesystem dato un nome se esiste
     * @param filename nome del file
     * @throws IOException
     */
    public void remove(String filename) throws IOException {
        boolean successful = Files.deleteIfExists(uploadRoot.resolve(filename));
        // if (!successful) { ???? }
    }

    /**
     *  Restituisce un file dal filesystem dato un nome se esiste altrimenti restituisce null
     * @param filename nome del file
     * @return stream di dati del file o null
     */
    public InputStream get(String filename){
        File file = new File(uploadRoot.toFile(), filename);
        if (!file.exists() || !file.isFile())
            return null;
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
