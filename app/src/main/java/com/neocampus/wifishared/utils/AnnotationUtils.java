package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
}
