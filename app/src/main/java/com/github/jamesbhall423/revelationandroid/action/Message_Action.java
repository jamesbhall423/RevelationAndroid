package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class Message_Action extends CAction {
	public static final Message_Action MESSAGE = new Message_Action();
	private String message = "No value";
	/**
	 * Method Message_Action
	 *
	 *
	 */
	private Message_Action() {
		super(0,0,"Message",true);
	}
	@Override
	public String toString() {
		String out = super.toString();
		out+=message+LN;
		return out;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	@Override
	protected void process(BoxModel boxModel) {
		throw new UnsupportedOperationException("Message Action has not been implemented.");
	}
}
