package httpResponse;

public class HTTPMessage {
    HTTPStatusCodes statusCode;
    HTTPHeaders headers;
    String body;

    public HTTPMessage(HTTPStatusCodes statusCode) {
        this.statusCode = statusCode;
        this.headers = new HTTPHeaders();
        this.body = "";
    }

    public HTTPMessage(HTTPStatusCodes statusCode, HTTPHeaders headers) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = "";
    }

    public HTTPMessage(HTTPStatusCodes statusCode, String body) {
        this.statusCode = statusCode;
        this.headers = new HTTPHeaders();
        this.body = body;
    }

    public HTTPMessage(HTTPStatusCodes statusCode, HTTPHeaders headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }


    @Override
    public String toString() {
        return String.format("%s\n%s\n%s", statusCode.toString(), headers.toString(), body);
    }
}
