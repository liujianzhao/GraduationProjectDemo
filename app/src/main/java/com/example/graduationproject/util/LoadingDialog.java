package com.example.graduationproject.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.graduationproject.R;

/**
 * Created by liuji on 2017/5/9.
 */

public class LoadingDialog extends Dialog {

    private static LoadingDialog dialog;
    private static ImageView img_loading;

    public static LoadingDialog getInstance(Context context) {
        dialog = new LoadingDialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        img_loading = (ImageView) v.findViewById(R.id.img_loading);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(false);
        dialog.setContentView(v);
        return dialog;
    }

    private LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private LoadingDialog(Context context) {
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        img_loading.setImageResource(R.drawable.waitting);
        AnimationDrawable animationDrawable = (AnimationDrawable) img_loading.getDrawable();
        animationDrawable.start();
    }
}
