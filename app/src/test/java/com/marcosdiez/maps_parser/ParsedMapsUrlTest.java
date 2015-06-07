package com.marcosdiez.maps_parser;

/**
 * Created by Marcos on 07-Jun-15.
 */
public class ParsedMapsUrlTest {

    @org.junit.Test
    public void testInternetUrl() throws Exception {
        //45°49'45.9"N 15°58'41.5"E
        testHelper("http://goo.gl/maps/vL3w1", "45.829415", "15.978208");
    }

    @org.junit.Test
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

        testHelper("waze://?ll=45.8294151,15.978208&z=10");

        testHelper("https://maps.yandex.com/?rtext=~45.8294151,15.978208");
        testHelper("http://maps.yandex.com/?rtext=~45.8294151,15.978208");

        testHelper("http://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");
        testHelper("https://www.ingress.com/intel?pll=45.8294151,15.978208&ll=10,20");

        testHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");
        testHelper("https://www.ingress.com/intel?ll=10,20&pll=45.8294151,15.978208");


    }

    private void testHelper(String url) {
        String lat = "45.8294151";
        String lng = "15.978208";
        testHelper(url, lat, lng);
    }

    private void testHelper(String url, String latitude, String longitude) {
        ParsedMapsUrl p = new ParsedMapsUrl(url);
        if(p.needsInternet()){
            p.resolveInternetUrl();
        }
        //   org.junit.Assert.assertEquals(30, 30);
        org.junit.Assert.assertEquals(latitude, p.getLatitude());
        org.junit.Assert.assertEquals(longitude, p.getLongitude());
    }

}