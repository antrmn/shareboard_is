package persistence.repo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class BinaryContentRepositoryIT {
    @TempDir
    private Path uploadRoot;

    @Test
    public void insert() throws IOException {
        BinaryContentRepository binaryContentRepository = new BinaryContentRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        binaryContentRepository.insert(stream, "myfile");
        stream.close();

        try(InputStream fileStream = binaryContentRepository.get("myfile")){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }
    }

    @Test
    public void insertRandomName() throws IOException {
        BinaryContentRepository binaryContentRepository = new BinaryContentRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        String filename = binaryContentRepository.insert(stream);
        stream.close();

        try(InputStream fileStream = binaryContentRepository.get(filename)){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }
    }

    @Test
    public void remove() throws IOException {
        BinaryContentRepository binaryContentRepository = new BinaryContentRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        String filename = binaryContentRepository.insert(stream);
        stream.close();

        try(InputStream fileStream = binaryContentRepository.get(filename)){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }

        binaryContentRepository.remove(filename);
        Assertions.assertNull(binaryContentRepository.get(filename));
    }
}
