package com.justtennis.plugin.fft.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.justtennis.plugin.fft.R;
import com.justtennis.plugin.fft.dto.MatchDto;
import com.justtennis.plugin.fft.manager.InviteManager;
import com.justtennis.plugin.fft.model.Player;
import com.justtennis.plugin.fft.model.Ranking;
import com.justtennis.plugin.fft.model.Saison;
import com.justtennis.plugin.fft.query.response.MillesimeMatchResponse;
import com.justtennis.plugin.fft.resolver.InviteResolver;
import com.justtennis.plugin.fft.resolver.PlayerResolver;
import com.justtennis.plugin.fft.resolver.RankingResolver;
import com.justtennis.plugin.fft.resolver.SaisonResolver;
import com.justtennis.plugin.fft.resolver.ScoreSetResolver;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public abstract class CreateInviteTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = CreateInviteTask.class.getName();

    private static final long ID_UNKNOWN_PLAYER = -1l;
    private static final long ID_NC_RANKING = -1l;

    private SimpleDateFormat sdfFFT;
    private DateFormat sdfBirth;

    private Context context;
    private List<MillesimeMatchResponse.MatchItem> listMatch;
    private List<MatchDto> listMatchDto;
    private String millesime;

    protected CreateInviteTask(Context context, List<MillesimeMatchResponse.MatchItem> listMatch, List<MatchDto> listMatchDto, String millesime) {
        this.context = context;
        this.listMatch = listMatch;
        this.listMatchDto = listMatchDto;
        this.millesime = millesime;
        sdfFFT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        sdfBirth = new SimpleDateFormat(context.getString(R.string.msg_common_format_date), Locale.FRANCE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean ret = Boolean.TRUE;
        Long idSaison = createOrGetSaison(millesime);

        if (idSaison != null) {
            for (int i=0; i<listMatch.size(); i++) {
                MillesimeMatchResponse.MatchItem match = listMatch.get(i);
                MatchDto dto = listMatchDto.get(i);
                if (dto.selected) {
                    createInvite(match, idSaison);
                }
            }
        }
        return ret;
    }

    private void createInvite(MillesimeMatchResponse.MatchItem match, Long idSaison) {
        Long idPlayer = createOrGetPlayer(match, idSaison);
        Long idRanking = getRanking(match);
        Date date = new Date();
        try {
            date = sdfFFT.parse(match.date);
        } catch (ParseException e) {
            Log.e(TAG, MessageFormat.format("Formatting match.date:{0}", match.date), e);
        }
        String scoreResult = match.vicDef.startsWith("D") ? InviteManager.SCORE_RESULT.DEFEAT.toString() :  InviteManager.SCORE_RESULT.VICTORY.toString();
        Long idClub = null;

        InviteResolver inviteResolver = InviteResolver.getInstance();
        Long idInvite = inviteResolver.createInvite(context, idSaison, idPlayer, idRanking, date, scoreResult, idClub);

        createScoreSet(match, idInvite);
    }

    @Nullable
    private Long createOrGetPlayer(MillesimeMatchResponse.MatchItem match, Long idSaison) {
        Long ret = null;
        String playerName = match.name;
        if (playerName != null) {
            String firstname = playerName;
            String lastname = "";
            int iSep = playerName.indexOf(' ');
            if (iSep > 0) {
                firstname = playerName.substring(0, iSep);
                lastname = playerName.substring(iSep+1);
            }

            String birthday = getBirthday(match);

            PlayerResolver playerResolver = PlayerResolver.getInstance();
            List<Player> listPlayer = playerResolver.queryByName(context, firstname, lastname);
            if (listPlayer == null || listPlayer.isEmpty()) {
                Long idRanking = getRanking(match);
                ret = playerResolver.createPlayer(context, firstname, lastname, birthday, idSaison, idRanking);
            } else {
                ret = listPlayer.get(0).getId();
            }

            if (ret == null) {
                // Set ret to UNKNOW
                ret = ID_UNKNOWN_PLAYER;
            }
        }
        return ret;
    }

    @Nullable
    private String getBirthday(MillesimeMatchResponse.MatchItem match) {
        String ret = null;
        if (match.year != null && match.year.length() == 4) {
            try {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(match.year));
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR, 1);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                ret = sdfBirth.format(calendar.getTime());
            } catch (RuntimeException e) {
                Log.e(TAG, MessageFormat.format("Birthday formatting match.year:{0}", match.year), e);
            }
        }
        return ret;
    }

    private long getRanking(MillesimeMatchResponse.MatchItem match) {
        long ret = ID_NC_RANKING;
        if (match.ranking != null && !match.ranking.isEmpty()) {
            RankingResolver rankingResolver = RankingResolver.getInstance();
            List<Ranking> listRanking = rankingResolver.queryByRanking(context, match.ranking);
            if (listRanking != null && !listRanking.isEmpty()) {
                ret = listRanking.get(0).getId();
            }
        }
        if (ret == ID_NC_RANKING) {
            Log.w(TAG, MessageFormat.format("Ranking not found match.ranking:{0}", match.ranking));
        }
        return ret;
    }

    private void createScoreSet(MillesimeMatchResponse.MatchItem match, Long idInvite) {
        try {
            if (match.score != null && !match.score.isEmpty()) {
                Log.w(TAG, MessageFormat.format("ScoreSet creation for idInvite:{0} match.score:{1}", idInvite, match.score));
                ScoreSetResolver rankingResolver = ScoreSetResolver.getInstance();

                String[] listSet = match.score.split(" ");
                if (listSet.length > 0) {
                    int order = 0;
                    for (String set : listSet) {
                        String[] score = set.split("/");
                        if (score.length == 2) {
                            rankingResolver.createScoreSet(context, idInvite, order++, Integer.parseInt(score[0]), Integer.parseInt(score[1]));
                        }
                    }
                }

            }
        } catch (RuntimeException e) {
            Log.e(TAG, "ScoreSet creation", e);
        }
    }

    private Long createOrGetSaison(String millesime) {
        Long idSaison = null;
        SaisonResolver saisonResolver = SaisonResolver.getInstance();
        List<Saison> listSaison = saisonResolver.queryAll(context);
        for(Saison s : listSaison) {
            if (s.getName().endsWith(millesime)) {
                idSaison = s.getId();
                break;
            }
        }

        if (idSaison == null) {
            idSaison = saisonResolver.createSaison(context, millesime);
        }
        return idSaison;
    }
}
