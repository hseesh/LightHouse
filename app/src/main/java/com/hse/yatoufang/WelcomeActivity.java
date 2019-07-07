package com.hse.yatoufang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.hse.yatoufang.CustormView.MyTextView;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.UserInforUtil;

import java.util.UUID;

public class WelcomeActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wecome_layout);
        MyTextView myTextView = findViewById(R.id.tv_welcome_tip);
        VideoView videoView = findViewById(R.id.video);
        myTextView.showText();
        videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.logo));
        videoView.start();
        ConnectIntentService.upoLoadMessage(this,1000 + "==" + getUniqueId() + "%" + UserInforUtil.getAccount());
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        },1600);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private String getUniqueId() {
        StringBuilder sb = new StringBuilder();
        sb.append(Build.BOARD);
        sb.append(Build.BRAND);
        sb.append(Build.CPU_ABI);
        sb.append(Build.DEVICE);
        sb.append(Build.DISPLAY);
        sb.append(Build.HOST);
        sb.append(Build.ID);
        sb.append(Build.MANUFACTURER);
        sb.append(Build.MODEL);
        sb.append(Build.PRODUCT);
        UUID uuid =  new UUID(sb.toString().hashCode(),20190310);
        return  uuid.toString();
    }
}
