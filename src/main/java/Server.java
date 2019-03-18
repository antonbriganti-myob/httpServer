import reader.ReaderBufferedReader;
import socket.StandardSocket;
import writer.WriterPrintWriter;

import java.io.*;
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
                StandardSocket incomingClient = new StandardSocket(serverSocket.accept());
                ReaderBufferedReader reader = new ReaderBufferedReader(incomingClient);
                WriterPrintWriter writer = new WriterPrintWriter(incomingClient);


                executor.submit(new ClientHandler(incomingClient, reader, writer, fileDirectory));
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
