package com.github.jamesbhall423.revelationandroid.model;


import com.github.jamesbhall423.revelationandroid.action.*;

public class Scan extends SquareClickType {

    public Scan(int number, BoxModel model, boolean alwaysActionable, String description) {
        super("scan", number, model, alwaysActionable, description);
    }

    @Override
    public boolean doModelClick(int modelX, int modelY) {
        int player = model.player();
        int startTime = model.getTime();
        model.distribute(new ScanAction(player,startTime,modelX,modelY));
        model.distribute(CAction.TURN.create(player, startTime));
        return true;
    }

    
}
