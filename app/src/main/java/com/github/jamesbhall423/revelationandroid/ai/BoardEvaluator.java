package com.github.jamesbhall423.revelationandroid.ai;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.Pathfinder;
import com.github.jamesbhall423.revelationandroid.model.SquareCondition;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;

public class BoardEvaluator {
    private int nextX;
    private int nextY;
    private int twiceTurnCount;
    public BoardEvaluator(BoxModel model, final int player) {
        final int tokenPlayer = player==0 ? 1 : -1;
        int[][] values = new int[model.modelHeight()][model.modelWidth()];
        SquareModel[][] squares = model.boardModel();
        SquareCondition openCondition = new SquareCondition() {
            @Override
            public boolean conditionFulfilled(SquareModel square) {
                return square.getView(player) != -tokenPlayer;
            }
        };
        SquareCondition extensionCondition = new SquareCondition() {
            @Override
            public boolean conditionFulfilled(SquareModel square) {
                return square.getView(player) == tokenPlayer;
            }
        };
        TurnCounter counter = new TurnCounter(openCondition,extensionCondition,squares);
        if (player==0) counter.calcBoard(Pathfinder.constArray(0,model.displayHeight()),Pathfinder.lineArray(model.displayHeight()));
        else counter.calcBoard(Pathfinder.lineArray(model.displayWidth()),Pathfinder.constArray(0,model.displayWidth()));
        for (int y = 0; y < values.length; y++) for (int x = 0; x < values[y].length; x++) values[y][x] = counter.getEdgeTurns(x,y);
        counter = new TurnCounter(openCondition,extensionCondition,squares);
        if (player==0) counter.calcBoard(Pathfinder.constArray(model.displayWidth()-1,model.displayHeight()),Pathfinder.lineArray(model.displayHeight()));
        else counter.calcBoard(Pathfinder.lineArray(model.displayWidth()),Pathfinder.constArray(model.displayHeight()-1,model.displayWidth()));
        for (int y = 0; y < values.length; y++) for (int x = 0; x < values[y].length; x++) {
            int n1 = values[y][x];
            int n2 = counter.getEdgeTurns(x,y);
            if (n1<0||n2<0) values[y][x]=-1;
            else values[y][x]=n1+n2;
        }
        nextX = -1;
        nextY = -1;
        twiceTurnCount = 100;
        for (int y = 0; y < values.length; y++) for (int x = 0; x < values[y].length; x++) if (squares[y][x].getView(0)==0&&values[y][x]>0&&values[y][x]<twiceTurnCount) {
            twiceTurnCount=values[y][x];
            nextX=x;
            nextY=y;
        }
    }
    public int nextX() {
        return nextX;
    }
    public int nextY() {
        return nextY;
    }
    public int getTwiceTurnCount() {
        return twiceTurnCount;
    }
}
