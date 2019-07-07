package com.hse.yatoufang.PersonalCenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hse.yatoufang.R;
import com.hse.yatoufang.appUtils.FileInfor;
import com.hse.yatoufang.appUtils.MyApplication;


import java.util.ArrayList;


public class FileInforAdadpet extends BaseAdapter {

    private ArrayList<FileInfor> list;
    private LayoutInflater layoutInflater;

    public FileInforAdadpet(Context context) {
        layoutInflater = LayoutInflater.from(context);
        list = new ArrayList<>(4);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_file_infor, null);
        TextView name = view.findViewById(R.id.tv_file_name);
        TextView infor = view.findViewById(R.id.tv_save_time);
        TextView count = view.findViewById(R.id.tv_item_count);
        if(list.get(position) != null){
            name.setText(list.get(position).getName());
            count.setText(list.get(position).getCount());
            infor.setText(list.get(position).getDescribe());
        }

        return view;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.size();
    }

    public void setList(ArrayList<FileInfor> list) {
        System.out.println("the size is " + list.size());
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

//    private void initData(){
//
//        String[] name = new String[]{"","任务", "日记", "统计表"};
//        for (int i = 0; i < 4; i++) {
//            list.add(new FileInfor(name[i],"",""));
//        }
//    }



}
