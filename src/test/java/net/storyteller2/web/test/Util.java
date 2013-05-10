package net.storyteller2.web.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    // match any number after the the final '/'
    final static Pattern ID_FROM_URL = Pattern.compile("^.*/([0-9]+)");

    /**
     * Extract the resource id from a URL or URI in Struts REST Plugin format.
     * 
     * @see https://struts.apache.org/release/2.1.x/docs/rest-plugin.html
     * @param url
     *            or uri for a resource
     * @return Id or null if no match
     */
    public static String idFromUrl(String url) {
        String id = null;
        Matcher matcher = ID_FROM_URL.matcher(url);
        if (matcher.find()) {
            id = matcher.group(1);
        }
        return id;
    }
}
