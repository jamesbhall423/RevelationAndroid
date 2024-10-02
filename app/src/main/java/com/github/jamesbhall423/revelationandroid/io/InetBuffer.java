package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.CBuffer;
import com.github.jamesbhall423.revelationandroid.model.ChannelledObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class InetBuffer implements CBuffer, Runnable {
    private ArrayBlockingQueue<ChannelledObject> queue = new ArrayBlockingQueue<>(20);
    private ObjectInputStream in;
    private InetWriter out;
    public InetBuffer(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = new InetWriter(out);
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
        if (channel==ECHO) queue.offer(new ChannelledObject(channel,o));
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
            while (true) {
                queue.offer(new ChannelledObject(0,in.readObject()));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
