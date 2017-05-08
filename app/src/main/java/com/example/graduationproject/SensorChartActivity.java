package com.example.graduationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.view.SensorBarChartView;

public class SensorChartActivity extends AppCompatActivity {

//  https://github.com/xcltapestry/XCL-Charts
//	http://blog.csdn.net/ouyang_peng/article/details/49174183（我的Android进阶之旅------>【强力推荐】Android开源图表库XCL-Charts版本发布及展示页 ）

    private SensorBarChartView barChart_sensor;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_chart);
        initView();
        initData();
    }

    private void initView() {
        barChart_sensor = (SensorBarChartView)findViewById(R.id.barchart_sensor);
        barChart_sensor.setTitle("传感器数据统计表");
        barChart_sensor.setLeftTitle("(值)");
        barChart_sensor.setLowerTitle("(个数)");
    }

    private void initData() {
        dbManager = new DatabaseManager(this);
        dbManager.selectAllUser();
    }

}
