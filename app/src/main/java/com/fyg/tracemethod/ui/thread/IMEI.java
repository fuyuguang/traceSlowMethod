package com.fyg.tracemethod.ui.thread;

/**
 * Created by fuyuguang on 2022/8/18 3:09 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class IMEI {

    public static String getDeviceId(){

        System.out.println("imei : \n"+getStackTrace(Thread.currentThread().getStackTrace()));
        return null;
    }

    public static String getStackTrace(StackTraceElement[] stackTrace){
        if (stackTrace != null){
            if (stackTrace.length>0){
                StringBuilder sb = new StringBuilder(System.lineSeparator());
                for (int i = 0; i < stackTrace.length; i++) {
                    StackTraceElement traceElement = stackTrace[i];
                    String fileName = traceElement.getFileName();
                    String className = traceElement.getClassName();
                    String methodName = traceElement.getMethodName();
                    int lineNumber = traceElement.getLineNumber();
                    sb.append(fileName+"\t").append(className+"\t").append(methodName+"\t").append(lineNumber+"\t").append(System.lineSeparator());
                }
                return sb.toString();
            }
        }
        return null;
    }

}
