package com.tdevelopers.questo.UserTags;


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
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.tagccadapter;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingTags extends Fragment {

    public static tagccadapter adapter;
    RecyclerView recyclerView;
    GifImageView avl;

    ImageView notfound;

    public FollowingTags() {
        // Required empty public constructor
    }


    public static FollowingTags newInstance() {

        Bundle args = new Bundle();

        FollowingTags fragment = new FollowingTags();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following_tags, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvfollowingtags);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        avl = (GifImageView) view.findViewById(R.id.avloading);
        getData();

    }

    public void getData() {
        try {
            if (Profile.getCurrentProfile() != null)
                FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("tagsfollowing").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String, Boolean> tags = new HashMap<String, Boolean>();
                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                            tags = (HashMap<String, Boolean>) dataSnapshot.getValue();

                        final ArrayList<Tag> data = new ArrayList<Tag>();
                        adapter = new tagccadapter(data);
                        recyclerView.setAdapter(adapter);
                        // adapter.getFilter().filter("a");

                        if (tags != null && tags.keySet().size() == 0)
                            notfound.setVisibility(View.VISIBLE);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tag");
                        if (tags != null) {
                            for (String s : tags.keySet()) {
                                ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                            avl.setVisibility(View.GONE);

                                            Tag t = dataSnapshot.getValue(Tag.class);
                                            if (t != null) {
                                                data.add(t);
                                                Collections.sort(data, new Comparator<Tag>() {
                                                    @Override
                                                    public int compare(Tag lhs, Tag rhs) {
                                                        return lhs.name.compareToIgnoreCase(rhs.name);
                                                    }
                                                });
                                                adapter.full.add(t);
                                                adapter.notifyDataSetChanged();
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
            else {
                notfound.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }
}
