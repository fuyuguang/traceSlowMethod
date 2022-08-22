package com.fyg.tracemethod
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter


class TraceSlowMethodVisitorV0(
    api: Int, mv: MethodVisitor?, access: Int,
    name: String?, desc: String?, className: String?,
    var traceConfig: Config
) : AdviceAdapter(api, mv, access, name, desc) {

    private var methodName: String? = null
    private var name1: String? = null
    private var className: String? = null

    init {
        val traceMethod = TraceMethod.create(0, access, className, name, desc)
        this.methodName = traceMethod.getMethodNameText()
        this.className = className
        this.name1 = name
    }


    override fun onMethodEnter() {
        super.onMethodEnter()
        mv.visitLdcInsn(methodName)
        mv.visitMethodInsn(
            INVOKESTATIC,
            traceConfig.mBeatClass,
            "start",
            "(Ljava/lang/String;)V",
            false
        )

        if (traceConfig.mIsNeedLogTraceInfo) {
            println("MethodTrace-trace-method: ${TraceMethod.generatorMethodName(methodName) ?: "未知"}")
        }
    }

    override fun onMethodExit(opcode: Int) {
        mv.visitLdcInsn(TraceMethod.generatorMethodName(methodName))
        mv.visitMethodInsn(
            INVOKESTATIC,
            traceConfig.mBeatClass,
            "end",
            "(Ljava/lang/String;)V",
            false
        )
    }
}