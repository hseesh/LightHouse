package com.hse.yatoufang.PersonalCenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hse.yatoufang.MissionItemUtils.MissionItem;
import com.hse.yatoufang.MissionItemUtils.MissionItemManger;
import com.hse.yatoufang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder>{

    private diaryClickListener diaryClickListener;
    private List<MissionItem> diaryItems = new ArrayList<>();
    private Context context;
    private MissionItemManger missionItemManger;


    public DiaryAdapter(Context context){
        this.context = context;
        initData();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String text = diaryItems.get(position).getDiaryCraettime();
        holder.date.setText(text);
        holder.head.setText(getWeekend(text,true));
        holder.weekend.setText(getWeekend(text,false));
        holder.content.setText(diaryItems.get(position).getDiary());
        holder.layout.setTag(text);
    }

    @Override
    public int getItemCount() {
        return diaryItems.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary,null);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView content,date,head,weekend;
        LinearLayout layout;

        private MyViewHolder(View itemView) {
            super(itemView);
            weekend = itemView.findViewById(R.id.tv_weekend);
            content = itemView.findViewById(R.id.tv_content);
            date = itemView.findViewById(R.id.tv_date);
            head = itemView.findViewById(R.id.tv_head);
            layout = itemView.findViewById(R.id.ll_diary);

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            diaryClickListener.diaryClick(v);
        }
    }

    public interface diaryClickListener{
        void diaryClick(View view);
    }

    public void setDiaryClickListener(diaryClickListener listener){
        this.diaryClickListener = listener;
    }

    private void initData(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        missionItemManger = new MissionItemManger(context,3);
        diaryItems = missionItemManger.getThisMonthDiary(dateFormat.format(calendar.getTime()));

    }

    public void upData(String date){
        System.out.println(date);
        if(missionItemManger.isExistOfDiary(date)){
            diaryItems = missionItemManger.getThisMonthDiary(date);
            this.notifyDataSetChanged();
        }
    }

    private String getWeekend(String data,boolean isDate){
        String[] date = data.split("-");
        int y = Integer.valueOf(date[0]);
        int m = Integer.valueOf(date[1]);
        int d = Integer.valueOf(date[2]);
       if(isDate){
           return m + "月" + d + "日";
       }else{
           Calendar calendar = Calendar.getInstance();
           calendar.set(y,m,d);
           String[] weekend = new String[]{"周一","周一","周二","周三","周四","周五","周六","周日"};
           int count = calendar.get(Calendar.DAY_OF_WEEK);
           if(count > 1){
               -- count;
           }else if(count == 1){
               count = 7;
           }
           return weekend[count];
       }
    }



}
