package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineData;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuji on 2017/5/27.
 */

public class RealDataLineChartView extends BaseLineChartView {

    private String title = "";
    private String subTitle = "";
    private String dataType = "";

    private LinkedList<Double> dataSeries = new LinkedList<Double>();
    //标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    private LinkedList<LineData> chartData = new LinkedList<LineData>();
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
    //批注
    List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();

    public RealDataLineChartView(Context context) {
        super(context);
        initData();
    }

    public RealDataLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public RealDataLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    public void setTitle(String title){
        this.title = title;
        chart.setTitle(title);
    }

    public void setSubTitile(String subTitle){
        this.subTitle = subTitle;
        chart.addSubtitle(subTitle);
    }

    public void setDataType(String dataType){
        this.dataType = dataType;
    }

    public void addRealData(int value){
        chartData.clear();
        mAnchorSet.clear();
        int len = dataSeries.size();
        if(len <= 30){
            labels.add(String.valueOf(len));
        }
        if(len > 30){
            dataSeries.remove(0);
        }
        chartData.add(createLineData(value));
        for(int i=0;i<len;i++){
            if(dataSeries.get(i) > 90){
                AnchorDataPoint an4 = new AnchorDataPoint(0,i,XEnum.AnchorStyle.CAPRECT);
                an4.setAnchor("注意异常");
                an4.setBgColor(Color.rgb(255, 145, 126));
                mAnchorSet.add(an4);
            }
        }
        invalidate();
    }

    private LineData createLineData(int value) {
        dataSeries.add((double)value);
        LineData lineData = new LineData(dataType,dataSeries, Color.rgb(234, 83, 71));
        lineData.setDotStyle(XEnum.DotStyle.DOT);
        return lineData;
    }

    private void initData() {
        chartLabels();
        chartDataSet();
        chartDesireLines();

        //标题
        chart.setTitle(title);
        chart.addSubtitle(subTitle);

        //设定数据源
        chart.setCategories(labels);
        chart.setDataSource(chartData);
        chart.setAnchorDataPoint(mAnchorSet);
        chart.setCustomLines(mCustomLineDataset);
    }

    private void chartDataSet()
    {

    }

    private void chartLabels()
    {

    }

    /**
     * 期望线/分界线
     */
    private void chartDesireLines()
    {
        mCustomLineDataset.add(new CustomLineData("合理", 75d, Color.rgb(77, 184, 73), 3));
        mCustomLineDataset.add(new CustomLineData("偏高", 90d, Color.rgb(252, 210, 9), 4));
        mCustomLineDataset.add(new CustomLineData("异常", 100d, Color.RED, 5));
    }

}
