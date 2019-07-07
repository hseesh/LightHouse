package com.hse.yatoufang.CustormView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hse.yatoufang.appUtils.MyApplication;

import java.util.Calendar;


public class MyTextView extends AppCompatTextView {


    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void showText() {
        final String[] str = getWords();
        ValueAnimator animator = ValueAnimator.ofFloat(1, 10);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int count = (int) ((float) animation.getAnimatedValue());
                if (count < 10) {
                    setText(str[count]);
                }
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private String[] getWords() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("configuration", Context.MODE_PRIVATE);
        String[] str = new String[]{
                "                  愿",
                "                愿经",
                "              愿经历",
                "            愿经历千",
                "          愿经历千帆,",
                "        愿经历千帆,归",
                "      愿经历千帆,归来",
                "    愿经历千帆,归来仍",
                "  愿经历千帆,归来仍少",
                "愿经历千帆,归来仍少年"};
        if (date == 28) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int save = sharedPreferences.getInt("month", -1);
            if (month != save || save == -1) {
                editor.putInt("month", month);
                editor.apply();
                return new String[]{
                        "                  亲爱",
                        "                亲爱的",
                        "              亲爱的，你",
                        "            亲爱的，你真",
                        "          亲爱的，你真的",
                        "        亲爱的，你真的还",
                        "      亲爱的，你真的还不",
                        "    亲爱的，你真的还不够",
                        "  亲爱的，你真的还不够努",
                        "亲爱的，你真的还不够努力"};
            }
        }
        return str;
    }


}




