package com.marcosdiez.maps_parser.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class UrlParser implements HasLatLongInterface {
    final static String TAG = "UrlParser";
    final static Pattern mapsRegEx = Pattern.compile("(-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)");

    private String latitude = null;
    private String longitude = null;

    private String normalizedUrl = null;

    public boolean hasValue() {
        return latitude != null && longitude != null;
    }

    public UrlParser() {}

    public boolean parse(String url) throws MalformedURLException {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        if (url.startsWith("waze://")) {
            url = url.replace("waze://", "http://waze.com/");
        }
        normalizedUrl = url;
        URL theUrl = new URL(url);
        String hostName = theUrl.getHost();

        if (hostName.contains("ingress.com")) {
            return parseIngressUrl(url);
        }

        return parseNormalUrl(url);
    }

    public String getNormalizedUrl() { return normalizedUrl; }

    private boolean parseNormalUrl(String url){
        Matcher m = mapsRegEx.matcher(url);
        if (m.find() && m.groupCount() >= 4) {
            latitude = m.group(1);
            longitude = m.group(3);
            return true;
        }
        return false;
    }

    private boolean parseIngressUrl(String url) {
        String testUrl = url.replace("?", "&");
        if (testUrl.contains("&pll")) {
            return parseNormalUrl(testUrl.substring(testUrl.indexOf("&pll")));
        } else {
            return parseNormalUrl(url);
        }
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
