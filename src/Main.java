import server.SocketProcessor;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PORT = 8080;

    public static void main(String[] args) throws Throwable {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(socket)).start();
        }
    }
}
