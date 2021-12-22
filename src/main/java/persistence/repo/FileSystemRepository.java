package persistence.repo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemRepository {
    Path uploadRoot = Path.of(System.getProperty("openejb.home"), "uploads");

    public void insert(InputStream stream, String filename) throws IOException {
        File uploadRootFile = uploadRoot.toFile();
        uploadRootFile.getParentFile().mkdirs();
        File newFile = new File(uploadRootFile, filename);
        Files.copy(stream, newFile.toPath());
    }

    public void removeIfExists(String filename) throws IOException {
        Files.deleteIfExists(Path.of(uploadRoot.toString(), filename));
    }
}
