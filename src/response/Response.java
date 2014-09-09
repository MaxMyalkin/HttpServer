package response;


import request.Request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String DOCUMENT_ROOT = "/home/maxim/Projects/HighLoad/WebServer/static";
    public static final Map<String, String> suffixes = new HashMap<String, String>();

    private Code code;
    private OutputStream outputStream;
    private Request request;

    public Response(OutputStream os, Request request) {
        outputStream = os;
        this.request = request;
        fillMap();
    }

    private void fillMap() {
        suffixes.put(".jpg", "image/jpeg");
        suffixes.put(".jpeg", "image/jpeg");
        suffixes.put(".html", "text/html");
        suffixes.put(".js", "application/javascript");
        suffixes.put(".css", "text/css");
        suffixes.put(".png", "image/png");
        suffixes.put(".gif", "image/gif");
        suffixes.put(".swf", "application/x-shockwave-flash");
    }

    public static String makeHeader(Code code, String suffix, int length) {
        String type = suffixes.get(suffix);
        if(type == null)
            type = "content/unknown";
        return "HTTP/1.1 " + code.getNumeric() + " " + code.getTranscript() + " \r\n" +
                "Server: WebServer\r\n" +
                "Content-Type: " + type + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: keep-alive\r\n\r\n";
    }

    public void write(){
        try {
            File file = new File(DOCUMENT_ROOT, request.getPath());
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            if(file.exists())
                code = new Code(200);
            else
                code = new Code(404);


            String header = makeHeader(code, request.getSuffix(), bis.available());
            outputStream.write(header.getBytes());
            outputStream.flush();
            int in;
            byte[] byteArray = new byte[1024 * 50];
            while ((in = bis.read(byteArray)) != -1){
                outputStream.write(byteArray, 0, in);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
