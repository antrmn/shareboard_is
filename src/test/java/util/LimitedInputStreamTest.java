package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LimitedInputStreamTest {

    @Test
    void transferTestExceed(){
        byte[] b = new byte[5*1024];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 5*1024-1);
        Assertions.assertThrows(IOException.class, () -> limitedis.transferTo(OutputStream.nullOutputStream()));
    }

    @Test
    void transferTestDoesntExceed(){
        byte[] b = new byte[5*1024];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 5*1024);
        Assertions.assertDoesNotThrow(() -> limitedis.transferTo(OutputStream.nullOutputStream()));
    }

    @Test
    void readTwoBytesExceed(){
        byte[] b = new byte[2];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 2);
        Assertions.assertThrows(IOException.class, () -> {
            limitedis.read();
            limitedis.read();
            limitedis.read();
        });
    }

    @Test
    void readTwoBytesDontExceed(){
        byte[] b = new byte[2];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 2);
        Assertions.assertDoesNotThrow(() -> {
            limitedis.read();
            limitedis.read();
        });
    }

    @Test
    void readTenBytesDontExceed(){
        byte[] b = new byte[10];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 10);
        assertDoesNotThrow(() -> {
            byte[] result = new byte[10];
            System.out.println(limitedis.read(result));
        });
    }

    @Test
    void readTenBytesExceed(){
        byte[] b = new byte[10];
        new Random().nextBytes(b);
        InputStream is = new ByteArrayInputStream(b);
        InputStream limitedis = new LimitedInputStream(is, 0);
        assertThrows(IOException.class, () -> {
            byte[] result = new byte[10];
            limitedis.read(result);
        });
    }
}