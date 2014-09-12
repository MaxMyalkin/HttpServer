package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private final ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Socket> queue = new ConcurrentLinkedQueue<>();
    private final Object lock = new Object();
    AtomicInteger atomicInteger = new AtomicInteger(1);
    public ThreadPool(int numberThreads) throws Throwable {
        for(int i = 0; i < numberThreads; i++){
            Worker w = new Worker(this);
            workers.add(w);
            new Thread(w).start();
        }
    }

    public synchronized void addWorker(Worker worker) {
        workers.add(worker);
        handleSocket();
    }

    public void handleSocket() {
        try {
            Worker w = workers.poll();
            Socket s = queue.poll();
            if (w != null && s != null)
                w.setSocket(s);
            else {
                if (w == null) {
                    queue.add(s);
                    System.out.println("Socket returned");
                }
                if (s == null) {
                    workers.add(w);
                    System.out.println("Worker returned");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addSocket(Socket socket) {
            queue.add(socket);
            handleSocket();
    }
}
