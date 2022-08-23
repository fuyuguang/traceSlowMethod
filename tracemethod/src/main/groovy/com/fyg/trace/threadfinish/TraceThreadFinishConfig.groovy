package com.fyg.trace.threadfinish

import com.fyg.transform.base.BaseConfig
import org.objectweb.asm.Opcodes

/**
 * Created by fuyuguang on 2022/8/11 11:04 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class TraceThreadFinishConfig extends BaseConfig {
    boolean open;
    /**
     * 不能用final 修饰
     * 不能用private  修饰
     */
    String targetClassName = "java/lang/Thread";
    String targetMethodName = "<init>";
    String targetMethodDesc = "(Ljava/lang/Runnable;)V";
    int targetOpcode = Opcodes.NEW;


    String replaceClassName = "com/fyg/tracemethod/ui/thread/CustomThread";
    String replaceMethodName = "<init>";
    String replaceMethodDesc = "(Ljava/lang/Runnable;Ljava/lang/String;)V";
    int replaceOpcode = Opcodes.NEW;

    TraceThreadFinishConfig() {
        open = true;
    }
}
