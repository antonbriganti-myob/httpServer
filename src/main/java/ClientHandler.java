import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private PrintWriter writer;
    private BufferedReader reader;
    private File fileDirectory;

    /*
    Changes the constructor to have the writer initialised outside, and injected in.

    It is can be important to structure your code to support testability, in this case
    the class is more testable by having the outside interface changed. This way we
    can inject a Mock for testing, and actual implementation for Production. This is
    the same approach as the Interfaces we created previously.
     */
    public ClientHandler(PrintWriter writer, BufferedReader reader, File fileDirectory) throws IOException {
        this.writer = writer;
        this.reader = reader;
        this.fileDirectory = fileDirectory;
    }

    @Override
    public void run(){

        String requestLine = getRequestLine();
        System.out.println("Request Line: " + requestLine);

        HTTPHeaders requestHeaders = getHeaders();
        System.out.println("Headers: " + requestHeaders.toString());

        HTTPMessage response = processRequest(requestLine);

        sendResponseToClient(response);

        try{
            closeSocket();
        }
        catch (IOException e){
            System.out.println("Error when trying to get close client");
        }

    }

    public void sendResponseToClient(HTTPMessage response) {
        writer.println(response.toString());
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

    public HTTPHeaders getHeaders() {
        String input;
        String key;
        String value;
        HTTPHeaders headers = new HTTPHeaders();

        try{
            while ((!(input=reader.readLine()).isEmpty())){
                key = input.split(": ")[0];
                value = input.split(": ")[1];
                headers.addHeader(key, value);
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
        // The socket is closed in in Server.closeSocket(), which would need to be called elsewhere.
        // The socket will be implicitly closed when the JVM shuts down also.
        writer.close();
        reader.close();
    }
}
