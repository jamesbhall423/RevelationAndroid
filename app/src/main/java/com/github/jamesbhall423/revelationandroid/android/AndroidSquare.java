package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.jamesbhall423.revelationandroid.graphics.Disturbance;
import com.github.jamesbhall423.revelationandroid.graphics.RevelationDisplayLocal;
import com.github.jamesbhall423.revelationandroid.graphics.ShakeGraphics;
import com.github.jamesbhall423.revelationandroid.graphics.SquarePainter;
import com.github.jamesbhall423.revelationandroid.model.SquareClass;
import com.github.jamesbhall423.revelationandroid.model.SquareViewUpdater;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AndroidSquare extends View implements SquareViewUpdater, View.OnClickListener {
    Timer timer = new Timer();
    private final ModelClickListener listener;
    private int modelX;
    private int modelY;
    private SquareClass model;
    private RevelationDisplayLocal endDisplay;
    private static final int SIZE = 32;
    private int boardSize;
    private Paint paint = new Paint();
    private ShakeGraphics graphics;
    private AndroidGraphics canvasHolder;
    private boolean showTrueValue = false;
    public AndroidSquare(AppCompatActivity context, MainViewModel viewModel, SquareClass square, ModelClickListener listener, int boardSize) {
        super(context);
        canvasHolder = new AndroidGraphics(null,paint);
        graphics = new ShakeGraphics(canvasHolder);
        this.boardSize = boardSize;
        modelX = square.X;
        modelY = square.Y;
        model = square;
        this.listener = listener;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        setLayoutParams(param);
        setLayoutParams(param);
        if (viewModel!=null) model.registerUpdater(viewModel.wrapSquareViewUpdater(context,this,modelX,modelY));
        else model.registerUpdater(this);
        setOnClickListener(this);
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
        canvasHolder.setCanvas(canvas);
        SquarePainter.paint(model,graphics,endDisplay,showTrueValue);
    }

    @Override
    public void update(int x, int y) {

        //invalidate();
        paintAnimation();
    }
    public void paintAnimationSlice(Disturbance d) {
        d.setShake(graphics);
        postInvalidate();
    }
    public void paintAnimation() {
        List<Disturbance> animation = Disturbance.shakeAnimation();
        for (final Disturbance next: animation) {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    paintAnimationSlice(next);
                }
            },next.millis());
        }
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
    public void showTrueValue() {
        showTrueValue = true;
    }
}
