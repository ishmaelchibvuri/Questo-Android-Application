package com.tdevelopers.questo.Reactions.ReactionsOpen;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PeopleListAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class WrongUsers extends Fragment {
    public static PeopleListAdapter followingadapter;
    public static RecyclerView recyclerView;
    String id = "";

    public WrongUsers() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public WrongUsers(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static WrongUsers newInstance(String id) {

        Bundle args = new Bundle();

        WrongUsers fragment = new WrongUsers(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wrong_users, container, false);
    }

    public static void setPeople(HashMap<String, Boolean> people) {


        final ArrayList<user> data = new ArrayList<user>();
        followingadapter = new PeopleListAdapter(data);
        recyclerView.setAdapter(followingadapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("myUsers");
        if (people != null) {
            for (String s : people.keySet()) {
                ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            user t = dataSnapshot.getValue(user.class);
                            if (t != null) {
                                data.add(t);
                                followingadapter.full.add(t);
                                followingadapter.notifyDataSetChanged();
                            }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.wrongrv);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // adapter.getFilter().filter("a");

    }
}
