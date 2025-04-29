package com.github.jamesbhall423.revelationandroid.model;

public class Pathfinder {
	private Spot[][] board;	
	/**
	 * Method reset
	 *
	 *
	 */
	public void reset() {
		for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) board[y][x].reset();
	}

	/**
	 * Method path
	 *
	 *
	 * @param xfrom
	 * @param yfrom
	 * @param xto
	 * @param yto
	 *
	 * @return
	 *
	 */
	public boolean path(int[] xfrom, int[] yfrom, int[] xto, int[] yto) {
		for (int i = 0; i < xfrom.length; i++) if (xfrom[i]>=board[0].length) throw new IllegalArgumentException("xfrom val of "+xfrom[i]+" is greater than the allowed value of "+board[0].length);
		for (int i = 0; i < yfrom.length; i++) if (yfrom[i]>=board.length) throw new IllegalArgumentException("yfrom val of "+yfrom[i]+" is greater than the allowed value of "+board.length);
		for (int i = 0; i < xto.length; i++) if (xto[i]>=board[0].length) throw new IllegalArgumentException("xto val of "+xto[i]+" is greater than the allowed value of "+board[0].length);
		for (int i = 0; i < yto.length; i++) if (yto[i]>=board.length) throw new IllegalArgumentException("yto val of "+yto[i]+" is greater than the allowed value of "+board.length);
		
		for (int i = 0; i < xfrom.length; i++) if (path(xfrom[i],yfrom[i],xto,yto)) return true;
		return false;
	}
	/**
	 * Method path
	 *
	 *
	 * @param xfrom
	 * @param yfrom
	 * @param xto
	 * @param yto
	 *
	 * @return
	 *
	 */
	public Spot pathEndSpot(int[] xfrom, int[] yfrom, int[] xto, int[] yto) {
		for (int i = 0; i < xfrom.length; i++) if (xfrom[i]>=board[0].length) throw new IllegalArgumentException("xfrom val of "+xfrom[i]+" is greater than the allowed value of "+board[0].length);
		for (int i = 0; i < yfrom.length; i++) if (yfrom[i]>=board.length) throw new IllegalArgumentException("yfrom val of "+yfrom[i]+" is greater than the allowed value of "+board.length);
		for (int i = 0; i < xto.length; i++) if (xto[i]>=board[0].length) throw new IllegalArgumentException("xto val of "+xto[i]+" is greater than the allowed value of "+board[0].length);
		for (int i = 0; i < yto.length; i++) if (yto[i]>=board.length) throw new IllegalArgumentException("yto val of "+yto[i]+" is greater than the allowed value of "+board.length);
		
		for (int i = 0; i < xfrom.length; i++) {
			Spot out = pathEndSpot(xfrom[i],yfrom[i],xto,yto);
			if (out!=null) return out;
		}
		return null;
	}

	/**
	 * Method PathFinder
	 *
	 *
	 * @param game
	 * @param condition
	 *
	 */
	public Pathfinder(SquareModel[][] game, SquareCondition condition) {
		board = new Spot[game.length][game[0].length];
		for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) board[y][x] = new Spot(game[y][x],condition,5);
		add2DConnections();
		for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
			int[] teleporter = game[y][x].getTeleporter();
			if (teleporter!=null) board[y][x].setNeighbor(board[teleporter[1]][teleporter[0]],4);
		}
	}
	public Pathfinder(Spot[][] spots) {
		board=spots;
	}
	public Pathfinder(boolean[][] game) {
		board = new Spot[game.length][game[0].length];
		for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) board[y][x] = new Spot(game[y][x],4,x,y);
		add2DConnections();
	}
	private void add2DConnections() {
		for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
			if (y+1<board.length) board[y][x].setNeighbor(board[y+1][x],0);
			if (x+1<board[0].length) board[y][x].setNeighbor(board[y][x+1],1);
			if (y>0) board[y][x].setNeighbor(board[y-1][x],2);
			if (x>0) board[y][x].setNeighbor(board[y][x-1],3);
		}
	}

	/**
	 * Method pathEndSpot
	 *
	 *
	 * @param xfrom
	 * @param yfrom
	 * @param xto
	 * @param yto
	 *
	 * @return
	 *
	 */
	public Spot pathEndSpot(int xfrom, int yfrom, int[] xto, int[] yto) {
		Spot next = board[yfrom][xfrom];
		Spot last = null;
		if (!next.open()) return null;
		do {
			next.setLast(last);
			last=next;
			if (onList(next.x,next.y,xto,yto)) return next;
			do {
				next=last.next();
				if (next==null) last=last.getLast();
			} while (next==null&&last!=null);
		} while (next!=null);
		return null;
	}
	/**
	 * Method path
	 *
	 *
	 * @param xfrom
	 * @param yfrom
	 * @param xto
	 * @param yto
	 *
	 * @return
	 *
	 */
	public boolean path(int xfrom, int yfrom, int[] xto, int[] yto) {
		return pathEndSpot(xfrom, yfrom, xto, yto) != null;
	}
	public static boolean onList(int x, int y, int[] xto, int[] yto) {
		for (int i = 0; i < xto.length; i++) if (x==xto[i]&&y==yto[i]) return true;
		return false;
	}
	public void printStatus() {
		for (int y = 0; y< board.length; y++) {
			for (int x = 0; x < board[y].length; x++) System.out.print(board[y][x].open()? "1 " :"0 ");
			System.out.println();
		}
	}
	public void printConnections() {
		for (int y = 0; y< board.length; y++) {
			for (int x = 0; x < board[y].length; x++) board[y][x].printNeighbors();
			System.out.println();
		}
	}
	public static int[] constArray(int val, int size) {
		int[] out = new int[size];
		for (int i = 0; i < out.length; i++) out[i]=val;
		return out;
	}
	public static int[] lineArray(int size) {
		int[] out = new int[size];
		for (int i = 0; i < out.length; i++) out[i]=i;
		return out;
	}
}
