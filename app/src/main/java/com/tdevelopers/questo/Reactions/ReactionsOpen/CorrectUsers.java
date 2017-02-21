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
public class CorrectUsers extends Fragment {

    public static PeopleListAdapter followingadapter;
    public static RecyclerView recyclerView;
    String id = "";

    @SuppressLint("ValidFragment")
    public CorrectUsers(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public CorrectUsers() {

    }

    public static CorrectUsers newInstance(String id) {

        Bundle args = new Bundle();

        CorrectUsers fragment = new CorrectUsers(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_correct_users, container, false);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.correctrv);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


    }
}
