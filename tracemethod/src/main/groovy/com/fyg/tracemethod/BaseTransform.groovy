package com.fyg.tracemethod

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
/**
 * Created by fuyuguang on 2022/8/11 11:07 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public abstract class BaseTransform extends Transform {
    protected Project project;
    public BaseTransform(Project project){
        this.project = project;
    }

    @Override
    public abstract String getName();

    @Override
    public Set<ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
//        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);


        init(project,transformInvocation);

        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                traceSrcFiles(directoryInput, outputProvider)
            }

            input.jarInputs.each { JarInput jarInput ->
                traceJarFiles(jarInput, outputProvider)
            }
        }
    }

    abstract void traceSrcFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider)
    abstract void traceJarFiles(JarInput jarInput, TransformOutputProvider outputProvider)

    abstract void init(Project project,TransformInvocation transformInvocation)

//     void traceSrcFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider, Project project){}
//     void traceJarFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider, Project project){}

}
