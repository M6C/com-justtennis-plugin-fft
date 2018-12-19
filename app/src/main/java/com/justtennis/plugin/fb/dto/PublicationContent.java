package com.justtennis.plugin.fb.dto;

import com.justtennis.plugin.fb.common.FBConfiguration;
import com.justtennis.plugin.fb.query.response.FBProfilPublicationResponse;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicationContent {

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
                return FBConfiguration.sdfFBTimeLine.parse(item.date);
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
