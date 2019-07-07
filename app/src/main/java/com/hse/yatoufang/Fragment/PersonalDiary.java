package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hse.yatoufang.PersonalCenter.DiaryAdapter;
import com.hse.yatoufang.PersonalCenter.missionDetail;
import com.hse.yatoufang.R;

import java.util.Calendar;

public class PersonalDiary extends Fragment implements View.OnClickListener {
    private Context context;
    private TextView tv_date;
    private int year;
    private int month;
    private DiaryAdapter diaryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycleview);
        tv_date = view.findViewById(R.id.tv_date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        diaryAdapter = new DiaryAdapter(context);
        diaryAdapter.setDiaryClickListener(new DiaryAdapter.diaryClickListener() {
            @Override
            public void diaryClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("date", view.getTag().toString());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                missionDetail missionDetails = new missionDetail();
                missionDetails.setArguments(bundle);
                fragmentTransaction.replace(R.id.ll_content, missionDetails);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(diaryAdapter);
        view.findViewById(R.id.iv_last).setOnClickListener(this);
        view.findViewById(R.id.iv_next).setOnClickListener(this);
        view.findViewById(R.id.tv_diary_day).setOnClickListener(this);
        //view.findViewById(R.id.tv_diary_love).setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        String date = year + "年" + month + "月";
        tv_date.setText(date);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_last:
                setDate(false);
                break;
            case R.id.iv_next:
                setDate(true);
                break;
//            case R.id.tv_diary_day:
//                break;
//            case R.id.tv_diary_love:
//                break;

        }
    }

    private void setDate(boolean isUp) {
        if (isUp) {
            if (month < 12 && month > 0) {
                month++;
            } else {
                month = 1;
                ++year;
            }
        } else {
            if (month <= 12 && month > 1) {
                month--;
            } else {
                month = 12;
                --year;
            }
        }
        String date = year + "年" + month + "月";
        tv_date.setText(date);
        String m = year + "-" + month;
        if (month < 10) {
            m = year + "-" + 0 + month;
        }
        diaryAdapter.upData(m);
    }
}
