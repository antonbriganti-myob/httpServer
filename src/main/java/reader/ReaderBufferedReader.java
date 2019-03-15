package reader;

import socket.SocketInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderBufferedReader implements ReaderInterface {

    BufferedReader reader;

    public ReaderBufferedReader(SocketInterface socket) throws IOException{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
