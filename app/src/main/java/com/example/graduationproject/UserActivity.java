package com.example.graduationproject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.entity.User;
import com.example.graduationproject.sql.DatabaseManager;
import com.example.graduationproject.util.BitmapUtil;
import com.example.graduationproject.util.CalendarDialog;
import com.example.graduationproject.util.SharedPreferencesUtil;

import java.io.FileNotFoundException;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private DatabaseManager dbManager;
    private String username;

    private RelativeLayout layout_chooseUserpic;
    private LinearLayout layout_changeNickname, layout_changePassword, layout_changeGender, layout_changeAddress, layout_changeBirthday, layout_changeSign;

    private ImageView img_pic;
    private TextView tv_nickname, tv_username,tv_gender, tv_address, tv_birthday, tv_sign;

    private CalendarDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        this.context = this;
        dbManager = new DatabaseManager(context);
        dialog = CalendarDialog.getInstance(context);
        username = SharedPreferencesUtil.loadLoginUserData(context).getUsername();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人信息");
        setSupportActionBar(toolbar);

        layout_chooseUserpic = (RelativeLayout) findViewById(R.id.layout_chooseuserpic);
        layout_changeNickname = (LinearLayout) findViewById(R.id.layout_changenickname);
        layout_changePassword = (LinearLayout) findViewById(R.id.layout_changepassword);
        layout_changeGender = (LinearLayout) findViewById(R.id.layout_changegender);
        layout_changeAddress = (LinearLayout) findViewById(R.id.layout_changeaddress);
        layout_changeBirthday = (LinearLayout) findViewById(R.id.layout_changebirthday);
        layout_changeSign = (LinearLayout) findViewById(R.id.layout_changesign);
        layout_chooseUserpic.setOnClickListener(this);
        layout_changeNickname.setOnClickListener(this);
        layout_changePassword.setOnClickListener(this);
        layout_changeGender.setOnClickListener(this);
        layout_changeAddress.setOnClickListener(this);
        layout_changeBirthday.setOnClickListener(this);
        layout_changeSign.setOnClickListener(this);

        img_pic = (ImageView) findViewById(R.id.img_userpic);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_sign = (TextView) findViewById(R.id.tv_sign);

        loadUserData();
    }

    private void loadUserData() {
        User user = dbManager.selectUserInfo(username);
        img_pic.setImageBitmap(user.getUserpic());
        tv_nickname.setText(user.getNickname());
        tv_username.setText(username);
        tv_gender.setText(user.getGender());
        tv_address.setText(user.getAddress());
        tv_birthday.setText(user.getBirthday());
        tv_sign.setText(user.getSign());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_chooseuserpic:
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_changenickname:
                showOneEditDialog("修改昵称", "字数限制30字以内",tv_nickname);
                break;
            case R.id.layout_changepassword:
                showThreeEditDialog();
                break;
            case R.id.layout_changegender:
                showMultiChooseDialog("请选择性别");
                break;
            case R.id.layout_changeaddress:
                showOneEditDialog("修改地址", "字数限制30字以内",tv_address);
                break;
            case R.id.layout_changebirthday:
                showCalendarDialog();
                break;
            case R.id.layout_changesign:
                showOneEditDialog("修改个性签名", "字数限制30字以内",tv_sign);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                /* 将Bitmap设定到ImageView */
                ContentValues cv = new ContentValues();
                cv.put("userpic", BitmapUtil.bitmapToByte(bitmap));
                if(dbManager.updateUserInfo(username,cv)){
                    img_pic.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showThreeEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_change_password,null);
        final EditText oldPassword = (EditText)v.findViewById(R.id.et_oldpassword);
        final EditText newPassword = (EditText)v.findViewById(R.id.et_newpassword);
        final EditText newPassword2 = (EditText)v.findViewById(R.id.et_newpassword2);
        final Button btn_cancle = (Button)v.findViewById(R.id.btn_cancle);
        final Button btn_commit = (Button)v.findViewById(R.id.btn_commit);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword.getText().toString().equals(dbManager.selectUserPassword(username))){
                    if(newPassword.getText().toString().equals(newPassword2.getText().toString())){
                        if(!newPassword.getText().toString().equals(oldPassword.getText().toString())){
                            ContentValues cv = new ContentValues();
                            cv.put("password", newPassword.getText().toString());
                            if(dbManager.updateUserInfo(username,cv)){
                                Toast.makeText(context,"密码修改成功!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }else{
                            Toast.makeText(context,"新旧密码不可以相同!",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"两次密码输入不同!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"密码输入错误!",Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    private void showCalendarDialog() {
        dialog.setCommitOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put("birthday", dialog.getDay());
                if(dbManager.updateUserInfo(username,cv)){
                    Toast.makeText(context,"生日修改成功!",Toast.LENGTH_LONG).show();
                    tv_birthday.setText(dialog.getDay());
                    dialog.dismissDialog();
                }
            }
        });
        dialog.setCancleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismissDialog();
            }
        });
        dialog.showDialog();
    }

    private void showMultiChooseDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String[] s = new String[]{"男", "女"};
        builder.setTitle(title);
        builder.setMultiChoiceItems(s, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                ContentValues cv = new ContentValues();
                cv.put("gender", s[which]);
                if(dbManager.updateUserInfo(username,cv)){
                    Toast.makeText(context,"性别修改成功!",Toast.LENGTH_LONG).show();
                    tv_gender.setText(s[which]);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showOneEditDialog(String title, String msg,final TextView textview) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText et = new EditText(context);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = et.getText().toString().trim();
                ContentValues cv = new ContentValues();
                if(textview == tv_nickname){
                    cv.put("nickname", s);
                    if(dbManager.updateUserInfo(username,cv)){
                        Toast.makeText(context,"昵称修改成功!",Toast.LENGTH_LONG).show();
                    }
                }else if(textview == tv_address){
                    cv.put("address", s);
                    if(dbManager.updateUserInfo(username,cv)){
                        Toast.makeText(context,"地址修改成功!",Toast.LENGTH_LONG).show();
                    }
                }else if(textview == tv_sign){
                    cv.put("sign", s);
                    if(dbManager.updateUserInfo(username,cv)){
                        Toast.makeText(context,"个性签名修改成功!",Toast.LENGTH_LONG).show();
                    }
                }
                textview.setText(et.getText().toString().trim());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
