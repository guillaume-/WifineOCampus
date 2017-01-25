package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */

public class ClassUtils {

    public static String getCurrentPackage() throws ClassNotFoundException {
        StackTraceElement [] sd = new RuntimeException().getStackTrace();
        String classNameOfPrevMethod = sd[1].getClassName();
        Class<?> aClass = Class.forName(classNameOfPrevMethod);
        return aClass.getPackage().getName();
    }


    public static Set<Class<?>> getClasses(Context context, String packageName)
            throws ClassNotFoundException, IOException, PackageManager.NameNotFoundException {

        Set<Class<?>> classes = new HashSet<>();
        ClassLoader cLoader = Thread.currentThread().getContextClassLoader();
        List<String> dexPaths = DexUtils.getDexPaths(context);
        for(String dexPath : dexPaths) {
            classes.addAll(getClasses(cLoader, dexPath, packageName));
        }
        return classes;
    }


    private static Set<Class<?>> getClasses(ClassLoader classLoader,
                                            String dexPath, String packageName) {
        DexFile dex;
        Set<Class<?>> classes = new HashSet<>();
        try {
            if (dexPath.endsWith(".zip")) {
                dex = DexFile.loadDex(dexPath, dexPath + ".tmp", 0);
            } else {
                dex = new DexFile(dexPath);
            }
            for (Enumeration<String> it =
                 dex.entries(); it.hasMoreElements(); ) {
                String entry = it.nextElement();
                if (entry.startsWith(packageName)) {
                    classes.add(classLoader.loadClass(entry));
                }
            }
            dex.close();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static Object newInstance(Class aClass)
    {
        try {
            return aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class aClass, String methodeName)
    {
        for(Method method : aClass.getDeclaredMethods()) {
            if(methodeName.equals(method.getName())) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

}
