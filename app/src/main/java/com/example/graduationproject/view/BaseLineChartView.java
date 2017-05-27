package com.example.graduationproject.view;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.view.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
/**
 * Created by liuji on 2017/5/27.
 */

public class BaseLineChartView extends ChartView{

    private String TAG = "LineChart02View";
    protected LineChart chart = new LineChart();

    public BaseLineChartView(Context context) {
        super(context);
        initView();
    }

    public BaseLineChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public BaseLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        chartRender();
        //綁定手势滑动事件
        this.bindTouch(this,chart);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }


    protected int[] getBarLnDefaultSpadding()
    {
        int [] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom
        return ltrb;
    }

    private void chartRender()
    {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(DensityUtil.dip2px(getContext(), 45),ltrb[1], ltrb[2],  ltrb[3]);

            //	chart.setDataSource(chartData);
            //chart.setCustomLines(mCustomLineDataset);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(120);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(10);
            //指隔多少个轴刻度(即细刻度)后为主刻度
            chart.getDataAxis().setDetailModeSteps(1);

            //背景网格
            chart.getPlotGrid().showHorizontalLines();

            //隐藏顶轴和右边轴
            //chart.hideTopAxis();
            //chart.hideRightAxis();

            //设置轴风格

            //chart.getDataAxis().setTickMarksVisible(false);
            chart.getDataAxis().getAxisPaint().setStrokeWidth(2);
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(2);
            chart.getDataAxis().showAxisLabels();

            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(2);
            chart.getCategoryAxis().hideTickMarks();

            //定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }

            });


            //定义线上交叉点标签显示格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }});

            //chart.setItemLabelFormatter(callBack)

            //允许线与轴交叉时，线会断开
            chart.setLineAxisIntersectVisible(false);

            //chart.setDataSource(chartData);
            //动态线
            chart.showDyLine();

            //不封闭
            chart.setAxesClosed(false);

            //扩展绘图区右边分割的范围，让定制线的说明文字能显示出来
            chart.getClipExt().setExtRight(150.f);

            //设置标签交错换行显示
            chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.ODD_EVEN);

            //仅能横向移动
            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);
            //chart.getDataAxis().hide();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        super.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            //交叉线
            if(chart.getDyLineVisible())
            {
                chart.getDyLine().setCurrentXY(event.getX(),event.getY());
                if(chart.getDyLine().isInvalidate())this.invalidate();
            }
        }
        return true;
    }

}
