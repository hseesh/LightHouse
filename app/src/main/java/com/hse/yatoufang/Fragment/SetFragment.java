package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hse.yatoufang.CustormView.RoundImageView;
import com.hse.yatoufang.MainPersonalActivity;
import com.hse.yatoufang.R;
import com.hse.yatoufang.UserStateActivity;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.MyApplication;
import com.hse.yatoufang.appUtils.UserInforUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * Created by Administrator on 2018-11-21.
 */

public class SetFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private RoundImageView user_img;
    private TextView user_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, null);
        view.findViewById(R.id.user_photo).setOnClickListener(this);
        view.findViewById(R.id.user_states).setOnClickListener(this);
        view.findViewById(R.id.user_statistics).setOnClickListener(this);
        view.findViewById(R.id.user_diaries).setOnClickListener(this);
        view.findViewById(R.id.user_sets).setOnClickListener(this);
        view.findViewById(R.id.news).setOnClickListener(this);
        view.findViewById(R.id.user_history).setOnClickListener(this);
        initData(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_photo:
                Intent intent = new Intent(context, MainPersonalActivity.class);
                intent.putExtra("type",1);
                startActivityForResult(intent,100);
                break;
            case R.id.user_states:
                Intent intent0 = new Intent(context, MainPersonalActivity.class);
                intent0.putExtra("type",2);
                startActivity(intent0);
                break;
            case R.id.user_statistics:
                Intent intent1 = new Intent(context, UserStateActivity.class);
                startActivity(intent1);
                break;
            case R.id.user_diaries:
                Intent intent2 = new Intent(context, MainPersonalActivity.class);
                intent2.putExtra("type",3);
                startActivity(intent2);
                break;
            case R.id.news:
                Intent intent3 = new Intent(context, MainPersonalActivity.class);
                intent3.putExtra("type",4);
                startActivity(intent3);
                break;
            case R.id.user_sets:
                Intent intent4 = new Intent(context, MainPersonalActivity.class);
                intent4.putExtra("type",5);
                startActivityForResult(intent4,100);
                break;
            case R.id.user_history:
                Intent intent5 = new Intent(context, MainPersonalActivity.class);
                intent5.putExtra("type",6);
                startActivity(intent5);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(resultCode == 101){
           SharedPreferences sharedPreferences = context.getSharedPreferences("user",0);
           user_name.setText(sharedPreferences.getString("user_nickname","游客"));
           String photo = sharedPreferences.getString("user_img","");
           if(!photo.equals("")){
               ConnectIntentService.upLoadMessage(context,1007,photo, UserInforUtil.getAccount());
               byte[] byteArray = Base64.decode(photo,Base64.DEFAULT);
               ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
               user_img.setImage(byteArrayInputStream);
               try {
                   byteArrayInputStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }else if( resultCode == 105){
           SharedPreferences sharedPreferences = context.getSharedPreferences("user",0);
           String name = sharedPreferences.getString("user_nickname","游客");
           user_name.setText(name);
       }
    }

    private void initData(View view){
        user_name = view.findViewById(R.id.user_name);
        TextView user_level = view.findViewById(R.id.user_level);
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",0);
        user_name.setText(sharedPreferences.getString("user_nickname","游客"));
        user_level.setText(sharedPreferences.getString("user_level","Lv1"));
        user_img = view.findViewById(R.id.user_photo);
        String photo = sharedPreferences.getString("user_img","");
        if(!photo.equals("")){
            byte[] byteArray = Base64.decode(photo,Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            user_img.setImage(byteArrayInputStream);
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
