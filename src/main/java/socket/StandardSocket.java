package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class StandardSocket extends Socket implements SocketInterface {

    private Socket socket;

    public StandardSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException{
        return this.socket.getInputStream();
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }


}
