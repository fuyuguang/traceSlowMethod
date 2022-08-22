package com.fyg.tracemethod;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;

import com.fyg.util.FilterUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by fuyuguang on 2022/8/10 6:04 PM.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 []()
 []()

 添加时间阀值

 */
public class TraceSlowMethodVisitorV1 extends ClassVisitor {
    private String ownerName;

    public TraceSlowMethodVisitorV1(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        ownerName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && FilterUtil.isGeneralMethod(access,name) ) {
            mv = new MethodTimerAdapter4(api, mv, access, name, descriptor,ownerName);
        }
        return mv;
    }

    private static class MethodTimerAdapter4 extends AdviceAdapter {
        private  String mOwnerName;
        private int slotIndex;

        public MethodTimerAdapter4(int api, MethodVisitor mv, int access, String name, String descriptor,String ownerName) {
            super(api, mv, access, name, descriptor);
            this.mOwnerName = ownerName;
        }

        @Override
        protected void onMethodEnter() {

            slotIndex = newLocal(Type.LONG_TYPE);
            /**
             invokeStatic(Type.getType("Ljava/lang/System;"), new Method("currentTimeMillis", "()J")); */
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            /**
             storeLocal(slotIndex); */
            mv.visitVarInsn(LSTORE, slotIndex);

        }

        @Override
        protected void onMethodExit(int opcode) {
            if (FilterUtil.isMethodEnd(opcode)) {
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                mv.visitVarInsn(LLOAD, slotIndex);
                mv.visitInsn(LSUB);
                mv.visitVarInsn(LSTORE, slotIndex);

                //new
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
                mv.visitMethodInsn(INVOKESTATIC, "android/os/Looper", "getMainLooper", "()Landroid/os/Looper;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "android/os/Looper", "getThread", "()Ljava/lang/Thread;", false);
                Label label0 = new Label();
                mv.visitJumpInsn(IF_ACMPNE, label0);
                mv.visitVarInsn(LLOAD, slotIndex);
                mv.visitLdcInsn(new Long(100L));
                mv.visitInsn(LCMP);
                mv.visitJumpInsn(IFLE, label0);
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitLdcInsn(mOwnerName+"."+this.getName() + this.methodDesc+"  differ : ");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitVarInsn(LLOAD, slotIndex);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                mv.visitLabel(label0);

            }
        }
    }
}
