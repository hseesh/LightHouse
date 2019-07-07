package com.hse.yatoufang;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.ActionCallback;
import com.hse.yatoufang.appUtils.Dialog;
import com.hse.yatoufang.appUtils.UserInforUtil;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_account;
    private EditText ed_password;
    private EditText ed_nickname;
    private  Handler handler;
    private View reject;
    private View login;
    private Button btn_login;
    private TextView tv_tips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.v_login);
        reject = findViewById(R.id.v_reject);
        tv_tips = findViewById(R.id.tv_tips);
        btn_login = findViewById(R.id.btn_login);
        ed_account = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_nickname = findViewById(R.id.ed_nickname);

        tv_tips.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        findViewById(R.id.ll_reject).setOnClickListener(this);
        findViewById(R.id.ll_login).setOnClickListener(this);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String data;
                handler = new Handler(Looper.getMainLooper());
                String account = ed_account.getText().toString();
                String password = ed_password.getText().toString();
                if (!account.equals("") && !password.equals("")) {
                    if(account.contains("@") && account.contains(".com")){
                        if(ed_nickname.getVisibility() == View.VISIBLE){
                            String nickname = ed_nickname.getText().toString();
                            writeInfor(account,password,nickname);
                            data =  1001 + "==" + UserInforUtil.getUserInformation();
                        }else{
                            data =  1002 + "==" + account + "%" + password ;

                        }
                        ConnectIntentService.upoLoadMessage(this, data, new ActionCallback() {
                            @Override
                            public void action(String data) {
                                setResult(102);
                                finish();
                            }
                        });
                        // 下载 头像
//                        if(ed_nickname.getVisibility() == View.INVISIBLE){
//                            data =  1006 + "==" + account;
//                            ConnectIntentService.upoLoadMessage(this,data);
//                        }

                    }else{
                        Toast.makeText(this,"邮箱不正确",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.ll_reject:
                ed_nickname.setVisibility(View.VISIBLE);
                reject.setBackgroundResource(R.color.colorGreen);
                login.setBackgroundResource(R.color.white);
                tv_tips.setVisibility(View.INVISIBLE);
                btn_login.setText("注册");
                break;
            case R.id.ll_login:
                ed_nickname.setVisibility(View.INVISIBLE);
                reject.setBackgroundResource(R.color.white);
                login.setBackgroundResource(R.color.colorGreen);
                tv_tips.setVisibility(View.VISIBLE);
                btn_login.setText("登录");
                break;
            case R.id.tv_tips:
                break;
        }
    }



    private void writeInfor(String account, String password, String nickname){
        SharedPreferences sharedPreferences = this.getSharedPreferences("user",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_tourist",false);
        editor.putString("account",account);
        editor.putString("password",password);
        editor.putString("user_nickname",nickname);
        editor.apply();

    }

    private void init(){
        ed_nickname.setVisibility(View.INVISIBLE);
        reject.setBackgroundResource(R.color.white);
        login.setBackgroundResource(R.color.colorGreen);
        tv_tips.setVisibility(View.VISIBLE);
        btn_login.setText("登录");
    }

    private void showAlet(String data){
        new Dialog(handler,LoginActivity.this,data,1000);
    }


}
