package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import org.xclcharts.chart.BarChart;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.view.ChartView;

import java.text.DecimalFormat;

/**
 * Created by liuji on 2017/5/4.
 * 通用的柱形图相关属性设置
 */

public class BaseBarChartView extends ChartView{

    private String TAG = "BarChartView";

    private int mOffsetHeight = 0;
    protected BarChart mChart = null;

    public BaseBarChartView(Context context, int offsetHeight) {
        super(context);

        this.mOffsetHeight = offsetHeight;
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        //mChart.setChartRange(w,h);
    }

    //Demo中bar chart所使用的默认偏移值。
    //偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding()
    {
        int [] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); //bottom
        return ltrb;
    }

    public void chartRender()
    {
        try {

            mChart = new BarChart();
            //图例
            mChart.getAxisTitle().setLeftTitle("百分比");

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int [] ltrb = getBarLnDefaultSpadding();
            mChart.setPadding(DensityUtil.dip2px(getContext(), 50),ltrb[1], ltrb[2], ltrb[3]);

            //数据轴
            mChart.getDataAxis().setAxisMax(100);
            mChart.getDataAxis().setAxisMin(0);
            mChart.getDataAxis().setAxisSteps(20);

            //定义数据轴标签显示格式
            mChart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){

                @Override
                public String textFormatter(String value) {
                    // TODO Auto-generated method stub
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return label+"%";
                }

            });
            //定义柱形上标签显示格式
            mChart.getBar().setItemLabelVisible(true);
            mChart.getBar().getItemLabelPaint().setColor(Color.rgb(72, 61, 139));
            mChart.getBar().getItemLabelPaint().setFakeBoldText(true);

            mChart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    // TODO Auto-generated method stub
                    DecimalFormat df=new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label+"%";
                }});


            mChart.DeactiveListenItemClick();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void render(Canvas canvas) {
        try{
            mChart.setChartRange(0.0f, mOffsetHeight, this.getWidth(),this.getHeight() - mOffsetHeight);
            //mChart.setChartRange(this.getMeasuredWidth(), this.getMeasuredHeight());
            mChart.render(canvas);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

}
