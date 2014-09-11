package server;

import request.Request;
import response.Response;

import java.io.IOException;
import java.net.Socket;

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
                    //System.err.println("Поток ждет");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.err.println("Client");
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
        System.err.println("Client processing finished");
    }
}