package com.github.jamesbhall423.revelationandroid.model;

public class Spot {
	private Spot[] neighbors;
	private boolean open;
	private Spot last;
	private int direction = -1;
	public final int x;
	public final int y;
	/**
	 * Method open
	 *
	 *
	 * @return
	 *
	 */
	public boolean open() {
		return open&&direction == -1;
	}

	/**
	 * Method next
	 *
	 *
	 * @return
	 *
	 */
	public Spot next() {
		do {
			direction++;
		} while (direction<neighbors.length&&(neighbors[direction]==null||!neighbors[direction].open()));
		if (direction<neighbors.length) {
			return neighbors[direction];
		}
		else  {
			return null;
		}
	}

	/**
	 * Method reset
	 *
	 *
	 */
	public void reset() {
		direction=-1;
		last=null;
	}

	/**
	 * Method last
	 *
	 *
	 * @return
	 *
	 */
	public Spot getLast() {
		return last;
	}
	public void setLast(Spot last) {
		this.last=last;
	}

	/**
	 * Method Spot
	 *
	 *
	 * @param square
	 * @param condition
	 * @param size
	 *
	 * @return
	 *
	 */
	public Spot(SquareModel square, SquareCondition condition, int size) {
		open = condition.conditionFulfilled(square)&&square.getType()!=SquareType.Mountain;
		neighbors = new Spot[size];
		x=square.getX();
		y=square.getY();
	}
	public Spot(boolean open, int size, int x, int y) {
		this.open=open;
		neighbors = new Spot[size];
		this.x=x;
		this.y=y;
	}

	/**
	 * Method setNeighbor
	 *
	 *
	 * @param spot
	 * @param index
	 *
	 */
	public void setNeighbor(Spot spot, int index) {
		neighbors[index]=spot;
	}
	@Override
	public String toString() {
		return "Spot at ("+x+","+y+")";
	}
	public void printNeighbors() {
		for (int i = 0; i < neighbors.length; i++) if (neighbors[i]!=null) System.out.print(neighbors[i].x+","+neighbors[i].y+" ");
	}
}
