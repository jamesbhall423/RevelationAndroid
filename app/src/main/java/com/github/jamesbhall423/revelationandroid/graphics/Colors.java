package com.github.jamesbhall423.revelationandroid.graphics;

public class Colors {

    public static int orange = 0xff7f00;
    public static int gray = 0x7f7f7f;
    public static int green = 0x00ff00;
    public static int blue = 0x0000ff;
    public static int black = 0x000000;
    public static int yellow = 0xffff00;
    public static int cyan = 0x00ffff;
    public static int magenta = 0xff00ff;
    public static int darker(int color) {
        int r = (color >> 16) % 256;
        int g = (color >> 8) % 256;
        int b = color % 256;
        return ((r/2)<<16)+((g/2)<<8)+(b/2);
    }

    
}
