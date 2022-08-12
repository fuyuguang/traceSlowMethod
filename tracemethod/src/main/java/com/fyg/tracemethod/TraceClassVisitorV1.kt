package com.fyg.tracemethod
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.lang.Long


class TraceClassVisitorV1(api: Int, cv: ClassVisitor?, var traceConfig: Config, var monitoringTimeThreshold : Long) :
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
            System.out.println("isConfigTraceClass is : "+isConfigTraceClass +"     className : "+className)
        }

        val isNotNeedTraceClass = isABSClass || !isConfigTraceClass
        if (traceConfig.mIsNeedLogTraceInfo && !isNotNeedTraceClass) {
            println("MethodTraceMan-trace-class: ${className ?: "未知"}")
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val isConstructor = MethodFilter.isConstructor(name)
        System.out.println("222222")
        System.out.println("isConfigTraceClass : "+isConfigTraceClass +" isABSClass : "+isABSClass +"  isConstructor : "+isConstructor +"     className : "+className+"     methodName : "+name)
        System.out.println("(!isConfigTraceClass || (isABSClass || isConstructor))  : "+(!isConfigTraceClass || (isABSClass || isConstructor)))
        System.out.println("---------")
        System.out.println("  ")

        return if (!isConfigTraceClass || (isABSClass || isConstructor)) {
            super.visitMethod(access, name, desc, signature, exceptions)
        } else {

            val mv = cv.visitMethod(access, name, desc, signature, exceptions)
            TraceMethodVisitorV2(api, mv, access, name, desc, className, traceConfig,monitoringTimeThreshold)
        }
    }
}