package com.example.danazone04.danazone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.androidannotations.annotations.EFragment;

/**
 * Created by PC on 1/17/2018.
 */

public abstract class BaseContainerFragment extends BaseFragment {
    private static BaseContainerFragment sInstance;

    public static BaseContainerFragment getInstance() {
        return sInstance;
    }

    public BaseContainerFragment() {
        sInstance = this;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.replace(R.id.mFrameContainers, fragment);
        transaction.commit();
    }


    public boolean popFragment() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStackImmediate();
            if (getChildFragmentManager().getBackStackEntryCount() >= 1) {
                BaseFragment baseFragment = ((BaseFragment) getChildFragmentManager().getFragments().
                        get(getChildFragmentManager().getBackStackEntryCount() - 1));
                if (baseFragment != null){

                }

            }
            return true;
        }
        return false;
    }

    public void popAllFragments() {
        try {
            if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (IllegalStateException ignored) {
        }
    }
}

