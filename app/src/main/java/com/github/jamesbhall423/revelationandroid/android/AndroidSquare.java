package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

import com.github.jamesbhall423.revelationandroid.graphics.Colors;
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
    private int boardSize;
    Paint paint = new Paint();
    public AndroidSquare(Context context, SquareClass square,ModelClickListener listener, int boardSize) {
        super(context);
        this.boardSize = boardSize;
        modelX = square.X;
        modelY = square.Y;
        model = square;
//        setOnClickListener(this);
        this.listener = listener;
//        setWillNotDraw(false);
//        setFocusable(true);
//        setFocusableInTouchMode(true);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec/=boardSize;
        heightMeasureSpec/=boardSize;
        if (heightMeasureSpec>widthMeasureSpec) heightMeasureSpec = widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
