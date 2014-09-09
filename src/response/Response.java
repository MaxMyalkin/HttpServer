package response;


import request.Request;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String DOCUMENT_ROOT = "D:\\Projects\\Highload\\WebServer\\document_root";
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
        suffixes.put("", "content/unknown");
        suffixes.put(".zip", "application/zip");
        suffixes.put(".jpg", "image/jpeg");
        suffixes.put(".jpeg", "image/jpeg");
        suffixes.put(".htm", "text/html");
        suffixes.put(".html", "text/html");
        suffixes.put(".text", "text/plain");
        suffixes.put(".txt", "text/plain");
        suffixes.put(".Java", "text/plain");
    }

    public static String makeHeader(Code code, String suffix, int length) {
        String type = suffixes.get(suffix);
        if(type == null)
            type = "content/unknown";
        return "HTTP/1.1 " + code.getNumeric() + " " + code.getTranscript() + " \r\n" +
                "Server: WebServer\r\n" +
                "Content-Type: " + type + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
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
            int in;
            byte[] byteArray = new byte[1024 * 50];
            while ((in = bis.read()) != -1){
                outputStream.write(byteArray, 0, in);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
