package com.tdevelopers.questo.Objects;

public class AbsModel {
    public String content = "";
    public String userid = "";
    public Long date = 0L;

    public AbsModel(String key, String value, Long date) {
        userid = key;
        content = value;
        this.date = date;

    }

    public AbsModel() {

    }
}
