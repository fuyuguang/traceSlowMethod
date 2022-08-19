package com.fyg.tracemethod
import com.fyg.util.FilterUtil
import com.fyg.util.Log
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import java.lang.Long


class TraceClassVisitorV2(api: Int, cv: ClassVisitor?, var traceConfig: Config, var monitoringTimeThreshold : Long) :
    ClassVisitor(api, cv) {

    private var className: String? = null
    private var isABSClass = false
    private var isBeatClass = false
    private var isConfigTraceClass = false

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)

        this.className = name
        //抽象方法或者接口
        if (access and Opcodes.ACC_ABSTRACT > 0 || access and Opcodes.ACC_INTERFACE > 0) {
            this.isABSClass = true
        }

        //插桩代码所属类
        val resultClassName = name?.replace(".", "/")
        if (resultClassName == traceConfig.mBeatClass) {
            this.isBeatClass = true
        }

        //是否是配置的需要插桩的类
        name?.let { className ->
            isConfigTraceClass = traceConfig.isConfigTraceClass(className)
        }

        //不需要跟踪
        val isNotNeedTraceClass = isABSClass || !isConfigTraceClass
        // 需要打印出所有被插桩的类和方法  |  需要跟踪
        if (traceConfig.mIsNeedLogTraceInfo && !isNotNeedTraceClass) {
            Log.e(this, "MethodTrace-trace-class: ", className?:"未知")
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, desc, signature, exceptions)
        return TraceMethodVisitorV2(api, mv, access, name, desc, className, traceConfig,monitoringTimeThreshold)
    }


    inner class TraceMethodVisitorV2(
        api: Int, mv: MethodVisitor?, access: Int, name: String?, desc: String?, className: String?,
        var traceConfig: Config,  val  monitoringTimeThreshold :Long
    ) : AdviceAdapter(api, mv, access, name, desc) {

        private var enablePrintTime: Boolean? =  false
        private var methodName: String? = null
        private var originMethodName: String? = null
        private var className: String? = null
        private val maxSectionNameLength = 127

        init {
            val traceMethod = TraceMethod.create(0, access, className, name, desc)
            this.methodName = traceMethod.getMethodNameText()
            this.className = className
            this.originMethodName = name

        }


        /**
            拦截无效的 文件：未被跟踪的，虚方法，构造方法等  */
        private fun interceptedInvalidateClass():Boolean{
            
            if(enablePrintTime!!){ //启用注解
                return false;
            }else{
                //不是被跟踪的类 | 是抽象方法 |  是构造方法   返回true, 拦截掉事件，不需要统计
                return !isConfigTraceClass || (isABSClass || MethodFilter.isConstructor(name))
            }
        }

        private var slotIndex = 0
        override fun onMethodEnter() {
            super.onMethodEnter()

            if (interceptedInvalidateClass()){
                return
            }

            val methodName = generatorMethodName()

            //new
            slotIndex = newLocal(Type.LONG_TYPE)
            /**
            invokeStatic(Type.getType("Ljava/lang/System;"), new Method("currentTimeMillis", "()J")); */
            /**
             * invokeStatic(Type.getType("Ljava/lang/System;"), new Method("currentTimeMillis", "()J"));  */
            mv.visitMethodInsn(
                INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false
            )

            /**
            storeLocal(slotIndex); */
            /**
             * storeLocal(slotIndex);  */
            mv.visitVarInsn(LSTORE, slotIndex)

            //new
            if (traceConfig.mIsNeedLogTraceInfo) {
                println("MethodTrace-trace-method: ${methodName ?: "未知"}")

                Log.e(this, "MethodTrace-trace-class: ", className?:"未知")
            }
        }

        override fun onMethodExit(opcode: Int) {

            if (interceptedInvalidateClass()){
                return
            }
            val methodName = generatorMethodName()

            if (FilterUtil.isMethodEnd(opcode)) {
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
                mv.visitVarInsn(LLOAD, slotIndex)
                mv.visitInsn(LSUB)
                mv.visitVarInsn(LSTORE, slotIndex)


                //new 1;
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "java/lang/Thread",
                    "currentThread",
                    "()Ljava/lang/Thread;",
                    false
                )
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "android/os/Looper",
                    "getMainLooper",
                    "()Landroid/os/Looper;",
                    false
                )
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "android/os/Looper",
                    "getThread",
                    "()Ljava/lang/Thread;",
                    false
                )
                val label0 = Label()
                mv.visitJumpInsn(IF_ACMPNE, label0)
                mv.visitVarInsn(LLOAD, slotIndex)
                mv.visitLdcInsn(monitoringTimeThreshold)


                mv.visitInsn(LCMP)
                mv.visitJumpInsn(IFLE, label0)
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
                mv.visitLdcInsn("  differ :  "+methodName)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                    false
                )
                mv.visitVarInsn(LLOAD, slotIndex)
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "append",
                    "(J)Ljava/lang/StringBuilder;",
                    false
                )
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/lang/StringBuilder",
                    "toString",
                    "()Ljava/lang/String;",
                    false
                )
                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V",
                    false
                )
                mv.visitLabel(label0)

                //new
            }


        }

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
            enablePrintTime = descriptor?.contains("Lcom/fyg/monitor/tracemethod/PrintTime;")
            return super.visitAnnotation(descriptor, visible)
        }

        private fun generatorMethodName(): String? {
            var sectionName = methodName
            var length = sectionName?.length ?: 0
            if (length > maxSectionNameLength && !sectionName.isNullOrBlank()) {
                // 先去掉参数
                val parmIndex = sectionName.indexOf('(')
                sectionName = sectionName.substring(0, parmIndex)
                // 如果依然更大，直接裁剪
                length = sectionName.length
                if (length > 127) {
                    sectionName = sectionName.substring(length - maxSectionNameLength)
                }
            }
            return sectionName
        }

    }



}