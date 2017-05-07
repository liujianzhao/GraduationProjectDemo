package com.example.graduationproject.sql;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SQLiteHelper extends SQLiteOpenHelper{


//    http://blog.csdn.net/zbw1185/article/details/47975965
//    http://blog.csdn.net/yangyang_1009/article/details/19168239
//    http://blog.csdn.net/jack_l1/article/details/6742426(完成从Mysql到SQLite数据库的整体迁移)

    private static final String DBNAME = "gateway.db";
    private static final int DBVERSION = 1;

    private Context context;
    private static SQLiteHelper sqliteHelper;

    public static SQLiteHelper getInstance(Context context){
        synchronized(SQLiteHelper.class){
            if(sqliteHelper == null){
                sqliteHelper = new SQLiteHelper(context);
            }
        }
        return sqliteHelper;
    }

    private SQLiteHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
        this.context = context;
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, null, DBVERSION);
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DBNAME, null, DBVERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DBNAME,"onCreate");
        AssetManager am = context.getAssets();
        BufferedReader reader = null;
        InputStream input = null;
        try {
            input = am.open("gateway.sql");
            reader = new BufferedReader(new InputStreamReader(input));
            executeSqlScript(db, reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DBNAME,"onUpgrade");
    }

    public void executeSqlScript(SQLiteDatabase db, BufferedReader reader)
            throws IOException {
        StringBuilder sql = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (TextUtils.isEmpty(line) || line.startsWith("--")) {
                continue;
            }
            Log.e("###### line : " ,line);
            line = line.trim();
            int index = line.indexOf(';');
            if (index >= 0) {
                String firstStr = line.substring(0, index + 1);
                sql.append(firstStr).append('\n');
                try {
                    db.execSQL(sql.toString()); // make database
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                sql = new StringBuilder();
                if (index < line.length()) {
                    String lastStr = line.substring(index + 1);
                    if (!TextUtils.isEmpty(lastStr)) {
                        sql.append(lastStr);
                    }
                }
            } else {
                sql.append(line).append('\n');
            }
        }
        if (sql.length() > 0) {
            try {
                db.execSQL(sql.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
