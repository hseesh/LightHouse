package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class TestView extends View {

    public TestView(Context context) {
        super(context);
        init();
    }


    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    Paint vPaint = new Paint(); //绘制样式物件
    RectF rectF = new RectF();
    private int i = 0; //弧形角度

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, 0, i, true, vPaint);
        if ((i += 1) < 360) {
            invalidate();
        }
//        canvas.drawLine(200, 200, 200 + i, 200 + i, vPaint);
//        if ((i += 1) < 560) {
//            invalidate();
//        }

    }

    private void init() {
        vPaint.setColor(Color.parseColor("#454545"));
        vPaint.setAntiAlias(true); //反锯齿
        vPaint.setStyle(Paint.Style.STROKE);
        vPaint.setStrokeWidth(5);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        rectF.set(60,60,dm.widthPixels - 60,dm.widthPixels-60);
        rectF.set(0,0,360,360);

    }
}
