package com.tdevelopers.questo.Add;

import android.os.Bundle;
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
import com.tdevelopers.questo.Adapters.ExsistingAdminAdapter;
import com.tdevelopers.questo.Adapters.PageAdminAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class AddAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    public String id;
    public String name;
    public PageAdminAdapter alladapter;
    public SearchView mSearchView;
    RecyclerView currentrv;

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
        setContentView(R.layout.activity_add_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Add Admin");
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
        currentrv = (RecyclerView) findViewById(R.id.currentrv);
        recyclerView = (RecyclerView) findViewById(R.id.adminrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");

        FirebaseDatabase.getInstance().getReference("Page").child(id).child("admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    final HashMap<String, Boolean> admins = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    if (admins != null) {


                        currentrv.setLayoutManager(new LinearLayoutManager(AddAdmin.this, LinearLayoutManager.HORIZONTAL, false));
                        currentrv.setAdapter(new ExsistingAdminAdapter(new HashSet<String>(admins.keySet()), id));

                        FirebaseDatabase.getInstance().getReference("myUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {

                                    ArrayList<user> users = new ArrayList<user>();
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        users.add(d.getValue(user.class));

                                    }
                                    Collections.sort(users, new Comparator<user>() {
                                        @Override
                                        public int compare(user lhs, user rhs) {
                                            return lhs.name.compareTo(rhs.name);
                                        }
                                    });
                                    HashSet<String> ad = new HashSet<String>(admins.keySet());

                                    alladapter = new PageAdminAdapter(users, ad, id, name);
                                    recyclerView.setAdapter(alladapter);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
