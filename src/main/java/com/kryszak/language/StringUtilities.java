package com.kryszak.language;

import java.io.UnsupportedEncodingException;

public class StringUtilities {

    public static final String STRING_EMPTY = "";

    public static String decodeDefaultEncoding(String value) {
        try {
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return STRING_EMPTY;
        }
    }

    public static String translate(String key) {
        return decodeDefaultEncoding(LanguageManager.getInstance().getResources().getString(key));
    }
}
