package com.fyg.tracemethod.ui

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.fyg.monitor.tracemethod.PrintTime
import com.fyg.tracemethod.R
import com.fyg.tracemethod.ui.test.TestSlowMethodActivity

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


    fun mBtnSleep100(v: View){
        Thread.sleep(100)
    }
    fun mGogoTestSlowMethodActivity(v: View){
        startActivity(Intent(this@SlowMethodActivity,TestSlowMethodActivity::class.java))
    }


    fun mGetImei(v: View){
        getIMEI()
    }




    fun exec() {
        Thread {
        Thread.sleep(1300)
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