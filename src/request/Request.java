package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

public class Request {
    private String method;
    private String path;
    private String suffix;
    private InputStream inputStream;

    public Request(InputStream is) throws IOException {
        inputStream = is;
        read();
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

    public void read() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while(true) {
            String s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                return;
            }
            if(!s.contains(":")) {
                int spaceIdx = s.indexOf(" ");
                this.method = s.substring(0, spaceIdx).trim();
                int httpIdx = s.indexOf("HTTP/1.");
                int queryIdx = s.indexOf("?");
                if(queryIdx == -1 || queryIdx > httpIdx)
                    this.path = s.substring(spaceIdx, httpIdx).trim();
                else
                    this.path = s.substring(spaceIdx, queryIdx).trim();
                this.path = URLDecoder.decode(path, "UTF-8");
                int suffixIdx = path.lastIndexOf(".");
                if(suffixIdx != -1) {
                    this.suffix = path.substring(suffixIdx, path.length());
                }
            }
        }
    }
}
