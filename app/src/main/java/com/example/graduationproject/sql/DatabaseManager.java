package com.example.graduationproject.sql;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduationproject.entity.Gateway;
import com.example.graduationproject.entity.OverAllSensor;
import com.example.graduationproject.entity.Sensor;
import com.example.graduationproject.entity.User;
import com.example.graduationproject.entity.ZigbeeNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
                gateway.setId(c.getInt(c.getColumnIndex("id")));
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

    public List<ZigbeeNode> selectZigbeeNode(String id) {
        List<ZigbeeNode> datas = null;
        ZigbeeNode data = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from t_zigbee_node where gateway_id = ? ORDER BY created DESC limit 2", new String[]{id});
        try {
            while (c.moveToNext()) {
                if(datas == null){
                    datas = new ArrayList<>();
                }
                data = new ZigbeeNode();
                data.setNode_name(c.getString(c.getColumnIndex("node_name")));
                data.setNode_addr(c.getInt(c.getColumnIndex("node_addr")));
                data.setGateway_id(c.getInt(c.getColumnIndex("gateway_id")));
                data.setNode_online(c.getInt(c.getColumnIndex("node_online")));
                data.setCreated(c.getString(c.getColumnIndex("created")));
                datas.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return datas;
    }

    public List<Sensor> selectRecentlySensorDatas(String gateway_id,String node_addr,String channel){
        List<Sensor> datas = null;
        Sensor data = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from t_sensor where gateway_id = ? and node_addr = ? and channel = ?  ORDER BY created DESC LIMIT ?", new String[]{gateway_id,node_addr,channel,"30"});
        try {
            while (c.moveToNext()) {
                if (datas == null) {
                    datas = new LinkedList<>();
                }
                data = new Sensor();
                data.setGateway_id(c.getInt(c.getColumnIndex("gateway_id")));
                data.setNode_addr(c.getInt(c.getColumnIndex("node_addr")));
                data.setChannel(c.getInt(c.getColumnIndex("channel")));
                data.setValue(c.getInt(c.getColumnIndex("value")));
                datas.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return datas;
    }

    public OverAllSensor selectOverAllSensorDatas(String gateway_id, String node_addr, String channel, String dateFormat){
        OverAllSensor data = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select count(case when value between 0 and 75 then value end),count(case when value between 76 and 90 then value end),count(case when value between 91 and 100 then value end) from t_sensor where gateway_id = ? and node_addr = ? and channel = ? and created LIKE "+"'"+dateFormat+"%'", new String[]{gateway_id,node_addr,channel});
        try {
            while (c.moveToNext()) {
                data = new OverAllSensor();
                data.setNormalData(c.getInt(c.getColumnIndex("count(case when value between 0 and 75 then value end)")));
                data.setHighData(c.getInt(c.getColumnIndex("count(case when value between 76 and 90 then value end)")));
                data.setErrorData(c.getInt(c.getColumnIndex("count(case when value between 91 and 100 then value end)")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return data;
    }
}
