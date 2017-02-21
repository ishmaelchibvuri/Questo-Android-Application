package com.tdevelopers.questo.UserTags;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.tagccadapter;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTags extends Fragment {
    public static tagccadapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    GifImageView avl;

    public AllTags() {
        // Required empty public constructor
    }

    public static tagccadapter getAdapter() {
        return adapter;
    }

    public static AllTags newInstance() {

        Bundle args = new Bundle();

        AllTags fragment = new AllTags();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_tags, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvalltags);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
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

            Query query = FirebaseDatabase.getInstance().getReference("Tag");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        avl.setVisibility(View.GONE);

                        swipeRefreshLayout.setRefreshing(false);
                        ArrayList<Tag> list = new ArrayList<>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (d != null) {
                                list.add(d.getValue(Tag.class));

                            }


                        }
                        Collections.sort(list, new Comparator<Tag>() {
                            @Override
                            public int compare(Tag lhs, Tag rhs) {
                                return lhs.name.compareToIgnoreCase(rhs.name);
                            }
                        });
                        adapter = new tagccadapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }
    }
}
