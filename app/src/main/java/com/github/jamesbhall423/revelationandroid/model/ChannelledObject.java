package com.github.jamesbhall423.revelationandroid.model;


import java.io.Serializable;

public class ChannelledObject implements Serializable {
	private static final long serialVersionUID = 1L;
	//public Object channel;
	public Object message;
	public ChannelledObject() {
		
	}	
	public ChannelledObject(Object channel, Object message) {
		//this.channel=channel;
		this.message=message;
	}
}
