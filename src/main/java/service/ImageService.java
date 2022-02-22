package service;

import persistence.repo.BinaryContentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@ApplicationScoped
public class ImageService {
    private final BinaryContentRepository binaryContentRepository;

    @Inject
    protected ImageService(BinaryContentRepository binaryContentRepository){
        this.binaryContentRepository = binaryContentRepository;
    }

    /**
     * Ritorna l'input stream di un immagine
     * @param filename nome dell'immagine
     * @return input stream dell'immagine
     * @throws IOException
     */
    public InputStream getImage(@NotBlank String filename) throws IOException {
        InputStream inputStream = binaryContentRepository.get(filename);
        if(inputStream == null)
            return null;

        inputStream = new BufferedInputStream(inputStream);
        String mimetype = URLConnection.guessContentTypeFromStream(inputStream);
        if(mimetype == null || !mimetype.startsWith("image/"))
            return  null;

        return inputStream;
    }
}
