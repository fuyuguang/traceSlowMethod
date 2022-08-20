package com.fyg.util;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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

//    public static <T extends ClassVisitor> byte[] transformV1(byte[] bytes,Class<T> classVisitor)  {
//
//        ClassReader classReader = new ClassReader(bytes);
//        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor cv = new TraceClassVisitorV2(Opcodes.ASM5, classWriter, config, traceMethodConfig.monitoringTimeThreshold)
//        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
//        return  classWriter.toByteArray();
//    }

//        ClassReader classReader = new ClassReader(file.bytes)
//        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//        ClassVisitor cv = new TraceClassVisitorV2(ASM_VERSION, classWriter, config,traceMethodConfig.monitoringTimeThreshold)
//        classReader.accept(cv, EXPAND_FRAMES)
//        byte[] code = classWriter.toByteArray()

    static final int ASM_VERSION = Opcodes.ASM5;
    public static <T extends ClassVisitor> byte[] transformV2(byte[] bytes,ASMTransform.Factory factory)  {

        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//        ClassVisitor cv = new TraceClassVisitorV2(Opcodes.ASM5, classWriter, config, traceMethodConfig.monitoringTimeThreshold);
        ClassVisitor cv = factory.create(ASM_VERSION, classWriter);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        return  classWriter.toByteArray();
    }

    public static <T extends ClassVisitor> byte[] transform(byte[] bytes,Class<T> classVisitor)  {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(classVisitor, MethodType.methodType(void.class,int.class, ClassVisitor.class));
            T invoke = (T) constructor.invoke(ASM_VERSION, classWriter);
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


    /**
     byte[] code = ASMTransform.transform2(file.bytes,TraceClassVisitorV2.class, config,traceMethodConfig.monitoringTimeThreshold)  */
    public static <T extends ClassVisitor> byte[] transform2(byte[] bytes,Class<T> classVisitor,Object... objs)  {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        if (objs != null){
            Class<?>[] ptypes = new Class[objs.length+2];
            ptypes[0] = int.class;
            ptypes[1] = ClassVisitor.class;
            for (int i = 0; i < objs.length; i++) {
                ptypes[i+2] = objs[i].getClass();
            }


            Object[] newArrays = (Object[]) Array.newInstance(Object.class, objs.length + 2);
            newArrays[0] = Opcodes.ASM5;
            newArrays[1] = classWriter;
            System.arraycopy(objs,0,newArrays,2,objs.length);

            try {

                MethodHandle constructor = MethodHandles.lookup().findConstructor(classVisitor, MethodType.methodType(void.class,ptypes));
//                MethodHandle constructor = MethodHandles.lookup().findConstructor(classVisitor, MethodType.methodType(void.class,int.class, ClassVisitor.class, Config.class,Long.class));
//                T invoke = (T) constructor.invoke(Opcodes.ASM5, classWriter,objs);

                /**
                    该方法接收一个可变参数的类型，不能直接传数组过去，需要展开操作
                 java.lang.invoke.WrongMethodTypeException: cannot convert MethodHandle(int,ClassVisitor,Config,Long)TraceClassVisitorV2 to (Object[])ClassVisitor
                 at java.base/java.lang.invoke.MethodHandle.asTypeUncached(MethodHandle.java:861)
                 at java.base/java.lang.invoke.MethodHandle.asType(MethodHandle.java:847)
                 at java.base/java.lang.invoke.Invokers.checkGenericType(Invokers.java:495)
                 */
//                T invoke = (T) constructor.invoke(newArrays);
                T invoke = (T) constructor.invoke(newArrays);
                classReader.accept(invoke, ClassReader.EXPAND_FRAMES);
                return classWriter.toByteArray();

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }




            Integer[] test = new Integer[] {1, 2, 5};
            int[] test2 = new int[] {1, 2, 5};

            // public static <T> List<T> asList(T... a)
            List ptypes1 = Arrays.asList(ptypes);
            List newArrays1 = Arrays.asList(newArrays);
            List result1 = Arrays.asList(test);
            List result2 = Arrays.asList(test2);

            System.out.println("ptypes1:" + ptypes1);
            System.out.println("newArrays1:" + newArrays1);
            System.out.println("result1:" + result1);
            System.out.println("result1 size:" + result1.size());
            System.out.println("result2:" + result2);
            System.out.println("result2 size:" + result2.size());


        }



//        try {
//            MethodHandle constructor = MethodHandles.lookup().findConstructor(classVisitor, MethodType.methodType(void.class,int.class, ClassVisitor.class, Config.class,Long.class));
//            T invoke = (T) constructor.invoke(Opcodes.ASM5, classWriter,objs);
//            classReader.accept(invoke, ClassReader.EXPAND_FRAMES);
//            return classWriter.toByteArray();
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
        return null;
    }



    public interface Factory {
        ClassVisitor create(int ASMVersion, ClassWriter classWriter);
    }

    public static String getTag(Object obj){
        Class<?> aClass = obj.getClass();
        if (aClass.isAnonymousClass() || aClass.isLocalClass()){
            return aClass.getName();
        }
        return aClass.getSimpleName();
    }
}
