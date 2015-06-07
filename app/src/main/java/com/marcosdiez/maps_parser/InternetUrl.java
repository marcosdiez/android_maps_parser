package com.marcosdiez.maps_parser;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class InternetUrl {

    URL url = null;

    public InternetUrl(URL theUrl) {
        this.url = theUrl;
    }

    public String resolve() {
        try {
            HttpURLConnection con = (HttpURLConnection) (url.openConnection());
            con.setInstanceFollowRedirects(false);
            con.connect();
            String location = con.getHeaderField("Location");
            return location;
        } catch (java.io.IOException e) {
            return null;
            //throw new InvalidParameterException(url.toString());
        }
    }

}
