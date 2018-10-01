package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResolver<M> {

    private static final String TAG = AbstractResolver.class.getName();

    protected abstract M createModel();
    protected abstract void updateModel(M model, String column, String data);

    protected List<M> query(ContentResolver contentResolver, Uri uri, String[] columns, String selection, String[] selectionArgs) {
        List<M> ret = new ArrayList<>();
        StringBuilder out = new StringBuilder("\r\nuri:").append(uri.toString()).append("\r\n");
        Cursor cur = contentResolver.query(uri, columns, selection, selectionArgs, null);
        if (cur != null) {
            out.append("query count:").append(cur.getCount());

            if (cur.moveToFirst()) {
                StringBuilder msg = new StringBuilder();
                do {
                    M model = createModel();
                    msg.append("\r\n");
                    for (String col : columns) {
                        String str = cur.getString(cur.getColumnIndex(col));
                        msg.append(col).append(":").append(str).append(" ");
                        updateModel(model, col, str);
                    }
                    ret.add(model);
                } while (cur.moveToNext());
                out.append("\r\n").append("query result:").append(msg.toString());
            }
            Log.i(TAG, out.toString());
        }
        else {
            out.append("Invite Provider not found [").append(uri.toString()).append("]");
            Log.w(TAG, out.toString());
        }
        return ret;
    }
}
