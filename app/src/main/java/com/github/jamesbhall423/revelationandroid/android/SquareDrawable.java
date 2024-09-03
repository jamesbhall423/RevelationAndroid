package com.github.jamesbhall423.revelationandroid.android;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.jamesbhall423.revelationandroid.graphics.Colors;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.graphics.SquarePainter;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;

public class SquareDrawable extends Drawable {
    private AndroidGraphics graphics;
    private SquareClass model;
    private RevelationDisplayLocal endDisplay = null;
    private Paint paint;
    public SquareDrawable(SquareClass model) {
        graphics = new AndroidGraphics(null, new Paint());
        this.model = model;
        paint = new Paint();
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();
        int x = getBounds().left;
        int y = getBounds().right;
        paint.setColor(Colors.blue);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(100,500,500,1200,paint);
//        graphics.setCanvas(canvas);
//        SquarePainter.paint(model,graphics,endDisplay);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
