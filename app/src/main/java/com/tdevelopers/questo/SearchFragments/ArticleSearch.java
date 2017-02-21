package com.tdevelopers.questo.SearchFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.ArticleListAdapter;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashSet;

public class ArticleSearch extends Fragment {
    public static RecyclerView recyclerView;
    public static int counter = 0;
    public static ArrayList<Article> tobesent = new ArrayList<>();
    static String tobesearch = "";
    static Context context;

    public ArticleSearch() {
        // Required empty public constructor
    }

    public static void setQuery(String q) {
        tobesearch = q;
        counter = 0;
        tobesent = new ArrayList<>();
        try {
            search();
        } catch (Exception e) {

        }
    }

    public static ArticleSearch newInstance() {
        Bundle args = new Bundle();
        ArticleSearch fragment = new ArticleSearch();
        fragment.setArguments(args);
        return fragment;
    }


    public static void search() throws Exception {
        try {
            if (context != null)
                if (recyclerView != null && tobesearch != null && tobesearch.trim().length() != 0) {
                    Query q = FirebaseDatabase.getInstance().getReference("Article");

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                HashSet<Article> dataList = new HashSet<>();

                                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {

                                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                                        if (d.getValue(Article.class) != null) {
                                            Article a = d.getValue(Article.class);

                                            if (a != null) {

                                                if (a.title.toLowerCase().contains(tobesearch.toLowerCase()) || a.description.contains(tobesearch.toLowerCase()))
                                                    dataList.add(a);
                                                else {
                                                    if (a.tags_here != null && a.tags_here.size() != 0) {
                                                        for (String s : a.tags_here.keySet()) {
                                                            if (s.toLowerCase().contains(tobesearch.toLowerCase()))
                                                                dataList.add(a);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                }
                                recyclerView.setAdapter(new ArticleListAdapter(new ArrayList<Article>(dataList)));
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
        } catch (Exception e) {

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.searcharticlerv);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        if (tobesearch != null && tobesearch.trim().length() != 0)
            setQuery(tobesearch + "");

    }
}
