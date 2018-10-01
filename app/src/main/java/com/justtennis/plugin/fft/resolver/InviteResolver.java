package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.manager.TypeManager;
import com.justtennis.plugin.fft.model.Club;
import com.justtennis.plugin.fft.model.Invite;
import com.justtennis.plugin.fft.model.Player;
import com.justtennis.plugin.fft.model.Saison;

import java.util.Date;
import java.util.List;

public class InviteResolver extends AbstractResolver<Invite> {

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

    private static InviteResolver instance;

    public static InviteResolver getInstance() {
        if (instance == null) {
            instance = new InviteResolver();
        }
        return instance;
    }

    public List<Invite> queryAllMatch(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        String[] columns = new String[]{COLUMN_ID, COLUMN_ID_SAISON, COLUMN_TYPE, COLUMN_ID_PLAYER, COLUMN_ID_RANKING, COLUMN_ID_CLUB, COLUMN_TIME, COLUMN_SCORE_RESULT};
        String selection = " " + COLUMN_TYPE + " = ? ";
        String[] selectionArgs = {TypeManager.TYPE.COMPETITION.toString()};
        return query(contentResolver, CONTENT_URI, columns, selection, selectionArgs);
    }

    @Override
    protected Invite createModel() {
        return new Invite();
    }

    @Override
    protected void updateModel(Invite model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_SAISON.equalsIgnoreCase(column)) {
            model.setSaison(data == null ? null : new Saison(Long.parseLong(data)));
        } else if (COLUMN_TYPE.equalsIgnoreCase(column)) {
            model.setType(TypeManager.TYPE.COMPETITION);
        } else if (COLUMN_ID_PLAYER.equalsIgnoreCase(column)) {
            model.setPlayer(data == null ? null : new Player(Long.parseLong(data)));
        } else if (COLUMN_ID_RANKING.equalsIgnoreCase(column)) {
            model.setIdRanking(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_CLUB.equalsIgnoreCase(column)) {
            model.setClub(data == null ? null : new Club(Long.parseLong(data)));
        } else if (COLUMN_TIME.equalsIgnoreCase(column)) {
            model.setDate(data == null ? null : new Date(Long.parseLong(data)));
        } else if (COLUMN_SCORE_RESULT.equalsIgnoreCase(column)) {
            model.setScoreResult(data == null ? null : Invite.SCORE_RESULT.valueOf(data));
        }
    }
}
