package com.fyg.monitor.tracemethod;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by fuyuguang on 2022/6/6 6:14 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */

@Target({METHOD}) @Retention(CLASS)
public @interface DebouncingOnClickListener {
}