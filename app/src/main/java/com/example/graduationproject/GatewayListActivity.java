package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.List;

public class GatewayListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int LOAD_COMPLETE = 0;

    private Context context;
    private DatabaseManager dbManager;

    private RefreshListView refreshListView;// 列表
    private List<Gateway> datas = new ArrayList<>();
    private GatewayAdapter adapter;

    private String pageId = "1";
    private boolean isRefresh;
    private boolean hasMore;

    private ImageView img_userPic;
    private TextView tv_userName,tv_roleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_list);
        this.context = this;
        dbManager = new DatabaseManager(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

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

        refreshListView = (RefreshListView)findViewById(R.id.listview);
        adapter = new GatewayAdapter(context, datas);
        refreshListView.setAdapter(adapter);

        refreshListView.setOnRefreshListener(new RefreshListView.IOnRefreshListener() {
            @Override
            public void OnRefresh() {
                pageId = "1";
                isRefresh = true;
                t.start();
            }
        });
        refreshListView.setOnLoadMoreListener(new RefreshListView.IOnLoadMoreListener() {
            @Override
            public void OnLoadMore() {
                isRefresh = false;
                t.start();
            }
        });
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent(GatewayListActivity.this,SensorChartActivity.class);
                startActivity(intent);
            }
        });
        // 自动加载列表
        refreshListView.firstLoadDatas();
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

    Thread t = new Thread(new Runnable(){

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getGatewayDatas();
        }
    });

    private void getGatewayDatas(){
        datas.clear();
        List<Gateway> list = dbManager.selectAllGateway();
        for(Gateway data:list){
            datas.add(data);
        }
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

        if(id == R.id.nav_about){
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
}
