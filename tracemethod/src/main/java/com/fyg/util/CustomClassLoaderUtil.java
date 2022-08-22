package com.fyg.util;

import com.fyg.util.Constant.InternalName;
import com.fyg.util.Constant.MethodDesc;

import org.objectweb.asm.Type;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by fuyuguang on 2022/8/22 10:18 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 *
 * /Users/fuyuguang/Github/ASM/traceSlowMethod/tracemethod/build/classes/java/main/com/fyg/util/Constant.class
 *
 */
public class CustomClassLoaderUtil {



    public static Class getCustomClass(String currrentProjectJarPath,String className) {
        String projectPath =  System.getProperty("user.dir");
        final String urlPath = "file:"+projectPath+currrentProjectJarPath;
        URLClassLoader urlClassLoader = null;
        try {
            // 通过URLClassLoader加载外部jar
            urlClassLoader = new URLClassLoader(new URL[]{new URL(urlPath)});
            Class<?> cls = Class.forName(className, false, urlClassLoader);
            return cls;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 卸载关闭外部jar
            try {
                urlClassLoader.close();
            } catch (IOException e) {
                System.out.println("关闭外部jar失败：" + e.getMessage());
            }
        }
        return null;
    }



    public static Class<?> loadClassForCustomClassLoarder(String classFilePath,String className) {
        CustomizedClassLoader customizedClassLoader = new CustomizedClassLoader("customClassLoader");
        customizedClassLoader.setPath(classFilePath);
        try {
            return customizedClassLoader.findClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getUrl() throws IOException {
        String path = "";

        //第一种：获取类加载的根路径
        path = this.getClass().getResource("/").getPath();
        System.out.println("第一种：获取类加载的根路径");
        System.out.println(path);

        //获取当前类的所在工程路径,不加“/”  获取当前类的加载目录
        path = this.getClass().getResource("").getPath();
        System.out.println("获取当前类的所在工程路径,不加“/”  获取当前类的加载目录");
        System.out.println(path);

        // 第二种：获取项目路径
        File file = new File("");
        path = file.getCanonicalPath();
        System.out.println("第二种：获取项目路径");
        System.out.println(path);

        // 第三种：
        URL path1 = this.getClass().getResource("");
        System.out.println("第三种：");
        System.out.println(path1);

        // 第四种：
        path = System.getProperty("user.dir");
        System.out.println(" 第四种：：");
        System.out.println(path);


        // 第五种：  获取项目路径
        path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println(" 第五种：  获取项目路径");
        System.out.println(path);

        //第六种  表示到项目的根目录下, 要是想到目录下的子文件夹,修改"/"即可
        //request.getSession().getServletContext().getRealPath("/"));
    }



    public static void main(String[] args) throws IOException {
        new CustomClassLoaderUtil(){}.getUrl();



        Class customClass = CustomClassLoaderUtil.getCustomClass("/tracemethod/libs/android-32.jar", "android.telephony.TelephonyManager");
        System.out.println("custom : "+ Type.getInternalName(customClass));


        String STRING_INTERNAL_NAME =  Type.getInternalName(String.class);
        String STRING_ClassDesc =  Type.getDescriptor(String.class);
        System.out.println(STRING_INTERNAL_NAME);
        System.out.println(STRING_ClassDesc);
        System.out.println(MethodDesc.P_Ls_R_v);
        System.out.println(InternalName.TelephonyManager_INTERNAL_NAME);



        Class<?> ConstantClassA = CustomClassLoaderUtil.loadClassForCustomClassLoarder(Thread.currentThread().getContextClassLoader().getResource("").getPath(),"com.fyg.util.Constant");
        Class<?> ConstantClassB = Constant.class;
        System.out.println("ConstantClassA path ："+(ConstantClassA.getName()));
        System.out.println("ConstantClassB："+(ConstantClassB.getName()));
        System.out.println("ConstantClassA ==  ConstantClassB ："+(ConstantClassA ==  ConstantClassB));

    }

}
