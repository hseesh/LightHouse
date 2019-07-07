package com.hse.yatoufang.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.RoundImageView;
import com.hse.yatoufang.LoginActivity;
import com.hse.yatoufang.SynchronizationActivity;
import com.hse.yatoufang.R;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.Dialog;
import com.hse.yatoufang.appUtils.UserInforUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalCount extends Fragment implements View.OnClickListener {
    private Uri imageUri = Uri.parse("file:///sdcard/temp.jpg");
    private RoundImageView iv_photo;
    private TextView tv_name;
    private TextView tv_email;
    private Context context;
    private boolean shouldUpdateImg;
    private boolean shouldUpdateInfor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count, null);
        iv_photo = view.findViewById(R.id.user_photo);
        tv_name = view.findViewById(R.id.tv_nickname);
        tv_email = view.findViewById(R.id.tv_account);
        view.findViewById(R.id.ac_img).setOnClickListener(this);
        view.findViewById(R.id.ac_password).setOnClickListener(this);
        view.findViewById(R.id.ac_nickname).setOnClickListener(this);
        view.findViewById(R.id.ac_synchronization).setOnClickListener(this);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        tv_name.setText(sharedPreferences.getString("user_nickname", "游客"));
        tv_email.setText(sharedPreferences.getString("account", ""));

        upDataImg();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_img:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
                shouldUpdateInfor = false;
                break;
            case R.id.ac_nickname:
                showAlet(1, "设置昵称");
                break;
            case R.id.ac_password:
                showAlet(3, "修改密码");
                break;
            case R.id.ac_synchronization:
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("数据同步")
                        .setMessage("备份本地数据至服务器或下载服务器上的数据至本地，此操作仅建议在跟换设备时进行")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(context,SynchronizationActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 102:
                shouldUpdateInfor = true;
                break;
        }
        switch (requestCode) {
            case 1000:
                if (data == null) {
                    return;
                } else {
                    Uri uri = data.getData();
                    System.out.println("the uri is " + uri);
                    imageUri = startImageZoom(uri);
                }
                break;
            case 1001:
                if (data == null) {
                    return;
                } else {
                    if (imageUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);
                        if (bitmap != null) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                            while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                                byteArrayOutputStream.reset();//重置baos即清空baos
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);//这里压缩options%，把压缩后的数据存放到baos中
                            }
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_img", imageString);
                            editor.apply();
                            upDataImg();
                            shouldUpdateImg = true;
                            try {
                                byteArrayOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldUpdateInfor){
            new Dialog(new Handler(Looper.getMainLooper()), context, "同步数据中", 1000);
            reloadInfor();
            upDataImg();
        }
       if(shouldUpdateImg){
           upDataImg();
       }


    }

    private void reloadInfor() {
        SharedPreferences infor = context.getSharedPreferences("user", 0);
        tv_name.setText(infor.getString("user_nickname", "游客"));
        tv_email.setText(infor.getString("account", ""));
    }


    /**
     * 通过Uri传递图像信息以供裁剪
     *
     * @param uri
     */
    private Uri startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        startActivityForResult(intent, 1001);
        return imageUri;
    }

    public Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    public void upDataImg() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        String imageString = sharedPreferences.getString("user_img", "");
        if (!imageString.equals("")) {
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            iv_photo.setImage(byteArrayInputStream);
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlet(final int type, String title) {
        boolean isTourist = UserInforUtil.isTourist();
        if (isTourist && type == 3) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivityForResult(intent, 100);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.edittext, null);
            final EditText editText = view.findViewById(R.id.ed_account);
            final EditText ed_newPassword = view.findViewById(R.id.ed_password);
            switch (type) {
                case 1:
                    editText.setHint("2～6位,可以是汉字、字母或者数字");
                    break;
                case 3:
                    view.findViewById(R.id.tv_ed_tips1).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_ed_tips2).setVisibility(View.VISIBLE);
                    ed_newPassword.setVisibility(View.VISIBLE);
                    editText.setHint("输入原密码");
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    ed_newPassword.setHint("输入新密码,6～16位,只能是字母或者数字");
                    break;

            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setView(view);
            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = editText.getText().toString();
                    if (text.equals("")) {
                        Toast.makeText(context, "不能什么都不写", Toast.LENGTH_LONG).show();
                    } else {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        switch (type) {
                            case 1:
                                String reg = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,6}";
                                Pattern pattern = Pattern.compile(reg);
                                Matcher matcher = pattern.matcher(text);
                                if (matcher.matches()) {
                                    tv_name.setText(text);
                                    editor.putString("user_nickname", text);
                                    editor.apply();
                                } else {
                                    Toast.makeText(context, "昵称格式不对", Toast.LENGTH_LONG).show();
                                }
                                break;

                            case 3:
                                String new_pwd = ed_newPassword.getText().toString();
                                String old_pwd = sharedPreferences.getString("password", "admin");
                                if (old_pwd.equals(text)) {
                                    if (new_pwd.length() <= 6) {
                                        Toast.makeText(context, "新密码（6～16位）只能是字母或者数字", Toast.LENGTH_LONG).show();
                                    } else {
                                        editor.putString("password", new_pwd);
                                        editor.apply();
                                        String data = 1003 + "==" + UserInforUtil.getUserInformation();
                                        ConnectIntentService.upoLoadMessage(context, data);
                                    }
                                } else {
                                    Toast.makeText(context, "旧密码错误", Toast.LENGTH_LONG).show();
                                }

                                break;
                        }
                    }

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();

        }
    }


    public boolean isShouldUpdateImg() {
        return shouldUpdateInfor || shouldUpdateImg;
    }
}
