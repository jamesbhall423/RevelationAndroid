package com.github.jamesbhall423.revelationandroid.model;

public interface CBuffer {
	int ECHO = 1;
	int NORMAL = 0;
	/**
	 * Method hasObjects
	 *
	 *
	 * @return
	 *
	 */
	boolean hasObjects();

	/**
	 * Method getObject
	 *
	 *
	 * @return
	 *
	 */
	ChannelledObject getObject();

	int sendObject(Object o, int channel);
	int recieveObject(ChannelledObject o);
	void close();
}
