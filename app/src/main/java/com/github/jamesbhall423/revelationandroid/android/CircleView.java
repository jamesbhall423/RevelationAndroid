package com.github.jamesbhall423.revelationandroid.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.jamesbhall423.revelationandroid.graphics.Colors;

public class CircleView extends View {
    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context,
                      @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context,
                      @Nullable AttributeSet attrs,
                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        //paint.setColor(Colors.green);
        canvas.drawCircle(60, 60, 60, paint);
    }
}
