package com.neocampus.wifishared.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.neocampus.wifishared.R;

import java.util.List;

/**
 * FragmentUtils permet d'�ffectuer des actions en relation avec les fragments
 */
public class FragmentUtils {

    /**
     * Affiche un fragment selon sa classe, cr�e une nouvelle instance si une aucune n'existe

     * @param activity activity o� affich� le fragment
     * @param aClass classe du fragment a affich�
     * @param animations tableau des animations a �ffectu� lors de l'affichage
     * @return instance du fragment affich�
     */
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

    /**
     * Affiche un fragment via une instance de celle-ci
     * @param activity activity o� affich� le fragment
     * @param fragment instance du fragment a affich�
     * @param animations tableau des animations a �ffectu� lors de l'affichage
     * @return instance du fragment affich�
     */
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

    /**
     * Renvoi le fragment visible au premier plan
     * @param activity activity o� est affich� le fragment
     * @return instance du fragment affich�
     */
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

    /**
     * Recherche une instance d'un fragment selon sa classe
     * @param activity activity s'est affich� le fragment recherch�
     * @param aClass classe du fragment recherch�
     * @return fragment recherch� si trouv�, null sinon
     */
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
