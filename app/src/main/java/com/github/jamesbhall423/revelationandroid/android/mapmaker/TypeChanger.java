package com.github.jamesbhall423.revelationandroid.android.mapmaker;

import com.github.jamesbhall423.revelationandroid.model.SquareState;
import com.github.jamesbhall423.revelationandroid.model.SquareType;

import java.util.ArrayList;
import java.util.List;

public class TypeChanger implements SquareStateChanger {
    private SquareType type;
    public TypeChanger(SquareType type) {
        this.type = type;
    }
    public SquareType type() {
        return type;
    }
    @Override
    public void alterState(SquareState state) {
        state.type = type;
        if (type == SquareType.Mountain) state.contents = 0;
    }
    public static List<TypeChanger> types() {
        List<TypeChanger> out = new ArrayList<>();
        for (SquareType next: SquareType.values()) if (next!=SquareType.Road) out.add(new TypeChanger(next));
        return out;
    }
}
