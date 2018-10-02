package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.model.Saison;

import java.util.Date;
import java.util.List;

public class SaisonResolver extends AbstractResolver<Saison> {

    private static final String TAG = SaisonResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.saison";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_BEGIN = "BEGIN";
    private static final String COLUMN_END = "END";
    private static final String COLUMN_ACTIVE = "ACTIVE";

    private static final String[] COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_BEGIN, COLUMN_END, COLUMN_ACTIVE};

    private static SaisonResolver instance;

    public static SaisonResolver getInstance() {
        if (instance == null) {
            instance = new SaisonResolver();
        }
        return instance;
    }

    public List<Saison> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        return query(contentResolver, null, null);
    }

    public Long createSaison(Context context, String millesime) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, millesime);
        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);

        return getIdFromUri(uri);
    }

    @Override
    protected Saison createModel() {
        return new Saison();
    }

    @Override
    protected void updateModel(Saison model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_NAME.equalsIgnoreCase(column)) {
            model.setName(data);
        } else if (COLUMN_BEGIN.equalsIgnoreCase(column)) {
            model.setBegin(data == null ? null : new Date(Long.parseLong(data)));
        } else if (COLUMN_END.equalsIgnoreCase(column)) {
            model.setEnd(data == null ? null : new Date(Long.parseLong(data)));
        } else if (COLUMN_ACTIVE.equalsIgnoreCase(column)) {
            model.setActive(data == null ? false : Boolean.valueOf(data));
        }
    }

    @Override
    protected Uri getUri() {
        return CONTENT_URI;
    }

    @Override
    protected String[] getColumns() {
        return COLUMNS;
    }
}
