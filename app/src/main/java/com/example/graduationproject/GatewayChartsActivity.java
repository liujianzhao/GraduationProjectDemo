package com.example.graduationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.graduationproject.entity.Sensor;
import com.example.graduationproject.view.SensorBarChartView;

import org.xclcharts.common.DensityUtil;

import java.util.LinkedList;

public class GatewayChartsActivity extends AppCompatActivity {

//  https://github.com/xcltapestry/XCL-Charts
//	http://blog.csdn.net/ouyang_peng/article/details/49174183（我的Android进阶之旅------>【强力推荐】Android开源图表库XCL-Charts版本发布及展示页 ）

    private RelativeLayout mLaychart = null;
    private int mMoveHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_charts);

        mLaychart = (RelativeLayout) this.findViewById(R.id.activity_gateway_charts);
        renderChart();
    }

    private void renderChart()
    {
        SensorBarChartView barChart = new SensorBarChartView(this,mMoveHeight,new LinkedList<Sensor>());
        int width = DensityUtil.dip2px(getApplicationContext(), 300);
        int height = DensityUtil.dip2px(getApplicationContext(), 400);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mLaychart.removeAllViews();
        mLaychart.addView(barChart,layoutParams);
    }
}
