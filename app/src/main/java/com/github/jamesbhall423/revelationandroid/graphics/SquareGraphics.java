package com.github.jamesbhall423.revelationandroid.graphics;

public interface SquareGraphics {
    public double getWidth();
    public double getHeight();
    public void setColor(int color);
    public void fillRectangle(int x, int y, int width, int height);
    public void fillEllipse(int x, int y, int width, int height);
    public void drawString(String string, int x, int y);
    public void setFontSize(int size);
    public void setFontBold();
    public void setFontPlain();
}
