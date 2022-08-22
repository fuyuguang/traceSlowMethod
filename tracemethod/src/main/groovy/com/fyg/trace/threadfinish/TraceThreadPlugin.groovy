package com.fyg.trace.threadfinish

import com.android.build.gradle.AppExtension
import com.fyg.trace.slowmethod.TraceMethodConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by fuyuguang on 2022/8/11 4:53 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * 思路：
 *
 */
class TraceThreadPlugin implements Plugin<Project>{
    @Override
    public void apply(Project project) {
        project.getExtensions().create("traceThread", TraceMethodConfig.class);
        println '*****************-------- TraceThreadPlugin  plugin apply  --------*********************'
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TraceThreadTransform(project));
    }
}