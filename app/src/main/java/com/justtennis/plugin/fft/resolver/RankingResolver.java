package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.model.Player;
import com.justtennis.plugin.fft.model.Ranking;

import java.util.List;

public class RankingResolver extends AbstractResolver<Ranking> {

    private static final String TAG = RankingResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.ranking";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String COLUMN_RANKING = "RANKING";
    public static final String COLUMN_SERIE = "SERIE";
    public static final String COLUMN_POSITION = "POSITION";

    private static final String[] COLUMNS = new String[]{COLUMN_ID, COLUMN_RANKING, COLUMN_SERIE, COLUMN_POSITION};

    private static RankingResolver instance;

    public static RankingResolver getInstance() {
        if (instance == null) {
            instance = new RankingResolver();
        }
        return instance;
    }

    public List<Ranking> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        return query(contentResolver, null, null);
    }

    public List<Ranking> queryByRanking(Context context, String ranking) {
        ContentResolver contentResolver = context.getContentResolver();
        String selection = " " + COLUMN_RANKING + " = ?";
        String[] selectionArgs = {ranking};
        return query(contentResolver, selection, selectionArgs);
    }

    @Override
    protected Ranking createModel() {
        return new Ranking();
    }

    @Override
    protected void updateModel(Ranking model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_RANKING.equalsIgnoreCase(column)) {
            model.setRanking(data);
        } else if (COLUMN_SERIE.equalsIgnoreCase(column)) {
            model.setSerie(data == null ? null : Integer.parseInt(data));
        } else if (COLUMN_POSITION.equalsIgnoreCase(column)) {
            model.setOrder(data == null ? null : Integer.parseInt(data));
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