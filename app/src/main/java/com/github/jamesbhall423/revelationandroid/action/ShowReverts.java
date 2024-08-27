package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;


public class ShowReverts extends CAction {
	public static final ShowReverts SHOW_REVERTS = new ShowReverts();	
	/**
	 * Method ShowReverts
	 *
	 *
	 */
	private ShowReverts() {
		super(0,0,"show filps",false);
	}
	@Override
	protected void process(BoxModel boxModel) {
		int height = boxModel.modelHeight();
		int width = boxModel.modelWidth();
		String result = "revealed:";
		for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
			SquareModel square = boxModel.getModelSquare(x,y);
			if (square.isView(boxModel.side())&&!square.isPlayer(boxModel.side())) {
				square.setView(square.player());
				result+= " "+SquareClass.coordinates(square.getX(),square.getY());
			}
		}
		setResponse(result);
	}	
}
