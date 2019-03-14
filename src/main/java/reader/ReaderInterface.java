package reader;

import java.io.IOException;

public interface ReaderInterface {

    String readLine() throws IOException;

    void close() throws IOException;
}
