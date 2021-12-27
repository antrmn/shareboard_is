package persistence.fs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Stateless
public class SimpleFileSystemManager implements FileSystemManager {

    @Override
    public void createFile(InputStream content, Path path, String name) throws IOException {
        CreateFile.execute(content, path, name).confirm();
    }

    @Override
    public void deleteFile(Path path) throws IOException {
        DeleteFile.execute(path).confirm();
    }
}
