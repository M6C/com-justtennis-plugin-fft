package com.justtennis.plugin.shared.manager;

import android.content.Context;
import android.widget.Toast;

public final class NotificationManager {
    private NotificationManager() {
    }

    public static void onTaskProcessUpdate(Context context, String... values) {
        if (values != null && values.length > 0) {
            String text = values[0];
            int duration = Toast.LENGTH_SHORT;//text.toLowerCase().startsWith("info") ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
        }
    }
}
