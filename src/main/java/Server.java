import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private File fileDirectory;
    private PrintWriter writer;
    private BufferedReader reader;

    public void startServer(int port, String fileDir) throws IOException{
        if (port < 0 || port > 65535){
            System.out.println("Error, port number is out range (0-65535, inclusive)");
        }

        fileDirectory = new File(fileDir);
        if (!fileDirectory.isDirectory()){
            System.out.println("Error, not a valid file directory");
            return;
        }
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        serverSocket = new ServerSocket(port,0, addr);
        System.out.println("waiting for client");
        clientSocket = serverSocket.accept();
        System.out.println("client found");


        // writer writes to the output stream of the socket (which is actually for input)
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));



    }

    public void closeServer() throws IOException{
        writer.close();
        reader.close();
        clientSocket.close();
        serverSocket.close();

    }

}
