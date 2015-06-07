package com.marcosdiez.maps_parser;

import android.util.Log;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class ParseStaticUrl {

    final static String TAG = "ParsedMapsUrl";
    final static Pattern mapsRegEx = Pattern.compile("(-?\\d+(\\.\\d+)?),(-?\\d+(\\.\\d+)?)");

    String latitude = null;
    String longitude = null;
    URL the_url = null;

    ParseInternetUrl parseInternetUrl = null;

    public boolean hasValue() {
        return latitude != null && longitude != null;
    }

    public URL getUrl() {
        return the_url;
    }

    public ParseStaticUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        this.the_url = url;
        String hostName = url.getHost();

        if (hostName.contains("ingress.com")) {
            parseIngressUrl(url.toString());
            return;
        }
        parseUrl(url.toString());
    }

    private void parseIngressUrl(String url) {
        String testUrl = url.replace("?", "&");
        if (testUrl.contains("&pll")) {
            parseUrl(testUrl.substring(testUrl.indexOf("&pll")));
        } else {
            parseUrl(url);
        }
    }

    private void say(String msg) {
        if (System.getProperty("java.specification.vendor").contains("Android")) {
            Log.d(TAG, msg);
        } else {
            System.out.println(msg);
        }
    }

    private void parseUrl(String url) {
        say("---------------");
        say(url);
        Matcher m = mapsRegEx.matcher(url);
        if (m.find() && m.groupCount() >= 4) {
            latitude = m.group(1);
            longitude = m.group(3);
            for (int i = 0; i < m.groupCount(); i++) {
                say("Found value: " + i + "\t" + m.group(i));
            }
        } else {
            say("NO MATCH");
        }
        say("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_");
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
