package server;

import request.Request;
import response.Response;

import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ThreadPool pool;

    public Worker(ThreadPool pool) {
        this.pool = pool;
    }

    public synchronized void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        notify();
    }

    public synchronized void run() {
        while (true) {
            if(socket == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handleRequest();
            pool.addWorker(this);
            socket = null;
        }
    }

    void handleRequest() {
        try {
            String request = Request.read(inputStream);
            Response.write(request, outputStream);
        } catch (Throwable ignored) {

        } finally {
            try {
                socket.close();
            } catch (Throwable ignored) {

            }
        }
        System.err.println("Client processing finished");
    }
}