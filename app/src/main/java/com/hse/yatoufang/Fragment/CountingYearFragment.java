package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hse.yatoufang.CustormView.StatisticsGraphYear;
import com.hse.yatoufang.CustormView.StatisticsYear;
import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;
import com.hse.yatoufang.R;


public class CountingYearFragment extends Fragment {
    private Context context;
    private StatisticsGraphYear statisticsGraphYear;
    private StatisticsYear statisticsYear;
    private MissionItemStatistics missionItemStatistics;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counting_year,null);
        statisticsYear = view.findViewById(R.id.StatisticsYear);
        statisticsGraphYear = view.findViewById(R.id.StatisticsGraphYear);
        missionItemStatistics = new MissionItemStatistics(context);
        return view;
    }



    public void updataView(String date) {
        double[] grade = missionItemStatistics.getMisssionCountOfYear(date);

        if (grade != null) {
            boolean isEmpty = false;
            for (int i = 0; i < grade.length; i++) {
                if(grade[i] == 0d){
                    isEmpty = true;
                }else{
                    isEmpty = false;
                    break;
                }
            }
            if(!isEmpty){
                statisticsGraphYear.updateView(grade);
                statisticsYear.updateView(missionItemStatistics.getState());
                missionItemStatistics.clearData();
            }else{
                Toast.makeText(getActivity(), "查询日期无数据", Toast.LENGTH_LONG).show();
            }
        }
    }
}
