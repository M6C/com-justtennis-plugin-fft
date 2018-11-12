package com.justtennis.plugin.fb.query.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FBProfilPublicationResponse {

    public List<TimeLineItem> timeLineList = new ArrayList<>();

    public static class TimeLineItem implements Serializable {
        public String date;
        public String text;

        @Override
        public String toString() {
            return "TimeLineItem{" +
                    "date='" + date + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FBProfilPublicationResponse{" +
                "timeLineList=" + (timeLineList == null ? 0 : timeLineList.size()) +
                '}';
    }
}