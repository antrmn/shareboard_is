package persistence.fs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface FileSystemManager {
    void createFile(InputStream content, Path path, String name) throws IOException;
    void deleteFile(Path path) throws IOException;
}
