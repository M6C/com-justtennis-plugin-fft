package com.justtennis.plugin.shared.network.model;

import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;

public class ResponseHttp {

    public int statusCode;
    public List<ResponseElement> header = new ArrayList<>();
    public List<ResponseElement> headerCookie = new ArrayList<>();
    public String body;
    public BufferedSource raw;


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