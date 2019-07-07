package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.hse.yatoufang.MissionItemUtils.MissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class StatisticsMonth extends HorizontalScrollView {
    Context context;
    Paint paint;
    Paint textPaint;
    Paint wavePaint;
    Paint colorPaint;
    Path path;
    Path circlePaint;
    Path wavePath;
    Path textPath;
    MissionItemStatistics missionItemStatistics;
    private String[] coordinateY = {"100%", "80%", "60%", "40%", "20%", "0%"};
    //    private double[] grade_week = {0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.86, 0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.86, 0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.86,
//            0.8, 0.5, 0.9, 0.8, 0.5, 0.7, 1.0, 0.8, 0.86};//{0.8, 0, 0, 0, 0, 0, 0,};//
    private int[] monthDay = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private double[] grade_week = new double[31];

    private float distance = 0f;
    private float last_point = 0f;
    private float step = 0f;
    private int maxDyas = 31;
    private int front = 0, rear = 9;
    private int currentDay = 0;
    private int movingCount = 0;

    public StatisticsMonth(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public StatisticsMonth(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.path = new Path();
        this.textPaint = new Paint();
        this.wavePaint = new Paint();
        this.circlePaint = new Path();
        this.colorPaint = new Paint();
        this.wavePath = new Path();
        this.wavePath = new Path();
        this.context = context;
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

        drawCoordinate(canvas);
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
                if (x > step && movingCount < 4) {// x 大于最小距离，最多滑动 4 次
                    if ( ev.getX() > last_point) {
                        if (front >= 0) {
                          //  System.out.println("front rear" + front + " " + rear);
                            if (front != 0) {
                                --front;
                                --rear;
                            }
                            ++movingCount;
                            invalidate();
                        }
                    } else {
                        if (rear < maxDyas) {
                          //  System.out.println("front rear" + front + " " + rear);
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

    private void drawCoordinate(Canvas canvas) {
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
        //textPaint.setTextSize(36f);
        //canvas.drawText("本月完成情况", (float) (0.4 * width), endPointY + (float) (0.3 * height), textPaint);
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
        //System.out.println("step " + step);


        wavePath.moveTo(startPointX, (float) (y_length * (1 - grade_week[front])) + startPointX);
        //canvas.drawText("哈哈",startPointX, (float) ((endPointY - startPointX) * (1 - grade_week[0])) + startPointX, colorPaint);//


        for (int i = 0, j = front; i < rear - front; i++, j++) {
            historgrimX = (startPointX + canvasLength * (i));

            canvas.drawText(monthDay[j] + "", historgrimX, (float) (historgrimY + 0.3 * startPointX), textPaint);//-------------x轴坐标

            if (j < currentDay) {
                if (grade_week[i] > 0.8) {
                    colorPaint.setColor(Color.parseColor("#28B78D"));
                } else if (grade_week[j] > 0.6 && grade_week[i] < 0.8) {
                    colorPaint.setColor(Color.parseColor("#fecc36"));
                } else if (grade_week[j] < 0.6) {
                    colorPaint.setColor(Color.parseColor("#D5544F"));
                }
                float x = startPointX + offset * (i);
                float y = (float) (y_length * (1 - grade_week[j])) + startPointX;
                canvas.drawText((grade_week[j] * 100) + "%", x, y, colorPaint);//-------------分数

                float x_location = startPointX + offset * (i);
                float y_location = (float) (y_length * (1 - grade_week[j])) + startPointX;
                wavePath.lineTo(x_location, y_location);
            }
        }
//        if (rear != maxDyas) {
//            wavePath.lineTo(startPointX + offset * 9, (float) (y_length * (1 - 0.8)) + startPointX);
//        }

        canvas.drawPath(wavePath, wavePaint);
        //System.out.println("------------" + Arrays.toString( grade_week));
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


        getMaxDyas(true, 1, 1);
        //System.out.println("maxDyas " + maxDyas);


    }

    private void getMaxDyas(boolean isCurrentMonth, int year, int month) {
        Calendar calendar = Calendar.getInstance();
        if (!isCurrentMonth) {
            if(month != calendar.get(Calendar.MONTH) + 1){
                calendar.set(year, month, 1);
            }
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //System.out.println("currentDay " + currentDay);
        maxDyas = calendar.get(Calendar.DATE);
        currentDay = !isCurrentMonth ? maxDyas : calendar.get(Calendar.DAY_OF_MONTH);
    }


    public void update(List<MissionItem> missionItems, String date) {
        if (missionItems.size() != 0) {
            clearALL();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            for (int i = 0, j = 0; i < maxDyas && j < missionItems.size(); i++) {
                String a = (missionItems.get(j).getMissionCreattime()).split("-")[2];
                if (a.contains("0") && Integer.parseInt(a) < 10) {
                    a = String.valueOf(a.charAt(1));
                }
                if (String.valueOf(i + 1).equals(a)) {

                    String[] grade = String.valueOf(missionItems.get(j).getMissionState()).split("");
                    int max = grade.length, rate = 0;
                    for (int k = 1; k < max; k++) {
                        if (grade[k].equals("1")) {
                            ++rate;
                        }
                    }
                    ++j;
                    double d = Double.valueOf(decimalFormat.format((double) (rate) / (max - 1)));
                    grade_week[i] = d;

                }
            }
            getMaxDyas(false, Integer.valueOf(date.split("-")[0]), Integer.valueOf(date.split("-")[1]));
            wavePath.reset();
            invalidate();
        }
    }


    private void clearALL(){
        for (int i = 0; i < grade_week.length; i++) {
            grade_week[i] = 0d;
        }
        front = 0;
        rear = 9;

    }



}
