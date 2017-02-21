package com.tdevelopers.questo.Objects;

import java.util.HashMap;

public class categories {


    public String name;
    public String pic;
    public HashMap<String, Boolean> tags;
    public Long tags_count;

    public categories() {
        tags_count = 0L;
        pic = name = new String();
        tags = new HashMap<>();
    }


}
