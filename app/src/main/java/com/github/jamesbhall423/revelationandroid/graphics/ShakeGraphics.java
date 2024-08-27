package com.github.jamesbhall423.revelationandroid.graphics;

public class ShakeGraphics implements SquareGraphics {
    private SquareGraphics baseGraphics;
    private int disturbX = 0;
    private int disturbY = 0;
    public ShakeGraphics(SquareGraphics baseGraphics) {
        this.baseGraphics = baseGraphics;
    }
    public void setDisturbance(int x, int y) {
        disturbX = x;
        disturbY = y;
    }

    @Override
    public double getWidth() {
        return baseGraphics.getWidth();
    }

    @Override
    public double getHeight() {
        return baseGraphics.getHeight();
    }

    @Override
    public void setColor(int color) {
        baseGraphics.setColor(color);
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        baseGraphics.fillRectangle(x+disturbX, y+disturbY, width, height);
    }

    @Override
    public void fillEllipse(int x, int y, int width, int height) {
        baseGraphics.fillEllipse(x+disturbX, y+disturbY, width, height);
    }

    @Override
    public void drawString(String string, int x, int y) {
        baseGraphics.drawString(string, x+disturbX, y+disturbY);
    }

    @Override
    public void setFontSize(int size) {
        baseGraphics.setFontSize(size);
    }

    @Override
    public void setFontBold() {
        baseGraphics.setFontBold();
    }

    @Override
    public void setFontPlain() {
        baseGraphics.setFontPlain();
    }
}
