package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private String method;
    private String path;
    private String suffix;
    private InputStream inputStream;

    public Request(InputStream is) {
        inputStream = is;
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

    public String read() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String request = "";
        while(true) {
            String s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                return request;
            }
            if(!s.contains(":")) {
                //первая строка с запросом и методом
                int spaceIdx = s.indexOf(" ");
                this.method = s.substring(0, spaceIdx).trim();
                int httpIdx = s.indexOf("HTTP/1.");
                this.path = s.substring(spaceIdx, httpIdx).trim();
                int suffixIdx = path.lastIndexOf(".");
                this.suffix = path.substring(suffixIdx, path.length());
            }
            request += s;

        }
    }
}
