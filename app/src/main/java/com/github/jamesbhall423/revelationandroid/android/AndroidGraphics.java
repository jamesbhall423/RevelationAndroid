package com.github.jamesbhall423.revelationandroid.android;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.jamesbhall423.revelationandroid.graphics.SquareGraphics;

public class AndroidGraphics implements SquareGraphics {
    private Canvas canvas;
    private Paint paint;
    public AndroidGraphics(Canvas canvas, Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
    }
    @Override
    public double getWidth() {
        return canvas.getWidth();
    }

    @Override
    public double getHeight() {
        return canvas.getHeight();
    }

    @Override
    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x,y,x+width,y+height,paint);
    }

    @Override
    public void fillEllipse(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(x,y,x+width,y+height,paint);
    }

    @Override
    public void drawString(String string, int x, int y) {
        canvas.drawText(string,x,y,paint);
    }

    @Override
    public void setFontSize(int size) {
        paint.setTextSize(size);
    }

    @Override
    public void setFontBold() {
        paint.setFakeBoldText(true);
    }

    @Override
    public void setFontPlain() {
        paint.setFakeBoldText(false);
    }
}
