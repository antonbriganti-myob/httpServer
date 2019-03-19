import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reader.MockReader;
import socket.MockSocket;
import writer.MockWriter;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    @Mock
    private BufferedReader reader;
    @Mock
    private Socket socket;
    private ClientHandler handler;

    @BeforeEach
    void setup() throws IOException {
//        reader = new MockReader();
        socket = mock(Socket.class);
        Socket outSocket = mock(Socket.class);
        reader = mock(BufferedReader.class);
        when(socket.getOutputStream()).thenReturn(outSocket);
//        writer = new MockWriter();
        String fileDirectory = "/Users/anton.briganti/Documents/Development/httpServerApplication/cob_spec/public";
        handler = new ClientHandler(socket, reader, new File(fileDirectory));
    }

    @Test
    void getRequestLineDecodesRequestLineFromRequest() {
        //Given
        String expectedRequestLine = "GET / HTTP/1.1";
//        this.reader.setReadLineResponse(expectedRequestLine + "\n");

        //When
        String line = this.handler.getRequestLine();

        //Then
        assertEquals(expectedRequestLine, line);
    }

    @Test
    void getHeadersReturnsRequestHeaders() {
        //Given
        String headers = "Header1: value1\nHeader2: value2\n\n";
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Header1", "value1");
        expectedHeaders.put("Header2", "value2");
//        this.reader.setReadLineResponse(headers);

        //When
        Map<String, String> actualHeaders = this.handler.getHeaders();


        //Then
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    void sendResponseToClient() {
        PrintWriter printWriter = mock(PrintWriter.class);
        HTTPMessage message = new HTTPMessage(HTTPStatusCodes.HTTP_OK);
        handler.sendResponseToClient(message);
        verify(printWriter, times(1)).println(HTTPStatusCodes.HTTP_OK.toString());
    }
}