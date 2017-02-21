package com.tdevelopers.questo.libraries;

import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.MyApplication;
import com.tdevelopers.questo.Objects.MyData;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;

    public CustomExceptionHandler() {

        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {

        FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String gcmid = (String) dataSnapshot.getValue();
                MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());
                if (MyApplication.getInstance() != null && MyApplication.getInstance().getApplicationContext() != null)
                    Toast.makeText(MyApplication.getInstance().getApplicationContext(), "We Tracked Exception ! We will fix it soon ! Sorry :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());


        defaultUEH.uncaughtException(t, e);
    }

}