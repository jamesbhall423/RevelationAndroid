package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class DoTurn extends CAction {
	public static final DoTurn TURN = new DoTurn();	
	/**
	 * Method DoTurn
	 *
	 *
	 */
	private DoTurn() {
		super(0,1,"next turn",false);
	}
	@Override
	protected void process(BoxModel boxModel) {
		if (boxModel.testBlocked()) boxModel.distribute(CAction.BLOCKED.create(player(),endTime()));
	}	
}
