package com.hse.yatoufang.CustormView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hse.yatoufang.R;


public class NewsDialog extends Dialog {

    public NewsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        private View contentView;
        private Context context;
        private String title;
        private String message;
        private String url;
        private OnClickListener positiveButtonListener;
        private OnClickListener negativeButtonListener;


        public Builder(Context context) {
            this.context = context;

        }

        public Builder setView(View view) {
            this.contentView = view;
            return this;
        }

        public Builder setUrl(String data) {
            this.url = data;
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


        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonListener = listener;
            return this;
        }


        public NewsDialog create() {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            final NewsDialog myDialog = new NewsDialog(context, R.style.MyDialogStyle);

            View view = layoutInflater.inflate(R.layout.dialog_news, null);
            LinearLayout linearLayout = view.findViewById(R.id.dialog_content);
            TextView title = view.findViewById(R.id.tv_title);
            TextView download = view.findViewById(R.id.tv_link);
            TextView content = view.findViewById(R.id.tv_message);

            if (this.title != null) {
                title.setText(this.title);
            }
            if (this.message != null) {
                content.setText(this.message);
            }
            if (this.url != null) {
                download.setText(this.url);
            }
            if (this.contentView != null) {
                linearLayout.addView(this.contentView);
            }

            view.findViewById(R.id.tv_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                    negativeButtonListener.onClick(myDialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });

            view.findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                    positiveButtonListener.onClick(myDialog, DialogInterface.BUTTON_POSITIVE);
                }
            });

            //设置dialog样式
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            myDialog.setContentView(view, params);

            return myDialog;
        }

    }

}
