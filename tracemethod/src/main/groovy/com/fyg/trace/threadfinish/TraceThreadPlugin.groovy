package com.fyg.trace.threadfinish

import com.android.build.gradle.AppExtension
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
//        project.getExtensions().create('traceThread', TraceThreadFinishConfig.class);
        def matrix = project.getExtensions().create('traceThread', TraceThreadFinishConfig.class);
//        project.tr
//        project.traceThread.getExtensions().create('nestExt', NestExt.class);
//        project.traceThread.getExtensions().create('nestExt', NestExt, project);
        matrix.getExtensions().create('nestExt', NestExt.class);

//        String traceConfigFile;
        println '*****************-------- TraceThreadPlugin  plugin apply  --------*********************'
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TraceThreadTransform(project));
    }
}