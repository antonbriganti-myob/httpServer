package httpResponse;

public class HTTPMessage {
    HTTPStatusCodes statusCode;
//    Map<String, String> headers;
    String body;

    public HTTPMessage(HTTPStatusCodes statusCode) {
        this.statusCode = statusCode;
        this.body = "";
    }

    public HTTPMessage(HTTPStatusCodes statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("%s\n\n%s", statusCode.toString(), body);
    }
}
