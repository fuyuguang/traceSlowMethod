package com.fyg.util;


import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import com.fyg.tracemethod.TraceClassVisitorV2;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

    public static <T extends ClassVisitor> byte[] transform(byte[] bytes,Class<T> classVisitor)  {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(classVisitor, MethodType.methodType(void.class,int.class, ClassVisitor.class));
            T invoke = (T) constructor.invoke(Opcodes.ASM5, classWriter);
            classReader.accept(invoke, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }



    public interface Factory{
        ClassVisitor create(int ASMVersion,ClassWriter classWriter);
    }





    public static String getTag(Object obj){
        Class<?> aClass = obj.getClass();
        if (aClass.isAnonymousClass() || aClass.isLocalClass()){
            return aClass.getName();
        }
        return aClass.getSimpleName();
    }
}
