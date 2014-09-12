package server;

import request.Request;
import response.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {

    private Socket socket;
    private ThreadPool pool;
    private AtomicInteger atomicInteger = new AtomicInteger(1);

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
                    //System.err.println("Поток ждет");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                handleRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                socket = null;
                pool.addWorker(this);
            }
        }
    }

    private void handleRequest() throws IOException {
        Request request = new Request(socket.getInputStream());
        Response response = new Response(socket.getOutputStream(), request);
        response.write();

    }
}