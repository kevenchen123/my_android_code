package com.keven.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liangfeizc on 6/2/15.
 */
public class StringUtils {
    private static Pattern sWordPattern = Pattern.compile("\\w+");

    public static String capitalizeSentence(final String sentence) {
        String result = sentence;
        Matcher matcher = sWordPattern.matcher(result);
        while (matcher.find()) {
            String word = matcher.group();
            result = result.replace(matcher.group(), capitalize(word));
        }
        return result;
    }

    public static String capitalize(final String word) {
        if (word.length() > 1) {
            return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
        }
        return word;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        String text = writer.toString();
        return text;
    }
}