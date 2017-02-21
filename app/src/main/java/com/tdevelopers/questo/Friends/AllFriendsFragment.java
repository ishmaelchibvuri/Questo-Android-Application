package com.tdevelopers.questo.Friends;


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
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PeopleListAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFriendsFragment extends Fragment {
    public static PeopleListAdapter alladapter;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    GifImageView avl;

    public AllFriendsFragment() {
        // Required empty public constructor
    }

    public static AllFriendsFragment newInstance() {

        Bundle args = new Bundle();

        AllFriendsFragment fragment = new AllFriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_friends, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvfbfriends);
        avl = (GifImageView) view.findViewById(R.id.avloadingall);
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
                            if (d != null) {
                                data.add(d.getValue(user.class));

                            }
                        }
                    }
                    Collections.sort(data, new Comparator<user>() {
                        @Override
                        public int compare(user lhs, user rhs) {
                            return lhs.name.compareToIgnoreCase(rhs.name);
                        }
                    });
                    alladapter = new PeopleListAdapter(data);
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
