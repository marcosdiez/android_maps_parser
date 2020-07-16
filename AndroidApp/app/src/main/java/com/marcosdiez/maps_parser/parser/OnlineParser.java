package com.marcosdiez.maps_parser.parser;

import com.marcosdiez.maps_parser.MdLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marcos on 2017-08-09.
 */

public class OnlineParser {
    private String theUrl;
    private HasLatLongInterface hasLatLongInterface = null;
    private String normalizedUrl = null;

    public OnlineParser() {
        MdLogger.say("------------------");
    }

    public String getUrl() {
        return theUrl;
    }

    public String getLatitude() {
        if (hasLatLongInterface == null) {
            return null;
        }
        return hasLatLongInterface.getLatitude();
    }

    public String getLongitude() {
        if (hasLatLongInterface == null) {
            return null;
        }
        return hasLatLongInterface.getLongitude();
    }

    public boolean hasValue() {
        return getLongitude() != null && getLatitude() != null;
    }

    public boolean parse(String url) throws IOException {
        if (parseOffline(url)) {
            return true;
        }
        return parseOnline();
    }

    public boolean parseOffline(String url) throws MalformedURLException {
        MdLogger.say("Parsing URL: [" + url +"]");
        this.theUrl = url;
        UrlParser urlParser = new UrlParser();
        urlParser.parse(url);

        if (urlParser.hasValue()) {
            this.hasLatLongInterface = urlParser;
            MdLogger.say("Parsed Coordinate From URL: " + hasLatLongInterface.getLatitude() + " / " + hasLatLongInterface.getLongitude());
            return true;
        }
        normalizedUrl = urlParser.getNormalizedUrl();
        return false;
    }

    public boolean parseOnline() throws IOException {
        return parseUrlOnline(normalizedUrl);
    }

    private boolean parseUrlOnline(String url) throws IOException {
        MdLogger.say("Fetching URL to parse it's contents...");
        URL aUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) (aUrl.openConnection());
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        String location = conn.getHeaderField("Location");
        if(location == null){
            location = conn.getHeaderField("location"); // HTTP2
        }
        if (location != null) {
            return parse(location);
        }
        return parseUrlContent(conn);
    }

    private boolean parseUrlContent(HttpURLConnection conn) throws IOException {
        InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
        BufferedReader buff = new BufferedReader(in);
        LineParser lineParser = new LineParser();
        String line;
        do {
            line = buff.readLine();
            lineParser.parse(line);
            if (lineParser.hasValue()) {
                hasLatLongInterface = lineParser;
                MdLogger.say("Parsed Coordinate From Content: " + hasLatLongInterface.getLatitude() + " / " + hasLatLongInterface.getLongitude());
                return true;
            }
        } while (line != null);
        return false;
    }
}
