import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        // writer writes to the output stream of the socket (which is actually for input)
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run(){
        File requestedFile;
        String inputLine = "";
        try{
            inputLine = reader.readLine();
        }
        catch (IOException e){
            System.out.println("Error when trying to get input from client");
        }



        if ("/".equals(inputLine)) {
            writer.println(HTTPResponses.HTTP_OK.toString());
        }
        else{
            requestedFile = new File(inputLine);
            if (!requestedFile.exists()){
                writer.println(HTTPResponses.HTTP_NOT_FOUND.toString());
            }
        }
        try{
            clientSocket.close();
        }
        catch (IOException e){
            System.out.println("Error when trying to get close client");
        }

    }
}
