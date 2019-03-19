import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private File fileDirectory;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public Server() {
    }

    public void startServer(int port, String fileDir){
        if (port < 0 || port > 65535){
            System.out.println("Error, port number is out range (0-65535, inclusive)");
        }

        fileDirectory = new File(fileDir);
        if (!fileDirectory.isDirectory()){
            System.out.println("Error, not a valid file directory");
            return;
        }

        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            serverSocket = new ServerSocket(port,0, addr);
        }
        catch (IOException e){
            System.out.println("Failure to start server on port " + (port));
        }


        while(true){
            try{
                System.out.println("waiting for client");
                Socket incomingClient = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(incomingClient.getInputStream()));
//                WriterPrintWriter writer = new WriterPrintWriter(incomingClient);
                PrintWriter writer = new PrintWriter(incomingClient.getOutputStream());


                executor.submit(new ClientHandler(writer, reader, fileDirectory));
                System.out.println("client found");
            }
            catch (IOException e){
                System.out.println("StandardSocket failed to connect");
            }

        }

    }

    public void closeServer() throws IOException{
        serverSocket.close();

    }

}
