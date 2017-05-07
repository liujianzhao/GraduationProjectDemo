package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Color;

import com.example.graduationproject.entity.Sensor;

import org.xclcharts.chart.BarData;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuji on 2017/5/4.
 * 创建sensor表数据的柱形图
 */

public class SensorBarChartView extends BaseBarChartView {

    private int mOffsetHeight = 0;

    private List<Sensor> sensorDatas = new LinkedList<>();
    //标签轴
    private List<String> chartLabels = new LinkedList<String>();
    private List<BarData> chartDatas = new LinkedList<BarData>();

    public SensorBarChartView(Context context, int offsetHeight, LinkedList<Sensor> sensorDatas) {
        super(context, offsetHeight);
        this.sensorDatas = sensorDatas;
        initData();
    }

    private void initData() {
        initChartLabels();
        initChartDatas();

        //数据源
        mChart.setDataSource(chartDatas);
        mChart.setCategories(chartLabels);
    }

    private void initChartLabels() {
        chartLabels.add("路人甲");
        chartLabels.add("路人乙");
        chartLabels.add("路人丙");
    }

    private void initChartDatas() {
        //标签对应的柱形数据集
        List<Double> dataSeriesA= new LinkedList<Double>();
        dataSeriesA.add(50d);
        dataSeriesA.add(25d);
        dataSeriesA.add(20d);
        BarData BarDataA = new BarData("Google",dataSeriesA, Color.rgb(73, 135, 218));

        List<Double> dataSeriesB= new LinkedList<Double>();
//        dataSeriesB.add(35d);
//        dataSeriesB.add(65d);
//        dataSeriesB.add(75d);
        BarData BarDataB = new BarData("Baidu",dataSeriesB,Color.rgb(224, 4, 0));

        List<Double> dataSeriesC= new LinkedList<Double>();
//        dataSeriesC.add(15d);
//        dataSeriesC.add(10d);
//        dataSeriesC.add(5d);
        BarData BarDataC = new BarData("Bing",dataSeriesC,Color.rgb(255, 185, 0));

        chartDatas.add(BarDataA);
        chartDatas.add(BarDataB);
        chartDatas.add(BarDataC);
    }

}
