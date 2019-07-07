package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.StatisticsMonth;
import com.hse.yatoufang.CustormView.StatisticsWeek;
import com.hse.yatoufang.MissionItemUtils.MissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;
import com.hse.yatoufang.R;

import java.util.List;

public class CountingMonthFragment extends Fragment {
    private Context context;
    private StatisticsMonth statisticsMonth;
    private StatisticsWeek statisticsWeek;
    private MissionItemStatistics missionItemStatistics;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counting_month,null);
        statisticsWeek = view.findViewById(R.id.statistics_week);
        statisticsMonth = view.findViewById(R.id.statistics_month);
        return view;
    }

    public void updataView(String date) {
        if(missionItemStatistics == null){
            missionItemStatistics = new MissionItemStatistics(context);
        }
        List<MissionItem> list = missionItemStatistics.getMissionCount(date);
        if (list != null) {
            statisticsMonth.update(list, date);
            statisticsWeek.update(missionItemStatistics.getState());
            missionItemStatistics.clearData();
        } else {
            Toast.makeText(context, "查询日期无数据", Toast.LENGTH_LONG).show();
        }
    }

}
