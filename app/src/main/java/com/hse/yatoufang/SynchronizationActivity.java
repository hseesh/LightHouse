package com.hse.yatoufang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.MissionItemUtils.MissionItemManger;
import com.hse.yatoufang.PersonalCenter.FileInforAdadpet;
import com.hse.yatoufang.appSocket.ConnectIntentService;

import com.hse.yatoufang.appUtils.Dialog;
import com.hse.yatoufang.appUtils.FileInfor;
import com.hse.yatoufang.appUtils.MessageCallback;
import com.hse.yatoufang.appUtils.UserInforUtil;


import java.util.ArrayList;

public class SynchronizationActivity extends AppCompatActivity implements View.OnClickListener {

    private FileInforAdadpet adadpet;
    private ArrayList<FileInfor> list;
    private View reject;
    private View login;
    private TextView load;
    private boolean banAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        ListView listView = findViewById(R.id.list_file_infor);
        load = findViewById(R.id.tv_load);
        login = findViewById(R.id.v_login);
        reject = findViewById(R.id.v_reject);
        load.setOnClickListener(this);

        findViewById(R.id.ll_reject).setOnClickListener(this);
        findViewById(R.id.ll_login).setOnClickListener(this);
        adadpet = new FileInforAdadpet(this);
        listView.setAdapter(adadpet);
        list = new ArrayList<>(4);
        initLocalData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_load:
                final String account = UserInforUtil.getAccount();
                if (load.getText().equals("上传")) {
                    for (int i = 0; i < 3; i++) {
                        String content = list.get(i + 1).getDescribe() + "--" + list.get(i + 1).getCount();
                        ConnectIntentService.uploadData(this, getPath(i), 1023 + 3 * i, account, content, new MessageCallback() {
                            @Override
                            public void getData(String data) {
                                System.out.println("data = " + data);
                                final String str = data;
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SynchronizationActivity.this, str, Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        });
                    }
                } else {
                   if(!banAction){
                       AlertDialog.Builder builder = new AlertDialog.Builder(this);
                       builder.setTitle("警告")
                               .setMessage("此操作将会用服务器数据替换本地数据，仅建议在跟换设备时进行，否则可能会导致数据丢失并且不可撤销！\n \n是否继续执行操作？")
                               .setPositiveButton("知道了，继续", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       for (int i = 0; i < 3; i++) {
                                           ConnectIntentService.uploadData(SynchronizationActivity.this, getPath(i), 1024 + 3 * i, account, "", new MessageCallback() {
                                               @Override
                                               public void getData(String data) {
                                                   System.out.println("data = " + data);
                                                   final String str = data;
                                                   Handler handler = new Handler(Looper.getMainLooper());
                                                   handler.post(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           Toast.makeText(SynchronizationActivity.this, str, Toast.LENGTH_LONG).show();

                                                       }
                                                   });
                                               }
                                           });
                                       }
                                   }
                               })
                               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               });
                       AlertDialog dialog = builder.create();
                       dialog.show();
                   }
                }
                break;

            case R.id.ll_reject:
                initLocalData();
                reject.setBackgroundResource(R.color.color_blue_light);
                login.setBackgroundResource(R.color.white);
                load.setText("上传");
                break;

            case R.id.ll_login:
                initHostData();
                reject.setBackgroundResource(R.color.white);
                login.setBackgroundResource(R.color.color_blue_light);
                load.setText("下载");
                writeConfig();
                break;
        }
    }


    private void initHostData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        if (sharedPreferences.getBoolean("is_tourist", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            new Dialog(new Handler(getMainLooper()), this, "正在获取远程文件信息...", 1000);
            list.clear();
            String account = sharedPreferences.getString("account", "游客");
            ConnectIntentService.upoLoadMessage(this, 1035 + "==" + account, new MessageCallback() {
                @Override
                public void getData(String data) {
                    if (data != null) {
                        System.out.println("data = " + data);
                        if (data.equals("获取远程信息失败")) {
                            data = " -- % -- % -- % -- ";
                            banAction = true;
                        }
                        final String str = data;
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                loadInfer(str);
                            }
                        });

                    }
                }
            });
        }
    }


    private void initLocalData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        if (sharedPreferences.getBoolean("is_tourist", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            list.clear();
            new Dialog(new Handler(getMainLooper()), this, "正在获取本地文件信息...", 500);
            MissionItemManger items = new MissionItemManger(this, 1);
            MissionItemManger diary = new MissionItemManger(this, 3);
            MissionItemManger missionItemState = new MissionItemManger(this, 2);
            list.add(new FileInfor("名称", "持续时间", "记录次数"));
            FileInfor infor = items.getItemsInfor();
            if (infor != null) {
                list.add(items.getItemsInfor());
                list.add(diary.getDiaryInfor());
                list.add(missionItemState.getMissionItemStateinfor());
            }
            adadpet.setList(list);
        }

    }

    private void loadInfer(String data) {
        list.clear();
        String[] name = new String[]{"任务表", "日记表", "统计表"};
        String str[] = data.split("%");
        list.add(new FileInfor("名称", "持续时间", "记录次数"));
        for (int i = 0; i < str.length - 1; i++) {
            String[] infor = str[i].split("--");
            list.add(new FileInfor(name[i], infor[0], infor[1] + " "));
        }
        list.add(new FileInfor("上传于", str[3], ""));

        adadpet.setList(list);
    }

    private String getPath(int type) {
        String[] str = new String[]{"missionItem", "diary", "missionItemState"};
        return this.getDatabasePath(str[type]).getAbsolutePath();
    }

    private void writeConfig() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("configuration", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("reloadView", true);
        editor.apply();
    }


}

//    private void initData() {
//        String path = context.getDatabasePath("missionItemState").getAbsolutePath();
//        System.out.println(path);
//        ConnectIntentService.uploadData(context, path, 1023, "2435899106@qq.com", "2019-02-01--2019-02-09--8");
//
//    }
//  /data/data/com.hse.yatoufang/databases/missionState
//  /data/data/com.hse.yatoufang/databases/missionItemState
//  /data/data/com.hse.yatoufang/databases/diary
//  /data/data/com.hse.yatoufang/databases/missionItem