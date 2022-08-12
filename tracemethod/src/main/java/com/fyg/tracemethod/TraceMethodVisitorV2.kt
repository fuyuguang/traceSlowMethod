package com.fyg.tracemethod
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import java.lang.Long

/**
 * Created by fuyuguang on 2022/8/11 7:09 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */
class TraceMethodVisitorV2(
    api: Int, mv: MethodVisitor?, access: Int,
    name: String?, desc: String?, className: String?,
    var traceConfig: Config,  val  monitoringTimeThreshold :Long
) : AdviceAdapter(api, mv, access, name, desc) {

    private var methodName: String? = null
    private var name1: String? = null
    private var className: String? = null
    private val maxSectionNameLength = 127


    init {
        val traceMethod = TraceMethod.create(0, access, className, name, desc)
        this.methodName = traceMethod.getMethodNameText()
        this.className = className
        this.name1 = name

    }

    private var slotIndex = 0

    override fun onMethodEnter() {
        super.onMethodEnter()

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
            println("MethodTraceMan-trace-method: ${methodName ?: "未知"}")
        }
    }

    override fun onMethodExit(opcode: Int) {

        val methodName = generatorMethodName()
        println("fyg : methodName    ${methodName ?: "未知"}")

        if (opcode >= IRETURN && opcode <= RETURN || opcode == ATHROW) {
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
//            mv.visitLdcInsn(100L)
//            mv.visitLdcInsn(Long(monitoringTimeThreshold))
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