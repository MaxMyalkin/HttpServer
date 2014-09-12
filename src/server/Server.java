package server;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static final int PORT = 9000;
    public static final int THREADS = 4;

    public static void main(String[] args) throws Throwable {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ThreadPool pool = new ThreadPool(THREADS);
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);

        while(true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                Socket s = socketChannel.socket();
                pool.addSocket(s);
            }
        }
        /*while (true) {
            try {
                Socket socket = serverSocket.accept();
                pool.addSocket(socket);
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }*/
    }
}
