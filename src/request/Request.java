package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    public static String read(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String request = "";
        while(true) {
            String s = br.readLine();
            request += s;
            if(s == null || s.trim().length() == 0) {
                return request;
            }

        }

    }
}
