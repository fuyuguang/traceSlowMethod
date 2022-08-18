package com.fyg.tracethread

import com.android.build.api.transform.*
import com.fyg.tracemethod.BaseTransform
import com.fyg.tracemethod.Config
import com.fyg.tracemethod.TraceMethodConfig
import com.fyg.util.ASMTransform
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES
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

    int ASM_VERSION = Opcodes.ASM5;
    @Override
    public void traceSrcFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (config.isNeedTraceClass(name)) {

                    println '*****************-------- TraceThreadPlugin  traceSrcFiles --------*********************'
                    byte[] code = ASMTransform.transform(file.bytes,TraceThreadVisitor.class)
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

    @Override
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
                if (config.isNeedTraceClass(entryName)) {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(ASMTransform.transform(IOUtils.toByteArray(inputStream),TraceThreadVisitor.class))
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

}


