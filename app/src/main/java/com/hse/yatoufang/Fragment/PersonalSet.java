package com.hse.yatoufang.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.LoadingDialog;
import com.hse.yatoufang.R;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.Dialog;
import com.hse.yatoufang.appUtils.UserInforUtil;

public class PersonalSet extends Fragment implements View.OnClickListener {

    private Context context;
    private boolean shouldReDraw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_setting,null);

        view.findViewById(R.id.rl_about).setOnClickListener(this);
        view.findViewById(R.id.rl_update).setOnClickListener(this);
        view.findViewById(R.id.rl_logout).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        view.findViewById(R.id.rl_disclaimer).setOnClickListener(this);
        view.findViewById(R.id.rl_synchronization).setOnClickListener(this);

        return view;
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.rl_about:

                AlertDialog.Builder about = new AlertDialog.Builder(context);
                View view1 = View.inflate(context,R.layout.set_about,null);
                view1.findViewById(R.id.tv_github).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://github.com/hseesh/Git"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        context.startActivity(intent); //启动浏览器
                    }
                });
                about.setView(view1);
                AlertDialog dialog = about.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.rl_update:
                AlertDialog.Builder update = new AlertDialog.Builder(context);
                update.setMessage("已是最新版本");
                AlertDialog dialog3 = update.create();
                dialog3.setCanceledOnTouchOutside(true);
                dialog3.show();
                break;
            case R.id.rl_feedback:
                AlertDialog.Builder feedback = new AlertDialog.Builder(context);
                View view2 = View.inflate(context,R.layout.set_feedback,null);
                final EditText feedtext = view2.findViewById(R.id.ed_feedback);
                feedback.setTitle("意见反馈");
                feedback.setView(view2);
                final AlertDialog dialog4 = feedback.create();
                dialog4.setCanceledOnTouchOutside(true);
                dialog4.show();
                view2.findViewById(R.id.iv_send).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = feedtext.getText().toString();
                        if(!(text.length() < 1)){
                            ConnectIntentService.upLoadMessage(context,1010,text, UserInforUtil.getAccount());
                        }
                        Toast.makeText(context,"感谢你的反馈，我们会努力的",Toast.LENGTH_SHORT).show();
                        dialog4.dismiss();
                    }
                });
                break;
            case R.id.rl_disclaimer:
                final AlertDialog.Builder disclaimer = new AlertDialog.Builder(context);
                disclaimer.setView(View.inflate(context,R.layout.set_disclaimer,null));
                AlertDialog dialog2 = disclaimer.create();
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.show();
                break;
            case R.id.rl_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("退出登录");
                builder.setMessage("退出登录将清除本地账号信息，保留用户数据，确认继续吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearData();
                    }
                });
                AlertDialog dialog5 = builder.create();
                dialog5.setCanceledOnTouchOutside(true);
                dialog5.show();
                break;
            case R.id.rl_synchronization:
                AlertDialog.Builder builder6 = new AlertDialog.Builder(context);
                builder6.setTitle("如何删除");
                ImageView imageView = new ImageView(context);
                imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_modify, null));
                builder6.setView(imageView);
                builder6.setMessage("左右滑动项目即可删除或者修改项目");
                AlertDialog dialog6 = builder6.create();
                dialog6.setCanceledOnTouchOutside(true);
                dialog6.show();
                break;
        }
    }


    private void clearData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_tourist",true);
        editor.putString("account","游客");
        editor.putString("password","admin");
        editor.putString("user_nickname","游客");
        editor.apply();
        shouldReDraw = true;
        new Dialog(new Handler(Looper.getMainLooper()),context,"加载中...",1000);

    }


    public boolean isShouldReDraw() {
        return shouldReDraw;
    }


}
