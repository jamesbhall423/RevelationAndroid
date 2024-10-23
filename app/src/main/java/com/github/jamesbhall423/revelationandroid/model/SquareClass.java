package com.github.jamesbhall423.revelationandroid.model;

import java.io.Serializable;

public class SquareClass implements SquareModel, Serializable {
	private static final long serialVersionUID = 1L;
	private SquareState state;
	private int[] view = new int[2];
//	private boolean called1 = false;
//	private boolean called2 = false;
	private boolean highlight = false;
    private SquareViewUpdater updater = null;
	public final int X;
	public final int Y;
	private boolean flipDisplay = false;
	private int globalPlayerIndex;
    public void registerUpdater(SquareViewUpdater updater) {
        this.updater = updater;
    }
	/**
	 * Method Square
	 *
	 *
	 * @param x
	 * @param y
	 * @param state
	 *
	 */
	public SquareClass(int x, int y, SquareState state) {
		X=x;
		Y=y;
		this.state = state;
		view[0]=state.contents;
		view[1]=state.contents;
	}
	public void setGlobalPlayerIndex(int globalPlayerIndex) {
		this.globalPlayerIndex = globalPlayerIndex;
	}
	public void setFlipDisplay(boolean flipDisplay) {
		this.flipDisplay = flipDisplay;
	}

	/**
	 * Method isEmpty
	 *
	 *
	 * @return
	 *
	 */
	@Override
	public boolean isEmpty() {
		return state.contents==0;
	}

	/**
	 * Method isPlayer
	 *
	 *
	 * @param player1
	 *
	 * @return
	 *
	 */
	@Override
	public boolean isPlayer(boolean player1) {
		if (player1) return state.contents==1;
		else return state.contents==-1;
	}
	/**
	 * Method isView
	 *
	 *
	 * @param player1
	 *
	 * @return
	 *
	 */
	@Override
	public boolean isView(int callingPlayer,boolean player1) {
		if (player1) return view[callingPlayer]==1;
		else return view[callingPlayer]==-1;
	}

	/**
	 * Method player
	 *
	 *
	 * @return
	 *
	 */
	@Override
	public int player() {
		return state.contents;
	}

	/**
	 * Method setPlayer
	 *
	 *
	 * @param player
	 *
	 */
	@Override
	public void setPlayer(int callingPlayer,boolean player) {
		if (player) {
			state.contents=1;
			view[callingPlayer]=1;
		} else {
			state.contents=-1;
			view[callingPlayer]=-1;
		}
		if (callingPlayer==globalPlayerIndex) updater.update(X,Y);
	}

	/**
	 * Method getTeleporter
	 *
	 *
	 * @return
	 *
	 */
	@Override
	public int[] getTeleporter() {
		if (state.teleporter!=null) return (int[]) state.teleporter.clone();
		else return null;
	}

	/**
	 * Method setTeleporter
	 *
	 *
	 * @param dest
	 *
	 */
	@Override
	public void setTeleporter(int[] dest) {
		state.teleporter=dest;
		if (dest!=null) state.teleporter = (int[]) dest.clone();
		updater.update(X,Y);
	}

	/**
	 * Method time
	 *
	 *
	 * @return
	 *
	 */
	@Override
	public int time() {
		switch (state.type) {
			case Empty:
			return 1;
			case Mountain:
			return 0;
			case Forest:
			return 2;
			case Road:
			return -2;
		}
		throw new Error("Type is immposible: "+state.type);
	}
	/**
	 * Method canPlace
	 *
	 *
	 * @param player
	 *
	 * @return
	 *
	 */
	@Override
	public boolean canPlace(int player, boolean revert) {
//		if (player) called1=true;
//		else called2=true;
		if (time()==0) return false;
		else if (view[player]!=0) return false;
		else if (state.contents==0) return !revert;
		else return revert;
//		else return revert&&(player!=(state.contents>0));
	}

	/**
	 * Method getView
	 *
	 *
	 * @return
	 *
	 */
	@Override
	public int getView(int callingPlayer) {
		return view[callingPlayer];
	}

	/**
	 * Method setView
	 *
	 *
	 * @param observed
	 *
	 */
	@Override
	public void setView(int callingPlayer, int observed) {
		view[callingPlayer]=observed;
		if (callingPlayer==globalPlayerIndex) updater.update(X,Y);
	}
	@Override
	public void setHighlight(boolean highlight) {
		this.highlight=highlight;
		updater.update(X,Y);
	}
	@Override
	public boolean getHighlight() {
		return highlight;
	}
	@Override
	public SquareType getType() {
		return state.type;
	}
	@Override
	public void setType(SquareType type) {
		state.type=type;
		updater.update(X,Y);
	}
	@Override
	public int getRoad() {
		return state.road;
	}
	@Override
	public void setRoad(int road) {
		state.road=road;
		updater.update(X,Y);
	}
//	@Override
//	public boolean called(boolean player) {
//		if (time()==0) return true;
//		if (player) return called1;
//		else return called2;
//	}
	public static String coordinates(int X, int Y) {
		char first = (char)('a'+Y);
		int x = X+1;
		if (x<10) return first+"0"+x;
		else return first+""+x;
	}
	@Override
	public SquareState getState() {
		return state;
	}
	@Override
	public SquareModel setState(SquareState state) {
		this.state=state;
		view[0]=state.contents;
		view[1]=state.contents;
//		if (state.contents!=0) {
//			called1=true;
//			called2=true;
//		}
		return this;
	}
	@Override
	public void setPlayer(int callingPlayer, int player) {
		view[callingPlayer]=player;
		state.contents=player;
		if (callingPlayer==globalPlayerIndex) updater.update(X,Y);
	}
	@Override
	public void updatePlayer(int player) {
		state.contents=player;
	}
	public int valueSide(boolean side) {
		if (side) return 1;
		else return -1;
	}
	@Override
	public int getX() {
		return X;
	}
	@Override
	public int getY() {
		return Y;
	}
	public boolean flipDisplay() {
		return flipDisplay;
	}
	public boolean isRoad(int direction) {
		return (state.road&direction)>0;
	}
	public static int flipSingleDir(int dir) {
		if (dir<=RIGHT) return dir*4;
		else return dir/4;
	}
	public int displayPlayer() {
		if (flipDisplay()) return -state.contents;
		else return state.contents;
	}
	@Override
	public int displayX() {
		return flipDisplay ? Y : X;
	}
	@Override
	public int displayY() {
		return flipDisplay ? X : Y;
	}
}
