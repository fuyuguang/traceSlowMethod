package com.fyg.trace.referencemethod;

import com.fyg.util.FilterUtil;
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
public class TraceReferenceMethodVisitor extends ClassVisitor {

    private final String targetClassName;
    private final String targetMethodName;
    private final String targetMethodDesc;
    private final ReferenceMethodConfig configuration;
    private String currentClassName;


    public TraceReferenceMethodVisitor(int api, ClassVisitor classVisitor,String targetClassName,String targetMethodName,String targetMethodDesc,ReferenceMethodConfig configuration) {
        super(api, classVisitor);
        this.targetClassName = targetClassName;
        this.targetMethodName = targetMethodName;
        this.targetMethodDesc = targetMethodDesc;
        this.configuration = configuration;
    }



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.currentClassName = name;
    }

    @Override
    public void visitInnerClass(final String s, final String s1, final String s2, final int i) {
        super.visitInnerClass(s, s1, s2, i);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (FilterUtil.isGeneralMethod(access,name)){
            return new TraceMethodAdapter(api, methodVisitor, access, name, desc, this.currentClassName);
        }
        return methodVisitor;
    }


    @Override
    public void visitEnd() {
        ReferenceMethodManager.println();
        super.visitEnd();
    }


    public  class TraceMethodAdapter extends AdviceAdapter {

        private final String currentMethodName;
        private final String currentClassName;
        private final String currentMethodDesc;

        protected TraceMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc, String className) {
            super(api, mv, access, name, desc);
            this.currentClassName = className;
            this.currentMethodName = name;
            this.currentMethodDesc = desc;
        }


        @Override
        public void visitTypeInsn(int opcode, String s) {
            super.visitTypeInsn(opcode, s);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

//            Log.e("TraceReferenceMethodVisitor", "opcode :%s  ,owner : %s  , className  : %s , method : %s, name :  %s ,  desc:%s",opcode,owner, className, methodName, name, desc);


            if (owner.equals(targetClassName) && name.equals(targetMethodName) && desc.equals(targetMethodDesc)) {
                ReferenceMethodManager.add(currentClassName,currentMethodName);

                super.visitMethodInsn(Opcodes.INVOKESTATIC,  configuration.getReplaceClassName(),  configuration.getReplaceMethodName(),  configuration.getReplaceMethodDesc(),  false);
                Log.e(this, "opcode :%s  ,owner : %s  , className  : %s , method : %s, name :  %s ,  desc:%s",opcode,owner, currentClassName, currentMethodName, name, desc);
                Log.e(this, "opcode :%s  ,configuration.getReplaceClassName() : %s  , configuration.getReplaceMethodName()  : %s , configuration.getReplaceMethodDesc() : %s, ",Opcodes.INVOKESTATIC,configuration.getReplaceClassName(), configuration.getReplaceMethodName(), configuration.getReplaceMethodDesc());
                return;
            }

//            if (owner.equals(TelephonyManager_INTERNAL_NAME) && name.equals("getDeviceId") && desc.equals(MethodDesc.P_Ls_R_v)) {
////                Log.e(this, "get imei className:%s, method:%s, name:%s,  desc:%s", className, methodName, name, desc);
////                super.visitMethodInsn();
//                ObserverManager.add(className,methodName);
//            }

            super.visitMethodInsn( opcode,  owner,  name,  desc,  itf);

        }
    }
}
