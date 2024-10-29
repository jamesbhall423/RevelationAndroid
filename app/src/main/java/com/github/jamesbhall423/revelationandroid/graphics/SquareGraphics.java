package com.github.jamesbhall423.revelationandroid.graphics;

public interface SquareGraphics {
    double getWidth();
    double getHeight();
    void setColor(int color);
    void fillRectangle(int x, int y, int width, int height);
    void fillEllipse(int x, int y, int width, int height);
    void drawString(String string, int x, int y);
    void setFontSize(int size);
    void setFontBold();
    void setFontPlain();
}
