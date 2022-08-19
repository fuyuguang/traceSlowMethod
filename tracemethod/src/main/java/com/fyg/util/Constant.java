package com.fyg.util;

import static com.fyg.util.Constant.InternalName.STRING_INTERNAL_NAME;

import com.fyg.tracemethod.TraceClassVisitorV2;

import org.objectweb.asm.Type;

/**
 * Created by fuyuguang on 2022/8/18 9:44 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 */
public class Constant {



    public interface InternalName{
        /** java/lang/String  */
        String STRING_INTERNAL_NAME =  Type.getInternalName(String.class);
        String Thread_INTERNAL_NAME =  Type.getInternalName(Thread.class);
        /**
         "android/telephony/TelephonyManager"  */
        String TelephonyManager_INTERNAL_NAME = "android/telephony/TelephonyManager";

    }

    public interface ClassDesc{
        /** Ljava/lang/String;  */
        String STRING_ClassDesc =  Type.getDescriptor(String.class);

    }


    public interface TAG{

        String TAG = "TAG:";
        String TAG1 = "1TAG:";
        String TAG2 = "2TAG:";
        String TAG3 = "3TAG:";


    }


    public interface MethodDesc{
        /**
         "()Ljava/lang/String;"  */
        String P_Ls_R_v =  Type.getMethodDescriptor(Type.getObjectType(STRING_INTERNAL_NAME));

    }

    public static void main(String[] args) {

        String STRING_INTERNAL_NAME =  Type.getInternalName(String.class);
        String STRING_ClassDesc =  Type.getDescriptor(String.class);
        System.out.println(STRING_INTERNAL_NAME);
        System.out.println(STRING_ClassDesc);
        System.out.println(MethodDesc.P_Ls_R_v);
    }
}
