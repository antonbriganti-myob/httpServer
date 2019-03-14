package socket;

import java.io.IOException;
import java.io.OutputStream;

public interface SocketInterface {
    OutputStream getOutputStream() throws IOException;

    void close() throws IOException;
}
