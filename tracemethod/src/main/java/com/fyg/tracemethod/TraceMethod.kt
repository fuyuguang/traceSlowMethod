package com.fyg.tracemethod
import org.objectweb.asm.Opcodes

class TraceMethod {

    private var id: Int = 0
    private var accessFlag: Int = 0
    private var className: String? = null
    private var methodName: String? = null
    private var desc: String? = null

    companion object {
        fun create(id: Int, accessFlag: Int, className: String?, methodName: String?, desc: String?): TraceMethod {
            val traceMethod = TraceMethod()
            traceMethod.id = id
            traceMethod.accessFlag = accessFlag
            traceMethod.className = className?.replace("/", ".")
            traceMethod.methodName = methodName
            traceMethod.desc = desc?.replace("/", ".")
            return traceMethod
        }


        fun generatorMethodName(methodName:String?): String? {
            val maxSectionNameLength = 127
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


    fun getMethodNameText(): String {
        return if (desc == null || isNativeMethod()) {
            this.className + "." + this.methodName
        } else {
            this.className + "." + this.methodName + "." + desc
        }
    }



    override fun toString(): String {
        return if (desc == null || isNativeMethod()) {
            "$id,$accessFlag,$className $methodName"
        } else {
            "$id,$accessFlag,$className $methodName $desc"
        }
    }


    fun isNativeMethod(): Boolean {
        return accessFlag and Opcodes.ACC_NATIVE != 0
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is TraceMethod) {
            val tm = obj as TraceMethod?
            return tm!!.getMethodNameText() == getMethodNameText()
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}