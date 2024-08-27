package com.github.jamesbhall423.revelationandroid.graphics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.github.jamesbhall423.revelationandroid.model.*;
import static com.github.jamesbhall423.revelationandroid.model.BoxModel.EndStatus;


public class RevelationDisplayGlobal {
    public boolean displayPlayerDeclared;
    public boolean declarePlayerVictory;
    public boolean flipDisplay;
    public List<RevelationDisplayLocal> parts;
    public static final String REVELATION = "REVELATION";
    
    public RevelationDisplayGlobal(BoxModel model) {
        this.flipDisplay = model.flipDisplay();
        EndStatus endStatus = model.getEndStatus();
        SquareCondition routeCondition;
        final int declaringPlayer = model.playerSide(model.playerDeclareVictory()) ? 1 : -1;
        displayPlayerDeclared = model.playerDeclareVictory()==model.player();
        declarePlayerVictory = displayPlayerDeclared == (endStatus==EndStatus.WIN);
        if (declarePlayerVictory) {
            routeCondition = new SquareCondition() {
                @Override
                public boolean conditionFulfilled(SquareModel square) {
                    return square.player() == declaringPlayer;
                }
            };
        } else if (displayPlayerDeclared) {
            routeCondition = new SquareCondition() {
                @Override
                public boolean conditionFulfilled(SquareModel square) {
                    return square.getView() == declaringPlayer;
                }
            };
        } else {
            routeCondition = new SquareCondition() {
                @Override
                public boolean conditionFulfilled(SquareModel square) {
                    return square.player() != 0;
                }
            };
        }
        Pathfinder finder = new Pathfinder(model.boardModel(),routeCondition);
        Spot endSpot = model.findPathSpot(finder, model.playerSide(model.playerDeclareVictory()));
        if (endSpot==null) throw new RuntimeException();
        int lastBreak = 0;
        Stack<Spot> path = new Stack<>();
        do {
            path.push(endSpot);
            lastBreak++;
            if (declaringPlayer!=model.getModelSquare(endSpot.x, endSpot.y).player()) {
                lastBreak = 1;
            }
            endSpot = endSpot.getLast();
        } while (endSpot!=null);
        int startingIndex = 0;
        if (!declarePlayerVictory&&lastBreak>REVELATION.length()) {
            startingIndex = lastBreak-10;
        }
        for (int i = 0; i < startingIndex; i++) path.pop();
        Queue<String> displayValues = displayValues(path.size());
        parts = new ArrayList<>();
        for (int i = 0; i < 10 && !path.isEmpty(); i++) {
            parts.add(new RevelationDisplayLocal(this,path.pop(),displayValues.poll()));
        }
    }
    public static Queue<String> displayValues(int length) {
        int numCuts = REVELATION.length()-length;
        int[] cutOrder = new int[] {6,8,4,2,0}; // TI, ON, LA, ON, VE, RE
        if (numCuts>cutOrder.length) numCuts = cutOrder.length;
        Set<Integer> cuts = new HashSet<>();
        for (int i = 0; i < numCuts; i++) cuts.add(cutOrder[i]);
        Queue<String> out = new ArrayDeque<>();
        for (int i = 0; i < REVELATION.length(); i++) {
            if (cuts.contains(i)) {
                out.add(REVELATION.substring(i,i+2));
                i++;
            } else {
                out.add(REVELATION.substring(i,i+1));
            }
        }
        return out;
    }
}
