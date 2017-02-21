package com.tdevelopers.questo.Objects;

import java.util.HashMap;

public class Article {
    public String description;
    public String image;
    public Long date;
    public String uploaded_by;
    public String username;
    public HashMap<String, Boolean> tags_here;
    public String title;
    public Long views_count;
    public Long likes_count, nlikes_count;
    public String id;


    public Article(String id, Long date, String title, String content, String user_id, String username, String image) {
        this.description = content;
        this.title = title;
        tags_here = new HashMap<>();
        this.id = id;
        this.date = date;
        this.uploaded_by = user_id;
        this.username = username;
        this.nlikes_count = this.likes_count = 0L;
        this.image = image;


    }

    public Article() {

    }

}
