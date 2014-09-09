import server.ThreadPool;
import server.Worker;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final int PORT = 8080;
    public static final int THREADS = 2;



    public static void main(String[] args) throws Throwable {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ThreadPool pool = new ThreadPool(THREADS);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                pool.addSocket(socket);

            }
            catch (Exception e) {
                break;
            }
        }
    }
}
