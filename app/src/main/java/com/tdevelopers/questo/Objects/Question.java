package com.tdevelopers.questo.Objects;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Question {
    public String username;
    public String uploaded_by;
    public String question;
    public String id;
    public String choice0;
    public String choice1;
    public String choice2;
    public String choice3;
    public HashMap<String, Boolean> tags_here;
    public String pagepic;
    public Integer answer;
    public String explanation;
    public Long likes_count;
    public String comment_id;
    public Long date;
    public Long viewcount, comment_count;

    public String media;

    public Question(String id, Long date, String username, String question, String explanation, String choice0, String choice1, String choice2, String choice3, int answer, String uploaded_by, String media) {
        this.uploaded_by = uploaded_by;
        this.date = date;
        this.media = media;
        this.answer = 0;
        this.viewcount = 0L;
        this.comment_count = 0L;
        this.comment_id = new String();
        this.id = id;
        this.likes_count = 0L;
        this.username = username;
        this.explanation = explanation;
        this.choice0 = choice0;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.answer = answer;
        this.tags_here = new HashMap<>();
        this.question = question;
    }


    public Question() {
    }

    public DatabaseReference buildRef() {


        if (id != null && id.trim().length() != 0)
            return FirebaseDatabase.getInstance().getReference("Question").child(id);

        else
            return null;
    }

}

