import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class ClientHandlerTest {

    /*
     We setup the Mock objects to fence off the functionality we want to test. In this case
     we are interested in testing the ClientHandler, so all else should be mocked/stubbed.

     Mock = empty implementation of the Class, to facilitate
     Stub = Functional implementation, setup to facilitate testing
      */

    @Mock
    private PrintWriter writer;
    @Mock
    private BufferedReader reader;
    private ClientHandler handler;

    @BeforeEach
    void setup() throws IOException {
        writer = mock(PrintWriter.class);
        reader = mock(BufferedReader.class);
        String fileDirectory = "/Users/anton.briganti/Documents/Development/httpServerApplication/cob_spec/public";
        handler = new ClientHandler(writer, reader, new File(fileDirectory));
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
    void sendResponseToClient_invocation_writesToSocket() {
        HTTPMessage message = new HTTPMessage(HTTPStatusCodes.HTTP_OK);
        handler.sendResponseToClient(message);
        //Verify that the stub method writer.println() was called, with any argument
        verify(writer).println(anyString());
    }

    @Test
    void sendResponseToClient_invocation_sendsOKFormattedMessage() {
        //Setup the expected response format
        String expectedClientResponse = String.format("%s\n\n", HTTPStatusCodes.HTTP_OK);
        HTTPMessage message = new HTTPMessage(HTTPStatusCodes.HTTP_OK);
        handler.sendResponseToClient(message);
        //Verify that the stub method writer.println() was called with the actual argument
        verify(writer).println(expectedClientResponse);
    }
}