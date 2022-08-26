package com.fyg.transform.base

import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.gradle.internal.pipeline.TransformManager
import com.fyg.tracemethod.Config
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
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
    public Project project;
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

    public abstract byte[] transformSrcFiles(File file) throws FileNotFoundException;
    public void traceSrcFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (Config.isNeedTraceClass(name)) {
                    byte[] code = transformSrcFiles(file);

//                    byte[] code = ASMTransform.transformV2(file.bytes, new ASMTransform.Factory() {
//                        @Override
//                        ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
//                            return new TraceClassVisitorV2(ASMVersion,classWriter,config,traceMethodConfig.monitoringTimeThreshold)
//                        }
//                    })

                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }

        //处理完输出给下一任务作为输入
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }



   public abstract byte[] transformJarFiles(byte[] bytes);
    void traceJarFiles(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            //重命名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())

            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()

            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))

            //循环jar包里的文件
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                if (Config.isNeedTraceClass(entryName)) {
                    jarOutputStream.putNextEntry(zipEntry)
//                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
//                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//                    ClassVisitor cv = new TraceClassVisitorV2(ASM_VERSION, classWriter, config,traceMethodConfig.monitoringTimeThreshold)
//                    classReader.accept(cv, EXPAND_FRAMES)
//                    byte[] code = classWriter.toByteArray()


//                    byte[] code = ASMTransform.transformV2(IOUtils.toByteArray(inputStream),new ASMTransform.Factory() {
//                        @Override
//                        ClassVisitor create(int ASMVersion, ClassWriter classWriter) {
//                            return new TraceClassVisitorV2(ASM_VERSION, classWriter, config,traceMethodConfig.monitoringTimeThreshold)
//                        }
//                    });

                    tranbyte[] code = sformJarFiles(IOUtils.toByteArray(inputStream))

                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            jarFile.close()

            //处理完输出给下一任务作为输入
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)

            tmpFile.delete()
        }
    }

    abstract void init(Project project,TransformInvocation transformInvocation)

}
