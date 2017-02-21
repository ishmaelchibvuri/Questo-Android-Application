package com.tdevelopers.questo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Objects.MyData;

public class ScoreChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_chart);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setTitle("Chart");
            setSupportActionBar(toolbar);
            if (toolbar != null) {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finish();
                    }
                });
            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String gcmid = (String) dataSnapshot.getValue();
                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }

}
