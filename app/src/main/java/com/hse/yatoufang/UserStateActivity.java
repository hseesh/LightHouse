package com.hse.yatoufang;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hse.yatoufang.Fragment.CountingMonthFragment;
import com.hse.yatoufang.Fragment.CountingYearFragment;

import java.util.Calendar;


public class UserStateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_date;
    private int year, month ,date;
    private boolean isMonth;
    private CountingYearFragment countingYearFragment;
    private CountingMonthFragment countingMonthFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state);
        tv_date = findViewById(R.id.tv_date);

        findViewById(R.id.iv_last).setOnClickListener(this);
        findViewById(R.id.iv_next).setOnClickListener(this);
        findViewById(R.id.tv_month_mission).setOnClickListener(this);
        findViewById(R.id.tv_year_mission).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        countingMonthFragment = new CountingMonthFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_state_content, countingMonthFragment);
        fragmentTransaction.commit();

        isMonth = true;
        initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_last:
                setDate(false);
                break;
            case R.id.iv_next:
                setDate(true);
                break;
            case R.id.tv_month_mission:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (countingMonthFragment == null) {
                    countingMonthFragment = new CountingMonthFragment();
                    fragmentTransaction.replace(R.id.ll_state_content, countingMonthFragment);
                } else {
                    if (countingMonthFragment.isHidden()) {
                        fragmentTransaction.hide(countingYearFragment);
                        fragmentTransaction.show(countingMonthFragment);
                    }
                }
                fragmentTransaction.commit();
                isMonth = true;
                initData();
                break;
            case R.id.tv_year_mission:
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                if (countingYearFragment == null) {
                    countingYearFragment = new CountingYearFragment();
                    fragmentTransaction1.hide(countingMonthFragment);
                    fragmentTransaction1.add(R.id.ll_state_content, countingYearFragment);
                } else {
                    if (countingYearFragment.isHidden()) {
                        fragmentTransaction1.hide(countingMonthFragment);
                        fragmentTransaction1.show(countingYearFragment);
                    }
                }
                fragmentTransaction1.commit();
                isMonth = false;
                String m = date + "年";
                tv_date.setText(m);
                initView();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void setDate(boolean isUp) {
        if(isMonth){
            if (isUp) {
                if (month < 12 && month > 0) {
                    month++;
                } else {
                    month = 1;
                    ++year;
                }
            } else {
                if (month <= 12 && month > 1) {
                    month--;
                } else {
                    month = 12;
                    --year;
                }
            }
        }else{
            if(isUp){
                date ++;
            }else{
                date --;
            }
        }
        String date = isMonth ? year + "年" + month + "月" : this.date + "年";
        tv_date.setText(date);
        initView();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        date = year;
        month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "年" + month + "月";
        tv_date.setText(date);
        initView();
    }

    private void initView() {
        String m;
        if (month < 10) {
            m = year + "-" + 0 + month;
        } else {
            m = year + "-" + month;
        }
        updataView(m);
    }

    private void updataView(final String data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMonth) {
                    countingMonthFragment.updataView(data);
                } else {
                    countingYearFragment.updataView(String.valueOf(year));
                }
            }
        }, 500);
    }

}
