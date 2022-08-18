package com.fyg.util;


import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by fuyuguang on 2022/8/18 11:16 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class ASMTransform {

    public static <T extends ClassVisitor> byte[] transform(byte[] bytes,Class<T> classVisitorClass) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        try {
            Constructor<? extends ClassVisitor> constructor = classVisitorClass.getConstructor(int.class, ClassVisitor.class);
            constructor.setAccessible(true);
            ClassVisitor classVisitor = constructor.newInstance(Opcodes.ASM5, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public interface Factory{
        ClassVisitor create(int ASMVersion,ClassWriter classWriter);
    }
}
