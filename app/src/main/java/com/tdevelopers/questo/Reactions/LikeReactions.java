package com.tdevelopers.questo.Reactions;


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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PeopleListAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeReactions extends Fragment {

    public static PeopleListAdapter followingadapter;
    RecyclerView recyclerView;
    String id;

    public LikeReactions() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public LikeReactions(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static LikeReactions newInstance(String id) {

        Bundle args = new Bundle();

        LikeReactions fragment = new LikeReactions(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.likereactionsrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if (id != null && id.trim().length() != 0) {

            Query query = FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(id + "");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Boolean> people = new HashMap<String, Boolean>();
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        people = (HashMap<String, Boolean>) dataSnapshot.getValue();


                    final ArrayList<user> data = new ArrayList<user>();
                    followingadapter = new PeopleListAdapter(data);
                    recyclerView.setAdapter(followingadapter);
                    // adapter.getFilter().filter("a");
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
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_reactions, container, false);
    }

}
