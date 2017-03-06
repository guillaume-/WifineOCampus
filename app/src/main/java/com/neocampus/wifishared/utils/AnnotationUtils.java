package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AnnotationUtils permet d'�ffectuer des actions en relation les annotations
 */
public class AnnotationUtils {

    /**
     * Liste les classes de l'application ayant une certaine annotation
     * @param context contexte de l'application
     * @param annotation annotation permettant le filtre
     * @return la liste des classes ayant l'annotation
     *
     * @throws PackageManager.NameNotFoundException si le package n'existe pas
     * @throws IOException si le fichier compr�ss� des classes n'existe pas
     * @throws ClassNotFoundException si la classe n'existe pas
     *
     * @see ClassUtils#getClasses(Context, String)
     * @see Class#isAnnotationPresent(Class)
     */
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

    /**
     * Liste les m�thodes d'une classe ayant une certaine annotation
     * @param aClass classe dont on recherche les m�thodes
     * @param annotation  permettant le filtre
     * @return Liste des m�thodes ayant l'annotation
     *
     * @see Class#getMethods()
     * @see Method#isAnnotationPresent(Class)
     */
    public static Set<Method> getAnnotationsMethods(Class aClass,
                                                    Class<? extends Annotation> annotation)
    {
        Set<Method> results = new HashSet<>();
        Method[] methods = aClass.getDeclaredMethods();
        if(methods != null) {
            for (Method method : methods) {
                if(method.isAnnotationPresent(annotation)) {
                    method.setAccessible(true);
                    results.add(method);
                }
            }
        }
        return results;
    }
}
