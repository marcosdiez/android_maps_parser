package com.marcosdiez.maps_parser;

import android.util.Log;

/**
 * Created by Marcos on 2017-08-06.
 */

public class MdLogger {
    public static void say(String msg) {
        if (System.getProperty("java.specification.vendor").contains("Android")) {
            Log.d("MapsParser", msg);
        } else {
            System.out.println(msg);
        }
    }
}
