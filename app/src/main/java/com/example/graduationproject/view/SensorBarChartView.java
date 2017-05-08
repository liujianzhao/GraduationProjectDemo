package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.graduationproject.entity.Sensor;

import org.xclcharts.chart.BarData;
import org.xclcharts.chart.CustomLineData;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by liuji on 2017/5/4.
 * 创建sensor表数据的柱形图
 */

public class SensorBarChartView extends BaseBarChartView {

    private String title = "";
    private String subTitle = "";
    private String leftTitle = "";
    private String lowerTitle = "";
    private List<Sensor> sensorDatas;

    private List<String> chartLabels = new LinkedList<String>();
    private List<BarData> chartDatas = new LinkedList<BarData>();
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    public SensorBarChartView(Context context) {
        super(context);
        initData();
    }

    public SensorBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public SensorBarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    public void setTitle(String title) {
        this.title = title;
        chart.setTitle(title);
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        chart.addSubtitle(subTitle);
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
        chart.getAxisTitle().setLeftTitle(leftTitle);
    }

    public void setLowerTitle(String lowerTitle) {
        this.lowerTitle = lowerTitle;
        chart.getAxisTitle().setLowerTitle(lowerTitle);
    }

    public void setSensorDatas(List<Sensor> sensorDatas) {
        this.sensorDatas = sensorDatas;
        initChartDatas();
    }

    private void initData() {
        initChartTitle();
        initChartLabels();
        initChartDatas();
        initChartDesireLines();

        //数据源
        chart.setDataSource(chartDatas);
        chart.setCategories(chartLabels);
        chart.setCustomLines(mCustomLineDataset);
    }

    private void initChartTitle() {
        //图标题
        chart.setTitle(title);
        chart.addSubtitle(subTitle);

        //坐标轴标题
        chart.getAxisTitle().setLeftTitle(leftTitle);
        chart.getAxisTitle().setLowerTitle(lowerTitle);
    }

    private void initChartLabels() {
        for (int i = 1; i < 51; i++) {
            if (1 == i || i % 5 == 0) {
                chartLabels.add(Integer.toString(i));
            } else {
                chartLabels.add("");
            }
        }
    }

    private void initChartDatas() {
        chartDatas.clear();
        if (sensorDatas == null) {
            //标签对应的柱形数据集
            List<Double> dataSeriesA = new LinkedList<Double>();
            //依数据值确定对应的柱形颜色.
            List<Integer> dataColorA = new LinkedList<Integer>();

            int max = 35;
            int min = 15;

            for (int i = 1; i < 51; i++) {
                Random random = new Random();
                int v = random.nextInt(max) % (max - min + 1) + min;
                dataSeriesA.add((double) v);

                if (v <= 18.5d) //适中
                {
                    dataColorA.add(Color.rgb(77, 184, 73));
                } else if (v <= 24d) { //超重
                    dataColorA.add(Color.rgb(252, 210, 9));
                } else if (v <= 27.9d) { //偏胖
                    dataColorA.add(Color.rgb(171, 42, 96));
                } else {  //肥胖
                    dataColorA.add(Color.RED);
                }
            }
            //此地的颜色为Key值颜色及柱形的默认颜色
            BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
                    Color.rgb(53, 169, 239));

            chartDatas.add(BarDataA);
        } else {
            //标签对应的柱形数据集
            List<Double> dataSeriesA = new LinkedList<Double>();
            //依数据值确定对应的柱形颜色.
            List<Integer> dataColorA = new LinkedList<Integer>();

            int len = sensorDatas.size();
            for (int i = 0; i < len; i++) {
                double data = sensorDatas.get(i).getValue();
                dataSeriesA.add(data);
                if (data <= 18.5d) //适中
                {
                    dataColorA.add(Color.rgb(77, 184, 73));
                } else if (data <= 24d) { //超重
                    dataColorA.add(Color.rgb(252, 210, 9));
                } else if (data <= 27.9d) { //偏胖
                    dataColorA.add(Color.rgb(171, 42, 96));
                } else {  //肥胖
                    dataColorA.add(Color.RED);
                }
            }

            BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
                    Color.rgb(53, 169, 239));
            chartDatas.add(BarDataA);
        }
    }

    /**
     * 期望线/分界线
     */
    private void initChartDesireLines() {
        mCustomLineDataset.add(new CustomLineData("适中", 18.5d, Color.rgb(77, 184, 73), 3));
        mCustomLineDataset.add(new CustomLineData("超重", 24d, Color.rgb(252, 210, 9), 4));
        mCustomLineDataset.add(new CustomLineData("偏胖", 27.9d, Color.rgb(171, 42, 96), 5));
        mCustomLineDataset.add(new CustomLineData("肥胖", 30d, Color.RED, 6));
    }

}
