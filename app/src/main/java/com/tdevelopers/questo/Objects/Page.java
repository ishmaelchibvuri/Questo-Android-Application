package com.tdevelopers.questo.Objects;

import java.util.HashMap;

public class Page {
    public Long followers;
    public String pic;
    public Long question_count;
    public Long article_count;
    public HashMap<String, Boolean> admins;
    public Long created_at;
    public String name;
    public String website;
    public String desc;
    public String id;


    public Page() {
        followers = question_count = article_count = created_at = 0L;
        name = pic = id = desc = website = new String();
        admins = new HashMap<>();
    }
}
