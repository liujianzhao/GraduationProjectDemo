package com.example.graduationproject.util;

import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.example.graduationproject.R;

import java.text.SimpleDateFormat;

/**
 * Created by liuji on 2017/5/11.
 */

public class CalendarDialog extends Dialog{

    private static CalendarDialog dialog;
    private static DatePicker datePicker;
    private static Button btn_cancle,btn_commit;

    public static CalendarDialog getInstance(Context context) {
        dialog = new CalendarDialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        btn_cancle = (Button)v.findViewById(R.id.btn_cancle);
        btn_commit = (Button)v.findViewById(R.id.btn_commit);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.setContentView(v);
        return dialog;
    }

    public void setCommitOnClickListener(View.OnClickListener onClickListener){
        btn_commit.setOnClickListener(onClickListener);
    }

    public void setCancleOnClickListener(View.OnClickListener onClickListener){
        btn_cancle.setOnClickListener(onClickListener);
    }

    public String getDay(){
        StringBuffer sb = new StringBuffer();
        sb.append(datePicker.getYear()+"-");
        int month = datePicker.getMonth()+1;
        if(month<10){
            sb.append("0"+month+"-");
        }else{
            sb.append(month+"-");
        }
        if(datePicker.getDayOfMonth()<10){
            sb.append("0"+datePicker.getDayOfMonth());
        }else{
            sb.append(datePicker.getDayOfMonth());
        }
        return sb.toString();
    }

    private CalendarDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private CalendarDialog(Context context) {
        super(context);
    }

    public void showDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
