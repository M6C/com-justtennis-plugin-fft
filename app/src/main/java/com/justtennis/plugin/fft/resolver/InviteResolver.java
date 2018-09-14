package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class InviteResolver {

    private static final String TAG = InviteResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.invite";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ID_SAISON = "ID_SAISON";
    private static final String COLUMN_ID_PLAYER = "ID_PLAYER";
    private static final String COLUMN_TIME = "TIME";
    private static final String COLUMN_STATUS = "STATUS";
    private static final String COLUMN_TYPE = "TYPE";
    private static final String COLUMN_ID_EXTERNAL = "ID_EXTERNAL";
    private static final String COLUMN_ID_CALENDAR = "ID_CALENDAR";
    private static final String COLUMN_ID_RANKING = "ID_RANKING";
    private static final String COLUMN_ID_RANKING_ESTIMATE = "ID_RANKING_ESTIMATE";
    private static final String COLUMN_SCORE_RESULT = "SCORE_RESULT";
    private static final String COLUMN_ID_ADDRESS = "ID_ADDRESS";
    private static final String COLUMN_ID_CLUB = "ID_CLUB";
    private static final String COLUMN_ID_TOURNAMENT = "ID_TOURNAMENT";
    private static final String COLUMN_BONUS_POINT = "BONUS_POINT";

    public static void queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        Uri mContacts = CONTENT_URI;
        String[] columns = new String[]{COLUMN_ID, COLUMN_ID_SAISON, COLUMN_ID_CLUB, COLUMN_ID_PLAYER, COLUMN_TIME, COLUMN_STATUS};
        Cursor cur = contentResolver.query(mContacts, columns, null, null, null);
        if (cur != null) {
            Log.i(TAG, "query count:" + cur.getCount());

            if (cur.moveToFirst()) {
                StringBuilder msg = new StringBuilder();
                do {
                    msg.append("\r\n");
                    for (String col : columns) {
                        msg.append(cur.getString(cur.getColumnIndex(col))).append(" ");
                    }
                } while (cur.moveToNext());
                Log.i(TAG, "query result:" + msg.toString());
            }
        }
        else {
            Log.w(TAG, "Invite Provider not found [" + CONTENT_URI.toString() + "]");
        }
    }
}
