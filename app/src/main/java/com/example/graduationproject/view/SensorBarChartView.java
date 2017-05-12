package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.graduationproject.entity.OverAllSensor;
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
    private OverAllSensor data;

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

    public void setRecentlySensorDatas(List<Sensor> sensorDatas) {
        this.sensorDatas = sensorDatas;
        initChartDatas();
    }

    public void setOverAllSensorData(OverAllSensor data){
        this.data = data;
        changeViewShow();
        initChartData();
    }

    private void initData() {
        initChartTitle();
        initChartDatas();


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

    private void changeViewShow(){
        chart.getDataAxis().setAxisMax(4000);
        chart.getDataAxis().setAxisMin(0);
        chart.getDataAxis().setAxisSteps(100);
        //指隔多少个轴刻度(即细刻度)后为主刻度
        chart.getDataAxis().setDetailModeSteps(2);
    }

    private void initChartData(){
        chartDatas.clear();
        if (data != null) {
            initChartLabel();
            //标签对应的柱形数据集
            List<Double> dataSeriesA = new LinkedList<Double>();
            //依数据值确定对应的柱形颜色.
            List<Integer> dataColorA = new LinkedList<Integer>();

            dataSeriesA.add((double)data.getNormalData());
            dataSeriesA.add((double)data.getHighData());
            dataSeriesA.add((double)data.getErrorData());
            dataColorA.add(Color.rgb(77, 184, 73));
            dataColorA.add(Color.rgb(252, 210, 9));
            dataColorA.add(Color.RED);

            BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
                    Color.rgb(53, 169, 239));
            chartDatas.add(BarDataA);
            invalidate();
        }
    }

    private void initChartLabel(){
        chartLabels.clear();
        chartLabels.add("正常数据");
        chartLabels.add("偏高数据");
        chartLabels.add("异常数据");
    }

    private void initChartDatas() {
        chartDatas.clear();
        if (sensorDatas != null) {
            initChartLabels();
            initChartDesireLines();
            //标签对应的柱形数据集
            List<Double> dataSeriesA = new LinkedList<Double>();
            //依数据值确定对应的柱形颜色.
            List<Integer> dataColorA = new LinkedList<Integer>();

            int len = sensorDatas.size();
            for (int i = 0; i < len; i++) {
                double data = sensorDatas.get(i).getValue();
                dataSeriesA.add(data);
                if (data <= 75d) //合理数据
                {
                    dataColorA.add(Color.rgb(77, 184, 73));
                } else if (data <= 90d) { //偏高数据
                    dataColorA.add(Color.rgb(252, 210, 9));
                }  else {  //异常数据
                    dataColorA.add(Color.RED);
                }
            }

            BarData BarDataA = new BarData("", dataSeriesA, dataColorA,
                    Color.rgb(53, 169, 239));
            chartDatas.add(BarDataA);
            invalidate();
        }
    }

    private void initChartLabels() {
        chartLabels.clear();
        int len = sensorDatas.size();
        for (int i = 1; i <= len; i++) {
            if (1 == i || i % 5 == 0) {
                chartLabels.add(Integer.toString(i));
            } else {
                chartLabels.add("");
            }
        }
    }

    /**
     * 期望线/分界线
     */
    private void initChartDesireLines() {
        mCustomLineDataset.add(new CustomLineData("合理", 75d, Color.rgb(77, 184, 73), 3));
        mCustomLineDataset.add(new CustomLineData("偏高", 90d, Color.rgb(252, 210, 9), 4));
        mCustomLineDataset.add(new CustomLineData("异常", 100d, Color.RED, 5));
    }


    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();

    /**
     * 解决viewpager和chart同时执行滑动事件的冲突问题
     **/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = event.getX();
        curP.y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = event.getX();
            downP.y = event.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }
}
