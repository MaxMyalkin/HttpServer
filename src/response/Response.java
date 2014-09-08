package response;


import java.io.IOException;
import java.io.OutputStream;

public class Response {
    public static void write(String str, OutputStream outputStream) throws IOException{
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: WebServer\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + str.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + str;
        outputStream.write(result.getBytes());
        outputStream.flush();

    }
}
