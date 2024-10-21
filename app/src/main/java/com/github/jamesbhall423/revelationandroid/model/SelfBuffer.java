package com.github.jamesbhall423.revelationandroid.model;

import java.util.concurrent.ArrayBlockingQueue;
public class SelfBuffer implements CBuffer {
	private ArrayBlockingQueue<ChannelledObject> queue = new ArrayBlockingQueue<ChannelledObject>(20);
	private CBuffer[] other;
	/**
	 * Method hasObjects
	 *
	 *
	 * @return
	 *
	 */
	public boolean hasObjects() {
		return queue.peek()!=null;
	}

	/**
	 * Method getObject
	 *
	 *
	 * @return
	 *
	 */
	public ChannelledObject getObject() {
		return queue.remove();
	}

	/**
	 * Method newBuffer
	 *
	 *
	 * @param parm1
	 *
	 * @return
	 *
	 */
//	public CBuffer newBuffer(ChannelledObject o) {
//		return (CBuffer) (o.channel);
//	}

	/**
	 * Method sendObject
	 *
	 *
	 * @param parm1
	 *
	 * @return
	 *
	 */
//	public int sendObject(ChannelledObject o) {
//		return ((CBuffer) (o.channel)).recieveObject(o);
//	}

	/**
	 * Method sendObject
	 *
	 *
	 * @param parm1
	 * @param parm2
	 *
	 * @return
	 *
	 */
	public int sendObject(Object object, int channel) {
		ChannelledObject out = new ChannelledObject(this,object);
		if ((channel&ECHO)>0) recieveObject(out);
		int ret = 0;
		for (int i = 0; i < other.length; i++) ret &= other[i].recieveObject(out);
		return ret;
	}
	public void setOther(CBuffer[] other) {
		this.other=other;
	}
	public int recieveObject(ChannelledObject o) {
		if (queue.offer(o)) return 0;
		else return 1;
	}

	@Override
	public void close() {

	}

	public static void setLinks(SelfBuffer[] buffers) {
		CBuffer[][] links = new CBuffer[buffers.length][buffers.length-1];
		for (int i = 0; i < buffers.length; i++) {
			int k = 0;
			for (int j = 0; j < buffers.length-1; j++) {
				if (i==k) k++;
				links[i][j]=buffers[k];
				k++;
			}
			buffers[i].setOther(links[i]);
		}
	}
}
