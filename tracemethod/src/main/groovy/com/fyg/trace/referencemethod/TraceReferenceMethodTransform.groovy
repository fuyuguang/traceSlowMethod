package com.fyg.trace.referencemethod

import com.android.build.api.transform.TransformInvocation
import com.fyg.trace.click.DebouncingClickVisitor
import com.fyg.transform.base.BaseTransform
import com.fyg.util.ASMTransform
import com.fyg.util.Log
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
/**
 * Created by fuyuguang on 2022/8/22 11:48 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceReferenceMethodTransform extends BaseTransform {
    ReferenceMethodConfig configuration;

    public TraceReferenceMethodTransform(Project project) {
        super(project);
    }

    @Override
    public String getName() {
        return "TraceReferenceMethodTransform";
    }



    @Override
    public byte[] transformSrcFiles(File file) throws FileNotFoundException {

         return ASMTransform.transformV2(file.bytes,new ASMTransform.Factory() {
             @Override
             ClassVisitor create(int ASMVersion, ClassWriter classWriter) {

                 DebouncingClickVisitor debouncingClickVisitor = new DebouncingClickVisitor(ASMVersion,classWriter);
                 return new TraceReferenceMethodVisitor(ASMVersion,debouncingClickVisitor,configuration.targetClassName,configuration.targetMethodName,configuration.targetMethodDesc,configuration)

//                 return new TraceReferenceMethodVisitor(ASMVersion,classWriter,configuration.targetClassName,configuration.targetMethodName,configuration.targetMethodDesc,configuration)
             }
         });
    }

    @Override
    public byte[] transformJarFiles(byte[] bytes) {
        return   ASMTransform.transformV2(bytes,new ASMTransform.Factory() {
            @Override
            ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
                DebouncingClickVisitor debouncingClickVisitor = new DebouncingClickVisitor(ASMVersion,classWriter);
                return new TraceReferenceMethodVisitor(ASMVersion,debouncingClickVisitor,configuration.targetClassName,configuration.targetMethodName,configuration.targetMethodDesc,configuration)

//                return new TraceReferenceMethodVisitor(ASMVersion,classWriter,configuration.targetClassName,configuration.targetMethodName,configuration.targetMethodDesc,configuration)
            }
        })
    }

    @Override
    public void init(Project project, TransformInvocation transformInvocation) {
        initConfig(project);
    }


    public void initConfig(Project project) {

        println '*****************-------- TraceReferenceMethodTransform  initConfig() --------*********************'
        configuration = project.referenceMethodConfig
        String targetClassName = configuration.targetClassName;
        String targetMethodName = configuration.targetMethodName;
        String targetMethodDesc = configuration.targetMethodDesc;

        Log.e(this,"%s  , %s , %s ",targetClassName,targetMethodName,targetMethodDesc)
        Log.e(this,"%s  , %s , %s ",configuration.replaceClassName,configuration.replaceMethodName,configuration.replaceMethodDesc)

    }
}
