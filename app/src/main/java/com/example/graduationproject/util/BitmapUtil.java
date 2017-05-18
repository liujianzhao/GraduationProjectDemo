package com.example.graduationproject.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by liuji on 2017/5/18.
 */

public class BitmapUtil {

    public static byte[] bitmapToByte(Bitmap bitmap) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] in){
        Bitmap bitmap = BitmapFactory.decodeByteArray(in,0,in.length);
        return bitmap;
    }

}
