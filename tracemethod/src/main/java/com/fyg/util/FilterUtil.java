package com.fyg.util;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
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

    /**
     * 众多opcode集合中，有一个没有，结果为false  ，
     * @param access
     * @param args
     * @return
     */
    public static boolean hasOpcodesWithAnd(int access, int... args){
        if (args == null){
            return false;
        }
        for (int item : args){
            if ((access & item) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 众多opcode集合中，只要命中一个，结果为true
     * @param access
     * @param args
     * @return
     */
    public static boolean hasOpcodesWithOr(int access, int... args){
        if (args == null){
            return false;
        }

        for (int item : args){
            if ((access & item) != 0){
                return true;
            }else{

            }
        }
        return false;
    }

    public static boolean isAbstract(int access){
        return (access & ACC_ABSTRACT) != 0;//1
    }

    public static boolean isInterface(int access){
        return (access & ACC_INTERFACE) != 0;//1
    }


    public static  boolean isNativeMethod(int access){
        return (access & ACC_NATIVE) != 0;
    }


    /** invokevirtual | invokestatic | invokeinterface |invokedynamic */
    public static  boolean isGeneralMethod(int access,String name){
        return !isConstructor(name) &&  !isClassStaticInit(name) && !isAbstract(access)  && !isNativeMethod(access);
    }


    public static boolean isMethodEnd(int opcode){
        return (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW);
    }


}
