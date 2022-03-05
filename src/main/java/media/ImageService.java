package media;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Classe contenente la logica per il recupero di immagini da unità persistenti
 */
@ApplicationScoped
public class ImageService {
    private MediaRepository mediaRepository;

    protected ImageService(){}

    @Inject
    protected ImageService(MediaRepository mediaRepository){
        this.mediaRepository = mediaRepository;
    }

    /**
     * Ritorna l'input stream di un immagine
     * @param filename nome dell'immagine
     * @return input stream dell'immagine
     * @throws IOException
     */
    public InputStream getImage(@NotBlank String filename) throws IOException {
        InputStream inputStream = mediaRepository.get(filename);
        if(inputStream == null)
            return null;

        inputStream = new BufferedInputStream(inputStream);
        String mimetype = URLConnection.guessContentTypeFromStream(inputStream);
        if(mimetype == null || !mimetype.startsWith("image/"))
            return  null;

        return inputStream;
    }
}
