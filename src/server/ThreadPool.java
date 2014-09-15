package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPool {
    private final ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Socket> queue = new ConcurrentLinkedQueue<>();
    public ThreadPool(int numberThreads) throws Throwable {
        for(int i = 0; i < numberThreads; i++){
            Worker w = new Worker(this);
            workers.add(w);
            new Thread(w).start();
        }
    }

    public synchronized void addWorker(Worker worker) {
        Socket socket = queue.poll();
        if(socket != null) {
            try {
                worker.setSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            workers.add(worker);
    }

    public synchronized void addSocket(Socket socket) {
        Worker worker = workers.poll();
        if(worker != null)
            try {
                worker.setSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            queue.add(socket);
    }
}
