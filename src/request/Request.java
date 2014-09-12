package request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Request {
    private String method;
    private String path;
    private String suffix;
    private String request;

    public Request(String request) {
        this.request = request;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getRequest() {
        return request;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        try {
            this.path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
