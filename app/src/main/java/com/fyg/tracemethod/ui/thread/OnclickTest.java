package com.fyg.tracemethod.ui.thread;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by fuyuguang on 2022/8/23 3:40 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class OnclickTest {

//    public void onclick(View view){
//
//        view.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }


    public void onclick(View view){

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag(view.getId());
                if (tag != null && tag instanceof Long){
                    Long temTime = (Long)tag;
                    if (temTime > 300){
                        return;
                    }
                }
                view.setTag(view.getId(),System.currentTimeMillis());


                System.out.println("66666666");
            }
        });
    }
}
