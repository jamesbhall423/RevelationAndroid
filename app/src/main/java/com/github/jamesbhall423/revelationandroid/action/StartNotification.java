package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class StartNotification extends CAction {
	private String message;
	/**
	 * Method StartAction
	 *
	 *
	 * @param player
	 * @param message
	 *
	 */
	public StartNotification(int player, String message) {
		super(player,0,"StartMessage",false);
		this.message=message;
	}	
	public String toString() {
		String out = super.toString();
		out += message+LN;
		return out;
	}
	@Override
	protected void process(BoxModel boxModel) {
		throw new UnsupportedOperationException("The 'process' method of 'StartNotification' should never be called.");
	}
}
