package com.justtennis.plugin.fft.resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.justtennis.plugin.fft.model.User;

import java.util.List;

public class UserResolver extends AbstractResolver<User> {

    private static final String TAG = UserResolver.class.getName();

    private static final String CONTENT_AUTHORITY = "justtennis.com.justtennis.provider.user";
    private static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String COLUMN_ID_SAISON = "ID_SAISON";
    private static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    private static final String COLUMN_LASTNAME = "LASTNAME";
    private static final String COLUMN_BIRTHDAY = "BIRTHDAY";
    private static final String COLUMN_PHONENUMBER = "PHONENUMBER";
    private static final String COLUMN_ADDRESS = "ADDRESS";
    private static final String COLUMN_POSTALCODE = "POSTALCODE";
    private static final String COLUMN_LOCALITY = "LOCALITY";
    private static final String COLUMN_ID_RANKING = "ID_RANKING";
    private static final String COLUMN_ID_RANKING_ESTIMAGE = "ID_RANKING_ESTIMAGE";

    private static final String[] COLUMNS = new String[]{
            COLUMN_ID, COLUMN_ID_SAISON, COLUMN_FIRSTNAME,
            COLUMN_LASTNAME, COLUMN_BIRTHDAY, COLUMN_PHONENUMBER,
            COLUMN_ADDRESS, COLUMN_POSTALCODE, COLUMN_LOCALITY,
            COLUMN_ID_RANKING, COLUMN_ID_RANKING_ESTIMAGE};

    private static UserResolver instance;

    public static UserResolver getInstance() {
        if (instance == null) {
            instance = new UserResolver();
        }
        return instance;
    }

    public List<User> queryAll(Context context) {
        ContentResolver contentResolver = context.getContentResolver();

        return query(contentResolver, null, null);
    }

    public List<User> queryBySaison(Context context, Long idSaison) {
        ContentResolver contentResolver = context.getContentResolver();
        String selection = " " + COLUMN_ID_SAISON + " = ?";
        String[] selectionArgs = {idSaison.toString()};
        return query(contentResolver, selection, selectionArgs);
    }

    public Long createUser(Context context, Long idSaison, User user) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID_SAISON, idSaison);
        contentValues.put(COLUMN_FIRSTNAME, user.getFirstName());
        contentValues.put(COLUMN_LASTNAME, user.getLastName());
        contentValues.put(COLUMN_BIRTHDAY, user.getBirthday());
        contentValues.put(COLUMN_PHONENUMBER, user.getPhonenumber());
        contentValues.put(COLUMN_ADDRESS, user.getAddress());
        contentValues.put(COLUMN_POSTALCODE, user.getPostalCode());
        contentValues.put(COLUMN_LOCALITY, user.getLocality());
        contentValues.put(COLUMN_ID_RANKING, user.getIdRanking());
        contentValues.put(COLUMN_ID_RANKING_ESTIMAGE, user.getIdRankingEstimate());
        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);

        return getIdFromUri(uri);
    }

    @Override
    protected User createModel() {
        return new User();
    }

    @Override
    protected void updateModel(User model, String column, String data) {
        if (COLUMN_ID.equalsIgnoreCase(column)) {
            model.setId(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_SAISON.equalsIgnoreCase(column)) {
            model.setIdSaison(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_FIRSTNAME.equalsIgnoreCase(column)) {
            model.setFirstName(data);
        } else if (COLUMN_LASTNAME.equalsIgnoreCase(column)) {
            model.setLastName(data);
        } else if (COLUMN_BIRTHDAY.equalsIgnoreCase(column)) {
            model.setBirthday(data);
        } else if (COLUMN_PHONENUMBER.equalsIgnoreCase(column)) {
            model.setPhonenumber(data);
        } else if (COLUMN_ADDRESS.equalsIgnoreCase(column)) {
            model.setAddress(data);
        } else if (COLUMN_POSTALCODE.equalsIgnoreCase(column)) {
            model.setPostalCode(data);
        } else if (COLUMN_LOCALITY.equalsIgnoreCase(column)) {
            model.setLocality(data);
        } else if (COLUMN_ID_RANKING.equalsIgnoreCase(column)) {
            model.setIdRanking(data == null ? null : Long.parseLong(data));
        } else if (COLUMN_ID_RANKING_ESTIMAGE.equalsIgnoreCase(column)) {
            model.setIdRankingEstimate(data == null ? null : Long.parseLong(data));
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