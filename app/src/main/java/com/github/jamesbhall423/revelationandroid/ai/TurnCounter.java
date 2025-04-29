package com.github.jamesbhall423.revelationandroid.ai;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.Pathfinder;
import com.github.jamesbhall423.revelationandroid.model.SquareCondition;
import com.github.jamesbhall423.revelationandroid.model.SquareModel;

import java.util.HashSet;
import java.util.Set;

public class TurnCounter {
    private Set<Node> current;
    private Set<Node>[] pathContinuations = new Set[5];
    private Node[][] board;
    int turnCount = 0;

    public TurnCounter(SquareCondition openCondition, SquareCondition extensionCondition, SquareModel[][] game) {
        for (int i = 0; i < pathContinuations.length; i++) pathContinuations[i] = new HashSet<>();
        board = new Node[game.length][game[0].length];
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) board[y][x] = new Node(game[y][x],openCondition,extensionCondition,5);
        add2DConnections();
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
            int[] teleporter = game[y][x].getTeleporter();
            if (teleporter!=null) board[y][x].setNeighbor(board[teleporter[1]][teleporter[0]],4);
        }
    }
    private void add2DConnections() {
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) {
            if (y+1<board.length) board[y][x].setNeighbor(board[y+1][x],0);
            if (x+1<board[0].length) board[y][x].setNeighbor(board[y][x+1],1);
            if (y>0) board[y][x].setNeighbor(board[y-1][x],2);
            if (x>0) board[y][x].setNeighbor(board[y][x-1],3);
        }
    }
    public void calcBoard(int[] xfrom, int[] yfrom) {
        for (int i = 0; i < xfrom.length; i++) {
            Node next = board[yfrom[i]][xfrom[i]];
            if (next.open()) {
               calcSubNode(next);
            }
        }
        while (hasUntestedPaths()) {
            updatePathContinuations();
            for (Node next: current) if (next.open()) {
                calcNode(next);
            }
        }
    }
    private void calcNode(Node last) {
        Node next;
        last.setEdgeTurns(turnCount);
        while ((next=(Node)last.next())!=null) {
            calcSubNode(next);
        }
    }
    private void calcSubNode(Node next) {
        pathContinuations[next.getTurns()].add(next);
    }
    private void updatePathContinuations() {
        if (pathContinuations[0].size()==0) {
            for (int i = 0; i < pathContinuations.length-1; i++) {
                pathContinuations[i]=pathContinuations[i+1];
            }
            pathContinuations[pathContinuations.length-1]=new HashSet<Node>();
            turnCount++;
        }
        current = pathContinuations[0];
        pathContinuations[0]=new HashSet<Node>();
    }

    private boolean hasUntestedPaths() {
        for (int i = 0; i < pathContinuations.length; i++) if (pathContinuations[i].size()>0) return true;
        return false;
    }
    public int getEdgeTurns(int x, int y) {
        return board[y][x].getEdgeTurns();
    }
    public Node get(int x, int y) {
        return board[y][x];
    }
    public static void printEdgeLengths(BoxModel model) {
        SquareModel[][] squares = model.boardModel();
        SquareCondition openCondition = new SquareCondition() {
            @Override
            public boolean conditionFulfilled(SquareModel square) {
                return square.getView(0) != -1;
            }
        };
        SquareCondition extensionCondition = new SquareCondition() {
            @Override
            public boolean conditionFulfilled(SquareModel square) {
                return square.getView(0) == 1;
            }
        };
        TurnCounter counter = new TurnCounter(openCondition,extensionCondition,squares);
        counter.calcBoard(Pathfinder.constArray(0,8),Pathfinder.lineArray(8));
        for (int y = 0; y < 8; y++) for (int x = 0; x < 8; x++) {
            System.out.println("("+y+","+x+"): "+counter.getEdgeTurns(x,y));
        }
    }
    public void reset() {
        for (int y = 0; y < board.length; y++) for (int x = 0; x < board[y].length; x++) board[y][x].reset();
    }
}
