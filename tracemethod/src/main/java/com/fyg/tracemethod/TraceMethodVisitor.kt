package com.fyg.tracemethod
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter


class TraceMethodVisitor(
    api: Int, mv: MethodVisitor?, access: Int,
    name: String?, desc: String?, className: String?,
    var traceConfig: Config
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


    override fun onMethodEnter() {
        super.onMethodEnter()
        val methodName = generatorMethodName()
        println("fyg : methodName    ${methodName ?: "未知"}")
        mv.visitLdcInsn(methodName)
        mv.visitMethodInsn(
            INVOKESTATIC,
            traceConfig.mBeatClass,
            "start",
            "(Ljava/lang/String;)V",
            false
        )

        if (traceConfig.mIsNeedLogTraceInfo) {
            println("MethodTraceMan-trace-method: ${methodName ?: "未知"}")
        }
    }

    override fun onMethodExit(opcode: Int) {
        mv.visitLdcInsn(generatorMethodName())
        mv.visitMethodInsn(
            INVOKESTATIC,
            traceConfig.mBeatClass,
            "end",
            "(Ljava/lang/String;)V",
            false
        )

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