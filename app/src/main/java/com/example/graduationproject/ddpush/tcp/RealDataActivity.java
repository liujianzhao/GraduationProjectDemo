package com.example.graduationproject.ddpush.tcp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.graduationproject.R;
import com.example.graduationproject.ddpush.tcp.service.OnlineService;
import com.example.graduationproject.entity.Sensor;
import com.example.graduationproject.view.RealDataLineChartView;
import com.example.graduationproject.view.SensorBarChartView;

import java.util.LinkedList;
import java.util.List;

public class RealDataActivity extends AppCompatActivity {

    private static final int GET_PUSHVALUE_COMPLETE = 0;

    private OnlineService.PushBinder pushBinder;
    private OnlineService onlineService;

    private RealDataLineChartView lineChart_realdata;
    private List<Integer> values = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);

        initView();
        saveParamsAndConnect();
    }


    private void initView() {
        lineChart_realdata = (RealDataLineChartView)findViewById(R.id.linechart_realdata);
        lineChart_realdata.setTitle("实时数据折线图");
        lineChart_realdata.setDataType(getIntent().getStringExtra("title").substring(0,2));
    }

    private void saveParamsAndConnect() {
        saveAccountInfo();
        Intent startSrv = new Intent(this, OnlineService.class);
        startSrv.putExtra("CMD", "RESET");
        this.startService(startSrv);
        this.bindService(startSrv, connection, BIND_AUTO_CREATE);
    }

    protected void saveAccountInfo(){
        SharedPreferences account = this.getSharedPreferences(Params.DEFAULT_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.putString(Params.SERVER_IP, Params.SERVER_IP_DATA);
        editor.putString(Params.SERVER_PORT, Params.SERVER_PORT_DATA);
        editor.putString(Params.PUSH_PORT,Params.PUSH_PORT_DATA);
        if(getIntent().getStringExtra("title").equals("温度实时查看")){
            editor.putString(Params.USER_NAME, Params.USER_NAME_DATA01);
        }else{
            editor.putString(Params.USER_NAME, Params.USER_NAME_DATA02);
        }
        editor.putString(Params.SENT_PKGS, Params.SENT_PKGS_DATA);
        editor.putString(Params.RECEIVE_PKGS, Params.RECEIVE_PKGS_DATA);
        editor.commit();
    }

    public void ChartChange(int value){
        Message msg = new Message();
        msg.what = GET_PUSHVALUE_COMPLETE;
        msg.arg1 = value;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int key = msg.what;
            switch (key){
                case GET_PUSHVALUE_COMPLETE:
                    lineChart_realdata.addRealData(msg.arg1);
                    break;
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            pushBinder = (OnlineService.PushBinder) service;
            onlineService = pushBinder.getService();
            /**
             * 实现回调，得到实时刷新的数据
             */
            onlineService.setCallback(new OnlineService.Callback() {
                @Override
                public void getNum(int num) {
                    ChartChange(num);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Intent stopSrv = new Intent(this, OnlineService.class);
        this.unbindService(connection);
        this.stopService(stopSrv);
    }

}
