package persistence.repo;


import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class BinaryContentRepository {
    Path uploadRoot = Path.of(System.getProperty("openejb.home"), "uploads");

    public String insert(InputStream stream, String filename) throws IOException {
        File file = new File(uploadRoot.toFile(), filename);
        Files.copy(stream, file.toPath());
        return filename;
    }

    public String insert(InputStream stream) throws IOException {
        return insert(stream, UUID.randomUUID().toString());
    }

    public void remove(String filename) throws IOException {
        boolean successful = Files.deleteIfExists(uploadRoot.resolve(filename));
        // if (!successful) { ???? }
    }

    public InputStream get(String filename){
        File file = new File(uploadRoot.toFile(), filename);
        if (!file.exists() && !file.isFile())
            return null;
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
