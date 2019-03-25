import httpResponse.HTTPHeaders;
import httpResponse.HTTPMessage;
import httpResponse.HTTPStatusCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        String fileDirectory = "/Users/anton.briganti/Documents/Development/httpServerApplication/cob_spec/public/";
        handler = new ClientHandler(writer, reader, new File(fileDirectory));
    }

    @Test
    void getRequestLineDecodesRequestLineFromRequest() throws IOException {
        //Given
        String expectedRequestLine = "GET / HTTP/1.1";
        when(reader.readLine()).thenReturn(expectedRequestLine);

        //When
        String line = this.handler.getRequestLine();

        //Then
        assertEquals(expectedRequestLine, line);
    }

    @Test
    void getHeadersReturnsRequestHeaders() throws IOException {
        //Given
        String[] headers = {"Header1: value1", "Header2: value2", ""};
        HTTPHeaders expectedHeaders = new HTTPHeaders();
        expectedHeaders.addHeader("Header1", "value1");
        expectedHeaders.addHeader("Header2", "value2");
        when(reader.readLine()).thenReturn(headers[0]).thenReturn(headers[1]).thenReturn(headers[2]);

        //When
        HTTPHeaders actualHeaders = handler.getHeaders();

        //Then
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    void sendResponseToClient_invocation_writesToSocket() {
        //Given
        HTTPMessage message = new HTTPMessage(HTTPStatusCodes.HTTP_OK);

        //When
        handler.sendResponseToClient(message);

        //Then
        //Verify that the stub method writer.println() was called, with any argument
        verify(writer).println(anyString());
    }

    @Test
    void sendResponseToClient_invocation_sendsOKFormattedMessage() {
        //Given
        //Setup the expected response format
        HTTPStatusCodes sampleStatus = HTTPStatusCodes.HTTP_OK;
        HTTPHeaders sampleHeaders = new HTTPHeaders();
        sampleHeaders.addHeader("Header1", "value1");
        sampleHeaders.addHeader("Header2", "value2");
        String body = "testbody";
        String expectedClientResponse = String.format("%s\n%s\n%s", HTTPStatusCodes.HTTP_OK, sampleHeaders.toString(), body);
        HTTPMessage message = new HTTPMessage(HTTPStatusCodes.HTTP_OK, sampleHeaders, body);

        //When
        handler.sendResponseToClient(message);

        //Then
        //Verify that the stub method writer.println() was called with the actual argument
        verify(writer).println(expectedClientResponse);
    }
}