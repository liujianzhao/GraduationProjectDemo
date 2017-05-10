package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduationproject.entity.User;
import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.util.LoadingDialog;
import com.example.graduationproject.util.SharedPreferencesUtil;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN_COMPLETE = 0;
    private static final int LOGIN_FAILURE = 1;
    private Context context;

    private EditText et_username,et_password;
    private CheckBox checkbox_remember;
    private Button btn_login;
    private ImageView img_pic;

    private DatabaseManager dbManager;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        dialog = LoadingDialog.getInstance(context);
        dbManager = new DatabaseManager(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        checkbox_remember = (CheckBox)findViewById(R.id.checkbox_remember);
        btn_login = (Button)findViewById(R.id.btn_login);
        img_pic = (ImageView)findViewById(R.id.img_pic);

        User user = SharedPreferencesUtil.loadLoginUserData(context);
        if(user.isRemember()){
            et_username.setText(user.getUsername());
            et_password.setText(user.getPassword());
            checkbox_remember.setChecked(true);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
                t.start();
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int key = msg.what;
            switch(key){
                case 0:
                    dialog.dismissDialog();
                    Intent intent = new Intent(LoginActivity.this, GatewayListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    dialog.dismissDialog();
                    Toast.makeText(context,"用户名或密码错误！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    Thread t = new Thread(new Runnable(){

        @Override
        public void run() {
            checkLoginUser();
        }
    });

    private void checkLoginUser(){
        boolean LoginComplete = false;
        List<User> users = dbManager.selectAllUser();
        String loginName = et_username.getText().toString().trim();
        String loginPassword = et_password.getText().toString().trim();
        for(User user:users){
            if(loginName.equals(user.getUsername()) && loginPassword.equals(user.getPassword())){
                if(checkbox_remember.isChecked()){
                    user.setRemember(true);
                }else{
                    user.setRemember(false);
                }
                SharedPreferencesUtil.saveLoginUserData(context,user);
                LoginComplete = true;
            }
        }
        if(LoginComplete){
            handler.sendEmptyMessage(LOGIN_COMPLETE);
        }else{
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

}
