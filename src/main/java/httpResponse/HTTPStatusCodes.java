package httpResponse;

public enum HTTPStatusCodes {
    HTTP_OK("HTTP/1.1 200 OK"),
    HTTP_NOT_FOUND("HTTP/1.1 404 NOT FOUND"),
    HTTP_INTERNAL_SERVER_ERROR("HTTP/1.1 500 INTERNAL SERVER ERROR"),
    HTTP_NOT_IMPLEMENTED("HTTP/1.1 501 NOT IMPLEMENTED");

    private final String response;

    HTTPStatusCodes(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return response;
    }
}
