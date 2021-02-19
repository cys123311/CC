package com.sprout.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class BitmapUtils {


    /**
     * 图片文件的二次采用
     * @param path
     * @param outW
     * @param outH
     * @return
     */
    public static Bitmap getScaleBitmap(String path,int outW,int outH) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            throw new IOException("图片不存在");
        }
        // 设置采用的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置只读图片的边框=宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        //原始图片的宽高
        int width = options.outWidth;
        int height = options.outHeight;
        //图片的缩放比例
        int scale = 1;
        while(width/scale > outW || height/scale > outH){
            scale *= 2;
        }
        //设置读取图片内容的参数
        options.inJustDecodeBounds = false;
        //设置图片读取的缩放比例
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path,options);
    }

    /**
     * bitmap 转 bytes
     * @param bitmap
     * @return
     */
    public static byte[] getBytesByBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray(); // 转为byte数组
        return data;
        //byte 转 String
        //val path = Base64.encodeToString(data, Base64.DEFAULT)
    }

}
