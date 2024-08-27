package com.github.jamesbhall423.revelationandroid.model;

public enum SquareType {
	Empty,Mountain,Forest,Road;
	public static SquareType getType(int ordinal) {
		switch (ordinal) {
			case 0:
			return Empty;
			case 1:
			return Mountain;
			case 2:
			return Forest;
			case 3:
			return Road;
			default:
			throw new IllegalArgumentException("Ordinal ("+ordinal+") does not reflect the range of the enum. (0-3)");
		}
		
	}
}