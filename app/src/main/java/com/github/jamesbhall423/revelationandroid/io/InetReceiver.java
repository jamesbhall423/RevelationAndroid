package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.CBuffer;
import com.github.jamesbhall423.revelationandroid.model.ChannelledObject;

import java.io.* ;
import java.net.* ;
public class InetReceiver implements Runnable {
	public ObjectInputStream istream;
	public ObjectOutputStream ostream;
	public CBuffer buffer;
	public InetReceiver(Socket socket) throws IOException {
		istream = new ObjectInputStream(socket.getInputStream());
		ostream = new ObjectOutputStream(socket.getOutputStream());
	}
	@Override
	public void run() {
		try {
			while (true) {
				ostream.writeInt(buffer.recieveObject((ChannelledObject) istream.readObject()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}