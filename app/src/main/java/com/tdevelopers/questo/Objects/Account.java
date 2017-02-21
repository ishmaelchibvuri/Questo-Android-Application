package com.tdevelopers.questo.Objects;

/**
 * Created by saitej dandge on 21-09-2016.
 */
public class Account {
public String name;
    public String id;

    public Account() {
        id = new String();
    }

    public Account(String pic,String name) {

        this.id = pic;
        this.name=name;
    }
}
