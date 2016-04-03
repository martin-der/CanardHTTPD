package net.tetrakoopa.canardhttpd.util;

import junit.framework.Assert;


public class WebUtilTest {

    final static String USERAGENT_S3 = "Mozilla/5.0 (Linux; Android 4.4.4; GT-I9300 Build/KTU84Q) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36";

    public void findMobileDevices() {

        Assert.assertTrue(WebUtil.isMobileUserAgent(USERAGENT_S3));
    }


    public static void main(String args[]) {
        //new WebUtilTest().findMobileDevices();
        System.out.println(WebUtil.isMobileUserAgent(USERAGENT_S3));

    }
}
