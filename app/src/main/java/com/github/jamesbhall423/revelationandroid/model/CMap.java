package com.github.jamesbhall423.revelationandroid.model;


import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
public class CMap implements Cloneable, Serializable {
	private static final long serialVersionUID = 2L;
	public SquareState[][] squares;
	public Player[] players;	
	/**
	 * Method writeCMap
	 * @param name
	 *
	 */
	public void writeCMap(String name) throws IOException {
		File writeTo;
		writeTo = new File("james/games/connection/maps/"+name+".cmap");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(writeTo));
		out.writeObject(this);
		out.close();
	}

	public CMap(SquareState[][] squares, Player[] players) {
		this.squares=squares;
		this.players=players;
	}
	public static String expand(String name) {
		return "maps/" +name+".cmap";
	}
	public Object clone() {
		try {
			CMap out = (CMap)super.clone();
			out.players=players.clone();
			for (int i = 0; i < players.length; i++) out.players[i]=(Player)players[i].clone();
			out.squares=squares.clone();
			for (int i = 0; i < squares.length; i++) out.squares[i]=squares[i].clone();
			for (int y = 0; y < squares.length; y++) for (int x = 0; x < squares[0].length; x++) out.squares[y][x]=(SquareState)(squares[y][x].clone());
			return out;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	public static CMap read(String fileName) throws IOException {
		CMap out;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
			out = (CMap) in.readObject();
			in.close();
		} catch (ClassNotFoundException e) {
			throw new IOException("Invalid Class in Inputing "+fileName,e);
		} catch (ClassCastException e) {
			throw new IOException("Invalid Class in Inputing "+fileName,e);
		}
		return out;
	}

    public SquareClass[][] createBoard(boolean flip) {
        SquareClass[][] out = new SquareClass[squares.length][squares[0].length];
		for (int y = 0; y < out.length; y++) for (int x = 0; x < out[y].length; x++) {
			out[y][x] = new SquareClass(x,y,squares[y][x]);
			out[y][x].setFlipDisplay(flip);
		}
		return out;
    }
	// public SquareClass[][] createBoard(boolean flip) {
	// 	if (flip) {
	// 		SquareClass[][] out = new SquareClass[squares[0].length][squares.length];
	// 		for (int y = 0; y < out.length; y++) for (int x = 0; x < out[y].length; x++) {
	// 			out[y][x] = new SquareClass(x,y,squares[x][y]);
	// 			out[y][x].flipContents();
	// 		}
	// 		return out;
	// 	} else {
	// 		return createBoard();
	// 	}
    // }
}
