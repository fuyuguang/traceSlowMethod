package com.fyg.util;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import org.objectweb.asm.Opcodes;

/**
 * Created by fuyuguang on 2022/8/19 5:23 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class FilterUtil {


    public static boolean isConstructor(String name){
        return "<init>".equals(name);
    }

    public static  boolean isClassStaticInit(String name){
        return "<clinit>".equals(name);
    }

    public static boolean isAbstractMethod(int access){
        return (access & ACC_ABSTRACT) != 0;//1
    }

    public static  boolean isNativeMethod(int access){
        return (access & ACC_NATIVE) != 0;
    }


    /** invokevirtual | invokestatic | invokeinterface |invokedynamic */
    public static  boolean isGeneralMethod(int access,String name){
        return !isConstructor(name) &&  !isClassStaticInit(name) && !isAbstractMethod(access)  && !isNativeMethod(access);
    }


    public static boolean isMethodEnd(int opcode){
        return (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW);
    }


}
