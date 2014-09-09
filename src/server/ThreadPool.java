package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPool {
    private ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Socket> queue = new ConcurrentLinkedQueue<>();

    public ThreadPool(int numberThreads) throws Throwable {
        for(int i = 0; i < numberThreads; i++){
            workers.add(new Worker(this));
        }
        for(Worker worker : workers){
            new Thread(worker).start();
            System.err.println("Воркер");
        }
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
        if(queue != null && !queue.isEmpty()) {
            handleSocket(queue.poll());
            System.err.println("Воркер взял задачу после добавления в очередь");
        }
    }

    public void handleSocket(Socket socket) {
        if(workers != null && !workers.isEmpty())
            try {
                workers.poll().setSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void addSocket(Socket socket) {
        if(!workers.isEmpty()) {
            handleSocket(socket);
            System.err.println("Сразу взял");
        }
        else {
            queue.add(socket);
            System.err.println("Сокет в очереди");
        }
    }
}
