package com.fyg.tracethread

import com.android.build.gradle.AppExtension
import com.fyg.tracemethod.TraceMethodConfig
import com.fyg.tracemethod.TraceMethodTransform
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
 * 统计方法运行时间，耗时方法大于阀值用mmap存到本地，用workmanager 定时上报，间接统计anr？
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