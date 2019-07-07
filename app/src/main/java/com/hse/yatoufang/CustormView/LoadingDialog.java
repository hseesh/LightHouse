package com.hse.yatoufang.CustormView;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.LinearLayout;



import com.hse.yatoufang.R;

public class LoadingDialog extends Dialog {


    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private Context context;
        private View contentView;


        public Builder(Context context){
            this.context = context;
        }



        public LoadingDialog.Builder setView(View view) {
            this.contentView = view;
            return this;
        }



        public LoadingDialog create() {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            LoadingDialog myDialog = new LoadingDialog(context, R.style.MyDialogStyle);

            View view = layoutInflater.inflate(R.layout.dialog_frame, null);

            LinearLayout linearLayout = view.findViewById(R.id.ll_dialog_content);

            if (this.contentView != null) {
                linearLayout.addView(this.contentView);
            }

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            myDialog.setContentView(view, params);
           // myDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            return myDialog;
        }

    }


}
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//       new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                AnimationSet animationSet = new AnimationSet(true);
//                RotateAnimation r1 = new RotateAnimation(0, 270, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                RotateAnimation r2 = new RotateAnimation(270, 450, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                RotateAnimation r3 = new RotateAnimation(450, 540, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                RotateAnimation r4 = new RotateAnimation(430, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                r1.setDuration(800);
//
//                r2.setDuration(800);
//                r2.setStartOffset(800);
//
//                r3.setDuration(600);
//                r3.setStartOffset(1600);
//
//                r4.setDuration(600);
//                r4.setStartOffset(2200);
//
//                animationSet.addAnimation(r1);
//                animationSet.addAnimation(r2);
//                animationSet.addAnimation(r3);
//                animationSet.addAnimation(r4);
//                imageView.setAnimation(animationSet);
//                myDialog.dismiss();
//            }
//        });

