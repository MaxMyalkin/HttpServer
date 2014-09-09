package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadPool {
    private ConcurrentLinkedQueue<Worker> workers;

    public ThreadPool(int numberThreads) throws Throwable {
        workers = new ConcurrentLinkedQueue<>();
        for(int i=0; i< numberThreads; i++){
            workers.add(new Worker(this));
        }
        for(Worker worker : workers){
            new Thread(worker).start();
        }
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void handleSocket(Socket socket) throws IOException {
        if(workers != null && !workers.isEmpty())
            workers.poll().setSocket(socket);
    }

    public boolean isEmpty() {
        return workers != null && workers.isEmpty();

    }
}
