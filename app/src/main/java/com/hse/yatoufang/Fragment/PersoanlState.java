package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hse.yatoufang.R;

public class PersoanlState extends Fragment {
    private Context context;
    private TextView level;
    private TextView grade;
    private TextView days;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, null);
        level = view.findViewById(R.id.tv_level);
        grade = view.findViewById(R.id.tv_grade);
        days = view.findViewById(R.id.tv_alldays);

        initData();
        return view;
    }



    private void initData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_level = sharedPreferences.getString("user_level", "LV1");
        int user_grade = sharedPreferences.getInt("user_grade", 0);
        int user_days = sharedPreferences.getInt("user_days", 0);
        //int user_day = sharedPreferences.getInt("user_day",0);

        level.setText(user_level);
        grade.setText(user_grade + "/36500");
        days.setText(user_days + "");


    }


}
