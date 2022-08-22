package com.fyg.trace.slowmethod

import com.android.build.api.transform.*
import com.fyg.tracemethod.Config
import com.fyg.tracemethod.TraceSlowMethodVisitor
import com.fyg.transform.base.BaseTransform
import com.fyg.util.ASMTransform
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

/**
 * Created by fuyuguang on 2022/8/11 11:50 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceMethodTransform extends BaseTransform{
    TraceMethodConfig traceMethodConfig ;
    Config config ;

    public TraceMethodTransform(Project project) {
        super(project);
    }

    @Override
    public String getName() {
        return "TraceMethodTransform";
    }

    @Override
    public byte[] transformSrcFiles(File file) {
        return ASMTransform.transformV2(file.bytes,new ASMTransform.Factory() {
            @Override
            ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
                return new TraceSlowMethodVisitor(ASMVersion,classWriter,config,traceMethodConfig.monitoringTimeThreshold)
            }
        })
    }

    @Override
    public byte[] transformJarFiles(byte[] bytes) {
        return ASMTransform.transformV2(bytes,new ASMTransform.Factory() {
            @Override
            ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
                return new TraceSlowMethodVisitor(ASMVersion, classWriter, config,traceMethodConfig.monitoringTimeThreshold)
            }
        });
    }


    @Override
    void init(Project project,TransformInvocation transformInvocation) {
        traceMethodConfig = project.traceMethod;
        String output = traceMethodConfig.output
        if (output == null || output.isEmpty()) {
            traceMethodConfig.output = project.getBuildDir().getAbsolutePath() + File.separator + "traceman_output"
        }
        if (traceMethodConfig.open){
            config = initConfig();
            config.parseTraceConfigFile()
        }
    }


    public Config initConfig() {
        Config config = new Config()
        config.MTraceConfigFile = traceMethodConfig.traceConfigFile
        config.MIsNeedLogTraceInfo = traceMethodConfig.logTraceInfo
        return config
    }

}


