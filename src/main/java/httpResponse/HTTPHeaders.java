package httpResponse;

import java.util.HashMap;
import java.util.Map;

public class HTTPHeaders{
    private Map<String, String> headers;

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
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return obj.toString().equals(this.toString());
    }
}
