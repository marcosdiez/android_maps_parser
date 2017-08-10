package com.marcosdiez.maps_parser.parser;

import java.net.MalformedURLException;

/**
 * Created by Marcos on 2017-08-09.
 */

public interface HasLatLongInterface {

    String getLatitude();

    String getLongitude();

    boolean hasValue();

    boolean parse(String url) throws MalformedURLException;

}
