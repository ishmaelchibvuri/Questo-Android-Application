package com.tdevelopers.questo.MainFragments;


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

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.EndlessRecyclerViewScrollListener;
import com.tdevelopers.questo.Adapters.QuestionRecyclerAdapter;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trending extends Fragment {

    public static ArrayList<String> FilterTags = new ArrayList<>();
    public static Context context;
    public static RecyclerView recyclerView;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static QuestionRecyclerAdapter adapter;
    public static ArrayList<Question> tobesenter;
    public static int counter = 0;
    public static LinearLayoutManager l;
    public static ArrayList<Question> tobesent = new ArrayList<>();
    public static GifImageView avl;
    public static boolean filterflag;
    public static ImageView notfound;
    public static ArrayList<Question> dataList;

    public Trending() {


    }

    public static Trending newInstance() {
        Bundle args = new Bundle();
        Trending fragment = new Trending();
        fragment.setArguments(args);
        return fragment;
    }

    public static void setFilterTags(ArrayList<String> filterTags) {
        FilterTags = filterTags;
        tobesent = new ArrayList<>();
        // Toast.makeText(context, "tags setted" + FilterTags.toString(), Toast.LENGTH_SHORT).show();
        if (FilterTags != null && FilterTags.size() == 0)
            LoadData();
        else if (FilterTags != null && FilterTags.size() > 0) {
            l = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(l);
            counter = 0;
            FilterLoadData();
        }
    }

    public static void FilterLoadData() {
        filterflag = false;
        notfound.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        dataList = new ArrayList<>();
        adapter = new QuestionRecyclerAdapter(dataList);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);
        try {
            if (FilterTags != null && FilterTags.size() > 0) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TagUploads");
                for (String s : FilterTags) {
                    DatabaseReference c = ref.child(s).child("Questions");
                    c.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                                    final HashMap<String, Boolean> datamap = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                    DatabaseReference base = FirebaseDatabase.getInstance().getReference("Question");
                                    if (datamap != null && datamap.size() > 0) {
                                        notfound.setVisibility(View.GONE);
                                        filterflag = true;
                                        for (final Map.Entry<String, Boolean> g : datamap.entrySet()) {

                                            base.child(g.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    try {
                                                        if (dataSnapshot != null) {
                                                            Question a = dataSnapshot.getValue(Question.class);

                                                            boolean flag = false;

                                                            for (Question cc : dataList) {
                                                                if (cc.id.equals(a.id)) {
                                                                    flag = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (a != null && !flag) {
                                                                //  data.add(a);
                                                                dataList.add(a);
                                                                Collections.sort(dataList, new Comparator<Question>() {
                                                                    @Override
                                                                    public int compare(Question s1, Question s2) {

                                                                        return (int) (s1.viewcount - s2.viewcount);
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

        notfound.setVisibility(View.GONE);
        final Query q = FirebaseDatabase.getInstance().getReference("Question").orderByChild("viewcount").limitToFirst(10);
        final LinearLayoutManager l = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(l);
        //avl.setVisibility(View.VISIBLE);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.getChildren() != null) {
                        //without filter
                        avl.setVisibility(View.GONE);
                        tobesenter = new ArrayList<Question>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (d != null) {
                                Question q = d.getValue(Question.class);
                                tobesenter.add(q);
                            }


                        }
                        // tobesenter.add(null);
                        adapter = new QuestionRecyclerAdapter(tobesenter);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(l) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {

                                final Query extra = FirebaseDatabase.getInstance().getReference("Question").orderByChild("viewcount").limitToFirst(totalItemsCount + 5);
                                extra.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {


                                            ArrayList<Question> extras = new ArrayList<>();
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                extras.add(d.getValue(Question.class));
                                            }
                                            tobesenter.removeAll(tobesenter);
                                            tobesenter.addAll(extras);
                                            //   tobesenter.remove(null);
                                            //   tobesenter.add(null);
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
        return inflater.inflate(R.layout.fragment_trending, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.rvtrending);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        try {

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

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (MainActivity.springFloatingActionMenu != null) {
                        MainActivity.springFloatingActionMenu.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }


}
