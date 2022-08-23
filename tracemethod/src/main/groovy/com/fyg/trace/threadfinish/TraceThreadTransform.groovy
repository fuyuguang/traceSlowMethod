package com.fyg.trace.threadfinish


import com.android.build.api.transform.TransformInvocation
import com.fyg.tracemethod.Config
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
public class TraceThreadTransform extends BaseTransform{

    Config config ;
    TraceThreadFinishConfig configuration
    public TraceThreadTransform(Project project) {
        super(project);
    }

    @Override
    public String getName() {
        return "TraceThreadTransform";
    }

    @Override
    void init(Project project,TransformInvocation transformInvocation) {


        configuration = project.traceThread;
//        String output = traceMethodConfig.output
//        if (output == null || output.isEmpty()) {
//            traceMethodConfig.output = project.getBuildDir().getAbsolutePath() + File.separator + "tracethread_output"
//        }
        if (configuration.open){
            config = initConfig();
//            config.parseTraceConfigFile()
        }
    }


    public Config initConfig() {

        println '*****************-------- TraceThreadPlugin  initConfig() --------*********************'
        configuration = project.traceThread
        configuration.targetClassName.isBlank()
        if (!configuration.targetClassName){
            configuration.targetClassName = com.fyg.util.Constant.InternalName.Thread_INTERNAL_NAME
        }

        /**  if (str== null || str.equals( "" ))  */
        if (!configuration.replaceClassName){ 
            configuration.replaceClassName = "com/fyg/tracemethod/ui/thread/CustomThread"
        }

        Config config = new Config()
//        config.MTraceConfigFile = configuration.traceConfigFile
//        config.MIsNeedLogTraceInfo = configuration.logTraceInfo
        return config
    }



    @Override
    byte[] transformSrcFiles(File file) {
        return ASMTransform.transformV2(file.bytes,new ASMTransform.Factory() {
            @Override
            ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
                return new TraceThreadVisitorV2(ASMVersion,classWriter,configuration);
            }
        })
    }

    @Override
    byte[] transformJarFiles(byte[] bytes) {
        return ASMTransform.transformV2(bytes,new ASMTransform.Factory() {
            @Override
            ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
                return new TraceThreadVisitorV2(ASMVersion,classWriter,configuration);
            }
        })
    }
}


