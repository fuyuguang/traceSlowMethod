package com.fyg.trace.threadfinish;

import com.fyg.util.FilterUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fuyuguang on 2022/8/18 10:51 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceThreadVisitorV2 extends ClassVisitor {

    private String currentVisitClassName;
    private String currentMethodName;
    TraceThreadFinishConfig configuration;
    private static AtomicInteger THREAD_ID = new AtomicInteger(0);

    public TraceThreadVisitorV2(int i, ClassVisitor classVisitor,TraceThreadFinishConfig configuration) {
        super(i, classVisitor);
        this.configuration = configuration;
    }



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.currentVisitClassName = name;

    }

    @Override
    public void visitInnerClass(final String s, final String s1, final String s2, final int i) {
        super.visitInnerClass(s, s1, s2, i);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        if (FilterUtil.hasOpcodesWithOr(access,Opcodes.ACC_ABSTRACT,Opcodes.ACC_NATIVE)){
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        return new TraceMethodAdapter(api, methodVisitor, access, currentMethodName = name, desc, this.currentVisitClassName);
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }


    public  class TraceMethodAdapter extends AdviceAdapter {


        private boolean find = false;



        protected TraceMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className) {
            super(api, mv, access, name, desc);

        }


        @Override
        public void visitTypeInsn(int opcode, String s) {

            if (opcode == Opcodes.NEW && configuration.getTargetClassName().equals(s)) {
                find = true;
                mv.visitTypeInsn(Opcodes.NEW, configuration.getReplaceClassName());
                return;
            }
            super.visitTypeInsn(opcode, s);

        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

            if (configuration.getTargetClassName().equals(owner) && !currentVisitClassName.equals(configuration.getReplaceClassName()) &&
                    opcode == Opcodes.INVOKESPECIAL && find) {

                if (name.equals(configuration.getTargetMethodName()) && configuration.getTargetMethodDesc().equals(desc)) {
                    mv.visitLdcInsn(currentVisitClassName+"."+currentMethodName+".thread"+THREAD_ID.getAndIncrement());
                    mv.visitMethodInsn(opcode, configuration.getReplaceClassName(), configuration.getReplaceMethodName(), configuration.getReplaceMethodDesc(), itf);
                    return;
                }

                find = false;
                mv.visitMethodInsn(opcode, configuration.getReplaceClassName(), name, desc, itf);
                return;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

        @Override
        protected void onMethodEnter() {

        }

        @Override
        protected void onMethodExit(int opcode) {

        }
    }


}
