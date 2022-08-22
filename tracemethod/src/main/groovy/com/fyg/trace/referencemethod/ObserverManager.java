package com.fyg.trace.referencemethod;

import com.fyg.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObserverManager {

    private static final Map<String, List<String>> OBSERVERS = new HashMap<>();


    public synchronized static <T> void add(String className,String methodName) {

        List<String> methodNames = OBSERVERS.get(className);
        if (methodNames == null) {
            methodNames = new ArrayList<>();
            OBSERVERS.put(className, methodNames);
        }
        methodNames.add(methodName);
    }




    public static void println() {
        for (Map.Entry<String,List<String>> entry : OBSERVERS.entrySet()) {
            if (entry.getKey() != null) {
                List<String> list = entry.getValue();
                Log.e("ObserverManager", "  ");
                Log.e("ObserverManager", "Class :%s  ",entry.getKey());
                Log.e("ObserverManager", "");
                for (String item : list) {
                    if (item != null) {
                        Log.e("ObserverManager", "className : %s ,methodName : %s ",entry.getKey(),item);
                    }
                }
            }
        }
    }




}
