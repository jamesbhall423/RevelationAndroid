package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import com.github.jamesbhall423.revelationandroid.model.SquareState;
import com.github.jamesbhall423.revelationandroid.model.SquareType;

import java.util.ArrayList;
import java.util.List;

public class PlayerContentsChanger implements SquareStateChanger {
    private int contents;
    public PlayerContentsChanger(int contents) {
        this.contents = contents;
    }
    @Override
    public void alterState(SquareState state) {
        if (state.type!= SquareType.Mountain) state.contents = contents;
    }
}
