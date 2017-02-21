package com.tdevelopers.questo.TagOpenFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.ArticleListAdapter;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagOpenArticleFragment extends Fragment {

    String id = "";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    GifImageView avl;
    ImageView notfound;

    public TagOpenArticleFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TagOpenArticleFragment(String id) {
        this.id = id;
    }

    public static TagOpenArticleFragment newInstance(String id) {

        Bundle args = new Bundle();

        TagOpenArticleFragment fragment = new TagOpenArticleFragment(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_open_article, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.tagopenarticlerv);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notfound.setVisibility(View.GONE);
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getData() {
        try {
            if (id != null && id.trim().length() != 0) {

                Query q = FirebaseDatabase.getInstance().getReference("TagUploads").child(id + "").child("Articles");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Boolean> data = new HashMap<String, Boolean>();

                        final ArrayList<Article> dataList = new ArrayList<>();
                        final ArticleListAdapter adapter = new ArticleListAdapter(dataList);
                        recyclerView.setAdapter(adapter);
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            data = (HashMap<String, Boolean>) dataSnapshot.getValue();

                        }
                        if (data != null) {
                            if (data.size() == 0) {
                                avl.setVisibility(View.GONE);
                                notfound.setVisibility(View.VISIBLE);
                            }
                            for (String s : data.keySet())
                                FirebaseDatabase.getInstance().getReference("Article").child(s + "").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        avl.setVisibility(View.GONE);
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                            if (dataSnapshot.getValue(Article.class) != null) {

                                                dataList.add(dataSnapshot.getValue(Article.class));
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        } catch (Exception e) {

        }
    }
}
