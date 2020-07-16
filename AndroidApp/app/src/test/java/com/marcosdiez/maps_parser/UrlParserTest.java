package com.marcosdiez.maps_parser;

import com.marcosdiez.maps_parser.parser.LineParser;
import com.marcosdiez.maps_parser.parser.OnlineParser;
import com.marcosdiez.maps_parser.parser.UrlParser;

import org.junit.Assert;

import java.io.IOException;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class UrlParserTest {

    @org.junit.Test
    public void testInternetUrl() throws Exception {
        MdLogger.say("Java Version: " + System.getProperty("java.version"));
        testUrlHelper("http://goo.gl/maps/vL3w1", "45.829415", "15.978208");    // this is easy
        testUrlHelper("http://goo.gl/maps/4SGx", "48.209953", "16.4261");    // this is easy

        testUrlHelperAlmostEqual("http://goo.gl/maps/n4SGx", 45.846364000000001, 15.968997999999999); // this is harder

        testUrlHelperAlmostEqual("http://maps.google.com/?q=Sutarwadi+Baner+Gaon+Rd%2C+Baner&ftid=0x3bc2becfa70b8b3f:0xd210c15f634bcec3&hl=en&gl=u", 18.559002, 73.7802239);

        testUrlHelperAlmostEqual("http://maps.google.com/?cid=10083010587172303116&hl=en&gl=us" , 45.814771999999998, 15.974068000000001);
        testUrlHelperAlmostEqual("http://goo.gl/maps/e6rdH" , 45.814771999999998, 15.974068000000001);
        testUrlHelperAlmostEqual("http://maps.google.com/?q=Sutarwadi+Baner+Gaon+Rd%2C+Baner&ftid=0x3bc2becfa70b8b3f:0xd210c15f634bcec3&hl=en&gl=u", 18.5596948, 73.7802239);
        testUrlHelperAlmostEqual("https://maps.google.com/maps?q=Stenglinstrasse+2,+Kriegshaber,+Augsburg,+Deutschland", 48.384905, 10.8358127);
    }

    @org.junit.Test
    public void testLine() throws Exception {
        LineParser lp = new LineParser();

        lp.parse(" cacheResponse([[[2641.732748957977,15.9691179,45.846342],[0,0,0],[1024,730],13.10000038146973],\"/maps-lite/js/2/ml_20170801_1\",107");

        Assert.assertEquals(true, lp.hasValue());
        Assert.assertEquals("45.846342", lp.getLatitude());
        Assert.assertEquals("15.9691179", lp.getLongitude());
    }

    @org.junit.Test
    public void testOfflineUrls() throws Exception {

        testUrlHelper("https://www.google.com/maps/search/?api=1&query=England%2c+United+Kingdom&center=52.77146932915935%2c-1.6809082031249951&vdpId=10270&ppois=52.77146932915935_-1.6809082031249951_England%2c+United+Kingdom",
        "52.77146932915935", "-1.6809082031249951");
        testUrlHelper("https://www.google.com/maps/dir/?api=1&travelmode=&origin=Current%20Location&destination=-31.95363998413086%2c115.8572006225586",
                "-31.95363998413086","115.8572006225586");
        testUrlHelper("http://www.google.com/maps?q=10.11,12.13", "10.11", "12.13");
        testUrlHelper("http://www.google.com/maps?q=10.11,-12.13", "10.11", "-12.13");
        testUrlHelper("http://www.google.com/maps?q=-10.11,12.13", "-10.11", "12.13");
        testUrlHelper("http://www.google.com/maps?q=-10.11,-12.13", "-10.11", "-12.13");

        testUrlHelper("http://www.google.com/maps?q=45.8294151,15.978208");
        testUrlHelper("https://www.google.com/maps?q=45.8294151,15.978208");
        testUrlHelper("http://www.google.com/maps?daddr=45.8294151,15.978208");
        testUrlHelper("https://www.google.com/maps?daddr=45.8294151,15.978208");

        testUrlHelper("https://www.google.hr/maps/@45.8294151,15.978208,14z");
        testUrlHelper("https://www.google.hr/maps/place/Golf+%26+Country+Club+Zagreb/@45.8294151,15.978208,13z/data=!4m2!3m1!1s0x0000000000000000:0x1c38bd501b9ed201");
        testUrlHelper("https://www.google.com.br/maps/dir//GO-334,+Crix%C3%A1s+-+GO,+76510-000/@45.8294151,15.978208,13z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x936818cb9a754013:0x64b7078156975a32!2m2!1d-50.1042642!2d-14.9715302");


        testUrlHelper("https://maps.yandex.com/?rtext=~45.8294151,15.978208");
        testUrlHelper("http://maps.yandex.com/?rtext=~45.8294151,15.978208");

        testUrlHelper("http://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");
        testUrlHelper("https://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");

        testUrlHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");
        testUrlHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");

        testUrlHelper("waze://?ll=45.8294151,15.978208&z=10");
    }

    @org.junit.Test
    public void testNonUrl() throws Exception {
        UrlParser urlParser = new UrlParser();
        urlParser.parse("https://www.google.com");
        Assert.assertEquals(false, urlParser.hasValue());
    }

    @org.junit.Test
    public void testUrlParser() throws Exception {
        UrlParser urlParser = new UrlParser();
        urlParser.parse("https://www.google.com/maps/dir/?api=1&travelmode=&origin=CurrentLocation&destination=-31.95363998413086,115.8572006225586");
        Assert.assertEquals(true, urlParser.hasValue());
        Assert.assertEquals("-31.95363998413086", urlParser.getLatitude());
        Assert.assertEquals("115.8572006225586", urlParser.getLongitude());

        urlParser = new UrlParser();
        urlParser.parse("https://www.google.com/maps/dir/?api=1&travelmode=&origin=CurrentLocation&destination=-31.95363998413086%2c115.8572006225586");
        Assert.assertEquals(true, urlParser.hasValue());
        Assert.assertEquals("-31.95363998413086", urlParser.getLatitude());
        Assert.assertEquals("115.8572006225586", urlParser.getLongitude());

    }

    private void testUrlHelper(String url) throws IOException {
        String lat = "45.8294151";
        String lng = "15.978208";
        testUrlHelper(url, lat, lng);
    }

    private void testUrlHelper(String url, String latitude, String longitude) throws IOException {
        OnlineParser onlineParser = new OnlineParser();
        onlineParser.parse(url);
        Assert.assertEquals(true, onlineParser.hasValue());
        Assert.assertEquals(latitude, onlineParser.getLatitude());
        Assert.assertEquals(longitude, onlineParser.getLongitude());
    }

    private void testUrlHelperAlmostEqual(String url, double latitude, double longitude) throws IOException, NumberFormatException {
        OnlineParser onlineParser = new OnlineParser();
        onlineParser.parse(url);

        Assert.assertEquals(true, onlineParser.hasValue());

        Assert.assertEquals(latitude, Double.parseDouble(onlineParser.getLatitude()), 0.01);
        Assert.assertEquals(longitude, Double.parseDouble(onlineParser.getLongitude()), 0.01);
    }
}