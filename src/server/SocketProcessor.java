package server;

import request.Request;
import response.Response;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class SocketProcessor implements Runnable {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public SocketProcessor(Socket socket) throws Throwable {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public void run() {
        try {
            Request.read(inputStream);
            Response.write("<html><body><h1>Hello from Habrahabr</h1></body></html>", outputStream);
        } catch (Throwable t) {
            /*do nothing*/
        } finally {
            try {
                socket.close();
            } catch (Throwable t) {
                /*do nothing*/
            }
        }
        System.err.println("Client processing finished");
    }
}