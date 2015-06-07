package com.marcosdiez.maps_parser;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class ParseInternetUrl {

    final private String TAG = "InternetUrl";
//    ParseStaticUrl parsedUrl = null;
//
//    public ParseInternetUrl(ParseStaticUrl parsedUrl) {
//        this.parsedUrl = parsedUrl;
//    }
//
//    public ParseStaticUrl resolve() {
//        System.out.println("Resolving URL in the Internet");
//
//        try {
//            while (!parsedUrl.hasValue()) {
//                URL location = resolveUrl(parsedUrl.getUrl());
//                parsedUrl = new ParseStaticUrl(location);
//            }
//            return parsedUrl;
//
//        } catch (java.io.IOException e) {
//            return null;
//            //throw new InvalidParameterException(url.toString());
//        }
//    }

    public static URL resolveUrl(URL theUrl) throws IOException {
        String theUrlString = theUrl.toString();
        if (theUrl.getHost().equals("goo.gl")) {
            return new URL(resolveGogGlUrl(theUrl));
        }
        if (theUrlString.contains("cid=")) {
            return new URL(getCidCoordinatesFromUrl(theUrlString));
        }
        theUrlString = theUrlString.replace("http://", "https://");
        String json_output = "&output=json";
        if (!theUrlString.contains(json_output)) {
            theUrlString += json_output;
        }

        return new URL(getCoordinatesFromGoogle(theUrlString));

    }

    private static String resolveGogGlUrl(URL theUrl) throws IOException {
        System.out.println("resolveGogGlUrl");
        HttpURLConnection con = (HttpURLConnection) (theUrl.openConnection());
        con.setInstanceFollowRedirects(false);
        con.connect();
        String location = con.getHeaderField("Location");
        return location;
    }


    private static String getCidCoordinatesFromUrl(String url) throws IOException {
        System.out.println("getCidCoordinatesFromUrl");
        Uri newUri = Uri.parse(url);
        String cid = newUri.getQueryParameter("cid");
        return getCoodinatesFromCid(cid);
    }

    public static String getCoodinatesFromCid(String cid) throws IOException {
        System.out.println("getCoodinatesFromCid");
        final String URL_FORMAT = "https://maps.google.com/maps?cid=%s&q=a&output=json";
        String theUrl = String.format(URL_FORMAT, cid);
        return getCoordinatesFromGoogle(theUrl);
    }

    private static String getCoordinatesFromGoogle(String theUrl) throws IOException {
        // http://stackoverflow.com/questions/23968937/android-convert-cid-location-to-coordinates
        System.out.println("getCoordinatesFromGoogle: " + theUrl);

        final String LATLNG_BEFORE = "viewport:{center:{";
        final String LATLNG_AFTER = "}";
        final String LATLNG_SEPARATOR = ",";
        final String LAT_PREFIX = "lat:";
        final String LNG_PREFIX = "lng:";

        try {


            URL theURL = new URL(theUrl);
            HttpURLConnection urlConnection = null;
            StringBuilder total = new StringBuilder();
            try {
                urlConnection = (HttpURLConnection) theURL.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                String result;
                while ((result = r.readLine()) != null) {
                    total.append(result);
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            String text = total.toString();

            int startIndex = text.indexOf(LATLNG_BEFORE);
            if (startIndex == -1)
                return null;

            startIndex += LATLNG_BEFORE.length();
            int endIndex = text.indexOf(LATLNG_AFTER, startIndex);

            // Should be "lat:<number>,lng:<number>"
            String[] parts = text.substring(startIndex, endIndex).split(LATLNG_SEPARATOR);
            if (parts.length != 2)
                return null;

            if (parts[0].startsWith(LAT_PREFIX))
                parts[0] = parts[0].substring(LAT_PREFIX.length());
            else
                return "";

            if (parts[1].startsWith(LNG_PREFIX))
                parts[1] = parts[1].substring(LNG_PREFIX.length());
            else
                return "";

            return "http://maps.google.com/maps?q=" + parts[0] + "," + parts[1];

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
