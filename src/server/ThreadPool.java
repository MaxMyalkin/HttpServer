package server;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private final ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<AsynchronousSocketChannel> channels = new ConcurrentLinkedQueue<>();
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
            AsynchronousSocketChannel ch = channels.poll();
            if (w != null && ch != null)
                w.setChannel(ch);
            else {
                if (w == null && ch != null) {
                    channels.add(ch);
                    System.out.println("Socket returned");
                }
                if (ch == null && w != null) {
                    workers.add(w);
                    System.out.println("Worker returned");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addSocketChannel(AsynchronousSocketChannel channel) {
            channels.add(channel);
            handleSocket();
    }
}
