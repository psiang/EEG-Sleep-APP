package com.siang.pc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.siang.pc.sleep.R;
import com.siang.pc.view.EcgView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by siang on 2018/10/6.
 */

public class DayFragment extends Fragment {
    Timer timer = new Timer();

    private PieChart picChart;

    private List<Integer> datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();

    private int flag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.day_layout, container,false);
        setPieMap(v);
        loadDatas();
        simulator();
        return v;
    }

    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    /**
     * 模拟心电发送，心电数据是一秒500个包，所以
     */
    private void simulator(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(EcgView.isRunning){
                    if(data0Q.size() > 0){
                        EcgView.addEcgData0(data0Q.poll());
                    }
                }
            }
        }, 0, 2);
    }

    private void loadDatas(){
        try{
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for(String str : data0s){
                datas.add(Integer.parseInt(str));
            }

            data0Q.addAll(datas);
        }catch (Exception e){}

    }

    protected void setPieMap(View v) {
        picChart = v.findViewById(R.id.pic_chart);

        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(20f,"stage1"));
        strings.add(new PieEntry(20f,"stage2"));
        strings.add(new PieEntry(20f,"stage3"));
        strings.add(new PieEntry(20f,"stage4"));
        strings.add(new PieEntry(20f,"REM"));

        PieDataSet dataSet = new PieDataSet(strings,"Label");


        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.stage1));
        colors.add(getResources().getColor(R.color.stage2));
        colors.add(getResources().getColor(R.color.stage3));
        colors.add(getResources().getColor(R.color.stage4));
        colors.add(getResources().getColor(R.color.REM));
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);

        picChart.setData(pieData);
        picChart.invalidate();

        Description description = new Description();
        description.setText("");
        picChart.setDescription(description);
        picChart.setEntryLabelColor(Color.parseColor("#000000"));
        picChart.getLegend().setEnabled(false);

        picChart.setCenterText("Good");
        picChart.setCenterTextSize(30f);
        picChart.setCenterTextColor(Color.parseColor("#66BAB7"));
    }
}

