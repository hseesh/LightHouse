package com.hse.yatoufang.PersonalCenter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hse.yatoufang.CustormView.RoundImageView;
import com.hse.yatoufang.MissionItemUtils.MissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemManger;
import com.hse.yatoufang.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;

public class missionDetail extends Fragment {
    private Context context;
    private String date;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        Bundle bundle = getArguments();
        date = (String) bundle.get("date");
        initData(view);
        return view;
    }


    private void initData(View view) {
        TextView user_name = view.findViewById(R.id.user_name);
        TextView user_level = view.findViewById(R.id.user_level);
        TextView user_diary = view.findViewById(R.id.tv_diary);
        TextView user_diaryCreatime = view.findViewById(R.id.tv_time);
        TextView user_grade = view.findViewById(R.id.tv_grade);

        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_name.setText(sharedPreferences.getString("user_nickname", "游客"));
        user_level.setText(sharedPreferences.getString("user_level", "Lv1"));
        RoundImageView user_imgs = view.findViewById(R.id.user_photo);
        String photo = sharedPreferences.getString("user_img", "");
        if (!photo.equals("")) {
            byte[] byteArray = Base64.decode(photo, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            user_imgs.setImage(byteArrayInputStream);
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!date.equals("")) {
            MissionItemManger missionItemManger1 = new MissionItemManger(context, 2);
            MissionItem missionItemState = missionItemManger1.getMissionState(date);
            if (missionItemState != null) {
                MissionItemManger missionItemManger2 = new MissionItemManger(context, 3);
                MissionItem missionItemDiary = (MissionItem) missionItemManger2.getThisDayDiary(date);
                if (missionItemDiary != null) {
                    user_diary.setText(missionItemDiary.getDiary());
                }
                user_diaryCreatime.setText(getWeekend(missionItemState.getMissionsSavetime()));
                String[] items = missionItemState.getMissionItems().split(",");
                String[] levels = missionItemState.getMissionLevels().split(",");
                String[] state = String.valueOf(missionItemState.getMissionState()).split("");
                LinearLayout finish = view.findViewById(R.id.ll_finish);
                LinearLayout unfinish = view.findViewById(R.id.ll_unfinish);
                int j = 0;
                for (int i = 0; i < items.length; i++) {
                    View item = LayoutInflater.from(context).inflate(R.layout.item_mission, null);

                    TextView mission_name = item.findViewById(R.id.tv_missionnaem);
                    RatingBar mission_level = item.findViewById(R.id.rb_item);
                    CheckBox mission_state = item.findViewById(R.id.cb_item);
                    mission_name.setText(items[i]);
                    mission_level.setRating(Float.parseFloat(levels[i]));
                    mission_level.setIsIndicator(true);
                    if (state[i + 1].equals("1")) {
                        mission_state.setChecked(true);
                        finish.addView(item);
                        j++;
                    } else {
                        unfinish.addView(item);
                    }
                }
                if (j < items.length) {
                    TextView tv_unfinish = view.findViewById(R.id.tv_unfinish);
                    tv_unfinish.setText("未完成计划");
                }

                String grade = "<font color = '#707070'>完成了</font>" + "<font color = '#0000ff'>" + j + "</font>" + "项计划，获得了"
                        + "<font color =  '#0000ff'>" + (100 * j / items.length) + "</font>" + "<font color = '#707070'>点经验</font>";

                user_grade.setText(Html.fromHtml(grade));

            }

        }


    }

    private String getWeekend(String data) {
        String[] date = this.date.split("-");
        int y = Integer.valueOf(date[0]);
        int m = Integer.valueOf(date[1]);
        int d = Integer.valueOf(date[2]);
//        int h = Integer.valueOf(date[3]);
//        int s = Integer.valueOf(date[4]);
//        y + "年" + m + "月" + d + "日   " + h + ":" + s + ":" +  date[5]

        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d);
        String[] weekend = new String[]{"周一", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        int count = calendar.get(Calendar.DAY_OF_WEEK);
        if (count > 1) {
            --count;
        } else if (count == 1) {
            count = 7;
        }
        return "存档于：  " +  data + "   " + weekend[count] ;

    }


}

