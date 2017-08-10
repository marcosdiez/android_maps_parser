package com.marcosdiez.maps_parser.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class LineParser implements HasLatLongInterface {
    final static String TAG = "LineParser";
    final static Pattern regExHtmlLine = Pattern.compile("\\[\\[\\[-?\\d+\\.\\d+,(-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)\\]");

    private String latitude = null;
    private String longitude = null;

    public boolean hasValue() {
        return latitude != null && longitude != null;
    }

    public LineParser() {}

    public boolean parse(String line) {
        Matcher m = regExHtmlLine.matcher(line);
        if (m.find() && m.groupCount() >= 4) {
            latitude = m.group(3);
            longitude = m.group(1);
            return true;
        }
        return false;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
