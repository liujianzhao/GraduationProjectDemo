package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.adapter.GatewayAdapter;
import com.example.graduationproject.entity.Gateway;
import com.example.graduationproject.entity.User;
import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.util.LoadingDialog;
import com.example.graduationproject.util.SharedPreferencesUtil;
import com.example.graduationproject.view.RefreshListView;
import com.example.graduationproject.zxing.android.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class GatewayListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    private static final int LOAD_COMPLETE = 0;

    private Context context;
    private DatabaseManager dbManager;

    private RefreshListView refreshListView;// 列表
    private List<Gateway> backup_datas = new ArrayList<>();
    private List<Gateway> datas = new ArrayList<>();
    private GatewayAdapter adapter;

    private String pageId = "1";
    private boolean isRefresh;
    private boolean hasMore;

    private ImageView img_userPic;
    private TextView tv_userName,tv_roleName;

    private EditText et_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_list);
        this.context = this;
        dbManager = new DatabaseManager(context);

        //标题
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        //菜单资源获取
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        img_userPic = (ImageView)headerLayout.findViewById(R.id.img_userpic);
        tv_userName = (TextView)headerLayout.findViewById(R.id.tv_username);
        tv_roleName = (TextView)headerLayout.findViewById(R.id.tv_rolename);
        User user = SharedPreferencesUtil.loadLoginUserData(context);
        tv_userName.setText(user.getUsername());
        tv_roleName.setText(user.getRoleName());

        //筛选过滤器
        et_filter = (EditText)findViewById(R.id.et_filter);
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //具体筛选数据的操作
                String data = et_filter.getText().toString().trim();
                filterData(data);
            }
        });

        //下拉刷新，上拉加载的listview控件
        refreshListView = (RefreshListView)findViewById(R.id.listview);
        adapter = new GatewayAdapter(context, datas);
        refreshListView.setAdapter(adapter);

        refreshListView.setOnRefreshListener(new RefreshListView.IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                pageId = "1";
                isRefresh = true;
                new Thread(run).start();
            }
        });
        refreshListView.setOnLoadMoreListener(new RefreshListView.IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                isRefresh = false;
                new Thread(run).start();
            }
        });
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //点击item跳转详情页面
                Gateway gateway = datas.get(position-1);
                Intent intent = new Intent(GatewayListActivity.this,GatewayDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("gateway",gateway);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        // 进入第一次自动加载列表
        refreshListView.firstLoadDatas();
    }

    private void filterData(String data){
        datas.clear();
        for(Gateway gateway:backup_datas){
            if(gateway.getName().contains(data)){
                datas.add(gateway);
            }
        }
        adapter.notifyDataSetChanged();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int key = msg.what;
            switch(key){
                case 0:
                    loadComplete();
                    break;
                default:
                    break;
            }
        }
    };

    Runnable run = new Runnable() {
        @Override
        public void run() {
            //休眠1秒钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getGatewayDatas();
        }
    };

    private void getGatewayDatas(){
        backup_datas.clear();
        datas.clear();
        List<Gateway> list = dbManager.selectAllGateway();
        for(Gateway data:list){
            backup_datas.add(data);
        }
        datas.addAll(backup_datas);
        handler.sendEmptyMessage(LOAD_COMPLETE);
    }


    private void loadComplete() {
        adapter.notifyDataSetChanged();
        if (isRefresh) {
            refreshListView.onRefreshComplete();
            refreshListView.onLoadMoreComplete(hasMore);
        } else {
            refreshListView.onLoadMoreComplete(hasMore);
        }
    }

    //边布局的弹出和收回
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //边布局的item点击事件处理
        if(id == R.id.nav_scan){
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }else if(id == R.id.nav_about){
            Intent intent = new Intent(context,AboutActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_logout){
            Intent intent = new Intent(context,LoginActivity.class);
            startActivity(intent);
            finish();
        }
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //startactivityforresult方法返回的数据处理，处理扫一扫返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                boolean readComplete = false;
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                for(Gateway gateway:datas){
                    if(gateway.getName().equals(content)){
                        readComplete = true;
                        Intent intent = new Intent(GatewayListActivity.this,GatewayDetailActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("gateway",gateway);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
                if(!readComplete){
                    Toast.makeText(context,"没有相关终端信息！",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
