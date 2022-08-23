package com.fyg.tracemethod.ui.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReferenceMethodManager {

    private static final Map<String, List<Thread>> OBSERVERS = new HashMap<>();


    public synchronized static <T> void add(String className,Thread run) {

        List<Thread> methodNames = OBSERVERS.get(className);
        if (methodNames == null) {
            methodNames = new ArrayList<>();
            OBSERVERS.put(className, methodNames);
        }
        methodNames.add(run);
    }

    public synchronized static <T> void remove(String className,Runnable run) {

        List<Thread> methodNames = OBSERVERS.get(className);
        if (methodNames != null && !methodNames.isEmpty()){
            methodNames.remove(run);
        }
    }


    public static void println() {
        for (Map.Entry<String,List<Thread>> entry : OBSERVERS.entrySet()) {
            if (entry.getKey() != null) {
                List<Thread> list = entry.getValue();
                Log.e("ObserverManager", " ");
                Log.e("ObserverManager", "");
                System.out.println("线程集合 count "+ list.size());
                for (Thread item : list) {
                    if (item != null) {
                        Log.e("ReferenceMethodManager", " run  : %s ", item.getClass().getEnclosingClass()+"   "+item.getClass().getName() + ""+item.getName());
                    }
                }
            }
        }
    }
}