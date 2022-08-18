package com.fyg.tracemethod.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

/**
 * Created by fuyuguang on 2022/8/16 3:31 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class HelloWorld {


    public static void main(String[] args){

        exec();

    }

    public static void exec(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(11111);
            }
        }).start();

        h1();
    }


    public static void h1(){
        h2();
    }

    public  static void h2(){
        h3();
    }

    public static void   h3(){
        System.out.println(33333);
    }


}
