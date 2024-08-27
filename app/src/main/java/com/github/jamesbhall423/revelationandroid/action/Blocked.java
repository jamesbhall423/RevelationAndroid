package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;

public class Blocked extends CAction {
	public static final Blocked BLOCKED = new Blocked();	
	/**
	 * Method Blocked
	 *
	 *
	 */
	private Blocked() {
		super(0,0,"Blocked",false);
	}
	@Override
	protected void process(BoxModel boxModel) {
		boxModel.updateBlocked(player());
		if (boxModel.allBlocked()) boxModel.setEndStatus(BoxModel.EndStatus.BLOCKED);
	}	
}
