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
        handleSocket();
        //System.err.println("Воркер взял задачу после добавления в очередь");
    }

    public void handleSocket() {
            try {
                Worker w = workers.poll();
                Socket s = queue.poll();
                if(w != null && s != null)
                    w.setSocket(s);
                else {
                    if(w != null)
                        workers.add(w);
                    if(s != null)
                        queue.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void addSocket(Socket socket) {
            queue.add(socket);
            handleSocket();
            //System.err.println("Сразу взял");
    }
}
