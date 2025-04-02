package com.github.jamesbhall423.revelationandroid.model;


import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import com.github.jamesbhall423.revelationandroid.io.RevelationInputStream;
import com.github.jamesbhall423.revelationandroid.io.RevelationOutputStream;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

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
    public void writeCMap(String name, JSONSerializer serializer) throws IOException {
        File writeTo;
        writeTo = new File("com/github/jamesbhall423/revelationandroid/maps/"+name+".json");
        RevelationOutputStream out = new RevelationOutputStream(new FileOutputStream(writeTo),serializer,false);
        try {
            out.writeCMap(this);
            out.close();
        } catch (IllegalAccessException e) {
            throw new IOException(e);
        }
    }/**
     * Method writeCMap
     * @param name
     *
     */
    public void writeCMap(String packageName, String name, JSONSerializer serializer) throws IOException {
        File writeTo;
        writeTo = new File(packageName+"/"+name+".json");
        RevelationOutputStream out = new RevelationOutputStream(new FileOutputStream(writeTo),serializer,false);
        try {
            out.writeCMap(this);
            out.close();
        } catch (IllegalAccessException e) {
            throw new IOException(e);
        }
    }

    public CMap(SquareState[][] squares, Player[] players) {
        this.squares=squares;
        this.players=players;
    }
    public static String expand(String name) {
        return "maps/" +name+".json";
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
    public static CMap read(String fileName, JSONSerializer serializer) throws IOException {
        CMap out;
        try {
            RevelationInputStream in = new RevelationInputStream(new FileInputStream(fileName),serializer);
            out = in.readCMap();
            in.close();
        } catch (IllegalAccessException e) {
            throw new IOException("Illegal access in Inputing "+fileName,e);
        } catch (ClassCastException e) {
            throw new IOException("Invalid Class in Inputing "+fileName,e);
        }
        return out;
    }

    public SquareClass[][] createBoard(boolean flip, int displayPlayer) {
        SquareClass[][] out = new SquareClass[squares.length][squares[0].length];
        for (int y = 0; y < out.length; y++) for (int x = 0; x < out[y].length; x++) {
            out[y][x] = new SquareClass(x,y,squares[y][x]);
            out[y][x].setFlipDisplay(flip);
            out[y][x].setGlobalPlayerIndex(displayPlayer);
        }
        return out;
    }
}