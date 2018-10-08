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
import com.siang.pc.fragment.MonthFragment;
import com.siang.pc.fragment.SleepTipsFragment;
import com.siang.pc.fragment.WeekFragment;
import com.siang.pc.fragment.YourSleepFragment;
import com.siang.pc.view.EcgView;

public class MainActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private PieChart picChart;

    private List<Integer> datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();

    private int flag = 0;

    private ViewPager myviewpager1;
    private ViewPager myviewpager2;
    //选项卡中的按钮
    private Button btn_day;
    private Button btn_week;
    private Button btn_month;
    private Button btn_your_sleep;
    private Button btn_sleep_tips;
    //作为指示标签的按钮
    private Button cursor;
    private Button cursor2;
    //所有标题按钮的数组
    private Button[] btnArgs;
    private Button[] btnArgs2;
    //fragment的集合，对应每个子页面
    private ArrayList<Fragment> fragments;
    private ArrayList<Fragment> fragments2;

    //通过include其他界面layout实现界面切换
    private View include1;
    private View include2;
    private View include3;
    private ImageButton imgButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //private ImageButton imgButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setButton();
        initView();
        buildFragmentAdapter();
        myviewpager1.addOnPageChangeListener(this);
        myviewpager2.addOnPageChangeListener(this);
    }

    public void findView() {//唤起menu边栏的view
        include1 = (View) findViewById(R.id.include_data);
        include2 = (View) findViewById(R.id.include_analysis);
        include3 = (View) findViewById(R.id.include_connect);
        imgButton = (ImageButton) findViewById(R.id.top_account);//边栏唤起的icon
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
                        include1.setVisibility(View.VISIBLE);
                        include2.setVisibility(View.GONE);
                        include3.setVisibility(View.VISIBLE);
                        myviewpager1.setVisibility(View.VISIBLE);
                        myviewpager2.setVisibility(View.GONE);
                        break;
                    case R.id.nav_analysis_advices:
                        include1.setVisibility(View.GONE);
                        include2.setVisibility(View.VISIBLE);
                        include3.setVisibility(View.GONE);
                        myviewpager1.setVisibility(View.GONE);
                        myviewpager2.setVisibility(View.VISIBLE);
                        /*Intent intent = new Intent(MainActivity.this, AnalysisActivity.class);
                        startActivity(intent);*/
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
        //data collection界面
        fragments = new ArrayList<Fragment>();
        fragments.add(new DayFragment());
        fragments.add(new WeekFragment());
        fragments.add(new MonthFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        myviewpager1.setAdapter(adapter);
        myviewpager1.setOffscreenPageLimit(2);//缓存页面数目
        //analysis and advice界面
        fragments2 = new ArrayList<Fragment>();
        fragments2.add(new YourSleepFragment());
        fragments2.add(new SleepTipsFragment());
        MyFragmentPagerAdapter adapter2 = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments2);
        myviewpager2.setAdapter(adapter2);
        myviewpager2.setOffscreenPageLimit(2);
    }

    //初始化布局
    public void initView(){
        myviewpager1 = (ViewPager)this.findViewById(R.id.myviewpager_data);

        btn_day = (Button)this.findViewById(R.id.btn_day);
        btn_week = (Button)this.findViewById(R.id.btn_week);
        btn_month = (Button)this.findViewById(R.id.btn_month);
        //button字体样式设置
        btn_day.setTypeface(null, Typeface.NORMAL);
        btn_week.setTypeface(null, Typeface.NORMAL);
        btn_month.setTypeface(null, Typeface.NORMAL);
        //初始化按钮数组
        btnArgs = new Button[]{btn_day,btn_week,btn_month};
        //指示标签设置为加粗
        cursor = btn_day;
        cursor.setTypeface(null, Typeface.BOLD);

        btn_day.setOnClickListener(this);
        btn_week.setOnClickListener(this);
        btn_month.setOnClickListener(this);

        myviewpager2 = (ViewPager)this.findViewById(R.id.myviewpager_analysis);

        btn_your_sleep = (Button)this.findViewById(R.id.btn_your_sleep);
        btn_sleep_tips = (Button)this.findViewById(R.id.btn_sleep_tips);

        btn_your_sleep.setTypeface(null, Typeface.NORMAL);
        btn_sleep_tips.setTypeface(null, Typeface.NORMAL);
        //初始化按钮数组
        btnArgs2 = new Button[]{btn_your_sleep,btn_sleep_tips};
        //指示标签设置为加粗
        cursor2 = btn_your_sleep;
        cursor2.setTypeface(null, Typeface.BOLD);

        btn_your_sleep.setOnClickListener(this);
        btn_sleep_tips.setOnClickListener(this);

    }
    @Override
    public void onClick(View whichbtn) {
        // TODO Auto-generated method stub

        switch (whichbtn.getId()) {
            case R.id.btn_day:
                myviewpager1.setCurrentItem(0);
                break;
            case R.id.btn_week:
                myviewpager1.setCurrentItem(1);
                break;
            case R.id.btn_month:
                myviewpager1.setCurrentItem(2);
                break;
            case R.id.btn_your_sleep:
                myviewpager2.setCurrentItem(0);
                break;
            case R.id.btn_sleep_tips:
                myviewpager2.setCurrentItem(1);
                break;
            case R.id.top_account:
                drawerLayout.openDrawer(GravityCompat.START);
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
        if (myviewpager1.getVisibility() == View.VISIBLE) {
            //还原加粗
            cursor.setTypeface(null, Typeface.NORMAL);
            //设置加粗
            cursor = btnArgs[arg0];
            cursor.setTypeface(null, Typeface.BOLD);
        }
        else {
            //还原加粗
            cursor2.setTypeface(null, Typeface.NORMAL);
            //设置加粗
            cursor2 = btnArgs2[arg0];
            cursor2.setTypeface(null, Typeface.BOLD);
        }
    }


}
