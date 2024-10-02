package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

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
        model.registerUpdater(this);
        setOnClickListener(this);
        this.listener = listener;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
       setLayoutParams(param);
       setLayoutParams(param);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec/boardSize;
        int height = heightMeasureSpec/boardSize;
        if (height>width) height=width;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);

    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AndroidGraphics graphics = new AndroidGraphics(canvas,paint);
        SquarePainter.paint(model,graphics,endDisplay);
    }

    @Override
    public void update(int x, int y) {
        invalidate();
    }

    @Override
    public void onClick(View v) {
        System.out.println("Hello");
        listener.doClick(modelX,modelY);
        invalidate();
    }

    public void setRevelationDisplay(RevelationDisplayLocal endDisplay) {
        this.endDisplay = endDisplay;
    }
}
