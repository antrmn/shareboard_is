package media;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Stream che permette la lettura di byte fino a un limite arbitrario.</p>
 * <p>Se il limite fissato viene superato, un ulteriore tentativo di lettura provoca il lancio di una {@link ReadLimitExceededException}</p>
 */
public class LimitedInputStream extends FilterInputStream {
    private final long byteLimit;
    private long byteRead = 0;


    public LimitedInputStream(InputStream in, long byteLimit){
        super(in);
        this.byteLimit = byteLimit;
    }

    @Override
    public int read() throws IOException {
        int bite = super.read();
        this.byteRead++;
        if(this.byteRead > byteLimit)
            throw new ReadLimitExceededException();
        return bite;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int byteRead = super.read(b,off,len);
        this.byteRead += byteRead;
        if(this.byteRead > byteLimit)
            throw new ReadLimitExceededException();
        return byteRead;
    }
}
