package com.example.graduationproject.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationproject.entity.Gateway;
import com.example.graduationproject.entity.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private Context context;
    private SQLiteHelper sqliteHelper;

    public DatabaseManager(Context context) {
        this.context = context;
        this.sqliteHelper = SQLiteHelper.getInstance(context);
    }

    public List<User> selectAllUser() {
        List<User> users = null;
        User user = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select username,password,roles_id,name from sys_user,sys_user_roles,sys_role where sys_user.id = sys_user_roles.sys_user_id and sys_user_roles.roles_id = sys_role.id", null);
        try {
            while (c.moveToNext()) {
                if (users == null) {
                    users = new ArrayList<User>();
                }
                user = new User();
                user.setUsername(c.getString(c.getColumnIndex("username")));
                user.setPassword(c.getString(c.getColumnIndex("password")));
                user.setRoleName(c.getString(c.getColumnIndex("name")));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return users;
    }

    public List<Gateway> selectAllGateway() {
        List<Gateway> gateways = null;
        Gateway gateway = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from t_gateway", null);
        try {
            while (c.moveToNext()) {
                if (gateways == null) {
                    gateways = new ArrayList<>();
                }
                gateway = new Gateway();
                gateway.setName(c.getString(c.getColumnIndex("name")));
                gateway.setIp(c.getString(c.getColumnIndex("ip")));
                gateway.setPort(c.getInt(c.getColumnIndex("port")));
                gateway.setMax_nodes(c.getInt(c.getColumnIndex("max_nodes")));
                gateway.setMax_channels(c.getInt(c.getColumnIndex("max_channels")));
                gateway.setPoll_interval(c.getInt(c.getColumnIndex("poll_interval")));
                gateway.setX(c.getFloat(c.getColumnIndex("X")));
                gateway.setY(c.getFloat(c.getColumnIndex("Y")));
                gateway.setDesc_string(c.getString(c.getColumnIndex("desc_string")));
                gateway.setPic(c.getString(c.getColumnIndex("pic")));
                gateways.add(gateway);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return gateways;
    }
}
