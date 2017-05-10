package com.example.graduationproject;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.graduationproject.entity.User;
import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.util.LoadingDialog;
import com.example.graduationproject.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by liuji on 2017/5/9.
 */

public class GPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
