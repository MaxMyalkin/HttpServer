package server;

import request.Request;
import response.Response;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {

    private Socket socket;
    private ThreadPool pool;

    public Worker(ThreadPool pool) {
        this.pool = pool;
    }

    public synchronized void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        notify();
    }

    public synchronized void run() {
        while (true) {
            if(socket == null) {
                try {
                    System.err.println("Поток ждет");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.err.println("Начато выполнение");
            handleRequest();
            socket = null;
            pool.addWorker(this);
        }
    }

    void handleRequest() {
        try {
            Request request = new Request(socket.getInputStream());
            request.read();
            Response response = new Response(socket.getOutputStream(), request);
            response.write();
        } catch (Throwable e) {
                e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        System.err.println("Client processing finished");
    }
}