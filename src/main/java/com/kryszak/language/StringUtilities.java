package com.kryszak.language;

import java.io.UnsupportedEncodingException;

public class StringUtilities {

    private static final String STRING_EMPTY = "";

    private static final String ISO_8859_1 = "ISO-8859-1";

    private static final String UTF_8 = "UTF-8";

    public static String translate(String key) {
        return decodeDefaultEncoding(LanguageManager.getInstance().getResources().getString(key));
    }

    private static String decodeDefaultEncoding(String value) {
        try {
            return new String(value.getBytes(ISO_8859_1), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return STRING_EMPTY;
        }
    }


}
