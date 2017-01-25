package com.neocampus.wifishared.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hirochi ☠ on 09/01/17.
 */

public class DexUtils {

    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";
    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_DEX_NUMBER = "dex.number";

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                Context.MODE_PRIVATE :
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * get all the dex path
     *
     * @param context the application context
     * @return all the dex path
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     */
    public static List<String> getDexPaths(Context context) throws PackageManager.NameNotFoundException, IOException {
        TimeLog.begin();
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);
        File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

        TimeLog.mark("\ngetDexPaths sourceDir=" + applicationInfo.sourceDir + ", dataDir=" + applicationInfo.dataDir);

        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path

        //the prefix of extracted file, ie: TrafficUtils.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;
        //the total dex numbers
        int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);

        TimeLog.mark( "getDexPaths totalDexNumber=" + totalDexNumber);

        for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
            String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
            File extractedFile = new File(dexDir, fileName);
            if (extractedFile.isFile()) {
                sourcePaths.add(extractedFile.getAbsolutePath());
            } else {
                throw new IOException("Missing extracted secondary dex file '" +
                        extractedFile.getPath() + "'");
            }
        }
        try {
            // handle dex files built by instant run
            File instantRunFilePath = new File(applicationInfo.dataDir,
                    "files" + File.separator + "instant-run" + File.separator + "dex");
            TimeLog.mark(  "getDexPaths instantRunFile exists=" + instantRunFilePath.exists() + ", isDirectory="
                        + instantRunFilePath.isDirectory() + ", getAbsolutePath=" + instantRunFilePath.getAbsolutePath());
            if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
                File[] sliceFiles = instantRunFilePath.listFiles();
                for (File sliceFile : sliceFiles) {
                    if (null != sliceFile && sliceFile.exists() && sliceFile.isFile() && sliceFile.getName().endsWith(".dex")) {
                        sourcePaths.add(sliceFile.getAbsolutePath());
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            TimeLog.end();
        }
        return sourcePaths;
    }

}