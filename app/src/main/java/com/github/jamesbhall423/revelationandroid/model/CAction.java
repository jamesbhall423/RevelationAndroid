package com.github.jamesbhall423.revelationandroid.model;

import java.io.Serializable;
import java.io.PrintStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.IOException;
import com.github.jamesbhall423.revelationandroid.action.*;

public abstract class CAction implements Cloneable, Serializable, Comparable<CAction> {
	public static final String LN = newLn();
	private static final long serialVersionUID = 1L;
	private static long nextID = 0L;
	public static final Exit EXIT = Exit.EXIT;
	public static final DeclareVictory DECLARE = DeclareVictory.DECLARE;
	public static final DoTurn TURN = DoTurn.TURN;
	public static final int VAL_TURN = 0;
	public static final int VAL_DELAYED = 2;
	public static final int VAL_STANDARD = 1;
	public static final int VAL_MAX = 2;
	public static final int VAL_MIN = 0;
	private long ID = nextID++;
	private int player;
	private int time;
	private int startTime;
	private String name;
	private String response;
	private boolean isPublic;


	/**
	 * Method CAction
	 *
	 *
	 * @param player
	 * @param time
	 *
	 */
	protected CAction(int player, int time,String name,boolean isPublic) {
		this.player=player;
		this.time=time;
		this.name=name;
		this.isPublic=isPublic;
	}
	public int player() {
		return player;
	}
	public int time() {
		return time;
	}
	public String getName() {
		return name;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response=response;
	}
	public CAction create(int player, int startTime) {
		try {
			CAction out = (CAction) super.clone();
			out.player=player;
			out.setStartTime(startTime);
			out.updateID();
			return out;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	public String toString() {
		String out = LN+name+"- player: "+(player+1)+" time: "+startTime;
		if (response!=null) out+=LN+"result: "+response;
		out+=LN;
		return out;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setStartTime(int startTime) {
		this.startTime=startTime;
	}
	public int getStartTime() {
		return startTime;
	}
	private static String newLn() {
		try {
			byte[] bytes = new byte[10];
			PipedInputStream read = new PipedInputStream();
			final PrintStream write = new PrintStream(new PipedOutputStream(read));
			new Thread(new Runnable() {
				public void run() {
					write.println();
				}
			}).start();
			int use = read.read(bytes,0,10);
			byte[] control = new byte[use];
			for (int i = 0; i < use; i++) control[i]=bytes[i];
			String ln = new String(control);
			write.close();
			return ln;
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	public int delay() {
		return time==0 ? 1 : 0;
	}
	public int endTime() {
		return startTime+delay()+time;
	}

	public void updateID() {
		ID=nextID++;
	}
	public long ID() {
		return ID;
	}
	public int compareTo(CAction a) {
		int out = startTime-a.startTime;
		if (out!=0) return out;
		out = player()-a.player();
		if (out!=0) return out;
		out = typeVal() - a.typeVal();
		if (out!=0) return out;
		long l = ID()-a.ID();
		if (l<0L) return -1;
		else if (l>0L) return 1;
		else return 0;
	}
	public int typeVal() {
		if (delay()>0) return VAL_DELAYED;
		else if (getClass()==DoTurn.class) return VAL_TURN;
		else return VAL_STANDARD;
	}
    protected abstract void process(BoxModel boxModel);
}
