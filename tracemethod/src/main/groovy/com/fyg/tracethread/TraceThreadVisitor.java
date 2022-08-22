package com.fyg.tracethread;

import static com.fyg.util.Constant.InternalName.Thread_INTERNAL_NAME;

import com.fyg.util.Constant;
import com.fyg.util.Constant.MethodDesc;
import com.fyg.util.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by fuyuguang on 2022/8/18 10:51 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceThreadVisitor extends ClassVisitor {

    private static final String CustomThreadInternalName = "com/fyg/tracemethod/ui/thread/CustomThread";
    private static final String IMEIClassName = "com/fyg/tracemethod/ui/thread/IMEI";
    private String className;


    public TraceThreadVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;

    }

    @Override
    public void visitInnerClass(final String s, final String s1, final String s2, final int i) {
        super.visitInnerClass(s, s1, s2, i);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        return new TraceMethodAdapter(api, methodVisitor, access, name, desc, this.className);
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }


    public static class TraceMethodAdapter extends AdviceAdapter {

        private final String methodName;
        private final String className;
        private boolean find = false;


        protected TraceMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className) {
            super(api, mv, access, name, desc);
            this.className = className;
            this.methodName = name;
        }


        @Override
        public void visitTypeInsn(int opcode, String s) {

            if (opcode == Opcodes.NEW && Thread_INTERNAL_NAME.equals(s)) {

                find = true;
                mv.visitTypeInsn(Opcodes.NEW, CustomThreadInternalName);
                return;
            }
            super.visitTypeInsn(opcode, s);

        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

            Log.e(Constant.TAG.TAG, "opcode :%s  ,owner : %s  , className  : %s , method : %s, name :  %s ,  desc:%s",opcode,owner, className, methodName, name, desc);

            if (Thread_INTERNAL_NAME.equals(owner) && !className.equals(CustomThreadInternalName) && opcode == Opcodes.INVOKESPECIAL && find) {
                find = false;
                mv.visitMethodInsn(opcode, CustomThreadInternalName, name, desc, itf);
//                Log.e(Constant.TAG.TAG, "className : %s,  method : %s, name : %s", className, methodName, name);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);


//            if (owner.equals(TelephonyManager_INTERNAL_NAME) && name.equals("getDeviceId") && desc.equals(MethodDesc.P_Ls_R_v)) {
////                Log.e(Constant.TAG.TAG, "get imei className:%s, method:%s, name:%s,  desc:%s", className, methodName, name, desc);
////                super.visitMethodInsn();
//            }



        }

        private int timeLocalIndex = 0;

        @Override
        protected void onMethodEnter() {
//            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//            timeLocalIndex = newLocal(Type.LONG_TYPE); //这个是LocalVariablesSorter 提供的功能，可以尽量复用以前的局部变量
//            mv.visitVarInsn(LSTORE, timeLocalIndex);
        }

        @Override
        protected void onMethodExit(int opcode) {

//            System.out.println(" fyguu : onMethodExit   start ");
////            Log.e("thread1",Type.getType("java/lang/StringBuilder").toString());
//
//
//
//            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//            mv.visitVarInsn(LLOAD, timeLocalIndex);
//            mv.visitInsn(LSUB);//此处的值在栈顶
//            mv.visitVarInsn(LSTORE, timeLocalIndex);//因为后面要用到这个值所以先将其保存到本地变量表中
//
//
//            System.out.println(" fyguu : onMethodExit   end 1");
//            int stringBuilderIndex = newLocal(Type.getType("Ljava/lang/StringBuilder"));
//            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
//            System.out.println(" fyguu : onMethodExit   end 2");
//            mv.visitInsn(Opcodes.DUP);
//            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//            System.out.println(" fyguu : onMethodExit   end 3");
//            mv.visitVarInsn(Opcodes.ASTORE, stringBuilderIndex);//需要将栈顶的 stringbuilder 保存起来否则后面找不到了
//            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
//            mv.visitLdcInsn(className + "." + methodName + " time:");
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//            System.out.println(" fyguu : onMethodExit   end 4");
//            mv.visitInsn(Opcodes.POP);
//            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
//            mv.visitVarInsn(Opcodes.LLOAD, timeLocalIndex);
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
//            System.out.println(" fyguu : onMethodExit   end 5");
//            mv.visitInsn(Opcodes.POP);
//            mv.visitLdcInsn("fyg thread");
//            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
//            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//            System.out.println(" fyguu : onMethodExit   end 6");
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);//注意： Log.d 方法是有返回值的，需要 pop 出去
//            System.out.println(" fyguu : onMethodExit   end 7");
//            mv.visitInsn(Opcodes.POP);//插入字节码后要保证栈的清洁，不影响原来的逻辑，否则就会产生异常，也会对其他框架处理字节码造成影响

        }
    }


}
