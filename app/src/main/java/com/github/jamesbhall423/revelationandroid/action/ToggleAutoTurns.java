package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class ToggleAutoTurns extends CAction {
	public static final ToggleAutoTurns TOGGLE_TURNS = new ToggleAutoTurns();	
	/**
	 * Method ToggleAutoTurns
	 *
	 *
	 */
	private ToggleAutoTurns() {
		super(0,1,"toggle turns",false);
	}
	@Override
	protected void process(BoxModel boxModel) {
		throw new UnsupportedOperationException("Unimplemented method 'ToggleAutoTurns.process'");
	}	
}
