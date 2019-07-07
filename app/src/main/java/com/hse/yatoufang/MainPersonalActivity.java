package com.hse.yatoufang;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hse.yatoufang.Fragment.NewsFragment;
import com.hse.yatoufang.Fragment.PersoanlHistory;
import com.hse.yatoufang.Fragment.PersoanlState;
import com.hse.yatoufang.Fragment.PersonalCount;
import com.hse.yatoufang.Fragment.PersonalDiary;
import com.hse.yatoufang.Fragment.PersonalSet;

public class MainPersonalActivity extends AppCompatActivity {

    private TextView title;
    private int flag;
    private PersonalCount personalCount;
    private PersonalDiary personalDiary;
    private PersoanlState persoanlState;
    private PersonalSet personalSet;
    private NewsFragment newsFragment;
    private PersoanlHistory persoanlHistory;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        title = findViewById(R.id.tv_title);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }
    private void initData(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent intent = getIntent();
        String text = "";
        flag = intent.getIntExtra("type",0);
        switch (flag){
            case 1:
                text = "我的账号";
                if(personalCount == null){
                    personalCount = new PersonalCount();
                }
                fragmentTransaction.replace(R.id.ll_content,personalCount);
                break;
            case 2:
                text = "我的状态";
                if(persoanlState == null){
                    persoanlState = new PersoanlState();
                }
                fragmentTransaction.replace(R.id.ll_content,persoanlState);
                break;
            case 3:
                text = "我的日记";
                if(personalDiary == null){
                    personalDiary = new PersonalDiary();
                }
                fragmentTransaction.replace(R.id.ll_content,personalDiary);
                break;
            case 4:
                text = "消息";
                if(newsFragment == null){
                    newsFragment = new NewsFragment();
                }
                fragmentTransaction.replace(R.id.ll_content,newsFragment);
                break;
            case 5:
                text = "关于";
                if(personalSet == null){
                    personalSet = new PersonalSet();
                }
                fragmentTransaction.replace(R.id.ll_content,personalSet);
                break;
            case 6:
                text = "我的足迹";
                if(persoanlHistory == null){
                    persoanlHistory = new PersoanlHistory();
                }
                fragmentTransaction.replace(R.id.ll_content,persoanlHistory);
                break;
        }
        title.setText(text);
        fragmentTransaction.commit();
    }

    @Override
    public void finish() {
        if(flag == 1 || flag == 5){
            if(personalCount != null && personalCount.isShouldUpdateImg()){
                setResult(101);
            }else if(personalSet != null && personalSet.isShouldReDraw()){
                setResult(105);
            }
        }
        super.finish();
    }
}
