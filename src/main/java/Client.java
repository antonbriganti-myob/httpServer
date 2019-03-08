import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public void startConnection(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);

        //Auto-flushing means data goes right through the buffer and then flushed out,
        //not kept in the buffer waiting for other data to arrive and accumulate.
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void endConnection() throws IOException{
        writer.close();
        reader.close();
        clientSocket.close();
    }

}
