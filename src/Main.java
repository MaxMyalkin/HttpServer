import server.ThreadPool;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PORT = 9000;
    public static final int THREADS = 4;

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
//TODO лишний слеш в конце путей