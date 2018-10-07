package com.siang.pc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siang.pc.sleep.R;

/**
 * Created by siang on 2018/10/6.
 */

public class SleepTipsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.sleep_tips_layout, container,false);
        return v;
    }

}

