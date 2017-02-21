package com.tdevelopers.questo.LeaderShip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tdevelopers.questo.Adapters.LeaderShipAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

public class Leadershipboard_activity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadershipboard_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Leadership Board");
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
        recyclerView = (RecyclerView) findViewById(R.id.scorerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query q = FirebaseDatabase.getInstance().getReference("myUsers").orderByChild("score");
        recyclerView.setAdapter(new LeaderShipAdapter(q, user.class));
    }

}
