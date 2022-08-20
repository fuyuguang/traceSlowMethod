package com.fyg.tracemethod.ui.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.fyg.tracemethod.R
import com.fyg.tracemethod.InstrumentationMethodActivity

/**
 * Created by fuyuguang on 2022/8/12 4:40 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 *
 * 该类不会被统计到，因为 tracemethodconfig.txt 中 指定了该包，不被统计
 * 注意包名要改为  com/fyg/tracemethod/ui/test
 */
class TestSlowMethodActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_slow_method)
    }


    fun mBtnSleep200(v: View){

        Thread.sleep(200)
    }

    fun mBtnSleep50(v: View){
        Thread.sleep(50)
    }

    fun mBtnSleep100(v: View){
        Thread.sleep(100)
    }

    fun mGogoTestSlowMethodActivity(v: View){
        startActivity(Intent(this@TestSlowMethodActivity, InstrumentationMethodActivity::class.java))
    }
}