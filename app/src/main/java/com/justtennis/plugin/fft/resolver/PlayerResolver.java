package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.manager.TypeManager;
import com.justtennis.plugin.fft.model.Player;

import java.util.List;

public class PlayerResolver extends AbstractResolver<Player> {

    private static final String TAG = PlayerResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.player";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    private static final String COLUMN_LASTNAME = "LASTNAME";
    private static final String COLUMN_BIRTHDAY = "BIRTHDAY";
    private static final String COLUMN_PHONENUMBER = "PHONENUMBER";
    private static final String COLUMN_ADDRESS = "ADDRESS";
    private static final String COLUMN_POSTALCODE = "POSTALCODE";
    private static final String COLUMN_LOCALITY = "LOCALITY";
    private static final String COLUMN_ID_SAISON = "ID_SAISON";
    private static final String COLUMN_ID_TOURNAMENT = "ID_TOURNAMENT";
    private static final String COLUMN_ID_CLUB = "ID_CLUB";
    private static final String COLUMN_ID_RANKING = "ID_RANKING";
    private static final String COLUMN_ID_RANKING_ESTIMAGE = "ID_RANKING_ESTIMAGE";
    private static final String COLUMN_ID_ADDRESS = "ID_ADDRESS";
    private static final String COLUMN_ID_EXTERNAL = "ID_EXTERNAL";
    private static final String COLUMN_ID_GOOGLE = "ID_GOOGLE";
    private static final String COLUMN_TYPE = "TYPE";

    private static PlayerResolver instance;

    public static PlayerResolver getInstance() {
        if (instance == null) {
            instance = new PlayerResolver();
        }
        return instance;
    }

    public List<Player> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = new String[]{COLUMN_ID, COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_BIRTHDAY, COLUMN_TYPE, COLUMN_ID_SAISON, COLUMN_ID_CLUB};
        String selection = " " + COLUMN_TYPE + " = ? ";
        String[] selectionArgs = {TypeManager.TYPE.COMPETITION.toString()};
        return query(contentResolver, CONTENT_URI, columns, selection, selectionArgs);
    }

    @Override
    protected Player createModel() {
        return new Player();
    }

    @Override
    protected void updateModel(Player model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(Long.parseLong(data));
        } else if (COLUMN_FIRSTNAME.equalsIgnoreCase(column)) {
            model.setFirstName(data);
        } else if (COLUMN_LASTNAME.equalsIgnoreCase(column)) {
            model.setLastName(data);
        } else if (COLUMN_BIRTHDAY.equalsIgnoreCase(column)) {
            model.setBirthday(data);
        } else if (COLUMN_TYPE.equalsIgnoreCase(column)) {
            model.setType(TypeManager.TYPE.COMPETITION);
        } else if (COLUMN_ID_SAISON.equalsIgnoreCase(column)) {
            model.setIdSaison(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_CLUB.equalsIgnoreCase(column)) {
            model.setIdClub(data == null ? null : Long.parseLong(data));
        }
    }
}
