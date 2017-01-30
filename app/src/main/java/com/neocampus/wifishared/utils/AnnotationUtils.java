package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */

public class AnnotationUtils {

    public  static Set<Class<?>> getAnnotationClasses(Context context,
                                                           Class<? extends Annotation> annotation)
            throws PackageManager.NameNotFoundException, IOException, ClassNotFoundException {

        Set<Class<?>> results = new HashSet<>();
        Set<Class<?>> classes =
                ClassUtils.getClasses(context, context.getPackageName());
        for (Class<?> aClass : classes) {
            if(aClass.isAnnotationPresent(annotation))
                results.add(aClass);
        }
        return results;
    }

    public static Set<Method> getAnnotationsMethods(Class aClass,
                                                    Class<? extends Annotation> annotation)
    {
        Set<Method> results = new HashSet<>();
        Method[] methods = aClass.getMethods();
        if(methods != null) {
            for (Method method : methods) {
                if(method.isAnnotationPresent(annotation)) {
                    results.add(method);
                }
            }
        }
        return results;
    }
}
