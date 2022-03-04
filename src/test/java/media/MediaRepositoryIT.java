package media;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class MediaRepositoryIT {
    @TempDir
    private Path uploadRoot;

    @Test
    public void insert() throws IOException {
        MediaRepository mediaRepository = new MediaRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        mediaRepository.insert(stream, "myfile");
        stream.close();

        try(InputStream fileStream = mediaRepository.get("myfile")){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }
    }

    @Test
    public void insertRandomName() throws IOException {
        MediaRepository mediaRepository = new MediaRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        String filename = mediaRepository.insert(stream);
        stream.close();

        try(InputStream fileStream = mediaRepository.get(filename)){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }
    }

    @Test
    public void remove() throws IOException {
        MediaRepository mediaRepository = new MediaRepository(uploadRoot);
        byte[] expected = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(expected);
        String filename = mediaRepository.insert(stream);
        stream.close();

        try(InputStream fileStream = mediaRepository.get(filename)){
            byte[] result = fileStream.readAllBytes();
            Assertions.assertArrayEquals(expected,result);
        }

        mediaRepository.remove(filename);
        Assertions.assertNull(mediaRepository.get(filename));
    }
}
