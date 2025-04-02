package com.github.jamesbhall423.revelationandroid.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

public class ShareServer {
    public static final String IDENTIFIER = "com.github.jamesbhall423.revelationandroid.io.ShareServer";
    public static final JSONSerializer SERIALIZER = JSONSerializer.REVELATION_SERIALIZER;
    public static final int PORT = 4111;
    private String packageName;
    private Updater updater;
    private ServerSocket serverSocket;
    private Socket connectSocket;
    public interface Updater {
        void update(String fileName);
    }
    public ShareServer(String packageName, Updater updater) throws IOException {
        this.packageName = packageName;
        this.updater = updater;
        serverSocket = new ServerSocket(PORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        connectSocket = serverSocket.accept();
                        RevelationInputStream inputStream = new RevelationInputStream(connectSocket.getInputStream(), SERIALIZER);
                        if (!inputStream.readString().equals(IDENTIFIER)) connectSocket.close();
                        else {
                            String fileName = inputStream.readString();
                            try {
                                inputStream.readCMap().writeCMap(ShareServer.this.packageName,fileName, SERIALIZER);
                                if (ShareServer.this.updater!=null) ShareServer.this.updater.update(fileName);
                            } catch (IllegalAccessException e) {

                            }
                        }
                    }
                } catch (IOException e) {

                }
            }
        }).start();
    }
    public void close() throws IOException {
        serverSocket.close();
    }
}
