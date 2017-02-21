package com.tdevelopers.questo.Objects;

public class Tag {
    public Long followers;
    public String pic;
    public Long question_count;
    public Long article_count;
    public String category;
    public Long created_at;
    public String name;

    public Tag() {
        followers = question_count = article_count = created_at = 0L;
        name = pic = category = new String();
    }
}
