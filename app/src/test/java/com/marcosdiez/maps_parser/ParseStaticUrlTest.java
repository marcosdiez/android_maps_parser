package com.marcosdiez.maps_parser;

import java.io.IOException;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class ParseStaticUrlTest {

    @org.junit.Test
    public void testInternetUrlsWithCid() throws Exception {
        String expected = "http://maps.google.com/maps?q=45.814771999999998,15.974068000000001";
        String parsed = ParseInternetUrl.getCoodinatesFromCid("10083010587172303116");
        org.junit.Assert.assertEquals(expected, parsed);
    }

    @org.junit.Test
    public void testInternetUrl() throws Exception {
        testHelper("http://goo.gl/maps/vL3w1", "45.829415", "15.978208");    // this is easy
        testHelper("http://goo.gl/maps/4SGx", "48.209953", "16.4261");    // this is easy
        testHelper("http://goo.gl/maps/n4SGx", "45.846364000000001", "15.968997999999999");    // this is easy
        testHelper("http://maps.google.com/?q=Sutarwadi+Baner+Gaon+Rd%2C+Baner&ftid=0x3bc2becfa70b8b3f:0xd210c15f634bcec3&hl=en&gl=u", "18.559002", "73.778433000000007");

        // these two can not be tested because it uses android native methods... blerg...
        // testHelper("http://maps.google.com/?cid=10083010587172303116&hl=en&gl=us" , "45.814771999999998","15.974068000000001");
        // testHelper("http://goo.gl/maps/e6rdH" , "45.814771999999998","15.974068000000001");
    }

    // @org.junit.Test
    public void testStandardUrl() throws Exception {

        testHelper("http://www.google.com/maps?q=10.11,12.13", "10.11", "12.13");
        testHelper("http://www.google.com/maps?q=10.11,-12.13", "10.11", "-12.13");
        testHelper("http://www.google.com/maps?q=-10.11,12.13", "-10.11", "12.13");
        testHelper("http://www.google.com/maps?q=-10.11,-12.13", "-10.11", "-12.13");

        testHelper("http://www.google.com/maps?q=45.8294151,15.978208");
        testHelper("https://www.google.com/maps?q=45.8294151,15.978208");
        testHelper("http://www.google.com/maps?daddr=45.8294151,15.978208");
        testHelper("https://www.google.com/maps?daddr=45.8294151,15.978208");

        testHelper("https://www.google.hr/maps/@45.8294151,15.978208,14z");
        testHelper("https://www.google.hr/maps/place/Golf+%26+Country+Club+Zagreb/@45.8294151,15.978208,13z/data=!4m2!3m1!1s0x0000000000000000:0x1c38bd501b9ed201");
        testHelper("https://www.google.com.br/maps/dir//GO-334,+Crix%C3%A1s+-+GO,+76510-000/@45.8294151,15.978208,13z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x936818cb9a754013:0x64b7078156975a32!2m2!1d-50.1042642!2d-14.9715302");


        testHelper("https://maps.yandex.com/?rtext=~45.8294151,15.978208");
        testHelper("http://maps.yandex.com/?rtext=~45.8294151,15.978208");

        testHelper("http://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");
        testHelper("https://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");

        testHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");
        testHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");

        testHelper("waze://?ll=45.8294151,15.978208&z=10");

    }

    private void testHelper(String url) throws IOException {
        String lat = "45.8294151";
        String lng = "15.978208";
        testHelper(url, lat, lng);
    }

    private void testHelper(String url, String latitude, String longitude) throws IOException {
        ParseUrl p = new ParseUrl(url);
        if (!p.hasValue()) {
            System.out.println("resolving internet URL");
            p.resolveInternetUrl();
        }
        //   org.junit.Assert.assertEquals(30, 30);
        org.junit.Assert.assertEquals(latitude, p.getLatitude());
        org.junit.Assert.assertEquals(longitude, p.getLongitude());
    }

}