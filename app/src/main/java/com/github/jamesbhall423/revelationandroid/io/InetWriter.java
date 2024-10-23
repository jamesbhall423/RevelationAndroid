package com.github.jamesbhall423.revelationandroid.io;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class InetWriter extends Thread {
    private final ClosingLock lock;
    private ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(20);
    private ObjectOutputStream ostream;
    private boolean closed = false;

    @Override
    public void run() {
        try {
            while (!closed) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }

                while(queue.size()>0) {
                    ostream.writeObject(queue.poll());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.close();
    }
    public void writeObject(Object o) {
        queue.offer(o);
        interrupt();
    }
    public InetWriter(ObjectOutputStream ostream, ClosingLock lock) {
        this.ostream = ostream;
        this.lock = lock;
        start();
    }
    public void close() {
        closed = true;
    }
}
