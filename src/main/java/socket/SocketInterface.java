package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SocketInterface {
    OutputStream getOutputStream() throws IOException;

    InputStream getInputStream() throws IOException;

    void close() throws IOException;
}
