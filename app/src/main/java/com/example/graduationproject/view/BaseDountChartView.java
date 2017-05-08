package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.xclcharts.chart.DountChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;
import org.xclcharts.view.ChartView;

import java.util.LinkedList;

/**
 * Created by liuji on 2017/5/8.
 */

public class BaseDountChartView extends ChartView{

    private String TAG = "BaseDountChartView";
    protected DountChart chart = new DountChart();

    public BaseDountChartView(Context context) {
        super(context);
        initView();
    }

    public BaseDountChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public BaseDountChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        chartRender();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w,h);
    }

    protected int[] getPieDefaultSpadding()
    {
        int [] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 20); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 65); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 20); //bottom
        return ltrb;
    }

    private void chartRender()
    {
        try {
            //设置绘图区默认缩进px值
            int [] ltrb = getPieDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1] + 100, ltrb[2], ltrb[3]);

            //标签显示(隐藏，显示在中间，显示在扇区外面)
            chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
            chart.getLabelPaint().setColor(Color.WHITE);

            //显示图例
            PlotLegend legend = chart.getPlotLegend();
            legend.show();
            legend.setType(XEnum.LegendType.ROW);
            legend.setHorizontalAlign(XEnum.HorizontalAlign.CENTER);
            legend.setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
            legend.showBox();
            legend.getBox().setBorderRectType(XEnum.RectType.RECT);

            //图背景色
            chart.setApplyBackgroundColor(true);
            chart.setBackgroundColor(Color.rgb(255, 255,255));

            //内环背景色
            chart.getInnerPaint().setColor(Color.rgb(255, 255,255));

            //显示边框线，并设置其颜色
            chart.getArcBorderPaint().setColor(Color.YELLOW);

            //保存标签位置
            chart.saveLabelsPosition(XEnum.LabelSaveType.ALL);

            //激活点击监听
            chart.ActiveListenItemClick();
            chart.showClikedFocus();

            chart.setInnerRadius(0.6f);
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
}

