import reader.ReaderInterface;
import socket.SocketInterface;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private SocketInterface clientSocket;
//    private PrintWriter writer;
    private ReaderInterface reader;

    public ClientHandler(SocketInterface socket, ReaderInterface reader) {
        this.clientSocket = socket;
        // writer writes to the output stream of the socket (which is actually for input)
//        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        this.reader = reader;
    }

    @Override
    public void run(){

        String requestLine = getRequestLine();
//        System.out.println(requestLine);

        Map<String, String> headers = getHeaders();
//        System.out.println(headers.keySet());
//        System.out.println(headers.values());
//
//        HTTPResponses response = processMessage(inputLine);
//        writer.println(response);

        try{
            closeSocket();
        }
        catch (IOException e){
            System.out.println("Error when trying to get close client");
        }

    }

    public String getRequestLine() {
        String inputLine = "";

        try{
            inputLine = reader.readLine();
        }
        catch (IOException e){
            System.out.println("Error when trying to get input from client");
        }
        return inputLine;
    }

    public Map<String, String> getHeaders() {
        String input;
        HashMap<String, String> headers = new HashMap<>();

        try{
            while ((!(input=reader.readLine()).isEmpty())){
                headers.put(input.split(": ")[0], input.split(": ")[1]);
            }
        }
        catch (IOException e){
            System.out.println("Error, ill formed headers received");
        }

        return headers;
    }

    private HTTPResponses processMessage(String inputLine) {
        File requestedFile;
        HTTPResponses response;

        if ("/".equals(inputLine)) {
            response = HTTPResponses.HTTP_OK;
        }
        else{
            requestedFile = new File(inputLine);
            if (!requestedFile.exists()){
                response = HTTPResponses.HTTP_NOT_FOUND;
            }
            else {
                response = HTTPResponses.HTTP_NOT_IMPLEMENTED;
            }
        }

        return response;
    }

    private void closeSocket() throws IOException {
        clientSocket.close();
//        writer.close();
        reader.close();
    }
}
