package com.tdevelopers.questo.ChatStuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.AbstractChatAdapter;
import com.tdevelopers.questo.Objects.AbsModel;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

public class ChatMain extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        recyclerView = (RecyclerView) findViewById(R.id.mainchat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Chat");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (Profile.getCurrentProfile() != null) {
            // Toast.makeText(ChatMain.this, "" + Profile.getCurrentProfile().getId(), Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("abstract").child(Profile.getCurrentProfile().getId());
            reference.orderByChild("date").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            ArrayList<AbsModel> tobesent = new ArrayList<AbsModel>();
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                tobesent.add(d.getValue(AbsModel.class));

                            }
                            /// Toast.makeText(ChatMain.this, ""+tobesent.toString(), Toast.LENGTH_SHORT).show();
                            recyclerView.setAdapter(new AbstractChatAdapter(tobesent));

                        }
                    } catch (Exception e) {

                        // Toast.makeText(ChatMain.this, e.toString()+"", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChatMain.this, NewChatActivity.class));
                }
            });
        }


    }

}
