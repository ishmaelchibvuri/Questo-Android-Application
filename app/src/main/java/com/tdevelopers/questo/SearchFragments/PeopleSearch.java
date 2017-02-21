package com.tdevelopers.questo.SearchFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PeopleListAdapter;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashSet;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleSearch extends Fragment {
    public static HashSet<user> FullList = new HashSet<>();
    public
    static GifImageView avl;
    static String tobesearch = "";
    static RecyclerView recyclerView;
    static Context context;
    static ImageView notfound;

    public PeopleSearch() {
        // Required empty public constructor
    }

    public static PeopleSearch newInstance() {

        Bundle args = new Bundle();

        PeopleSearch fragment = new PeopleSearch();
        fragment.setArguments(args);
        return fragment;
    }

    public static void setQuery(String q) {
        tobesearch = q;
        search();
    }

    public static void search() {

        if (notfound != null)
            notfound.setVisibility(View.GONE);
        if (recyclerView != null && tobesearch != null && tobesearch.trim().length() != 0) {
            if (FullList.size() == 0) {
                Query q = FirebaseDatabase.getInstance().getReference("myUsers");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<user> dataList = new ArrayList<user>();
                        avl.setVisibility(View.GONE);
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null) {

                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                if (d.getValue(user.class) != null) {
                                    user a = d.getValue(user.class);

                                    if (a != null) {

                                        if (a.name.toLowerCase().contains(tobesearch.toLowerCase()))
                                            dataList.add(a);
                                    }
                                }
                            }

                            if (dataList.size() == 0)
                                notfound.setVisibility(View.VISIBLE);
                            else
                                notfound.setVisibility(View.GONE);
                        } else {
                            // avl.setVisibility(View.GONE);
                            notfound.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(new PeopleListAdapter(dataList));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                ArrayList<user> dataList = new ArrayList<user>();
                for (user a : FullList) {

                    if (a != null) {

                        if (a.name.toLowerCase().contains(tobesearch.toLowerCase()))
                            dataList.add(a);
                    }
                }


                if (dataList.size() == 0)
                    notfound.setVisibility(View.VISIBLE);
                else
                    notfound.setVisibility(View.GONE);

                recyclerView.setAdapter(new PeopleListAdapter(dataList));
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.searchpeoplerv);
        avl = (GifImageView) view.findViewById(R.id.loading);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        if (tobesearch != null && tobesearch.trim().length() != 0)
            setQuery(tobesearch + "");
    }

}
