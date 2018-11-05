package com.justtennis.plugin.shared.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Helper for application's Fragment
 */
public class AppFragment extends Fragment {

    public AppCompatActivity getMainActivity() {
        return (AppCompatActivity)getActivity();
    }

    public void updateTitle(int title) {
        ActionBar actionBar = getMainActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void updateTitle(String title) {
        ActionBar actionBar = getMainActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected static void showShortMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
