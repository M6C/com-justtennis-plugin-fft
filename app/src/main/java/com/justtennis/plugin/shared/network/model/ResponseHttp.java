package com.justtennis.plugin.shared.network.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseHttp {

    public int statusCode;
    public List<ResponseElement> header = new ArrayList<>();
    public List<ResponseElement> headerCookie = new ArrayList<>();
//    public String pathRedirect;
    public String body;
//    public String action;
//    public Map<String, String> input = new HashMap<>();
//    public ResponseElement login = new ResponseElement();
//    public ResponseElement password = new ResponseElement();
//    public ResponseElement button = new ResponseElement();


    public String getHeader(String name) {
        for(ResponseElement head : header) {
            if (name.equalsIgnoreCase(head.name)) {
                return head.value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ResponseHttp{" +
                "statusCode=" + statusCode +
                ", header=" + header +
                ", body='" + body + '\'' +
                '}';
    }
}