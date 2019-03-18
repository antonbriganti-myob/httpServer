import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reader.MockReader;
import socket.MockSocket;
import writer.MockWriter;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    private MockReader reader;
    private MockSocket socket;
    private MockWriter writer;
    private ClientHandler handler;

    @BeforeEach
    void setup() {
        reader = new MockReader();
        socket = new MockSocket();
        writer = new MockWriter();
        handler = new ClientHandler(socket, reader, writer, new File("file"));
    }

    @Test
    void getRequestLineDecodesRequestLineFromRequest() {
        //Given
        String expectedRequestLine = "GET / HTTP/1.1";
        this.reader.setReadLineResponse(expectedRequestLine + "\n");

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
        this.reader.setReadLineResponse(headers);

        //When
        Map<String, String> actualHeaders = this.handler.getHeaders();


        //Then
        assertEquals(expectedHeaders, actualHeaders);
    }
}