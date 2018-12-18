package com.justtennis.plugin.shared.tool;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
    private static final String REGEX_ATTRIBUT_VALUE = "(?:\"%s\":\")(.*?)(?:\")";
    private static final String REGEX_UNICODE = "(\\\\u[0-9a-zA-Z]{4})";

    private JsonUtil() {
    }

    public static String extratAttributValue(String json, String attribut) {
        StringBuilder ret = new StringBuilder();
        if (json != null && !json.isEmpty()) {
            String format = String.format(REGEX_ATTRIBUT_VALUE, attribut);
            Pattern pattern = Pattern.compile(format);
            Matcher matcher = pattern.matcher(json);
            while (matcher.find()) {
                String str = matcher.group(1);
                str = replaceUnicode(str);
                str = str.replaceAll("\\\\/", "/");
                str = str.replaceAll("\\\\n", "\n");
                ret.append(str);
            }
        }
        System.out.println(String.format("JsonUtil_extratAttributValue_%s.json:%s", attribut, ret.toString()));
        return ret.toString();
    }

    private static String replaceUnicode(String str) {
        Map<Integer, String> mapUnicode = new HashMap<>();
        Pattern pattern = Pattern.compile(REGEX_UNICODE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String unicode = matcher.group(1);
            if (!mapUnicode.containsValue(unicode)) {
                mapUnicode.put(charFromUnicode(unicode), unicode);
            }
        }

        for(Integer key : mapUnicode.keySet()) {
            String s = mapUnicode.get(key).replaceAll("\\\\", "\\\\\\\\");
            str = str.replaceAll(s, new String(new char[]{(char)key.intValue()}));
        }
        return str;
    }

    private static Integer charFromUnicode(String unicode) {
        String s = unicode.substring(2).replaceFirst("^0*", "");
//        byte b = Byte.parseByte(s, 8);
//        return Byte.toString(b);
        return Integer.parseInt(s, 16);
    }
}
