package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MockSocket implements SocketInterface {
    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public void close() {

    }
}
