package socket;

import java.io.OutputStream;

public class MockSocket implements SocketInterface {
    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public void close() {

    }
}
