package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;

public class StatisticsGraphYear extends View {

    Context context;
    Paint paint;
    Paint textPaint;
    Paint wavePaint;
    Paint colorPaint;
    Path path;
    Path circlePaint;
    Path wavePath;

    private String[] coordinateY = {"100%", "80%", "60%", "40%", "20%", "0%"};
    private int[] monthDay = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private double[] grade_week = new double[12];
    private DecimalFormat decimalFormat;
    private float distance = 0f;
    private float last_point = 0f;
    private float step = 0f;
    private int front = 0, rear = 9;
    private int currentMonth = 12;
    private int movingCount = 0;

    public StatisticsGraphYear(Context context) {
        super(context);
    }


    public StatisticsGraphYear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.path = new Path();
        this.textPaint = new Paint();
        this.wavePaint = new Paint();
        this.circlePaint = new Path();
        this.colorPaint = new Paint();
        this.wavePath = new Path();
        this.context = context;
        decimalFormat = new DecimalFormat("0.00");
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        setMeasuredDimension(width, (int) (height * 0.35));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCooridate(canvas);
        if (grade_week.length != 0) {
            drawWave(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_point = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                distance += ev.getX() - last_point;
                float x = Math.abs(distance);
                if (x > step && movingCount < 4) {
                    if (ev.getX() > last_point) {
                        if (front >= 0) {
                            if (front != 0) {
                                --front;
                                --rear;
                            }
                            ++movingCount;
                            invalidate();
                        }
                    } else {
                        if (rear < 12) {
                            ++front;
                            ++rear;
                            ++movingCount;
                            invalidate();
                        }
                        distance = 0;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                distance = 0f;
                last_point = 0f;
                movingCount = 0;
                break;
        }
        return true;

    }

    private void drawCooridate(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#ffffff"));
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float startPointY = (float) (width * 0.06);
        float endPointY = (float) (height * 0.8);

        path.moveTo(startPointX, startPointY);
        path.lineTo(startPointX, (float) (endPointY + 0.1 * startPointX));

        for (int i = 1; i < 6; i++) {
            path.moveTo(startPointX, startPointY);
            path.rMoveTo(0, (float) (endPointY * i * 1 / 5.65));
            path.rLineTo(width, 0);
            canvas.drawText(coordinateY[i], (float) (startPointX - 0.9 * startPointX),
                    (float) (endPointY * (i + 1) * 1 / 5.65 - 0.2 * startPointX), textPaint);
        }

        canvas.drawPath(path, paint);

    }

    private void drawWave(Canvas canvas) {
        wavePath.rewind();
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float endPointY = (float) (height * 0.8);

        float x_length = width - 2 * startPointX;
        float y_length = endPointY - startPointX;
        float offset = (float) (x_length / 8.5);
        float canvasLength = (float) ((width - 2 * startPointX) / 8.5);//- 0.3 * startPointX);
        float historgrimX, historgrimY;
        historgrimY = (float) (endPointY + 0.4 * startPointX);
        step = startPointX + canvasLength;



        wavePath.moveTo(startPointX, (float) (y_length * (1 - grade_week[front])) + startPointX);

        for (int i = 0, j = front; i < rear - front; i++, j++) {
            historgrimX = (startPointX + canvasLength * (i));

            canvas.drawText(monthDay[j] + "", historgrimX, (float) (historgrimY + 0.3 * startPointX), textPaint);//-------------x轴坐标

            if (j < currentMonth) {
                if (grade_week[i] > 0.8) {
                    colorPaint.setColor(Color.parseColor("#28B78D"));
                } else if (grade_week[j] > 0.6 && grade_week[i] < 0.8) {
                    colorPaint.setColor(Color.parseColor("#fecc36"));
                } else if (grade_week[j] < 0.6) {
                    colorPaint.setColor(Color.parseColor("#D5544F"));
                }
                float x = startPointX + offset * (i);
                float y = (float) (y_length * (1 - grade_week[j])) + startPointX;
                canvas.drawText(decimalFormat.format(grade_week[j] * 100) + "%", x, y, colorPaint);//-------------分数
                float x_location = startPointX + offset * (i);
                float y_location = (float) (y_length * (1 - grade_week[j])) + startPointX;
                wavePath.lineTo(x_location, y_location);
            }
        }
        canvas.drawPath(wavePath, wavePaint);
    }


    private void init() {
        paint.setStrokeWidth(3f);
        paint.setColor(Color.parseColor("#454545"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        textPaint.setStrokeWidth(2f);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);

        colorPaint.setStrokeWidth(1.5f);
        colorPaint.setTextSize(25f);
        colorPaint.setAntiAlias(true);

        wavePaint.setStrokeWidth(1.5f);
        wavePaint.setColor(Color.parseColor("#8E0513"));
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setAntiAlias(true);
        wavePaint.setPathEffect(new CornerPathEffect(20));

    }



    public void updateView(double[] grade){
        wavePath.reset();
        grade_week = grade;

        clearALL();
        invalidate();
    }

    private void clearALL(){
        front = 0;
        rear = 9;

    }

}
