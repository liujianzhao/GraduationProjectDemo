package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.graduationproject.ddpush.tcp.RealDataActivity;
import com.example.graduationproject.entity.Gateway;
import com.example.graduationproject.entity.OverAllSensor;
import com.example.graduationproject.entity.Sensor;
import com.example.graduationproject.entity.ZigbeeNode;
import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.util.CalendarDialog;
import com.example.graduationproject.view.CustomPopupWindow;
import com.example.graduationproject.view.NodeDountChartView;
import com.example.graduationproject.view.SensorBarChartView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class GatewayDetailActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Context context;
    private static DatabaseManager dbManager;
    private static CalendarDialog dialog;

    //page1
    private static Gateway gateway;
    private static List<ZigbeeNode> zigbeeNodes;
    private static TextView tv_name, tv_ip, tv_port, tv_maxNodes, tv_maxChannels, tv_pollInterval;
    private static TextView tv_nodename, tv_nodeaddr, tv_nodeonline;
    private static NodeDountChartView dountChart_node;

    //page2
    private static int format = 1;
    private static CustomPopupWindow overall_customPopupWindow;
    private static Button btn_day, btn_month, btn_year;
    private static Button btn_date;
    private static SensorBarChartView barChart_overall_sensor;

    //page3
    private static CustomPopupWindow recently_customPopupWindow;
    private static SensorBarChartView barChart_recently_sensor;

    //page4
    private static ListView listView;
    private static ListAdapter adapter;
    private static List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_detail);
        this.context = this;
        format = 1;
        dbManager = new DatabaseManager(context);
        dialog = CalendarDialog.getInstance(context);
        gateway = (Gateway) getIntent().getExtras().getSerializable("gateway");

        //标题
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(gateway.getName());
        setSupportActionBar(toolbar);

        //左右切换
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //三个小标题
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(datas.isEmpty()){
            datas.add("温度实时查看");
            datas.add("湿度实时查看");
        }
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,datas);
    }

    //嵌入在activity里面的小页面
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_gateway_detail, container, false);
                tv_name = (TextView) rootView.findViewById(R.id.tv_name);
                tv_ip = (TextView) rootView.findViewById(R.id.tv_ip);
                tv_port = (TextView) rootView.findViewById(R.id.tv_port);
                tv_maxNodes = (TextView) rootView.findViewById(R.id.tv_maxnodes);
                tv_maxChannels = (TextView) rootView.findViewById(R.id.tv_maxchannels);
                tv_pollInterval = (TextView) rootView.findViewById(R.id.tv_pollinterval);

                tv_nodename = (TextView) rootView.findViewById(R.id.tv_nodename);
                tv_nodeaddr = (TextView) rootView.findViewById(R.id.tv_nodeaddr);
                tv_nodeonline = (TextView) rootView.findViewById(R.id.tv_nodeonline);
                dountChart_node = (NodeDountChartView) rootView.findViewById(R.id.dountchart_node);
                loadDetailData();
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_gateway_detail_sensor_overall, container, false);
                btn_day = (Button) rootView.findViewById(R.id.btn_day);
                btn_month = (Button) rootView.findViewById(R.id.btn_month);
                btn_year = (Button) rootView.findViewById(R.id.btn_year);
                btn_date = (Button) rootView.findViewById(R.id.btn_date);
                overall_customPopupWindow = (CustomPopupWindow) rootView.findViewById(R.id.custompopupwindow);
                barChart_overall_sensor = (SensorBarChartView) rootView.findViewById(R.id.barchart_overall_sensor);
                loadOverAllData();
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.fragment_gateway_detail_sensor_recently, container, false);
                recently_customPopupWindow = (CustomPopupWindow) rootView.findViewById(R.id.custompopupwindow);
                barChart_recently_sensor = (SensorBarChartView) rootView.findViewById(R.id.barchart_recently_sensor);
                loadRecentlyData();
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4){
                rootView = inflater.inflate(R.layout.fragment_gateway_detail_realdata, container, false);
                listView = (ListView)rootView.findViewById(R.id.listview);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent("RealDataActivity");
                        intent.putExtra("title",datas.get(position));
                        startActivity(intent);
                    }
                });
            }
            return rootView;
        }

        //第一个页面加载数据
        private void loadDetailData() {
            zigbeeNodes = dbManager.selectZigbeeNode(String.valueOf(gateway.getId()));
            tv_name.setText(gateway.getName());
            tv_ip.setText(gateway.getIp());
            tv_port.setText(String.valueOf(gateway.getPort()));
            tv_maxNodes.setText(String.valueOf(gateway.getMax_nodes()));
            tv_maxChannels.setText(String.valueOf(gateway.getMax_channels()));
            tv_pollInterval.setText(String.valueOf(gateway.getPoll_interval()));

            tv_nodename.setText(zigbeeNodes.get(0).getNode_name() + "," + zigbeeNodes.get(1).getNode_name());
            tv_nodeaddr.setText(zigbeeNodes.get(0).getNode_addr() + "," + zigbeeNodes.get(1).getNode_addr());
            StringBuffer sb = new StringBuffer();
            for (ZigbeeNode data : zigbeeNodes) {
                if (data.getNode_online() == 1) {
                    sb.append(data.getNode_addr() + ",");
                }
            }
            tv_nodeonline.setText(sb.toString().equals("") ? "" : sb.deleteCharAt(sb.length() - 1).toString());

            dountChart_node.setTitle("终端节点状态图");
            dountChart_node.setSubTitle("(节点最新在线情况)");
            LinkedList<ZigbeeNode> datas = new LinkedList<>(zigbeeNodes);
            dountChart_node.setNodeData(datas);
        }

        //第二个页面加载数据
        private void loadOverAllData() {
            List<String> nodeDatas = new ArrayList<>();
            nodeDatas.add("1");
            nodeDatas.add("64");
            List<String> channelDatas = new ArrayList<>();
            channelDatas.add("0");
            channelDatas.add("1");
            channelDatas.add("2");
            channelDatas.add("3");
            channelDatas.add("4");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String today = df.format(Calendar.getInstance().getTime());
            btn_date.setText(today);

            overall_customPopupWindow.setDatas(nodeDatas, channelDatas);
            overall_customPopupWindow.setOnClickListener(new CustomPopupWindow.OnPopupWindowClose() {
                @Override
                public void loadData() {
                    loadOverAllDatas();
                }
            });
            btn_day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    format = 1;
                    btn_day.setBackgroundResource(R.color.gray);
                    btn_month.setBackgroundResource(R.color.white);
                    btn_year.setBackgroundResource(R.color.white);
                    loadOverAllDatas();
                }
            });
            btn_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    format = 2;
                    btn_day.setBackgroundResource(R.color.white);
                    btn_month.setBackgroundResource(R.color.gray);
                    btn_year.setBackgroundResource(R.color.white);
                    loadOverAllDatas();
                }
            });
            btn_year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    format = 3;
                    btn_day.setBackgroundResource(R.color.white);
                    btn_month.setBackgroundResource(R.color.white);
                    btn_year.setBackgroundResource(R.color.gray);
                    loadOverAllDatas();
                }
            });
            btn_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setCommitOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_date.setText(dialog.getDay());
                            loadOverAllDatas();
                            dialog.dismissDialog();
                        }
                    });
                    dialog.setCancleOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismissDialog();
                        }
                    });
                    dialog.showDialog();
                }
            });

            barChart_overall_sensor.setTitle("传感器总体数据统计表");
            barChart_overall_sensor.setLeftTitle("(值)");
            barChart_overall_sensor.setLowerTitle("(个数)");
            OverAllSensor overall_data = dbManager.selectOverAllSensorDatas(String.valueOf(gateway.getId()), "", "","");
            barChart_overall_sensor.setOverAllSensorData(overall_data);
        }

        //第三个页面加载数据
        private void loadRecentlyData() {
            List<String> nodeDatas = new ArrayList<>();
            nodeDatas.add("1");
            nodeDatas.add("64");
            List<String> channelDatas = new ArrayList<>();
            channelDatas.add("0");
            channelDatas.add("1");
            channelDatas.add("2");
            channelDatas.add("3");
            channelDatas.add("4");
            recently_customPopupWindow.setDatas(nodeDatas, channelDatas);
            recently_customPopupWindow.setOnClickListener(new CustomPopupWindow.OnPopupWindowClose() {
                @Override
                public void loadData() {
                    List<Sensor> recently_datas = dbManager.selectRecentlySensorDatas(String.valueOf(gateway.getId()), recently_customPopupWindow.getNodeText(), recently_customPopupWindow.getChannelText());
                    barChart_recently_sensor.setRecentlySensorDatas(recently_datas);
                }
            });

            barChart_recently_sensor.setTitle("传感器最新数据统计表");
            barChart_recently_sensor.setLeftTitle("(值)");
            barChart_recently_sensor.setLowerTitle("(个数)");
            List<Sensor> recently_datas = dbManager.selectRecentlySensorDatas(String.valueOf(gateway.getId()), "", "");
            barChart_recently_sensor.setRecentlySensorDatas(recently_datas);
        }

        private void loadOverAllDatas(){
            if(!btn_date.getText().toString().equals("") && !overall_customPopupWindow.getNodeText().equals("") &&  !overall_customPopupWindow.getChannelText().equals(""))
            {
                OverAllSensor overall_data = dbManager.selectOverAllSensorDatas(String.valueOf(gateway.getId()),overall_customPopupWindow.getNodeText(), overall_customPopupWindow.getChannelText(),getDateFormat());
                barChart_overall_sensor.setOverAllSensorData(overall_data);
            }
        }

        private String getDateFormat(){
            StringBuffer dateFormat = new StringBuffer();
            String date = btn_date.getText().toString();
            if(format == 1){
                dateFormat.append(date);
            }else if(format == 2){
                dateFormat.append(date.substring(0,date.length()-2));
            }else if(format == 3){
                dateFormat.append(date.substring(0,date.length()-5));
            }
            return dateFormat.toString();
        }


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "终端信息";
                case 1:
                    return "总体分析";
                case 2:
                    return "近期分析";
                case 3:
                    return "实时查看";
            }
            return null;
        }
    }
}
