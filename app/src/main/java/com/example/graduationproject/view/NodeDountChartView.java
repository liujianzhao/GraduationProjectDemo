package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.graduationproject.entity.ZigbeeNode;

import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;

import java.util.LinkedList;

/**
 * Created by liuji on 2017/5/8.
 */

public class NodeDountChartView extends BaseDountChartView{

    private String title = "";
    private String subTitle = "";
    private String centerText = "";
    LinkedList<ZigbeeNode> NodeData;

    LinkedList<PieData> lPieData = new LinkedList<PieData>();

    public NodeDountChartView(Context context) {
        super(context);
        initData();
    }

    public NodeDountChartView(Context context, AttributeSet attrs){
        super(context, attrs);
        initData();
    }

    public NodeDountChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    public void setTitle(String title) {
        this.title = title;
        chart.setTitle(title);
    }

    public void setNodeData(LinkedList<ZigbeeNode> nodeData) {
        NodeData = nodeData;
        chartDataSet();
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
        chart.setCenterText(centerText);
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        chart.addSubtitle(subTitle);
    }

    private void initData() {
        initChartTitle();
        chartDataSet();
        addAttrInfo();

        //数据源
        chart.setDataSource(lPieData);
    }

    private void initChartTitle() {
        //标题
        chart.setTitle(title);
        chart.addSubtitle(subTitle);

        chart.getCenterTextPaint().setColor(Color.rgb(242, 167, 69));
        chart.setCenterText(centerText);
    }

    private void chartDataSet()
    {
        lPieData.clear();
        if(NodeData == null){
            //设置图表数据源
            //PieData(标签，百分比，在饼图中对应的颜色)
            lPieData.add(new PieData("正常运行","90%",90,Color.rgb(70, 70, 255)));
            lPieData.add(new PieData("异常状态","10%",10,Color.rgb(255,50, 50)));
        }else{
            int normal = 0;
            int error = 0;
            int len = NodeData.size();
            for(int i=0;i<len;i++){
                ZigbeeNode data = NodeData.get(i);
                if(data.isNode_online()){
                    normal++;
                }else{
                    error++;
                }
            }
            double x = normal/len*100;
            lPieData.add(new PieData("正常运行",String.valueOf(x),x,Color.rgb(70, 70, 255)));
            lPieData.add(new PieData("异常状态",String.valueOf(100-x),100-x,Color.rgb(255,50, 50)));
        }

    }

    private void addAttrInfo()
    {
        //设置附加信息
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30);
//        paint.setColor(Color.rgb(191, 79, 75));
        chart.getPlotAttrInfo().addAttributeInfo(XEnum.Location.LEFT, "正常运行", 0.5f, paint);
        chart.getPlotAttrInfo().addAttributeInfo(XEnum.Location.RIGHT, "异常状态", 0.4f, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            triggerClick(event.getX(),event.getY());
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x,float y)
    {
        if(!chart.getListenItemClickStatus())return;
        ArcPosition record = chart.getPositionRecord(x,y);
        if( null == record) return;

        PieData pData = lPieData.get(record.getDataID());

        boolean isInvaldate = true;
        for(int i=0;i < lPieData.size();i++)
        {
            PieData cData = lPieData.get(i);
            if(i == record.getDataID())
            {
                if(cData.getSelected())
                {
                    isInvaldate = false;
                    break;
                }else{
                    cData.setSelected(true);
                }
            }else
                cData.setSelected(false);
        }

        if(isInvaldate)this.invalidate();
    }
}
