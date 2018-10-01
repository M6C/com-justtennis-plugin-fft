package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.model.Club;

import java.util.List;

public class ClubResolver extends AbstractResolver<Club> {

    private static final String TAG = ClubResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.club";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_ID_ADDRESS = "ID_ADDRESS";

    private static ClubResolver instance;

    public static ClubResolver getInstance() {
        if (instance == null) {
            instance = new ClubResolver();
        }
        return instance;
    }

    public List<Club> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        String[] columns = new String[]{COLUMN_ID, COLUMN_ID_ADDRESS, COLUMN_NAME};
        return query(contentResolver, CONTENT_URI, columns, null, null);
    }

    @Override
    protected Club createModel() {
        return new Club();
    }

    @Override
    protected void updateModel(Club model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_NAME.equalsIgnoreCase(column)) {
            model.setName(data);
        } else if (COLUMN_ID_ADDRESS.equalsIgnoreCase(column)) {
            model.setSubId(data == null ? null : Long.parseLong(data));
        }
    }
}
