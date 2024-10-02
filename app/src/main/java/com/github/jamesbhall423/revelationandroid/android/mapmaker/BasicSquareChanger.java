package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareState;

public class BasicSquareChanger implements SquareChanger {
    private SquareStateChanger stateChanger;
    public BasicSquareChanger(SquareStateChanger stateChanger) {
        this.stateChanger = stateChanger;
    }
    @Override
    public void alterSquare(SquareClass square) {
        SquareState state = square.getState();
        stateChanger.alterState(state);
        square.setState(state);
    }

    @Override
    public void deactivate() {

    }
}
