package com.fyg.trace.click;

import com.fyg.util.FilterUtil;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
/**
 * Created by fuyuguang on 2022/8/23 5:07 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    
 */
public class DebouncingClickVisitor extends ClassVisitor {


    public DebouncingClickVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null) {
            if (FilterUtil.isGeneralMethod(access,name)
//                    && "onClick".equals(name) &&
//                    descriptor.equals("(Landroid/view/View;)V")
            ) {
                mv = new DebouncingClickAdapter(api, mv, access, name, descriptor,200);
            }
        }
        return mv;
    }



    private class DebouncingClickAdapter extends AdviceAdapter {
        private long customTime;
        private int slotIndex_3;
        private int slotIndex_2;
        private boolean enableDebouncing;

        public DebouncingClickAdapter(int api, MethodVisitor mv, int access, String name, String descriptor, long time) {
            super(api, mv, access, name, descriptor);
            customTime = time;
        }


        @Override
        public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
        }

        @Override
        protected void onMethodEnter() {

            if (!enableDebouncing){
                return;
            }

            slotIndex_2 = newLocal(Type.getType(Object.class));
            slotIndex_3 = newLocal(Type.getType(Object.class));

            MethodVisitor methodVisitor = mv;
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getId", "()I", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getTag", "(I)Ljava/lang/Object;", false);
            methodVisitor.visitVarInsn(ASTORE, slotIndex_2);
            methodVisitor.visitVarInsn(ALOAD, slotIndex_2);
            Label label0 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label0);
            methodVisitor.visitVarInsn(ALOAD, slotIndex_2);
            methodVisitor.visitTypeInsn(INSTANCEOF, "java/lang/Long");
            methodVisitor.visitJumpInsn(IFEQ, label0);
            methodVisitor.visitVarInsn(ALOAD, slotIndex_2);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Long");
            methodVisitor.visitVarInsn(ASTORE, slotIndex_3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            methodVisitor.visitVarInsn(ALOAD, slotIndex_3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
            methodVisitor.visitInsn(LSUB);
            methodVisitor.visitLdcInsn(new Long(300L));
            methodVisitor.visitInsn(LCMP);
            methodVisitor.visitJumpInsn(IFGE, label0);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "getId", "()I", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/view/View", "setTag", "(ILjava/lang/Object;)V", false);

        }

        @Override
        protected void onMethodExit(int opcode) {
            if (!enableDebouncing){
                return;
            }
        }


        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {

            enableDebouncing = "Lcom/fyg/monitor/tracemethod/DebouncingOnClickListener;".equals(descriptor);
            return super.visitAnnotation(descriptor, visible);

        }
    }
}
