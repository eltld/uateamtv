package com.github.tarasmazepa.uateam.uateamtv.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Uateamtv {
    private Uateamtv() {
    }

    public static final String HOME = "http://uateam.tv";
    public static final int timeout = 12000;

    public static final String SELECT = "div#ja-content table.adminlist tbody td:eq(1) a";

    public static boolean isAbsoluteUrl(String url) {
        return url.startsWith(HOME);
    }

    public static String absoluteUrl(String url) {
        return isAbsoluteUrl(url) ? url : HOME + url;
    }

    public static String relativeUrl(String url) {
        return isAbsoluteUrl(url) ? url.substring(HOME.length()) : url;
    }

    public static Document home() throws IOException {
        return page("");
    }

    public static Document page(String url) throws IOException {
        return Jsoup.parse(new URL(absoluteUrl(url)), timeout);
    }
}
