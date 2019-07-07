package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.hse.yatoufang.MissionItemUtils.MissionItemManger;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StatisticsGraphWeek extends View {

    Paint paint;
    Paint textPaint;
    Paint wavePaint;
    Paint colorPaint;
    Path path;
    Path circlePaint;
    Path wavePath;
    private String[] coordinateY = {"100%", "80%", "60%", "40%", "20%", "0%"};
    private String[] coordinateX = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private double[] grade_week = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,};//{0.8, 0, 0, 0, 0, 0, 0,};//

    private int counts = 0;
    private int days = 1;

    public StatisticsGraphWeek(Context context) {
        super(context);
        init();
    }

    public StatisticsGraphWeek(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.path = new Path();
        this.textPaint = new Paint();
        this.wavePaint = new Paint();
        this.circlePaint = new Path();
        this.colorPaint = new Paint();
        this.wavePath = new Path();
        this.wavePath = new Path();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        setMeasuredDimension(width, (int) (height * 0.35));
        //System.out.println(MeasureSpec.getSize(widthMeasureSpec) + "-" + MeasureSpec.getSize(heightMeasureSpec));
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinate(canvas);
        drawWave(canvas);


    }

    private void drawCoordinate(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#ffffff"));
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float startPointY = (float) (width * 0.06);
        float endPointY = (float) (height * 0.6);

        path.moveTo(startPointX, startPointY);
        path.lineTo(startPointX, (float) (endPointY + 0.1 * startPointX));

        for (int i = 1; i < 6; i++) {
            path.moveTo(startPointX, startPointY);
            path.rMoveTo(0, (float) (endPointY * i * 0.172));
            path.rLineTo(width - 2 * startPointX, 0);
            canvas.drawText(coordinateY[i], (float) (startPointX - 0.9 * startPointX),
                    (float) (endPointY * (i + 1) * 0.172 - 0.2 * startPointX), textPaint);
        }

        path.moveTo((width - startPointX), startPointY);
        path.lineTo((width - startPointX), (float) (endPointY + 0.1 * startPointX));

        float canvasLength = (float) ((width - 2 * startPointX) / 8.5);//- 0.3 * startPointX);
        float historgrimX, historgrimY;
        for (int i = 0; i < 7; i++) {
            historgrimX = (startPointX + canvasLength * (i + 1));
            historgrimY = (float) (endPointY + 0.4 * startPointX);
            canvas.drawText(coordinateX[i], historgrimX, (float) (historgrimY + 0.3 * startPointX), textPaint);//-----------------------

        }

        canvas.drawPath(path, paint);
    }

    private void drawWave(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float startPointX = (float) (width * 0.06);
        float endPointY = (float) (height * 0.6);

        float x_length = width - 2 * startPointX;
        float y_length = endPointY - startPointX;
        float offset = (float) (x_length / 8.5);
        if (counts == 0) {
            wavePath.moveTo(startPointX + offset, (float) ((endPointY - startPointX) * (1 - grade_week[0])) + startPointX);
        } else {
            float x = startPointX + offset * (counts + 1);
            float y = (float) (y_length * (1 - grade_week[counts])) + startPointX;
            wavePath.lineTo(x, y);
        }

        canvas.drawPath(wavePath, wavePaint);
        if ((counts) < days - 1) {
            ++counts;
            invalidate();
        } else {

            for (int i = 0; i < days; i++) {
                if (grade_week[i] > 0.8) {
                    colorPaint.setColor(Color.parseColor("#28B78D"));
                } else if (grade_week[i] > 0.6 && grade_week[i] < 0.8) {
                    colorPaint.setColor(Color.parseColor("#FFC100"));
                } else if (grade_week[i] < 0.6) {
                    colorPaint.setColor(Color.parseColor("#D5544F"));
                }
                float x = startPointX + offset * (i + 1);
                float y = (float) (y_length * (1 - grade_week[i])) + startPointX;
                canvas.drawText((grade_week[i] * 100) + "%", x, y, colorPaint);
            }
            textPaint.setTextSize(36f);
            canvas.drawText( "本周完成情况", (float)(0.4 * width),endPointY + (float)(0.3 * height), textPaint);
            counts = 0;
        }
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
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setAntiAlias(true);
        wavePaint.setPathEffect(new CornerPathEffect(20));

        getGrade();
        days = days == 1 ? 7 : days;
    }


    private void getGrade() {
        MissionItemManger missionItemManger = new MissionItemManger(getContext(), 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int counts = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String[] data = new String[7];
        counts = counts == 0 ? 7 : counts;
        this.days = 7;
        //System.out.println(Arrays.toString(data));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //System.out.println(simpleDateFormat.format(calendar.getTime()));
        while (counts > 0) {
            counts--;
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            data[counts] = simpleDateFormat.format(calendar.getTime());
        }
        for (int i = 0; i < 7; i++) {
            if (data[i] != null) {
                this.grade_week[i] = missionItemManger.getGrade(data[i]);
            }
        }
    }

    public void updateView(){
        getGrade();
        days = days == 1 ? 7 : days;
        invalidate();
    }



}

