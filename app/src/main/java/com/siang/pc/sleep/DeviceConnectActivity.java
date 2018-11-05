package com.siang.pc.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeviceConnectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_maclist);
        Transition explode= TransitionInflater.from(this).inflateTransition(R.transition.explode);
        //第一次进入时使用
        getWindow().setEnterTransition(explode);
        //再次进入时使用
        getWindow().setReenterTransition(explode);
        //退出时使用
        getWindow().setExitTransition(explode);


        //要显示的数据
        String[] strs = getIntent().getExtras().getStringArray("macList");
        //创建ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_expandable_list_item_1,strs);
        //获取ListView对象，通过调用setAdapter方法为ListView设置Adapter设置适配器
        ListView list_test = (ListView) findViewById(R.id.listView);
        list_test.setAdapter(adapter);

//        list_test.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch(position){
//                    case 0:
//                        Intent intentSuccess=new Intent(Activity_List.this,Activity_ConnectSuccess.class);
//                        startActivity(intentSuccess);
//                        break;
//                    case 1:
//                        Intent intentFailed=new Intent(Activity_List.this,Activity_ConnectFailed.class);
//                        startActivity(intentFailed);
//                        break;
//                }
//
//            }
//        });
    }
}
