package com.github.jamesbhall423.revelationandroid.io;

import java.io.IOException;
import java.net.Socket;

public class ClosingLock {
    private int closeCount = 0;
    private int maxCloseCount;
    private Socket socket;
    public ClosingLock(int maxCloseCount, Socket socket) {
        this.maxCloseCount = maxCloseCount;
        this.socket = socket;
    }
    public synchronized void close() {
        closeCount++;
        if (closeCount>=maxCloseCount) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
