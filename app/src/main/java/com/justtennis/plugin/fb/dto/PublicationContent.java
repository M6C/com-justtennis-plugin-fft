package com.justtennis.plugin.fb.dto;

import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PublicationContent {

    private static SimpleDateFormat sdfFBTimeLine = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);

    private PublicationContent() {}

    public static List<PublicationDto> toDto(List<FBProfilPublicationResponse.TimeLineItem> list) {
        List<PublicationDto> ret = new ArrayList<>();
        long i=1;
        for(FBProfilPublicationResponse.TimeLineItem item : list) {
            ret.add(createDto(i++, item));
        }
        return ret;
    }

    private static PublicationDto createDto(long position, FBProfilPublicationResponse.TimeLineItem item) {
        return new PublicationDto(position, getDate(item), item.text);
    }

    private static Date getDate(FBProfilPublicationResponse.TimeLineItem item) {
        if (item.date != null) {
            try {
                return sdfFBTimeLine.parse(item.date);
            } catch (ParseException e) {
                logMe(e);
            }
        }
        return null;
    }

    private static void logMe(Exception e) {
        e.printStackTrace();
    }
}
