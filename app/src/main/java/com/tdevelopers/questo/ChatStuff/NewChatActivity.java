package com.tdevelopers.questo.ChatStuff;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PeopleDynamicAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class NewChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PeopleDynamicAdapter alladapter;
    SwipeRefreshLayout swipeRefreshLayout;
    GifImageView avl;
    SearchView mSearchView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tagactivitymenu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);


                if (alladapter != null)
                    alladapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);


                if (alladapter != null)
                    alladapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("New Chat");
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

        recyclerView = (RecyclerView) findViewById(R.id.newchatrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        avl = (GifImageView) findViewById(R.id.avloadingall);
        // recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData() {
        try {
            swipeRefreshLayout.setRefreshing(true);
            FirebaseDatabase.getInstance().getReference("myUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<user> data = new ArrayList<user>();
                    //swipeRefreshLayout.setRefreshing(false);
                    avl.setVisibility(View.GONE);
                    if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                        for (DataSnapshot d : dataSnapshot.getChildren())

                        {
                            if (d != null)
                                data.add(d.getValue(user.class));
                        }
                    }
                    alladapter = new PeopleDynamicAdapter(data);
                    recyclerView.setAdapter(alladapter);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

}
