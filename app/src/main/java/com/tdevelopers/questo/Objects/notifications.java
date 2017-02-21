package com.tdevelopers.questo.Objects;

public class notifications {
    public Long created_at;
    public String userid;
    public String message;
    public String title;
    public String username;
    public String link;
    public String pagepic;

    public notifications() {
        created_at = 0L;
        userid = message = pagepic = title = username = link = new String();

    }
}
