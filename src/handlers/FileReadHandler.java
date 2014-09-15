package handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;

public class FileReadHandler<K, V> implements CompletionHandler<K,V> {

    private Handler handler;
    private ByteBuffer buffer;
    private AsynchronousFileChannel fileChannel;

    public FileReadHandler(Handler handler, ByteBuffer buffer, AsynchronousFileChannel fileChannel) {
        this.handler = handler;
        this.buffer = buffer;
        this.fileChannel = fileChannel;

    }

    @Override
    public void completed(K result, V attachment) {
        handler.writeToSocket(buffer);
        try {
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, V attachment) {

    }
}
