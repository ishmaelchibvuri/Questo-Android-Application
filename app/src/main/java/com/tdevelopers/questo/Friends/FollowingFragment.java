package com.tdevelopers.questo.Friends;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.Profile;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    public static PeopleListAdapter followingadapter;
    RecyclerView recyclerView;
    String id;
    GifImageView avl;
    ImageView notfound;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FollowingFragment(String id) {
        // Required empty public constructor
        this.id = id;
    }


    public static FollowingFragment newInstance(String id) {

        Bundle args = new Bundle();

        FollowingFragment fragment = new FollowingFragment(id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvfollowing);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        avl = (GifImageView) view.findViewById(R.id.avloadingfolww);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        getData();

    }

    public void getData() {
        try {

            if (Profile.getCurrentProfile() != null) {
                if (id != null && id.trim().length() != 0) {
                    Query query = FirebaseDatabase.getInstance().getReference("myUsers").child(id + "").child("following");

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
                                if (people.size() == 0) {
                                    // Toast.makeText(getContext(), "sorry nothing founs", Toast.LENGTH_SHORT).show();
                                    avl.setVisibility(View.GONE);
                                    notfound.setVisibility(View.VISIBLE);
                                }
                                for (String s : people.keySet()) {
                                    ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            avl.setVisibility(View.GONE);
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                user t = dataSnapshot.getValue(user.class);


                                                if (t != null) {
                                                    data.add(t);

                                                    Collections.sort(data, new Comparator<user>() {
                                                        @Override
                                                        public int compare(user lhs, user rhs) {
                                                            return lhs.name.compareToIgnoreCase(rhs.name);
                                                        }
                                                    });
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
            } else {
                notfound.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }
}
