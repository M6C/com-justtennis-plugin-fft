package com.justtennis.plugin.shared.manager;

import android.content.Context;
import android.widget.Toast;

public final class NotificationManager {
    private NotificationManager() {
    }

    public static void onTaskProcessUpdate(Context context, String... values) {
        if (values != null && values.length > 0) {
            String text = values[0];
            String textLow = text.toLowerCase();
            int duration = textLow.startsWith("failed") || textLow.startsWith("error") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        }
    }
}
