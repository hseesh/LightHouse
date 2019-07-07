package com.hse.yatoufang.appUtils;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hse.yatoufang.CustormView.LoadingDialog;
import com.hse.yatoufang.R;

public class Dialog {
    private Handler handler;
    public Dialog(Handler handler, Context context, String messgae, int second) {
        this.handler = handler;
        showLoadingView(handler,context,messgae,second);
    }

    private void showLoadingView(Handler handler, Context context, String messgae, int second){
        LoadingDialog.Builder builder = new LoadingDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view1 = layoutInflater.inflate(R.layout.dialog_loading_layout,null);
        TextView textView = view1.findViewById(R.id.tv_loading_tips);
        if(!messgae.equals("")){
            textView.setText(messgae);
        }
        ImageView imageView = view1.findViewById(R.id.iv_loading);
        setAnitimation(imageView);
        builder.setView(view1);
        final LoadingDialog dialog = builder.create();
        dialog.show();
       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },second);
    }


    private void setAnitimation(final ImageView image) {
       handler.post(new Runnable() {
            @Override
            public void run() {
                AnimationSet animationSet = new AnimationSet(true);
                RotateAnimation r1 = new RotateAnimation(0, 270, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                RotateAnimation r2 = new RotateAnimation(270, 450, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                RotateAnimation r3 = new RotateAnimation(450, 540, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                RotateAnimation r4 = new RotateAnimation(430, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                r1.setDuration(800);

                r2.setDuration(800);
                r2.setStartOffset(800);

                r3.setDuration(600);
                r3.setStartOffset(1600);

                r4.setDuration(600);
                r4.setStartOffset(2200);

                animationSet.addAnimation(r1);
                animationSet.addAnimation(r2);
                animationSet.addAnimation(r3);
                animationSet.addAnimation(r4);
                image.setAnimation(animationSet);
            }
        });
    }
}
