import reader.ReaderInterface;
import socket.SocketInterface;
import writer.WriterInterface;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private SocketInterface clientSocket;
    private WriterInterface writer;
    private ReaderInterface reader;
    private File fileDirectory;

    public ClientHandler(SocketInterface socket, ReaderInterface reader, WriterInterface writer, File fileDirectory) {
        this.clientSocket = socket;
        this.writer = writer;
        this.reader = reader;
        this.fileDirectory = fileDirectory;
    }

    @Override
    public void run(){

        String requestLine = getRequestLine();
        System.out.println("Request Line: " + requestLine);

        Map<String, String> headers = getHeaders();
        System.out.println("Headers: " + Arrays.toString(headers.entrySet().toArray()));

        HTTPMessage response = processRequest(requestLine);

        sendResponseToClient(response);

        try{
            closeSocket();
        }
        catch (IOException e){
            System.out.println("Error when trying to get close client");
        }

    }

    private void sendResponseToClient(HTTPMessage response) {
        try {
            writer.println(response.toString());
        } catch (IOException e) {
            System.out.println("Error when trying to write response to client");
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
        String key;
        String value;
        HashMap<String, String> headers = new HashMap<>();

        try{
            while ((!(input=reader.readLine()).isEmpty())){
                key = input.split(": ")[0];
                value = input.split(": ")[1];
                headers.put(key, value);
            }
        }
        catch (IOException e){
            System.out.println("Error, ill formed headers received");
        }

        return headers;
    }

    private HTTPMessage processRequest(String inputLine) {
        File requestedFile;
        HTTPMessage response;

        HTTPMethods requestMethod = HTTPMethods.valueOf(inputLine.split(" ")[0]);
        String requestedResource = inputLine.split(" ")[1];
        requestedFile = new File(fileDirectory, requestedResource);

        switch(requestMethod){
            case HEAD:
                response = performHEADRequest(requestedFile);
                break;
            case GET:
                try {
                    response = performGETRequest(requestedFile);
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                response = new HTTPMessage(HTTPStatusCodes.HTTP_NOT_IMPLEMENTED);
        }


        return response;
    }

    private HTTPMessage performHEADRequest(File requestedFile) {
        HTTPStatusCodes statusCode;
        statusCode =  (requestedFile.exists()) ? HTTPStatusCodes.HTTP_OK : HTTPStatusCodes.HTTP_NOT_FOUND;
        return new HTTPMessage(statusCode);
    }

    private HTTPMessage performGETRequest(File requestedFile) throws IOException {
        HTTPStatusCodes statusCode;
        String fileContents;
        HTTPMessage response;

        if (requestedFile.exists()){
            statusCode = HTTPStatusCodes.HTTP_OK;
            fileContents = new String(Files.readAllBytes(requestedFile.toPath()));
            response = new HTTPMessage(statusCode, fileContents);
        }
        else{
            statusCode = HTTPStatusCodes.HTTP_NOT_FOUND;
            response = new HTTPMessage(statusCode);
        }

        return response;
    }

    private void closeSocket() throws IOException {
        clientSocket.close();
        writer.close();
        reader.close();
    }
}
