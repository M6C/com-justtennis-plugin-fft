package com.justtennis.plugin.common.tool;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.justtennis.plugin.fft.R;

import java.util.Objects;

public class FragmentTool {

    private static final String TAG = FragmentTool.class.getSimpleName();

    private FragmentTool() {}

    public enum INIT_FAB_IMAGE {
        VALIDATE(R.drawable.ic_check_black_24dp),
        ADD(android.R.drawable.ic_input_add),
        BACK(R.drawable.ic_arrow_back_black_24dp),
        REFRESH(R.drawable.ic_refresh_black_48dp),
        INFORMATION(R.drawable.ic_refresh_black_48dp);

        @DrawableRes
        int res;
        INIT_FAB_IMAGE(@DrawableRes int res) {
            this.res = res;
        }
    }

    public static void replaceFragment(@NonNull FragmentActivity activity, @NonNull Fragment fragment) {
        replaceFragment(activity, fragment, R.id.fragment_container);
    }

    public static void replaceFragment(@NonNull FragmentActivity activity, @NonNull Fragment fragment, @IdRes int idRes) {
        String tag = fragment.getClass().getName();
        logMe("replaceFragment tag:" + tag);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(idRes, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public static void clearBackStackEntry(@NonNull FragmentActivity activity) {
        clearBackStackEntry(activity, 0);
    }

    public static void clearBackStackEntry(@NonNull FragmentActivity activity, int deep) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        int cntBack = fragmentManager.getBackStackEntryCount() - 1;
        logMe("clearBackStackEntry BackStackEntry Count:"+ cntBack);
        for(int i=cntBack ; i>=deep ; i--) {
            FragmentManager.BackStackEntry back = fragmentManager.getBackStackEntryAt(i);
            logMe("clearBackStackEntry BackStackEntry Pop " + i + " name:" + back.getName());
            fragmentManager.popBackStack(back.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void hideFab(@NonNull FragmentActivity activity) {
        View fab = activity.findViewById(R.id.fab);
        if (fab != null) {
            fab.setVisibility(View.GONE);
        }
    }

    public static void enableFab(@NonNull FragmentActivity activity, boolean enable) {
        View fab = activity.findViewById(R.id.fab);
        if (fab != null) {
            fab.setEnabled(enable);
        }
    }

    public static void finish(Activity activity) {
        Objects.requireNonNull(activity).onBackPressed();
    }

    public static void onClickFab(@NonNull FragmentActivity activity, View.OnClickListener listener) {
        View fab = activity.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(listener);
            fab.setVisibility(listener == null ?  View.GONE : View.VISIBLE);
        }
    }

    public static void initializeFabDrawable(@NonNull FragmentActivity activity, INIT_FAB_IMAGE type) {
        initializeFabDrawable(activity, type.res);
    }

    public static void initializeFabDrawable(@NonNull FragmentActivity activity, @DrawableRes int idRes) {
        FloatingActionButton fab = activity.findViewById(R.id.fab);
        if (fab != null) {
            fab.setImageResource(idRes);
        }
    }

    private static void logMe(String msg) {
//        com.crashlytics.android.Crashlytics.log(msg);
        System.out.println(msg);
    }
}
