package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.graphics.SquareGraphics;
import com.github.jamesbhall423.revelationandroid.graphics.SquarePainter;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareViewUpdater;

public class AndroidSquare extends View implements SquareViewUpdater, View.OnClickListener {
    private final ModelClickListener listener;
    private int modelX;
    private int modelY;
    private SquareClass model;
    private RevelationDisplayLocal endDisplay;
    private static final int SIZE = 32;
    public AndroidSquare(Context context, SquareClass square,ModelClickListener listener) {
        super(context);
        modelX = square.X;
        modelY = square.Y;
        model = square;
        setMeasuredDimension(SIZE,SIZE);
        setOnClickListener(this);
        this.listener = listener;
    }
    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        AndroidGraphics graphics = new AndroidGraphics(canvas,paint);
        SquarePainter.paint(model,graphics,endDisplay);
    }
    public void setEndDisplay(RevelationDisplayLocal endDisplay) {
        this.endDisplay = endDisplay;
        invalidate();
    }

    @Override
    public void update(int x, int y) {
        invalidate();
    }

    @Override
    public void onClick(View v) {
        listener.doClick(modelX,modelY);
    }
}
