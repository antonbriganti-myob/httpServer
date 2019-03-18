package writer;

import socket.SocketInterface;

import java.io.IOException;
import java.io.PrintWriter;

public class WriterPrintWriter implements WriterInterface {
    private PrintWriter writer;

    public WriterPrintWriter(SocketInterface socket) throws IOException {
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }


    @Override
    public void println(String input) throws IOException {
        this.writer.println(input);
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }
}
