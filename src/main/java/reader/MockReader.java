package reader;

public class MockReader implements ReaderInterface {

    private String readLineResponse;
    private int currentLine = -1;

    public void setReadLineResponse(String response) {
        this.readLineResponse = response;
    }

    @Override
    public String readLine() {
        currentLine++;
        try {
            return readLineResponse.split("\n")[currentLine];
        }
        catch (IndexOutOfBoundsException e){
            return "";
        }
    }

    @Override
    public void close() {

    }
}
