package com.github.jamesbhall423.revelationandroid.model;

import java.io.Serializable;


public class SquareState implements Cloneable, Serializable {
	private static final long serialVersionUID = 2L;
	public SquareType type = SquareType.Empty;
	public int road;
	public int[] teleporter = null;
	public int contents = 0;
	@Override
	public Object clone() {
		try {
			SquareState out = (SquareState) super.clone();
			if (teleporter!=null) out.teleporter=teleporter.clone();
			return out;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	public boolean hasTeleporter() {
		return teleporter!=null;
	}
	public int teleLabel() {
		return teleporter[2];
	}
}