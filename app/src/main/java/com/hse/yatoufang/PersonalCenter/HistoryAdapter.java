package com.hse.yatoufang.PersonalCenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hse.yatoufang.MissionItemUtils.HistoryMissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemStatistics;
import com.hse.yatoufang.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private List<HistoryMissionItem> list;


    public HistoryAdapter(Context context){
        this.context = context;
        initData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history,null);
        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getMissionName());
        holder.tv_time.setText(list.get(position).getMissionDate());
        holder.tv_count.setText(list.get(position).getMissionCount());
        holder.level.setRating(list.get(position).getMissionLevel());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void initData(){
        list = new ArrayList<>();
        MissionItemStatistics missionItemStatistics = new MissionItemStatistics(context);
        list = missionItemStatistics.getAllCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_time;
        TextView tv_count;
        RatingBar level;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_missionnaem);
            tv_time = itemView.findViewById(R.id.tv_build_time);
            tv_count = itemView.findViewById(R.id.tv_finish_count);
            level = itemView.findViewById(R.id.rb_item);
        }
    }

}
