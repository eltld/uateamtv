package com.github.tarasmazepa.uateam.uateamtv.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Uateamtv {
    private Uateamtv() {
    }

    public static final String HOME = "http://uateam.tv/";
    public static final int timeout = 12000;

    public static final String SELECT = "div#ja-content table.adminlist tbody td:eq(1) a";

    public static Document home() throws IOException {
        return page("");
    }

    public static Document page(String url) throws IOException {
        return Jsoup.parse(new URL(HOME + url), timeout);
    }
}
