package com.hse.yatoufang.CustormView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hse.yatoufang.R;


public class MyDialog extends Dialog {
    
    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private View contentView;
        private int imgId;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonListener;
        private OnClickListener negativeButtonListener;

        public Builder(Context context){
            this.context = context;
            this.positiveButtonText = "";
            this.negativeButtonText = "";
        }

        public Builder setImgId(int imgId) {
            this.imgId = imgId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessige(String message) {
            this.message = message;
            return this;
        }
        
        public Builder setView(View view){
            this.contentView = view;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(String positiveButtonText,OnClickListener listener) {
            this.negativeButtonText = positiveButtonText;
            this.negativeButtonListener = listener;
            return this;
        }

        public MyDialog create(){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            final MyDialog myDialog = new MyDialog(context, R.style.MyDialogStyle);

            View view = layoutInflater.inflate(R.layout.dialog_layout,null);
            ImageView headImage = view.findViewById(R.id.iv_title);
            TextView title = view.findViewById(R.id.tv_title);
            TextView content  = view.findViewById(R.id.tv_message);
            TextView negativeButton = view.findViewById(R.id.tv_negative);
            TextView positiveButton = view.findViewById(R.id.tv_positive);
            LinearLayout linearLayout = view.findViewById(R.id.dialog_content);
            RelativeLayout negative_layout = view.findViewById(R.id.quit_layout);
            RelativeLayout positive_layout = view.findViewById(R.id.sure_layout);

            if(this.imgId != 0){
                headImage.setImageResource(this.imgId);
            }
            if(this.title != null){
                title.setText(this.title);
            }
            if(this.message != null){
                content.setText(this.message);
            }
            if(this.contentView != null){
                linearLayout.removeAllViews();
                linearLayout.addView(this.contentView);
            }
            if(!this.negativeButtonText.equals("") && this.negativeButtonListener != null){
                negativeButton.setText(this.negativeButtonText);
                negative_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        negativeButtonListener.onClick(myDialog,DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }
            if(!this.positiveButtonText.equals("") && this.positiveButtonListener != null){
                positiveButton.setText(this.positiveButtonText);
                positive_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        positiveButtonListener.onClick(myDialog,DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            //设置dialog样式
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            myDialog.setContentView(view, params);

            return myDialog;
        }

    }

}
