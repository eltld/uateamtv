package com.github.tarasmazepa.uateam.uateamtv.model;

public class Link {
    public String groupTitle;
    public String title;
    public String link;
    public int season;
    public int episode;

    public Link() {
    }

    public Link(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
