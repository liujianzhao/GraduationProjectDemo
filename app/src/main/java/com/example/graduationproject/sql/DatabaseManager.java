package com.example.graduationproject.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationproject.entity.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private Context context;
    private SQLiteHelper sqliteHelper;

    public DatabaseManager(Context context){
        this.context = context;
        this.sqliteHelper = SQLiteHelper.getInstance(context);
    }

    public List<User> selectAllUser(){
        List<User> users = null;
        User user = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select username,password from sys_user",null);
        while(c.moveToNext()){
            if(users == null){
                users = new ArrayList<User>();
            }
            user = new User();
            user.setUsername(c.getString(c.getColumnIndex("username")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            users.add(user);
        }
        return users;
    }

}
