package writer;

import java.io.IOException;

public interface WriterInterface {
    void println(String input) throws IOException;

    void close() throws IOException;
}
