package com.tdevelopers.questo.MainArticles;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.ArticleListAdapter;
import com.tdevelopers.questo.Adapters.EndlessRecyclerViewScrollListener;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewArticles extends Fragment {

    public static ArrayList<String> FilterTags = new ArrayList<>();
    public static Context context;
    public static RecyclerView recyclerView;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static ArticleListAdapter adapter;
    public static ArrayList<Article> tobesenter = new ArrayList<>();
    public static LinearLayoutManager l;
    public static ArrayList<Article> tobesent = new ArrayList<>();
    public static GifImageView avl;
    public static ImageView notfound;
    public static ArrayList<Article> dataList;
    public static boolean filterflag;

    public NewArticles() {
        // Required empty public constructor
    }

    public static void setFilterTags(ArrayList<String> filterTags) {
        FilterTags = filterTags;
        tobesent = new ArrayList<>();
        Toast.makeText(context, "tags setted" + FilterTags.toString(), Toast.LENGTH_SHORT).show();
        if (FilterTags != null && FilterTags.size() == 0)
            LoadData();
        else if (FilterTags != null && FilterTags.size() > 0) {
            l = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(l);

            FilterLoadData();
        }
    }

    public static NewArticles newInstance() {

        Bundle args = new Bundle();
        NewArticles fragment = new NewArticles();
        fragment.setArguments(args);
        return fragment;
    }

    public static void FilterLoadData() {
        filterflag = false;
        notfound.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        dataList = new ArrayList<>();
        adapter = new ArticleListAdapter(dataList);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);
        try {
            if (FilterTags != null && FilterTags.size() > 0) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TagUploads");
                for (String s : FilterTags) {
                    DatabaseReference c = ref.child(s).child("Articles");
                    c.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                                    final HashMap<String, Boolean> datamap = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                    DatabaseReference base = FirebaseDatabase.getInstance().getReference("Article");
                                    if (datamap != null && datamap.size() > 0) {
                                        notfound.setVisibility(View.GONE);
                                        filterflag = true;
                                        for (final Map.Entry<String, Boolean> g : datamap.entrySet()) {

                                            base.child(g.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    try {
                                                        if (dataSnapshot != null) {
                                                            Article a = dataSnapshot.getValue(Article.class);

                                                            boolean flag = false;

                                                            for (Article cc : dataList) {
                                                                if (cc.id.equals(a.id)) {
                                                                    flag = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (a != null && !flag) {
                                                                //  data.add(a);
                                                                dataList.add(a);
                                                                Collections.sort(dataList, new Comparator<Article>() {
                                                                    @Override
                                                                    public int compare(Article s1, Article s2) {

                                                                        Date d1 = new Date(s1.date);
                                                                        Date d2 = new Date(s2.date);
                                                                        return (d2.compareTo(d1));
                                                                    }
                                                                });
                                                            }


                                                            swipeRefreshLayout.setRefreshing(false);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    } catch (Exception e) {

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                } else {

                                    if (!filterflag)
                                        notfound.setVisibility(View.VISIBLE);

                                    swipeRefreshLayout.setRefreshing(false);

                                }

                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        } catch (Exception e) {

        }
    }

    public static void LoadData() {
        final Query q = FirebaseDatabase.getInstance().getReference("Article").orderByPriority().limitToFirst(10);
        final LinearLayoutManager l = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(l);
        notfound.setVisibility(View.GONE);
        //  avl.setVisibility(View.VISIBLE);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.getChildren() != null) {
                        //without filter
                        avl.setVisibility(View.GONE);
                        tobesenter = new ArrayList<Article>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (d != null) {
                                Article q = d.getValue(Article.class);
                                tobesenter.add(q);
                            }


                        }
                        adapter = new ArticleListAdapter(tobesenter);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(l) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                final Query extra = FirebaseDatabase.getInstance().getReference("Article").orderByPriority().limitToFirst(totalItemsCount + 5);
                                extra.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            ArrayList<Article> extras = new ArrayList<>();
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                extras.add(d.getValue(Article.class));
                                            }
                                            tobesenter.removeAll(tobesenter);
                                            tobesenter.addAll(extras);
                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e) {
                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                        });

                    }
                } catch (Exception e) {
                    FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_articles, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.rvnew);
        avl = (GifImageView) view.findViewById(R.id.loading);
        avl.setVisibility(View.VISIBLE);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        try {
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            LoadData();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if ((FilterTags != null && FilterTags.size() == 0) || FilterTags == null)
                        LoadData();
                    else if (FilterTags != null && FilterTags.size() > 0)
                        FilterLoadData();
                }
            });
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }
}
