package com.github.jamesbhall423.revelationandroid.model;

import java.io.* ;
import java.net.* ;
public class InetReciever implements Runnable {
	private ObjectInputStream istream;
	private ObjectOutputStream ostream;
	private CBuffer buffer;
	public InetReciever(Socket socket, CBuffer buffer) throws IOException {
		this.buffer=buffer;
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