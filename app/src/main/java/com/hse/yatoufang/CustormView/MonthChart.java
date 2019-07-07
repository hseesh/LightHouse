package com.hse.yatoufang.CustormView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MonthChart extends View {
    Paint paint;
    Paint textPaint;
    Paint wavePaint;
    Paint circlePaint;
    Paint colorPaint;
    Path path;
    Path wavePath;
    Path circlePath;
    private String[] coordinateY = {"100%", "80%", "60%", "40%", "20%", "0%"};
    //    private String[] coordinateX = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
//            "21", "22", "23", "24", "25", "26", "27", "28", "29","30","31"};
    private String[] coordinateX = {"5", "10", "15", "20", "25", "30"};
    private double[] grade_month = {0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.5,
            0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.9, 0.7};//{0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0,};
    private String date = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31";
    private int count = 0;

    public MonthChart(Context context) {
        super(context);
        init();
    }


    public MonthChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MonthChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        setMeasuredDimension(width ,(int)(height * 0.35));
//        System.out.println(widthMeasureSpec + "-------------" + widthMeasureSpec);
//        System.out.println(MeasureSpec.getSize(widthMeasureSpec) + "-" + MeasureSpec.getSize(heightMeasureSpec));
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCooridate(canvas);
        drawWave(canvas);
    }

    private void init() {
        this.paint = new Paint();
        this.path = new Path();
        this.wavePaint = new Paint();
        this.wavePath = new Path();
        this.textPaint = new Paint();
        this.circlePaint = new Paint();
        this.circlePath = new Path();
        this.colorPaint = new Paint();


        paint.setStrokeWidth(3f);
        paint.setColor(Color.parseColor("#454545"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        textPaint.setStrokeWidth(2f);
//        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(30f);
        textPaint.setAntiAlias(true);

        wavePaint.setStrokeWidth(2f);
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setAntiAlias(true);

        colorPaint.setTextSize(18f);
        colorPaint.setAntiAlias(true);


        //wavePaint.setPathEffect(new CornerPathEffect(20));


    }

    private void drawCooridate(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#ffffff"));
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float startPointY = (float) (width * 0.06);
        float endPointY = (float) (height * 0.6);

        path.moveTo(startPointX, startPointY);
        path.lineTo(startPointX, (float)(endPointY + 0.1 * startPointX));

        for (int i = 1; i < 6; i++) {
            path.moveTo(startPointX, startPointY);
            path.rMoveTo(0, (float) (endPointY * i * 0.172));
            path.rLineTo(width - 2 * startPointX, 0);
            canvas.drawText(coordinateY[i], (float) (startPointX - 0.9 * startPointX),
                    (float) (endPointY * (i + 1) * 0.172 - 0.2 * startPointX), textPaint);
        }

        path.moveTo((width - startPointX), startPointY);
        path.lineTo((width - startPointX), (float)(endPointY + 0.1 * startPointX));

        float canvasLength = (float) ((width - 2 * startPointX) / 6.6);//- 0.3 * startPointX);
        float historgrimX, historgrimY;
        for (int i = 0; i < 6; i++) {
            historgrimX = (startPointX + canvasLength * (i + 1));
            historgrimY = (float) (endPointY + 0.4 * startPointX);
            canvas.drawText(coordinateX[i], historgrimX, (float) (historgrimY + 0.3 * startPointX), textPaint);//-----------------------

        }

        canvas.drawPath(path, paint);
        //;
    }

    private void drawWave(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float endPointY = (float) (height * 0.6);

        float x_length = width - 2 * startPointX;
        float y_length = endPointY - startPointX;
        float offset = x_length / 31;
        //System.out.println("y_length" + y_length + "---height" + height);
        if (count == 0) {
            wavePath.moveTo(startPointX, (float) ((endPointY - startPointX) * (1 - grade_month[0])) + startPointX);
            //circlePath.moveTo(startPointX, (float) ((endPointY - startPointX) * (1 - grade_month[0])) + startPointX);
            canvas.drawCircle(startPointX, (float) ((endPointY - startPointX) * (1 - grade_month[0])) + startPointX, 3, circlePaint);
        } else {
            float x = startPointX + offset * count;
            float y = (float) (y_length * (1 - grade_month[count])) + startPointX;
            wavePath.lineTo(x, y);
            wavePath.addCircle(x, y, 2, Path.Direction.CW);
        }

        canvas.drawPath(wavePath, wavePaint);
        //canvas.drawPath(circlePath,circlePaint);


        if ((count++) < 30) {
            invalidate();
        }else{
            for(int i = 0; i < grade_month.length; i++){
                if (grade_month[i] > 0.8) {
                    colorPaint.setColor(Color.parseColor("#28B78D"));
                } else if (grade_month[i] > 0.6 && grade_month[i] < 0.8) {
                    colorPaint.setColor(Color.parseColor("#fecc36"));
                } else if (grade_month[i] < 0.6) {
                    colorPaint.setColor(Color.parseColor("#D5544F"));
                }
                float x = startPointX + offset * i;
                float y = (float) (y_length * (1 - grade_month[i])) + startPointX;
                canvas.drawText((grade_month[i] * 100) + "%", x, y,colorPaint);
            }
            count = 0;
        }



    }

//    public void showAnimator() {
//        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mAnimProgress = (float) animation.getAnimatedValue();
//                System.out.println(mAnimProgress);
//                invalidate();
//            }
//        });
//        animator.start();
//    }
}
