package com.fyg.monitor.tracemethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created by fuyuguang on 2022/8/20 5:19 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 java 库如何引入 另一个java库？
 java 库不能引入android lib 库？


 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface PrintTime {
}
