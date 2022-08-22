package com.fyg.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
/**
 * Created by fuyuguang on 2022/8/22 9:29 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

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


 只能加载类，不能加载jar
 */
public class CustomizedClassLoader extends ClassLoader {

    private String classLoaderName;

    private String path;

    private String fileExtension = ".class";

    public CustomizedClassLoader(String classLoaderName) {
        super();
        this.classLoaderName = classLoaderName;
    }

    public CustomizedClassLoader(ClassLoader parent, String classLoaderName) {
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
//        System.out.println("findClass invoked : " + className);
//        System.out.println("class loader name : " + this.classLoaderName);
        byte[] data = this.loadClassData(className);
        return this.defineClass(className, data, 0, data.length);
    }

    private byte[] loadClassData(String className) {
        byte[] data = null;
        className = className.replace(".", "/");
        try(InputStream is = new FileInputStream(new File(this.path + className + this.fileExtension));
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int ch;
            while(-1 != (ch = is.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void setPath(String path) {
        this.path = path;
    }
}