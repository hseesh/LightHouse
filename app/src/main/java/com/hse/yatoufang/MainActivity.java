package com.hse.yatoufang;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.NewsDialog;
import com.hse.yatoufang.Fragment.MeFragment;
import com.hse.yatoufang.Fragment.MissionFragment;
import com.hse.yatoufang.Fragment.SetFragment;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.MessageCallback;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MissionFragment missionFragment;
    private MeFragment meFragment;
    private SetFragment setFragment;
    private TextView me;
    private TextView list;
    private TextView record;
    private long exitsTime;
    private boolean reloadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = findViewById(R.id.tv_me);
        list = findViewById(R.id.tv_list);
        record = findViewById(R.id.tv_record);

        me.setOnClickListener(this);
        list.setOnClickListener(this);
        record.setOnClickListener(this);

        loadInfor();
        initData();
        initView();

    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_record:
                clearState();
                record.setSelected(true);
                if (setFragment != null && !setFragment.isHidden()) {
                    fragmentTransaction.hide(setFragment);
                }
                if (missionFragment != null && !missionFragment.isHidden()) {
                    fragmentTransaction.hide(missionFragment);
                }
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.ll, meFragment);
                    fragmentTransaction.show(meFragment);
                } else {
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    boolean reload = preferences.getBoolean("isSaved", false);
                    if (reload || reloadView) {
                        fragmentTransaction.remove(meFragment);
                        meFragment = new MeFragment();
                        fragmentTransaction.add(R.id.ll, meFragment);
                        reloadView = false;
                    }
                    fragmentTransaction.show(meFragment);

                }
                break;

            case R.id.tv_list:
                clearState();
                list.setSelected(true);
                if (setFragment != null && !setFragment.isHidden()) {
                    fragmentTransaction.hide(setFragment);
                }
                if (meFragment != null && !meFragment.isHidden()) {
                    fragmentTransaction.hide(meFragment);
                }
                if (missionFragment == null) {
                    missionFragment = new MissionFragment();
                    fragmentTransaction.add(R.id.ll, missionFragment);
                    fragmentTransaction.show(missionFragment);
                } else {
                    if (reloadView) {
                        missionFragment = new MissionFragment();
                        fragmentTransaction.add(R.id.ll, missionFragment);
                        fragmentTransaction.show(missionFragment);
                    }
                    fragmentTransaction.show(missionFragment);
                }
                break;

            case R.id.tv_me:
                clearState();
                me.setSelected(true);
                if (missionFragment != null && !missionFragment.isHidden()) {
                    fragmentTransaction.hide(missionFragment);
                }
                if (meFragment != null && !meFragment.isHidden()) {
                    fragmentTransaction.hide(meFragment);
                }
                if (setFragment == null) {
                    setFragment = new SetFragment();
                    fragmentTransaction.add(R.id.ll, setFragment);
                    fragmentTransaction.show(setFragment);
                } else {
                    fragmentTransaction.show(setFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            if (System.currentTimeMillis() - exitsTime > 2000) {
                exitsTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = this.getSharedPreferences("configuration", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("reloadView", false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("reloadView", false);
            editor.apply();
            reloadView = true;
        }

    }

    /**
     *  获取广播
     */
    private void loadInfor(){
        ConnectIntentService.upLoadMessage(MainActivity.this, "1022==?", new MessageCallback() {
            @Override
            public void getData(String data) {
                if(data != null && !data.equals("") ){
                    SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("configuration", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int code = sharedPreferences.getInt("code",100);
                    final String[] dats = data.split("==");
                    int newCode = Integer.valueOf(dats[3]);
                    if(newCode != code) {
                        editor.putString("news",data);
                        editor.putInt("code",newCode);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                NewsDialog.Builder builder = new NewsDialog.Builder(MainActivity.this)
                                        .setTitle(dats[0])
                                        .setMessige(dats[1])
                                        .setUrl(dats[2])
                                        .setNegativeButton(new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setData(Uri.parse(dats[2]));
                                                intent.setAction(Intent.ACTION_VIEW);
                                                MainActivity.this.startActivity(intent);
                                            }
                                        })
                                        .setPositiveButton(new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                NewsDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                    editor.apply();
                }
            }
        });
    }


    private void clearState() {
        me.setSelected(false);
        list.setSelected(false);
        record.setSelected(false);
    }


    public void initData() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        if (preferences.getString("date", "").equals(dateString)) {
            editor.putBoolean("isFirst", false);//同一天
            editor.putBoolean("isToday", false);
            editor.apply();
        } else {//不是当天
            editor.putString("date", dateString);
            editor.putBoolean("isSaved", false);
            editor.putBoolean("isToday", true);
            editor.putBoolean("isFirst", true);
            editor.apply();
        }
    }

    private void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        missionFragment = new MissionFragment();
        fragmentTransaction.add(R.id.ll, missionFragment);
        fragmentTransaction.show(missionFragment);
        fragmentTransaction.commit();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
