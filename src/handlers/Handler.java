package handlers;


import request.Request;
import response.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.StandardOpenOption;

public class Handler {
    public static final String DOCUMENT_ROOT = "/home/maxim/Projects/HighLoad/WebServer/static";

    private Request request;
    private AsynchronousSocketChannel socketChannel;
    private Response response;

    public Handler(String request, AsynchronousSocketChannel socketChannel) {
        this.request = new Request(request);
        this.socketChannel = socketChannel;
        this.response = new Response();
    }

    public void parseRequest() throws IOException {
        String requestStr = request.getRequest();
        int firstLine = requestStr.indexOf("\r");
        int spaceIdx = requestStr.indexOf(" ");
        int httpIdx = requestStr.indexOf("HTTP/1.");
        int queryIdx = requestStr.indexOf("?");
        if(firstLine != -1) {
            if(spaceIdx != -1 && spaceIdx <= firstLine)
                request.setMethod(requestStr.substring(0, spaceIdx).trim());
            if(queryIdx != -1 && queryIdx < httpIdx) {
                request.setPath(requestStr.substring(spaceIdx, queryIdx).trim());
            } else {
                request.setPath(requestStr.substring(spaceIdx, httpIdx).trim());
            }
            String path = request.getPath();
            if(path != null) {
                int suffixIdx = path.lastIndexOf(".");
                if (suffixIdx != -1) {
                    request.setSuffix(path.substring(suffixIdx, path.length()));
                }
            }
            response.setSuffix(request.getSuffix());
            readFile();
        }

    }


    private void readFile() {
        boolean isDirectory = false;
        String dir = DOCUMENT_ROOT + request.getPath();
        File file = new File(dir);
        try {
            if(!file.getCanonicalPath().contains(DOCUMENT_ROOT)) {
                response.setCode(403);
            } else {
                if (file.isDirectory()) {
                    dir += "index.html";
                    file = new File(dir);
                    isDirectory = true;
                    response.setSuffix(".html");
                }
                if (!file.exists()) {
                    throw new FileNotFoundException("File not found");
                }
                response.setLength(file.length());
                if (request.getMethod().equals("GET")) {
                    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.READ);
                    ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
                    fileChannel.read(buffer, 0, null, new FileReadHandler<Integer, Void>(this, buffer, fileChannel));
                    response.setCode(200);
                    return;
                }
                if (request.getMethod().equals("HEAD")) {
                    response.setCode(200);
                    writeToSocket();
                    return;
                }

                response.setCode(405);
            }
            writeToSocket();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            response.setLength(0);
            if(isDirectory)
                response.setCode(403);
            else
                response.setCode(404);
            writeToSocket();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToSocket() {
        String responseStr = response.getHeader();
        ByteBuffer buffer = ByteBuffer.allocate(responseStr.length()).put(responseStr.getBytes());
        buffer.position(0);
        try {
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, buffer.capacity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketChannel.write(buffer, null, new SocketWriteHandler<Integer, Void>(socketChannel));
    }

    public void writeToSocket(ByteBuffer buffer) {
        int length = response.getHeader().length() + buffer.capacity();
        try {
            buffer.flip();
            ByteBuffer newB = ByteBuffer.allocate(length).put(ByteBuffer.wrap(response.getHeader().getBytes())).put(buffer);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, newB.capacity());
            newB.position(0);
            socketChannel.write(newB, null, new SocketWriteHandler<Integer, Void>(socketChannel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
