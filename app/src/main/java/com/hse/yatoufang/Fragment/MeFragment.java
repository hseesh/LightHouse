package com.hse.yatoufang.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hse.yatoufang.R;

/**
 * Created by Administrator on 2018-07-16.
 */

public class MeFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.statistics, null);
        View view = inflater.inflate(R.layout.fragment_me, null);
        return view;
    }



}




