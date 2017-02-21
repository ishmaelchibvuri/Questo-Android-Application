package com.tdevelopers.questo.Explore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.catrecycleradapter;
import com.tdevelopers.questo.Objects.categories;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class Explore_Activity extends AppCompatActivity {
    public GifImageView avlLoading;
    RecyclerView recyclerView;
    SearchView mSearchView;
    catrecycleradapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tagactivitymenu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);
                if (adapter != null)
                    adapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                if (adapter != null)
                    adapter.getFilter().filter(query);

                return false;
            }
        });
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        avlLoading = (GifImageView) findViewById(R.id.loading);
        setTitle("Explore");
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
        recyclerView = (RecyclerView) findViewById(R.id.explorerv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Query q = FirebaseDatabase.getInstance().getReference("categories").orderByChild("name");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<categories> data = new ArrayList<categories>();

                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                    avlLoading.setVisibility(View.GONE);
                    for (DataSnapshot d : dataSnapshot.getChildren())
                        data.add(d.getValue(categories.class));
                    adapter = new catrecycleradapter(data);

                    recyclerView.setAdapter(adapter);
                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
