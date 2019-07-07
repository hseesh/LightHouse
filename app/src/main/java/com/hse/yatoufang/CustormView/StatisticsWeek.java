package com.hse.yatoufang.CustormView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;
import com.hse.yatoufang.R;

import java.util.ArrayList;
import java.util.Map;

public class StatisticsWeek extends View {

    private Paint coordinatePaint;
    private Paint textPaint;
    private Paint colorPaint;
    private Path valuePath;
    private Path coordinatePath;
    private ArrayList<String> mission_name;// = {"说好的电话号码你", "我好开星", "你喜欢不是嘛们", "说好的明天寄托吧", "我会好好学习java源码的", "我不会告诉你的"};
    private ArrayList<Integer> mission_grade;// = new ArrayList();
    private int count = 0;
    private int count_value = 1;
    private boolean isMonth;
    private int size;

    public StatisticsWeek(Context context) {
        super(context);
        init();

    }


    public StatisticsWeek(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.StatisticsWeek);
        isMonth = array.getBoolean(R.styleable.StatisticsWeek_isMonth,false);
        array.recycle();
        init();
    }


    public StatisticsWeek(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinate(canvas);
        if (mission_grade != null ) {
            if(mission_grade.size() != 0){
                drawValue(canvas);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        size = mission_grade == null ? 5 : mission_grade.size();
        if (size > 6) {
            setMeasuredDimension(width, (int) (height * 0.35 + (mission_name.size()  - 4) * (height * 0.35) / 6));
        } else {
            setMeasuredDimension(width, (int) (height * (0.35)));
        }
    }// (height * 0.35) / 6)      is a default height of an item

    /**
     *  1. 预估并设置view的大小 onMeasure();
     *  2. 开始绘制view的内容 onDraw();
     *      2.1 绘制坐标轴
     *      2.2 绘制数值
     *
     *  具体数值均是根据屏幕宽度预估而来,没有太多字面意义
     *  如：取屏幕宽度的 十分之之一 长度的 三分之一 作为起点坐标 ，以这个点为相对位置 绘制整个表
     *
     *  动画部分写的很粗糙
     */
    private void drawCoordinate(Canvas canvas) {
        int width = getWidth();// all the number are the estimated value by device screen size
        float startPointX = (float) (width * 0.33);
        float startPointY = (float) (width * 0.05);


        float x_length = (width - startPointX) / 8;
        float y_part = (float) (width * 0.05 * 9 / 6); // is a default height of an item
        float y_length;
        if(size < 6){
            y_length = (startPointY * 9);
        }else{
            y_length = (startPointY + (size + 1) * y_part);
        }

        for (int i = 0; i < 8; i++) {
            float x = startPointX + (i * x_length);
            coordinatePath.moveTo(x, startPointY);
            coordinatePath.lineTo(x, y_length);
            // draw x coordinate
            if(i == 7){
                if(!isMonth){
                    canvas.drawText(7 + "",startPointX + ((i) * x_length),  y_length, textPaint);
                }
            }else{
                canvas.drawText((isMonth ? i  * 5 : i) + "", startPointX + ((i) * x_length), y_length, textPaint);
            }

        }

        canvas.drawPath(coordinatePath, coordinatePaint);
        // draw y  coordinate 
        if (mission_grade != null) {
            for (int i = 0; i < mission_name.size(); i++) {
                canvas.drawText(mission_name.get(i), (float) (width * 0.03), (float) (y_part * (i + 1.5)), textPaint);
            }
        }
    }

    private void drawValue(Canvas canvas) {
        int width = getWidth();
        float startPointX = (float) (width * 0.33);

        float x_length = (width - startPointX) / 8;
        float y_part = (float) (width * 0.05 * 9 / 6); // is a default height of an item
        x_length = isMonth ? x_length / 5 : x_length;

        valuePath.moveTo(startPointX, (float) (y_part * (count + 1.4)));
        if (mission_grade.get(count) == 0) {
            valuePath.lineTo(startPointX, (float) (y_part * (count + 1.4)));
        } else {
            valuePath.lineTo(startPointX + x_length * (count_value), (float) (y_part * (count + 1.4)));
            if(count == mission_grade.size() - 1  && size > 1){
                valuePath.lineTo(startPointX + x_length * (mission_grade.get(count - 1)), (float) (y_part * (count + 1.4)));
            }
        }

        canvas.drawPath(valuePath, colorPaint);

        // replace fellow method block with ValueAnimator to get better animator performance
        //may cause critical redraw action (I think so)
        if (count < mission_name.size() - 1) {
            if (count_value < mission_grade.get(count)) {
                ++count_value;
                invalidate();
            } else {
                ++count;
                count_value = 1;
                invalidate();
            }
        } else {
            count = 0;
            for (int i = 0; i < mission_grade.size(); i++) {
                canvas.drawText(mission_grade.get(i) + "次", (float) (startPointX + x_length * (mission_grade.get(i) + 0.3)), (float) (y_part * (i + 1.5)), textPaint);
            }
        }
    }

    private void init() {
        coordinatePaint = new Paint();
        coordinatePath = new Path();
        textPaint = new Paint();
        valuePath = new Path();
        colorPaint = new Paint();

        coordinatePaint.setStrokeWidth(2f);
        coordinatePaint.setColor(Color.parseColor("#6C6C6C"));
        coordinatePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint.setAntiAlias(true);

        textPaint.setTextSize(29f);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#E9F2FB"));

        colorPaint.setStrokeWidth(25f);
        coordinatePaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.STROKE);
        colorPaint.setColor(Color.parseColor("#DCE6F4"));

        if(!isMonth){
            MissionItemStatistics  missionItemStatistics = new MissionItemStatistics(getContext(), isMonth ? 1 : 2);
            Map<String, Integer> map = missionItemStatistics.getState();
            this.mission_name = new ArrayList<>(map.size());
            this.mission_grade = new ArrayList<>(map.size());
            for (Map.Entry<String, Integer> map1 : map.entrySet()) {
                mission_name.add(map1.getKey());
                mission_grade.add(map1.getValue());
            }
        }

    }


    public void update(Map<String, Integer> map){
        if(map.size() != 0){
            if(mission_grade != null){
                mission_name.clear();
                mission_grade.clear();
            }
            int size = map.size();
            mission_name = new ArrayList<>(size);
            mission_grade = new ArrayList<>(size);

            for (Map.Entry<String, Integer> map1 : map.entrySet()) {
                mission_name.add(map1.getKey());
                mission_grade.add(map1.getValue());
            }
            valuePath.reset();
            count = 0;
            count_value = 1;
            requestLayout();
        }
    }
}
