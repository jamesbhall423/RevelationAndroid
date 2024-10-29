package com.github.jamesbhall423.revelationandroid.model;


import java.io.Serializable;

public class ChannelledObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public Object message;
	public ChannelledObject(Object message) {
		this.message=message;
	}
}
