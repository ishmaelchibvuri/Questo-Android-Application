package com.tdevelopers.questo.PageFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PageAdapter;
import com.tdevelopers.questo.Objects.Page;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    RecyclerView recyclerView;
    public static PageAdapter pageadapter;

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance() {

        Bundle args = new Bundle();

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    GifImageView avl;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.pagesrv);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        avl = (GifImageView) view.findViewById(R.id.avloading);
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


        FirebaseDatabase.getInstance().getReference("Page").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Page> data = new ArrayList<Page>();
                avl.setVisibility(View.GONE);
                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {

                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Page p = d.getValue(Page.class);
                        if (p != null) {
                            data.add(p);
                        }
                    }
                    Collections.sort(data, new Comparator<Page>() {
                        @Override
                        public int compare(Page lhs, Page rhs) {
                            return lhs.name.compareTo(rhs.name);
                        }
                    });
                    pageadapter = new PageAdapter(data);
                    recyclerView.setAdapter(pageadapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

}
