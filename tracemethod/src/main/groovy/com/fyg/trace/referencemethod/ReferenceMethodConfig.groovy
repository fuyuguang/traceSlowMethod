package com.fyg.trace.referencemethod;

/**
 * Created by fuyuguang on 2022/8/11 11:04 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class ReferenceMethodConfig {

    boolean open;
    /**
     不能用final 修饰
     不能用private  修饰
     */
      String targetClassName;
      String targetMethodName;
      String targetMethodDesc;


    String replaceClassName;
    String replaceMethodName;
    String replaceMethodDesc;

    ReferenceMethodConfig() {
        open = true;
        targetClassName = ""
        targetMethodName = ""
        targetMethodDesc = ""
    }
}
