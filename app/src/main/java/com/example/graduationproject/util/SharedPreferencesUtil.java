package com.example.graduationproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.graduationproject.entity.User;

/**
 * Created by liuji on 2017/5/9.
 */

public class SharedPreferencesUtil {

    public static void saveData(Context context,boolean isFirst){
        SharedPreferences sp = context.getSharedPreferences("FirstLoadApp",Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.putBoolean("isFirst",isFirst);
        et.commit();
    }

    public static boolean loadData(Context context){
        SharedPreferences sp = context.getSharedPreferences("FirstLoadApp",Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst",true);
    }

    public static void saveLoginUserData(Context context,User user){
        SharedPreferences sp = context.getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.putString("username",user.getUsername());
        et.putString("password",user.getPassword());
        et.putString("rolename",user.getRoleName());
        et.putBoolean("remember",user.isRemember());
        et.commit();
    }

    public static User loadLoginUserData(Context context){
        SharedPreferences sp = context.getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
        User user = new User();
        user.setUsername(sp.getString("username",""));
        user.setPassword(sp.getString("password",""));
        user.setRoleName(sp.getString("rolename",""));
        user.setRemember(sp.getBoolean("remember",false));
        return user;
    }
}
