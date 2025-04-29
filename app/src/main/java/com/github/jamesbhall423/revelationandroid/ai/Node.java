package com.github.jamesbhall423.revelationandroid.ai;

import com.github.jamesbhall423.revelationandroid.model.Spot;
import com.github.jamesbhall423.revelationandroid.model.SquareCondition;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;

public class Node extends Spot {
    private int edgeTurns=-1;
    private int turns;

    public Node(SquareModel square, SquareCondition openCondition, SquareCondition extensionCondition, int size) {
        super(square, openCondition, size);
        if (extensionCondition.conditionFulfilled(square)) turns = 0;
        else if (square.time()==-2) turns = 1;
        else turns  = 2*square.time();
    }
    public void setStart() {
        edgeTurns = turns;
    }

    public Node(boolean open, int turns, int size, int x, int y) {
        super(open, size, x, y);
        this.turns = turns;
    }
    public void setEdgeTurns(int edgeTurns) {
        if (edgeTurns<0||this.edgeTurns<edgeTurns) this.edgeTurns = edgeTurns;
    }
    public int getEdgeTurns() {
        return edgeTurns;
    }
    public int getTurns() {
        return turns;
    }

    @Override
    public void reset() {
        super.reset();
        edgeTurns=-1;
    }
}
