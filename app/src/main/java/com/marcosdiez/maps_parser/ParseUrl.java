package com.marcosdiez.maps_parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class ParseUrl {

    ParseStaticUrl staticUrl;

    public ParseUrl(String url) throws MalformedURLException {
        if (url.startsWith("waze://")) {
            url = url.replace("waze://", "http://waze.com/");
        }
        URL theUrl = new URL(url);
        staticUrl = new ParseStaticUrl(theUrl);
    }

    public void resolveInternetUrl() throws IOException {
        System.out.println("--------");
        System.out.println("Resolving URL in the Internet");

        try {
            while (!staticUrl.hasValue()) {
                System.out.println("Resolving: " + staticUrl.getUrl().toString());
                URL location = ParseInternetUrl.resolveUrl(staticUrl.getUrl());
                System.out.println("middle");
                staticUrl = new ParseStaticUrl(location);
                System.out.println("ResolvED: " + staticUrl.getUrl().toString());
            }
            System.out.println("Result: " + staticUrl.getUrl().toString());
            return;

        } catch (java.io.IOException e) {
            System.out.println("IO Excaption: " + staticUrl.getUrl().toString());
            return;
            //throw new InvalidParameterException(url.toString());
        }
    }


    public String getUrl() {
        return staticUrl.getUrl().toString();
    }

    public boolean hasValue() {
        return staticUrl.hasValue();
    }

    public String getLatitude() {
        return staticUrl.getLatitude();
    }

    public String getLongitude() {
        return staticUrl.getLongitude();
    }

    public boolean needsInternet() {
        return !staticUrl.hasValue();
    }


}
