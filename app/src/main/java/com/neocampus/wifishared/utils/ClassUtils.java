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
 * ClassUtils permet d'éffectuer des actions en relation avec les classes
 */
public class ClassUtils {

    public static String getCurrentPackage() throws ClassNotFoundException {
        StackTraceElement [] sd = new RuntimeException().getStackTrace();
        String classNameOfPrevMethod = sd[1].getClassName();
        Class<?> aClass = Class.forName(classNameOfPrevMethod);
        return aClass.getPackage().getName();
    }

    /**
     * Liste tous les classes d'un package en incluant les sous répertoires,
     * @param context contexte de l'application
     * @param packageName repertoire dont on cherche les classes
     * @return liste des classes du package
     *
     * @throws PackageManager.NameNotFoundException si le package n'existe pas
     * @throws IOException si le fichier compréssé des classes n'existe pas
     * @throws ClassNotFoundException si la classe n'existe pas
     */
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

    /**
     * Liste tous les classes d'un répertoire
     * @param classLoader Gestionnaire des classes de l'application
     * @param dexPath répertoire dont on liste les classes
     * @param packageName package dont on liste les classes
     * @return liste des classes du répertoire
     */
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

    /**
     * Créer une instance d'une classe
     * @param aClass classe de l'instance
     * @return nouvelle instance de la classe
     */
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

    /**
     * Recherche une méthode dans une classe
     * @param aClass classe de la méthode recherché
     * @param methodeName nom de la méthode recherché
     * @return Méthode si trouvé, null sinon
     */
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
