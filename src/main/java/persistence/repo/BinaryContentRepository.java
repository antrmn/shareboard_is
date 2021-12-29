package persistence.repo;

import persistence.fs.TransactionalFileSystemManager;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class BinaryContentRepository {
    @Inject TransactionalFileSystemManager fileSystemManager;

    Path uploadRoot = Path.of(System.getProperty("openejb.home"), "uploads");

    @Transactional(Transactional.TxType.MANDATORY)
    public String insert(InputStream stream, String filename) throws IOException {
        fileSystemManager.createFile(stream, uploadRoot, filename);
        return filename;
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public String insert(InputStream stream) throws IOException {
        return insert(stream, UUID.randomUUID().toString());
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void remove(String filename) throws IOException {
        fileSystemManager.deleteFile(Path.of(uploadRoot.toString(), filename));
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
