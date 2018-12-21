package com.justtennis.plugin.shared.manager;

import android.app.Activity;
import android.widget.Toast;

import com.justtennis.plugin.common.MainActivity;

public final class NotificationManager {
    private NotificationManager() {
    }

    public static void onTaskProcessUpdate(Activity context, String... values) {
        if (values != null && values.length > 0) {
            String text = values[0];
            if (context instanceof MainActivity) {
                ((MainActivity)context).showMessage(text);
            } else {
                String textLow = text.toLowerCase();
                int duration = textLow.startsWith("failed") || textLow.startsWith("error") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
            }
        }
    }
}
