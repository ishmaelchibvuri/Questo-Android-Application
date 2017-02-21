package com.tdevelopers.questo.User;


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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.tagccadapter;
import com.tdevelopers.questo.Objects.Tag;
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
public class User_Tags_Following extends Fragment {

    public static tagccadapter adapter;
    static RecyclerView recyclerView;
    static String id;
    static TextView textView;
    static GifImageView avl;
    static ImageView notfound;
    static user userobj;

    public User_Tags_Following() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public User_Tags_Following(String id) {
        // Required empty public constructor
        User_Tags_Following.id = id;
    }

    public static void setCurrent(user u) {
        try {
            userobj = u;
            if (userobj != null) {


                HashMap<String, Boolean> tags = new HashMap<String, Boolean>();
                if (userobj.tagsfollowing != null)
                    tags = userobj.tagsfollowing;


                final ArrayList<Tag> data = new ArrayList<Tag>();
                adapter = new tagccadapter(data);
                recyclerView.setAdapter(adapter);
                // adapter.getFilter().filter("a");
                if (tags != null)
                    textView.setText(tags.size() + " Tags");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tag");
                if (tags != null) {
                    if (tags.size() == 0) {
                        avl.setVisibility(View.GONE);
                        notfound.setVisibility(View.VISIBLE);
                    }


                    for (String s : tags.keySet()) {
                        ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                avl.setVisibility(View.GONE);
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    Tag t = dataSnapshot.getValue(Tag.class);
                                    if (t != null) {

                                        data.add(t);
                                        Collections.sort(data, new Comparator<Tag>() {
                                            @Override
                                            public int compare(Tag lhs, Tag rhs) {
                                                return lhs.name.compareTo(rhs.name);
                                            }
                                        });
                                        adapter.full.add(t);


                                        Collections.sort(adapter.full, new Comparator<Tag>() {
                                            @Override
                                            public int compare(Tag lhs, Tag rhs) {
                                                return lhs.name.compareTo(rhs.name);
                                            }
                                        });
                                        adapter.notifyDataSetChanged();

                                        // textView.setText(data.size() + " Tags");
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {

                    avl.setVisibility(View.GONE);
                    notfound.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {

        } catch (Error e) {

        }
    }

    public static User_Tags_Following newInstance(String id) {

        Bundle args = new Bundle();

        User_Tags_Following fragment = new User_Tags_Following(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__tags__following, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.usertags);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        textView = (TextView) view.findViewById(R.id.headertt);

    }

}
