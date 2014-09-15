package response;

import request.Request;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Response {
    public static final String DOCUMENT_ROOT = "/home/maxim/Projects/HighLoad/static";

    private Code code;
    private OutputStream outputStream;
    private Request request;

    public Response(OutputStream os, Request request) {
        outputStream = os;
        this.request = request;
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public void writeHeader(String suffix, int length) {
        String type = Type.get(suffix);
        if(type == null)
            type = "content/unknown";
        String header = "HTTP/1.1 " + code.getNumeric() + " " + code.getTranscript() + "\r\n" +
                "Date: " + getTime() + "\r\n" +
                "Content-Type: " + type + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
        writeData(header);
    }

    private void writeData(String data) {
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedInputStream getFileStream(String path) {
        boolean isDirectory = false;
        String dir = DOCUMENT_ROOT + path;
        File file = new File(dir);
        try {
            if(!file.getCanonicalPath().contains(DOCUMENT_ROOT)) {
                code = new Code(403);
                return null;
            }
            if(file.isDirectory()) {
                dir += "index.html";
                file = new File(dir);
                isDirectory = true;
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            code = new Code(200);
            return bis;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            if(isDirectory)
                code = new Code(403);
            else
                code = new Code(404);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeFile(BufferedInputStream bis, boolean isHeadMethod) {
        try {
            int length = 0;
            if(bis != null)
                length = bis.available();
            writeHeader(request.getSuffix(), length);
            if(bis != null & !isHeadMethod) {
                int in;
                byte[] byteArray = new byte[1024 * 50];
                while ((in = bis.read(byteArray)) != -1) {
                    outputStream.write(byteArray, 0, in);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write() {
        if(request.getMethod() == null)
            return;
        BufferedInputStream bis;
        switch (request.getMethod()) {
            case "GET":
                bis = getFileStream(request.getPath());
                writeFile(bis, false);
                break;
            case "HEAD":
                bis = getFileStream(request.getPath());
                writeFile(bis, true);
                break;
            default:
                code = new Code(405);
                writeHeader(request.getSuffix(), 0);
                writeData("405 method not allowed");
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
