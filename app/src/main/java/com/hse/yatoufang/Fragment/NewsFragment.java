package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.LoadingDialog;
import com.hse.yatoufang.CustormView.NewsDialog;
import com.hse.yatoufang.MainActivity;
import com.hse.yatoufang.R;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.MessageCallback;
import com.hse.yatoufang.appUtils.MyApplication;
import com.hse.yatoufang.appUtils.UserInforUtil;

import java.util.UUID;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Looper.getMainLooper;


public class NewsFragment extends Fragment {
    private Context context;
    private TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        textView = view.findViewById(R.id.tv_news);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("configuration", Context.MODE_PRIVATE);
                String data = sharedPreferences.getString("news","无消息");
               if(!data.equals("无消息")){
                   final String[] dats = data.split("==");
                   NewsDialog.Builder builder = new NewsDialog.Builder(context)
                           .setTitle(dats[0])
                           .setMessige(dats[1])
                           .setUrl(dats[2])
                           .setNegativeButton(new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent intent = new Intent();
                                   intent.setData(Uri.parse(dats[2]));//Url 就是你要打开的网址
                                   intent.setAction(Intent.ACTION_VIEW);
                                   context.startActivity(intent); //启动浏览器
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
            }
        });
        initData();
        return view;
    }


    private void initData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("configuration", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("news","无消息");
       if(data.equals("无消息")){
           textView.setText(data);
       }else{
           final String[] dats = data.split("==");
           textView.setText(dats[0]);
       }
    }

}