package com.hse.yatoufang.Fragment;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.MyDialog;
import com.hse.yatoufang.MissionItemUtils.MissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemManger;
import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;
import com.hse.yatoufang.R;
import com.hse.yatoufang.appSocket.ConnectIntentService;
import com.hse.yatoufang.appUtils.Dialog;
import com.hse.yatoufang.appUtils.MessageCallback;
import com.hse.yatoufang.appUtils.MyApplication;
import com.hse.yatoufang.appUtils.UserInforUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2018-12-16.
 */

public class MissionFragment extends Fragment implements View.OnClickListener {

    private LinearLayout linearLayout;
    private List<MissionItem> missionItems;
    private MissionItemManger missionItemManger;
    private Context context;
    private LayoutInflater layoutInflate;
    private ImageView button;
    private EditText ed_diary;
    private ScrollView scrollView;
    private boolean[] missionState = new boolean[25];
    private boolean isSave = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mission, null);
        missionItemManger = new MissionItemManger(context, 1);
        this.layoutInflate = inflater;
        this.linearLayout = view.findViewById(R.id.ll_item);
        button = view.findViewById(R.id.iv_tip);
        ed_diary = view.findViewById(R.id.ed_diary);
        scrollView = view.findViewById(R.id.scrollView_mission);
        button.setOnClickListener(this);
        button.setTag(0);
        initView();

        for (int i = 0; i < 25; i++) {
            missionState[i] = false;
        }

        startAnimation(view);
        SharedPreferences preferences = context.getSharedPreferences("user", 0);
        isSave = preferences.getBoolean("isSaved", false);

        return view;
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        //  System.out.println(index + "  " + missionState[index]);
        switch (v.getId()) {
            case R.id.tv_delete:
                missionItemManger.deleteMission(missionItems.get(index));
                linearLayout.removeViewAt(index);
                initView();
                break;
            case R.id.tv_modification:
                showAlert(true, index);
                break;
            case R.id.cb_item:
                missionState[index] = !missionState[index];
                for (int i = 0; i < missionItems.size(); i++) {
                    if (missionState[i]) {
                        button.setImageResource(R.drawable.save);
                        break;
                    } else if (i == missionItems.size() - 1) {
                        button.setImageResource(R.drawable.ic_add);
                    }
                }
                break;
            case R.id.iv_tip:
                if (!missionState[index]) {
                    showAlert(false, 0);
                } else {
                    storeDiary();
                    storeMissionSate();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * 保存日记照片
     *
     * @param type
     * @return
     */
    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000) {
//            if (data != null) {
//
//                Uri uri = data.getData();
//
//                if (uri != null) {
//                    InputStream is = null;
//                    try {
//                        is = context.getContentResolver().openInputStream(uri);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    //InputStream ----> Bitmap
//
//                    if (is != null) {
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//
//                        if (bitmap != null) {
//                            //clearData();
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//                            while (byteArrayOutputStream.toByteArray().length / 1024 > 4000) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                                byteArrayOutputStream.reset();//重置baos即清空baos
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);//这里压缩options%，把压缩后的数据存放到baos中
//                            }
//                            MissionItemManger user_img = new MissionItemManger(context, 4);
//                            if (user_img.photoIsSaved(getDate(1))) {
//                                user_img.updateMyPhoto(getDate(1), byteArrayOutputStream.toByteArray());
//                                Toast.makeText(context, "今日照片已更新", Toast.LENGTH_LONG).show();
//                            } else {
//                                user_img.storeMyPhoto(getDate(1), byteArrayOutputStream.toByteArray());
//
//                                Toast.makeText(context, "照片保存成功", Toast.LENGTH_LONG).show();
//                            }
//                            //user_photo.setImageURI(imageUri);
//                            user_photo.setImageBitmap(bitmap);
//                            try {
//                                byteArrayOutputStream.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    }
    private String getDate(int type) {
        //0 月份 ， 1 天数 ， 2 秒
        String[] pattern = {"yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern[type]);
        return simpleDateFormat.format(calendar.getTime());
    }


    private void storeDiary() {
        String date = getDate(0);
        String time = getDate(1);
        String diary = ed_diary.getText().toString();
        if (diary.equals("")) {
            diary = "nothing";
        }
        MissionItem missionItem = new MissionItem(date, time, diary);
        MissionItemManger missionItemManger1 = new MissionItemManger(context, 3);
        if (isSave) {
            missionItemManger1.updateDiary(missionItem);
        } else {
            missionItemManger1.storeDiary(missionItem);
        }
    }


    private void storeMissionSate() {
        StringBuilder mission_state = new StringBuilder();
        StringBuilder items = new StringBuilder();
        StringBuilder levels = new StringBuilder();
        int count = 0;
        for (int i = 0; i < missionItems.size(); i++) {
            if (missionState[i]) {
                count++;
                mission_state.append(1);
            } else {
                mission_state.append(0);
            }

            items.append(missionItems.get(i).getMissionItemName() + ",");
            levels.append(missionItems.get(i).getMissionItemLevel() + ",");
        }

        String date = getDate(1);
        String time = getDate(2);
        MissionItem missionState = new MissionItem(date, time, items.toString(), levels.toString(), mission_state.toString());
        MissionItemManger missionStateManger = new MissionItemManger(context, 2);
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", 0);
        isSave = sharedPreferences.getBoolean("isSaved", false);
        boolean tourist = UserInforUtil.isTourist();
        if (isSave) {
            SharedPreferences.Editor editor_modification = sharedPreferences.edit();
            missionStateManger.updateMissionState(missionState);
            int user_grade = sharedPreferences.getInt("user_grade", 0);
            if (user_grade != 0) {
                user_grade -= sharedPreferences.getInt("last_grade", 0) + 100 * count / missionItems.size();
                user_grade = Math.abs(user_grade);
            } else {
                user_grade = 100 * count / missionItems.size();
            }
            editor_modification.putInt("user_grade", user_grade);
            editor_modification.putInt("last_grade", 100 * count / missionItems.size());
            String user_level = "LV" + (1 + user_grade / 365);
            editor_modification.putString("user_level", user_level);
            editor_modification.putString("grade_weeks", user_level);
            editor_modification.apply();
            Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
            System.out.println("tourist = " + tourist);
            if (!tourist) {
                ConnectIntentService.upoLoadMessage(context, 1003 + "==" + UserInforUtil.getUserInformation());
            }
        } else {
            missionStateManger.storeMissionState(missionState);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isSaved", true);
            int user_grade = sharedPreferences.getInt("user_grade", 0);
            user_grade += 100 * count / missionItems.size();
            //editor.putString("isFirst", dateFormat.format(calendar.getTime()));
            editor.putInt("user_grade", user_grade);
            editor.putInt("last_grade", 100 * count / missionItems.size());
            String user_level = "LV" + (1 + user_grade / 365);
            editor.putString("user_level", user_level);
            int user_days = sharedPreferences.getInt("user_days", 0) + 1;
            editor.putInt("user_days", user_days);
            editor.apply();
            isSave = true;
            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
            if (!tourist) {
                ConnectIntentService.upoLoadMessage(context, 1003 + "==" + UserInforUtil.getUserInformation());
            }
        }
    }

    private void initView() {

        linearLayout.removeAllViews();
        missionItems = missionItemManger.getAllMission();

        for (int i = 0; i < missionItems.size(); i++) {
            View mission_items = layoutInflate.inflate(R.layout.swipelayout, null);
            TextView missionName = mission_items.findViewById(R.id.tv_missionnaem);
            TextView delete = mission_items.findViewById(R.id.tv_delete);
            TextView modification = mission_items.findViewById(R.id.tv_modification);
            RatingBar ratingBar = mission_items.findViewById(R.id.rb_item);
            CheckBox checkBox = mission_items.findViewById(R.id.cb_item);
            MissionItem mission_item = missionItems.get(i);
            int levle = mission_item.getMissionItemLevel();
            missionName.setText(mission_item.getMissionItemName());

            ratingBar.setRating(levle);
            //ratingBar.setNumStars(levle);
            delete.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            modification.setOnClickListener(this);
            delete.setTag(i);
            checkBox.setTag(i);
            modification.setTag(i);

            linearLayout.addView(mission_items);
        }
    }


    private void showAlert(final boolean isUpdate, final int index) {
        if (missionItems.size() < 25) {
            View alert = layoutInflate.inflate(R.layout.alert_item, null);
            final EditText missionname = alert.findViewById(R.id.ed_add);
            final RatingBar ratingBar = alert.findViewById(R.id.ratingbar);

            MyDialog.Builder builder = new MyDialog.Builder(getActivity());
            builder.setTitle("给自己添加一个新任务");
            builder.setView(alert);

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Toast.makeText(getActivity(), "well done no", Toast.LENGTH_LONG).show();
                    hideInput();
                }
            });

            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (missionname.getText().toString().equals("")) {
                        Toast.makeText(context, "任务名称不能为空", Toast.LENGTH_LONG).show();
                    } else {
                        String mission_name = missionname.getText().toString();
                        int mission_level = (int) ratingBar.getRating();
                        if (isUpdate) {
                            MissionItem mission = missionItems.get(index);
                            mission.setMissionItemLevel(mission_level);
                            mission.setMissionItemName(mission_name);
                            missionItemManger.updateMission(mission);
                        } else {
                            missionItemManger.addMission(new MissionItem(mission_level, getDate(2), mission_name));
                        }
                        initView();
                        hideInput();
                    }
                }
            });

            MyDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);      //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
            dialog.show();

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                }
            });
        }

    }

    /**
     * 隐藏输入法
     */
    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public void startAnimation(final View view) {
        final ImageView iv_tip = view.findViewById(R.id.iv_tip);
        final ImageView iv_line = view.findViewById(R.id.line2);
        final TextView tv_tip = view.findViewById(R.id.tv_tip);
       // SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
       // final boolean isToday = preferences.getBoolean("isToday", false);

        // better ways is replace  ObjectAnimator with AnimationSet

        iv_line.setVisibility(View.VISIBLE);
        final AnimatorSet as = new AnimatorSet();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ObjectAnimator oa1 = ObjectAnimator.ofFloat(iv_tip, "rotation", 360, -360);

                ObjectAnimator oa2 = ObjectAnimator.ofFloat(iv_tip, "translationX", -260);
                ObjectAnimator oa3 = ObjectAnimator.ofFloat(iv_line, "scaleX", 20f);

                oa1.setDuration(300);
                oa1.setRepeatCount(1);
                oa1.start();//先旋转

                oa2.setDuration(300);
                oa3.setDuration(300);

                as.playTogether(oa2, oa3);
                as.start();

                if (isSave) {
                    tv_tip.setText("今日任务已存档！");
                } else {
                    tv_tip.setText("记录一下美好的一天吧！");
                }
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_tip.setText("");
                ObjectAnimator oa5 = ObjectAnimator.ofFloat(iv_tip, "rotation", 360, -360);

                ObjectAnimator oa6 = ObjectAnimator.ofFloat(iv_tip, "translationX", 70);
                ObjectAnimator oa7 = ObjectAnimator.ofFloat(iv_line, "scaleX", 0.01f);

                oa5.setDuration(300);
                oa5.setRepeatCount(1);
                oa5.start();//先旋转

                oa6.setDuration(300);
                oa7.setDuration(300);

                as.playTogether(oa6, oa7);
                as.start();
            }
        }, 1800);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestPermission();
            }
        }, 2000);

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionRequest();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            //用户不同意，向用户展示该权限作用
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionRequest();
            }
        }
    }

    private void showPermissionRequest() {
        new AlertDialog.Builder(context)
                .setTitle("运行权限")
                .setMessage("LightHouse2 需要存储权限才能正常运行")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .create()
                .show();

    }


}
//shouldShowRequestPermissionRationale方法返回值分几种情况，怎么使用看应用的具体交互需求。
//
//第一次请求该权限，返回false。
//请求过该权限并被用户拒绝，返回true。
//请求过该权限，但用户拒绝的时候勾选不再提醒，返回false。


