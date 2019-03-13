import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ServerTest {

    @Test
    void Server_SimpleHead_ForwardSlash_Returns200() throws IOException {
        int portNumber = 5000;
        String ipAddress = "127.0.0.1";
        Client client = new Client();
        client.startConnection(ipAddress, portNumber);
        String response = client.sendMessage("/");
        client.endConnection();
        assertEquals(response, HTTPResponses.HTTP_OK.toString());
    }

    @Test
    void Server_SimpleHead_NonExistingFile_Returns404() throws IOException {
        int portNumber = 5000;
        String ipAddress = "127.0.0.1";
        Client client = new Client();
        client.startConnection(ipAddress, portNumber);
        String response = client.sendMessage("/no_file_here.txt");
        client.endConnection();
        assertEquals(response, HTTPResponses.HTTP_NOT_FOUND.toString());
    }

    @Test
    void endConnection() {
    }
}