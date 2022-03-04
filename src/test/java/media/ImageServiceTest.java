package media;

import org.apache.bval.cdi.BValInterceptor;
import org.apache.openejb.testing.Classes;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.limburg.cdimock.CdiMock;
import usecase.ServiceTest;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Classes(cdi=true, value = ImageService.class,
            cdiInterceptors = BValInterceptor.class,
            cdiStereotypes = CdiMock.class)
class ImageServiceTest extends ServiceTest {
    @Mock
    MediaRepository mediaRepository;
    @Inject ImageService imageService;

    @Test
    void getImage() throws IOException {
        byte[] bytes = "GIF8".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new BufferedInputStream(new ByteArrayInputStream(bytes));
        when(mediaRepository.get(any())).thenReturn(stream);
        byte[] result = new byte[bytes.length];
        imageService.getImage("file").read(result);
        assertArrayEquals(bytes,result);
    }

    @Test
    void imageNotFound() throws IOException {
        when(mediaRepository.get(any())).thenReturn(null);
        assertNull(imageService.getImage("file"));
    }

    @Test
    void audioInsteadOfImage() throws IOException{
        byte[] bytes = "RIFF".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new BufferedInputStream(new ByteArrayInputStream(bytes));
        when(mediaRepository.get(any())).thenReturn(stream);
        byte[] result = new byte[bytes.length];
        assertNull(imageService.getImage("file"));
    }

    @Test
    void notAnImage() throws IOException{
        byte[] bytes = "NOTANIMAGE".getBytes(StandardCharsets.UTF_8);
        InputStream stream = new BufferedInputStream(new ByteArrayInputStream(bytes));
        when(mediaRepository.get(any())).thenReturn(stream);
        byte[] result = new byte[bytes.length];
        assertNull(imageService.getImage("file"));
    }
}