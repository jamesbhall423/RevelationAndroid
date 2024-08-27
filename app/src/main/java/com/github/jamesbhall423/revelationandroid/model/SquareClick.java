package com.github.jamesbhall423.revelationandroid.model;

public class SquareClick extends SquareClickType {
    private boolean revert;

    public SquareClick(String name, int number, BoxModel model, boolean alwaysActionable, boolean revert) {
        super(name, number, model, alwaysActionable);
        this.revert = revert;
    }

    @Override
    public boolean doModelClick(int modelX, int modelY) {
        return model.clickSquare(modelX,modelY,revert);
    }
    
}
