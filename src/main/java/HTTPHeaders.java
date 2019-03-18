import java.util.HashMap;
import java.util.Map;

public class HTTPHeaders{
    Map<String, String> headers;

    public HTTPHeaders() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(String key:headers.keySet()){
            result.append(key).append(": ").append(headers.get(key)).append("\n");
        }
        return result.toString();
    }
}
