package com.siang.pc.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.siang.pc.sleep.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by siang on 2018/10/6.
 */

public class WeekFragment extends Fragment {

    private BarChart mBarChart;
    protected String[] values = new String[]{
            "09/01", "09/02", "09/03", "09/04", "09/05", "09/06", "09/07"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.week_layout, container,false);
        mBarChart = v.findViewById(R.id.WeekBarChart);
        setBarChart();
        setDatas();
        return v;
    }

    public void setDatas() {
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        yVals.add(new BarEntry(0, 6f));
        yVals.add(new BarEntry(1, 8f));
        yVals.add(new BarEntry(2, 7.1f));
        yVals.add(new BarEntry(3, 8.3f));
        yVals.add(new BarEntry(4, 6.5f));
        yVals.add(new BarEntry(5, 4.9f));
        yVals.add(new BarEntry(6, 7f));

        HourFormatter hourformatter = new HourFormatter();
        BarDataSet set;
        set = new BarDataSet(yVals, "Time");
        //设置多彩 也可以单一颜色
        set.setColors(new int[]{Color.parseColor("#9ED874"), Color.parseColor("#61C9B5"), Color.parseColor("#61C9B5"), Color.parseColor("#1F7D6B"), Color.parseColor("#61C9B5"), Color.parseColor("#9ED874"), Color.parseColor("#1F7D6B")});
        set.setDrawValues(true);
        set.setValueTextColor(Color.parseColor("#FFFFFF"));
        set.setValueFormatter(hourformatter);
        set.setBarBorderWidth(0f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.5f);
        mBarChart.setData(data);
        mBarChart.setFitBars(true);
        mBarChart.invalidate();
    }

    public void setBarChart() {

        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(60);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);

        MyXFormatter Xformatter = new MyXFormatter(values);
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));
        xAxis.setTextSize(8f);
        xAxis.setValueFormatter(Xformatter);

        YAxis ya =mBarChart.getAxisLeft();
        ya.setAxisMinimum(0);
        ya.setSpaceTop(30f);

        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisLeft().setEnabled(false);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.animateY(2500);
        mBarChart.getLegend().setEnabled(false);
    }

    public class HourFormatter implements IValueFormatter {

        protected DecimalFormat mFormat;

        public HourFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        public HourFormatter(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " h";
        }
    }


    public class MyXFormatter  implements IAxisValueFormatter {

        private String[] mValues;

        public MyXFormatter(String[] values) {
            this.mValues = values;
        }
        private static final String TAG = "MyXFormatter";

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value % mValues.length];
        }
    }

}

