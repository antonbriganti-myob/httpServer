public enum HTTPResponses {
    HTTP_OK("HTTP/1.1 200 OK"),
    HTTP_NOT_FOUND("HTTP/1.1 404 NOT FOUND"),
    HTTP_NOT_IMPLEMENTED("HTTP/1.1 501 NOT IMPLEMENTED");

    private final String response;

    HTTPResponses(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return response;
    }
}
