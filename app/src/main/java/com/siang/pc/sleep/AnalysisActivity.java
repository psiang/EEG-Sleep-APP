package com.siang.pc.sleep;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import com.siang.pc.adapter.MyFragmentPagerAdapter;
import com.siang.pc.fragment.DayFragment;
import com.siang.pc.fragment.SleepTipsFragment;
import com.siang.pc.fragment.WeekFragment;
import com.siang.pc.fragment.YourSleepFragment;
import com.siang.pc.view.EcgView;

public class AnalysisActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private PieChart picChart;

    private List<Integer> datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();

    private int flag = 0;

    private ViewPager myviewpager;
    //选项卡中的按钮
    private Button btn_your_sleep;
    private Button btn_sleep_tips;
    //作为指示标签的按钮
    private Button cursor;
    //所有标题按钮的数组
    private Button[] btnArgs;
    //fragment的集合，对应每个子页面
    private ArrayList<Fragment> fragments;

    private ImageButton imgButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //private ImageButton imgButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        findView();
        setButton();
        initView();
        buildFragmentAdapter();
        myviewpager.addOnPageChangeListener(this);
    }

    public void findView() {
        imgButton = (ImageButton) findViewById(R.id.top_account);
        //imgButton2 = (ImageButton) findViewById(R.id.imgButtonBack);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setDrawerLeftEdgeSize(this, drawerLayout, 0.2f);
    }

    public void setButton() {
        imgButton.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_data_collection:
                        Intent intent = new Intent(AnalysisActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_analysis_advices:
                        break;
                }
                return true;
            }
        });
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }


    public void buildFragmentAdapter() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new YourSleepFragment());
        fragments.add(new SleepTipsFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        myviewpager.setAdapter(adapter);
        myviewpager.setOffscreenPageLimit(2);
    }

    //初始化布局
    public void initView(){
        myviewpager = (ViewPager)this.findViewById(R.id.myviewpager);

        btn_your_sleep = (Button)this.findViewById(R.id.btn_your_sleep);
        btn_sleep_tips = (Button)this.findViewById(R.id.btn_sleep_tips);

        btn_your_sleep.setTypeface(null, Typeface.NORMAL);
        btn_sleep_tips.setTypeface(null, Typeface.NORMAL);
        //初始化按钮数组
        btnArgs = new Button[]{btn_your_sleep,btn_sleep_tips};
        //指示标签设置为加粗
        cursor = btn_your_sleep;
        cursor.setTypeface(null, Typeface.BOLD);

        btn_your_sleep.setOnClickListener(this);
        btn_sleep_tips.setOnClickListener(this);

    }
    @Override
    public void onClick(View whichbtn) {
        // TODO Auto-generated method stub

        switch (whichbtn.getId()) {
            case R.id.btn_your_sleep:
                myviewpager.setCurrentItem(0);
                break;
            case R.id.btn_sleep_tips:
                myviewpager.setCurrentItem(1);
                break;
            case R.id.top_account:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            /*case R.id.imgButtonBack:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;*/
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        //还原加粗
        cursor.setTypeface(null, Typeface.NORMAL);
        //设置加粗
        cursor = btnArgs[arg0];
        cursor.setTypeface(null, Typeface.BOLD);
    }


}
