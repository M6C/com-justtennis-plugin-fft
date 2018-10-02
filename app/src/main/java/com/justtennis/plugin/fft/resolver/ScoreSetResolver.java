package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.manager.TypeManager;
import com.justtennis.plugin.fft.model.Invite;
import com.justtennis.plugin.fft.model.ScoreSet;

import java.util.List;

public class ScoreSetResolver extends AbstractResolver<ScoreSet> {

    private static final String TAG = ScoreSetResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.scoreset";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_ID_INVITE = "ID_INVITE";
    private static final String COLUMN_NUMBER = "NUMBER";
    private static final String COLUMN_VALUE1 = "VALUE1";
    private static final String COLUMN_VALUE2 = "VALUE2";

    private static final String[] COLUMNS = new String[]{COLUMN_ID, COLUMN_ID_INVITE, COLUMN_NUMBER, COLUMN_VALUE1, COLUMN_VALUE2};

    private static ScoreSetResolver instance;

    public static ScoreSetResolver getInstance() {
        if (instance == null) {
            instance = new ScoreSetResolver();
        }
        return instance;
    }

    public List<ScoreSet> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        return query(contentResolver, null, null);
    }

    public Long createScoreSet(Context context, Long idInvite, Integer order, Integer value1, Integer value2) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_INVITE, idInvite);
        contentValues.put(COLUMN_NUMBER, order);
        contentValues.put(COLUMN_VALUE1, value1);
        contentValues.put(COLUMN_VALUE2, value2);
        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);

        return getIdFromUri(uri);
    }

    @Override
    protected ScoreSet createModel() {
        return new ScoreSet();
    }

    @Override
    protected void updateModel(ScoreSet model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_INVITE.equalsIgnoreCase(column)) {
            model.setInvite(new Invite(Long.parseLong(data)));
        } else if (COLUMN_NUMBER.equalsIgnoreCase(column)) {
            model.setOrder(Integer.parseInt(data));
        } else if (COLUMN_VALUE1.equalsIgnoreCase(column)) {
            model.setValue1(Integer.parseInt(data));
        } else if (COLUMN_VALUE2.equalsIgnoreCase(column)) {
            model.setValue2(Integer.parseInt(data));
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