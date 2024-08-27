package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class Save extends CAction {
	public static final Save SAVE = new Save();	
	/**
	 * Method Save
	 *
	 *
	 */
	private Save() {
		super(0,0,"save",true);
	}
	@Override
	protected void process(BoxModel boxModel) {
		throw new UnsupportedOperationException("The save action has not been implemented.");
	}	
}
