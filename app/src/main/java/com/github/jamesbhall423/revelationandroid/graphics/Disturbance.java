package com.github.jamesbhall423.revelationandroid.graphics;

import java.util.ArrayList;
import java.util.List;

public class Disturbance {
    private int x;
    private int y;
    private int millis;
    private Disturbance(int x, int y, int millis) {
        this.x = x;
        this.y = y;
        this.millis = millis;
    }
    public int millis() {
        return millis;
    }
    public void setShake(ShakeGraphics graphics) {
        graphics.setDisturbance(x, y);
    }
    public static List<Disturbance> shakeAnimation() {
        List<Disturbance> out = new ArrayList<>();
        out.add(new Disturbance(1,1,0));
        out.add(new Disturbance(-1,-1,100));
        out.add(new Disturbance(0,0,200));
        return out;
    }
}
