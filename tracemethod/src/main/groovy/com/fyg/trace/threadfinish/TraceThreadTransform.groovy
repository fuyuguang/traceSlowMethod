package com.fyg.trace.threadfinish

import com.android.build.api.transform.*
import com.fyg.transform.base.BaseTransform
import com.fyg.tracemethod.Config
import com.fyg.trace.slowmethod.TraceMethodConfig
import com.fyg.util.ASMTransform
import org.gradle.api.Project

/**
 * Created by fuyuguang on 2022/8/11 11:50 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceThreadTransform extends BaseTransform{
    TraceMethodConfig traceMethodConfig ;
    Config config ;

    public TraceThreadTransform(Project project) {
        super(project);
    }

    @Override
    public String getName() {
        return "TraceThreadTransform";
    }

    @Override
    void init(Project project,TransformInvocation transformInvocation) {


        traceMethodConfig = project.traceThread;
        String output = traceMethodConfig.output
        if (output == null || output.isEmpty()) {
            traceMethodConfig.output = project.getBuildDir().getAbsolutePath() + File.separator + "tracethread_output"
        }
        if (traceMethodConfig.open){
            config = initConfig();
            config.parseTraceConfigFile()
        }
    }


    public Config initConfig() {

        println '*****************-------- TraceThreadPlugin  initConfig() --------*********************'
        TraceMethodConfig configuration = project.traceMethod
        Config config = new Config()
        config.MTraceConfigFile = configuration.traceConfigFile
        config.MIsNeedLogTraceInfo = configuration.logTraceInfo
        return config
    }



    @Override
    byte[] transformSrcFiles(File file) {
        return ASMTransform.transform(file.bytes,TraceThreadVisitor.class)
    }

    @Override
    byte[] transformJarFiles(byte[] bytes) {
        return ASMTransform.transform(bytes,TraceThreadVisitor.class)
    }
}


