package com.tdevelopers.questo.Objects;

import java.util.HashMap;


public class user {
    public String id;
    public String name;
    public HashMap<String, Boolean> catsfollowing;
    public HashMap<String, String> page_admin;
    public HashMap<String, Boolean> tagsfollowing;

    public HashMap<String, Boolean> pagesfollowing;
    public HashMap<String, Boolean> followers;
    public HashMap<String, Boolean> following;
    public String gcmid;
    public Long article_count;
    public Long question_count;
    public Long score;
    public String email;
    public String work;

    public user() {
        followers = new HashMap<>();
        following = new HashMap<>();
        catsfollowing = new HashMap<>();
        tagsfollowing = new HashMap<>();
        pagesfollowing = new HashMap<>();
        page_admin = new HashMap<>();
        email = id = gcmid = work = name = new String();
        question_count = score = article_count = 0L;

    }

    public boolean check() {

        return id != null && name != null && id.trim().length() != 0 && name.trim().length() != 0;
    }
}
