package com.example.graduationproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.graduationproject.sql.DatabaseManager;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username,et_password;
    private Button btn_login;
    private ImageView img_pic;

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        img_pic = (ImageView)findViewById(R.id.img_pic);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,SensorChartActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(LoginActivity.this,NodeChartActivity.class);
                startActivity(intent);
            }
        });


        dbManager = new DatabaseManager(this);
        dbManager.selectAllUser();
    }
}
