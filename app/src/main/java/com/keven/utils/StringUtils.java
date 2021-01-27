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
import java.util.regex.PatternSyntaxException;

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

    /*
     * 已为您将音量<phoneme py="tiao2">调</phoneme>至最高值30
     * 过滤<>
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx="<[^<>]*>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
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