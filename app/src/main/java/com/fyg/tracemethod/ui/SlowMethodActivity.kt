package com.fyg.tracemethod.ui

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.fyg.monitor.tracemethod.DebouncingOnClickListener
import com.fyg.monitor.tracemethod.PrintTime
import com.fyg.tracemethod.R
import com.fyg.tracemethod.ui.HelloWorld.exec
import com.fyg.tracemethod.ui.test.TestSlowMethodActivity
import com.fyg.tracemethod.ui.thread.Log
import com.fyg.tracemethod.ui.thread.ReferenceMethodManager

/**
 * Created by fuyuguang on 2022/8/12 4:40 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 *
 * 该类会被统计到，因为 tracemethodconfig.txt 中 指定了该包，
 * 注意包名要改为  com/fyg/tracemethod/ui
 */
class SlowMethodActivity : FragmentActivity() {
    var clickCount = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slow_method)
    }

    @PrintTime
    fun mBtnSleep200(v: View){

        Thread.sleep(200)
    }

    fun mBtnSleep50(v: View){
        Thread.sleep(50)
        exec()
    }

    /**
     v: View,必须作为第一个参数，不然会报错，
     该方法也必须是非静态方法
     */
//    @DebouncingOnClickListener
    fun onClick(str : String,v: View,v2: View){

        Log.e("fyg_click","单击了 ${(++clickCount)} 次");

    }
    fun ClickRepeatedly(v: View){

        onClick("",v,View(this));

    }


    fun mBtnSleep100(v: View){
        Thread.sleep(100)
    }
    fun mGogoTestSlowMethodActivity(v: View){
        startActivity(Intent(this@SlowMethodActivity,TestSlowMethodActivity::class.java))
    }


    fun mGetImei(v: View){
        getIMEI()
    }

    fun mWhile_true(v: View){

        Thread{
            while (true){
                Thread.sleep(4000)
                ReferenceMethodManager.println()
            }

        }.start()
    }



    fun exec() {
        Thread {
        Thread.sleep(3300)
            println(11111) }.start()
    }


    fun getIMEI(): String? {
        val telManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                permission.READ_PHONE_STATE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return ""
//        }

        if (telManager == null)
            return "null"
        else
            return  telManager.deviceId
    }
}