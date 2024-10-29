package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.CAction;
import com.github.jamesbhall423.revelationandroid.model.CBuffer;
import com.github.jamesbhall423.revelationandroid.model.ChannelledObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class InetBuffer implements CBuffer, Runnable {
    private ArrayBlockingQueue<ChannelledObject> queue = new ArrayBlockingQueue<>(20);
    private ObjectInputStream in;
    private InetWriter out;
    private boolean closed = false;
    private ClosingLock lock;
    public InetBuffer(ObjectInputStream in, ObjectOutputStream out, Socket socket) {
        this.in = in;
        lock = new ClosingLock(2,socket);
        this.out = new InetWriter(out,lock);
        new Thread(this).start();
    }
    @Override
    public boolean hasObjects() {
        return queue.size()>0;
    }

    @Override
    public ChannelledObject getObject() {
        return queue.poll();
    }

    @Override
    public int sendObject(Object o, int channel) {
        if (channel==ECHO) queue.offer(new ChannelledObject(o));
        out.writeObject(o);
        return 0;
    }

    @Override
    public int recieveObject(ChannelledObject o) {
        queue.offer(o);
        return 0;
    }

    @Override
    public void run() {
        try {
            while (!closed) {
                queue.offer(new ChannelledObject(in.readObject()));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Opponent Left");
            queue.offer(new ChannelledObject(CAction.EXIT.create(-1,-1)));
        }
        lock.close();
    }
    public void close() {
        closed = true;
        out.close();
    }
}
