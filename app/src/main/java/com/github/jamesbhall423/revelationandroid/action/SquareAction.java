package com.github.jamesbhall423.revelationandroid.action;

import com.github.jamesbhall423.revelationandroid.model.*;

public class SquareAction extends CAction {
	public final int x;
	public final int y;
	public final boolean revert;
	public final boolean side;
	private int displayX;
	private int displayY;
	/**
	 * Method SquareAction
	 *
	 *
	 * @param player
	 * @param time
	 * @param x
	 * @param y
	 *
	 */
	public SquareAction(int player, int x, int y, boolean revert,boolean side,int startTime) {
		super (player,1, revert ? "revert" : "place",false);
		this.x=x;
		this.y=y;
		this.displayX=x;
		this.displayY=y;
		this.revert=revert;
		this.side=side;
		setStartTime(startTime);
	}
	@Override
	public String toString() {
		String out = LN+getName()+"- player: "+(player()+1)+" time: "+getStartTime()+" at "+SquareClass.coordinates(displayX,displayY);
		if (getResponse()!=null) out+=LN+"result: "+getResponse();
		out+=LN;
		return out;
	}
	@Override
	protected void process(BoxModel boxModel) {
		SquareModel square = boxModel.getModelSquare(x,y);
		displayX=square.displayX();
		displayY=square.displayY();
		int contents = square.player();
		int result=contents;
		boolean called = square.called(side);
		if ((!called||!revert)&&square.canPlace(side,revert)) result=square.valueSide(side);
		if (player()==boxModel.player()) {
			square.setPlayer(result);
			setResponse(contents+" to "+result);
		} else {
			square.updatePlayer(result);
		}
	}
}
