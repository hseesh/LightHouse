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

import java.util.ArrayList;
import java.util.Map;

public class StatisticsYear extends View {

    private Paint coordiatePaint;
    private Paint textPaint;
    private Paint colorPaint;
    private Path valuePath;
    private Path cooridatePath;
    private ArrayList<String> mission_name;
    private ArrayList<Integer> mission_grade;
    private int size;

    public StatisticsYear(Context context) {
        super(context);
    }


    public StatisticsYear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public StatisticsYear(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    }

    private void drawCoordinate(Canvas canvas) {
        int width = getWidth();
        float startPointX = (float) (width * 0.33);
        float startPointY = (float) (width * 0.05);

        if (size > 6) {
            startPointY = (float) (width * (0.05 + 0.05 * (mission_name.size() / 6)));
        }


        float x_length = (width - startPointX) / 8;
        float y_length = (startPointY * 9);
        float y_part = (float) (width * 0.05 * 9 / 6); // is a default height of an item

        for (int i = 0; i < 8; i++) {
            float x = startPointX + (i * x_length);
            cooridatePath.moveTo(x, startPointY);
            cooridatePath.lineTo(x, startPointY + y_length);
            if(i != 7){
                canvas.drawText(i  * 50  + "" , startPointX + ((i) * x_length), (float) (startPointY * 1.8 + y_length), textPaint);
            }
            // 原生方法参数是《String》  且 参数为Int 时，不可直接写 int 因为这个参数会转换会 .R 文件的 直接引用
        }

        canvas.drawPath(cooridatePath, coordiatePaint);

        if (mission_grade != null) {
            for (int i = 0; i < mission_name.size(); i++) {
                canvas.drawText(mission_name.get(i), (float) (width * 0.03), (float) (y_part * (i + 1.5)), textPaint);
            }
        }
    }

    private void drawValue(Canvas canvas) {
        int width = getWidth();
        float startPointX = (float) (width * 0.33);

        float x_length = (width - startPointX) / 400;
        float y_part = (float) (width * 0.05 * 9 / 6); // is a default height of an item


        for (int i = 0; i < mission_grade.size(); i++) {
            valuePath.moveTo(startPointX,   (float) (y_part * (i + 1.4)));
            if (mission_grade.get(i) == 0) {
                valuePath.lineTo(startPointX,  (float) (y_part * (i + 1.4)));
            } else {
                valuePath.lineTo(startPointX + x_length * (mission_grade.get(i)),   (float) (y_part * (i + 1.4)));

            }
            canvas.drawText(mission_grade.get(i) + "次", (float) (startPointX + x_length * (mission_grade.get(i) + 0.3)), (float) (y_part * (i + 1.5)), textPaint);
        }

        canvas.drawPath(valuePath, colorPaint);

    }

    private void init() {
        coordiatePaint = new Paint();
        cooridatePath = new Path();
        textPaint = new Paint();
        valuePath = new Path();
        colorPaint = new Paint();

        coordiatePaint.setStrokeWidth(2f);
        coordiatePaint.setColor(Color.parseColor("#6C6C6C"));
        coordiatePaint.setStyle(Paint.Style.STROKE);
        coordiatePaint.setAntiAlias(true);

        textPaint.setTextSize(29f);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#E9F2FB"));

        colorPaint.setStrokeWidth(25f);
        coordiatePaint.setAntiAlias(true);
        colorPaint.setStyle(Paint.Style.STROKE);
        colorPaint.setColor(Color.parseColor("#DCE6F4"));

    }


    public void updateView(Map<String, Integer> map){
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
            requestLayout();
        }
    }
}
