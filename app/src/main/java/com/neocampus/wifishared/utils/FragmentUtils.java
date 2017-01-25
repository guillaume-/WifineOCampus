package com.neocampus.wifishared.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.neocampus.wifishared.R;

import java.util.List;

/**
 * Created by Hirochi ☠ on 25/01/17.
 */

public class FragmentUtils {

    public static Fragment showFragment(AppCompatActivity activity,
                                  Class<?> aClass, Integer... animations) {
        /*Check if already create*/
        Fragment fragment;
        if ((fragment = getFragmentFromClass(activity, aClass))  == null) {
            fragment = (Fragment) ClassUtils.newInstance(aClass);
        }
        final FragmentManager fm = activity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        if (animations.length == 0) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animations.length == 1) {
            ft.setCustomAnimations(animations[0], animations[0]);
        } else {
            ft.setCustomAnimations(animations[0], animations[1]);
        }
        ft.replace(R.id.iDFragmentShowing, fragment);
        ft.addToBackStack(null);
        ft.commit();
        return fragment;
    }

    public static Fragment showFragment(AppCompatActivity activity,
                                       Fragment fragment, Integer... animations) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        if (animations.length == 0) {
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (animations.length == 1) {
            ft.setCustomAnimations(animations[0], animations[0]);
        } else {
            ft.setCustomAnimations(animations[0], animations[1]);
        }
        ft.replace(R.id.iDFragmentShowing, fragment);
        ft.addToBackStack(null);
        ft.commit();
        return fragment;
    }

    public static Fragment getForegroundFragment(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public static Fragment getFragmentFromClass(AppCompatActivity activity, Class aClass)
    {
        Fragment fragment = null;
        List<Fragment> fragments =
                activity.getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragmentStored : fragments) {
                if (fragmentStored != null
                        && fragmentStored.getClass() == aClass) {
                    fragment = fragmentStored;
                    break;
                }
            }
        }
        return fragment;
    }
}
