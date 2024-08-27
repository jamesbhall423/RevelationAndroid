package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class DeclareVictory extends CAction {
	public static final DeclareVictory DECLARE = new DeclareVictory();	
	public static final String VICTORY = "Victory";
	public static final String LOSS = "Loss";
	/**
	 * Method DeclareVictory
	 *
	 *
	 */
	private DeclareVictory() {
		super(0,0,"declare win",true);
	}
	@Override
	protected void process(BoxModel boxModel) {
		switch (boxModel.testVictory(player())) {
			case -1:
			setResponse(LOSS);
			break;
			case 0:
			setResponse("Fail but still in");
			break;
			case 1:
			setResponse(VICTORY);
			break;
			default:
			throw new Error("Invalid result from testVictory().");
		}
	}	
}
