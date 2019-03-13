public enum HTTPResponses {
    HTTP_OK{
        public String toString() {return "HTTP/1.1 200 OK";}
    },
    HTTP_NOT_FOUND{
        public String toString() {return "HTTP/1.1 404 NOT FOUND";}
    }
}
