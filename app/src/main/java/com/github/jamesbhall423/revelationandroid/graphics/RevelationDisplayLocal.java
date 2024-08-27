package com.github.jamesbhall423.revelationandroid.graphics;

import com.github.jamesbhall423.revelationandroid.model.*;


public class RevelationDisplayLocal {
    private final int x;
    private final int y;
    public final RevelationDisplayGlobal global;
    private final String displayUpper;

    public RevelationDisplayLocal(RevelationDisplayGlobal global, Spot spot, String displayUpper) {
        this.x = spot.x;
        this.y = spot.y;
        this.global = global;
        this.displayUpper = displayUpper;
    }
    public String display(SquareClass model) {
        if (global.declarePlayerVictory) return displayUpper;
        else if (global.displayPlayerDeclared!=(model.displayPlayer()==1)) return displayUpper;
        else return displayUpper.toLowerCase();
    }
    public int displayX() {
        if (global.flipDisplay) return y;
        else return x;
    }
    public int displayY() {
        if (global.flipDisplay) return x;
        else return y;
    }
}
