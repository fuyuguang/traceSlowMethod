package com.fyg.tracemethod.ui.thread;

/**
 * Created by fuyuguang on 2022/8/23 9:28 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class Test {
    public static void main(String[] args) {

    }

    public void startThread(){

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },"22222").start();



//        new CustomThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },"22222").start();
    }


}
