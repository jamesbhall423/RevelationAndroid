package com.github.jamesbhall423.revelationandroid.model;

import java.util.ArrayList;
import java.util.List;

public abstract class SquareClickType extends SelectionItem {

    public SquareClickType(String name,int number, BoxModel model, boolean alwaysActionable) {
        super(name,number, model, alwaysActionable);
    }
    public abstract boolean doModelClick(int modelX, int modelY);
    public void doClick(int modelX, int modelY) {
        doClick(new PointInt2D(modelX, modelY));
    }
    @Override
    public boolean executeAction(Object details) {
        PointInt2D point = (PointInt2D) details;
        return doModelClick(point.x,point.y);
    }
    public static List<SquareClickType> clickTypes(BoxModel model) {
        List<SquareClickType> out = new ArrayList<>();
        out.add(new SquareClick("place", -1, model, true, false));
        out.add(new SquareClick("revert",model.getPlayerCMap().numReverts,model,false,true));
        out.add(new Scan(model.getPlayerCMap().scans,model,false));
        return out;
    }
}
