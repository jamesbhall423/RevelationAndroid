package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.ChannelledObject;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;

import java.io.* ;
import java.net.* ;
public class InetBuffer extends SelfBuffer {
	public ObjectOutputStream ostream;
	public ObjectInputStream istream;
	public InetBuffer(Socket output) throws IOException {
		ostream = new ObjectOutputStream(output.getOutputStream());
		istream = new ObjectInputStream(output.getInputStream());
	}
	@Override
	public int recieveObject(ChannelledObject o) {
		try {
			ostream.writeObject(o);
			return istream.readInt();
		} catch (IOException e) {
			return 1;
		}
	}
}