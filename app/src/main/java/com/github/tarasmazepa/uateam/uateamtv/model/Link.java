package com.github.tarasmazepa.uateam.uateamtv.model;

public class Link {
    public String title;
    public String link;

    public Link() {
    }

    public Link(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}
