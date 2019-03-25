import httpResponse.HTTPHeaders;
import httpResponse.HTTPMessage;
import httpResponse.HTTPMethods;
import httpResponse.HTTPStatusCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ClientHandler implements Runnable {
    private PrintWriter writer;
    private BufferedReader reader;
    private File baseDirectory;

    /*
    Changes the constructor to have the writer initialised outside, and injected in.

    It is can be important to structure your code to support testability, in this case
    the class is more testable by having the outside interface changed. This way we
    can inject a Mock for testing, and actual implementation for Production. This is
    the same approach as the Interfaces we created previously.
     */
    public ClientHandler(PrintWriter writer, BufferedReader reader, File baseDirectory) throws IOException {
        this.writer = writer;
        this.reader = reader;
        this.baseDirectory = baseDirectory;
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
        HTTPMessage response;

        HTTPMethods requestMethod = HTTPMethods.valueOf(inputLine.split(" ")[0]);
        String requestedResource = inputLine.split(" ")[1];

        switch(requestMethod){
            case HEAD:
                response = performHEADRequest(requestedResource);
                break;
            case GET:
                try {
                    response = performGETRequest(requestedResource);
                } catch (IOException e) {
                    e.printStackTrace();
                    response = new HTTPMessage(HTTPStatusCodes.HTTP_INTERNAL_SERVER_ERROR);
                }
                break;
            case PUT:
                response = performPUTRequest();
                break;
            case DELETE:
                response = performDELETERequest();
                break;
            case OPTIONS:
                response = performOPTIONSRequest();
                break;
            default:
                response = new HTTPMessage(HTTPStatusCodes.HTTP_NOT_ALLOWED);
        }


        return response;
    }

    private HTTPMessage performHEADRequest(String requestedResource) {
        HTTPStatusCodes statusCode;
        File requestedFile = new File(baseDirectory, requestedResource);
        statusCode =  (requestedFile.exists()) ? HTTPStatusCodes.HTTP_OK : HTTPStatusCodes.HTTP_NOT_FOUND;
        return new HTTPMessage(statusCode);
    }

    private HTTPMessage performGETRequest(String requestedResource) throws IOException {
        HTTPStatusCodes statusCode;
        HTTPMessage response;
        File requestedFile = new File(baseDirectory, requestedResource);

        if(isServerRootDirectory(requestedResource)){
            response = createGETListFileResponse(requestedFile);
        }
        else{
            if (requestedFile.exists()){
                response = createGETFileResponse(requestedFile);
            }
            else{
                statusCode = HTTPStatusCodes.HTTP_NOT_FOUND;
                response = new HTTPMessage(statusCode);
            }
        }


        return response;
    }

    private HTTPMessage performOPTIONSRequest() {
        HTTPMessage response;
        response = new HTTPMessage(HTTPStatusCodes.HTTP_NOT_IMPLEMENTED);
        return response;
    }

    private HTTPMessage performDELETERequest() {
        HTTPMessage response;
        response = new HTTPMessage(HTTPStatusCodes.HTTP_NOT_IMPLEMENTED);
        return response;
    }


    private HTTPMessage performPUTRequest() {
        HTTPMessage response;
        response = new HTTPMessage(HTTPStatusCodes.HTTP_NOT_IMPLEMENTED);
        return response;
    }



    private boolean isServerRootDirectory(String requestedResource) {
        return "/".equals(requestedResource);
    }

    private HTTPMessage createGETListFileResponse(File requestedFile) throws IOException {
        HTTPStatusCodes statusCode;
        HTTPMessage response;
        StringBuilder body = new StringBuilder();

        try (Stream<Path> paths = Files.walk(Paths.get(requestedFile.getPath()))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .forEach(body::append);
        }

        statusCode = HTTPStatusCodes.HTTP_OK;
        response = new HTTPMessage(statusCode, body.toString());
        return response;
    }

    private HTTPMessage createGETFileResponse(File requestedFile) throws IOException {
        HTTPStatusCodes statusCode;
        String fileContents;
        HTTPMessage response;

        statusCode = HTTPStatusCodes.HTTP_OK;
        fileContents = new String(Files.readAllBytes(requestedFile.toPath()));
        response = new HTTPMessage(statusCode, fileContents);
        return response;
    }

    private void closeSocket() throws IOException {
        // The socket is closed in in Server.closeSocket(), which would need to be called elsewhere.
        // The socket will be implicitly closed when the JVM shuts down also.
        writer.close();
        reader.close();
    }
}
