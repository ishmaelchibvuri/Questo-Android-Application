package com.tdevelopers.questo.PageOpenTabs;


import android.annotation.SuppressLint;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.QuestionRecyclerAdapter;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageOpenQuestions extends Fragment {

    String id = "";
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    GifImageView avl;
    ImageView notfound;

    public PageOpenQuestions() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PageOpenQuestions(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_open_questions, container, false);
    }

    public static PageOpenQuestions newInstance(String id) {

        Bundle args = new Bundle();

        PageOpenQuestions fragment = new PageOpenQuestions(id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.tagopenquestionrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        getData();
        avl = (GifImageView) view.findViewById(R.id.avloading);
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
                Query query = FirebaseDatabase.getInstance().getReference("pageuploads").child(id + "").child("question_uploads");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Boolean> tobesent = new HashMap<String, Boolean>();
                        final ArrayList<Question> data = new ArrayList<Question>();
                        final QuestionRecyclerAdapter adapter = new QuestionRecyclerAdapter(data);
                        recyclerView.setAdapter(adapter);
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            tobesent = ((HashMap<String, Boolean>) dataSnapshot.getValue());
                            if (tobesent != null) {
                                if (tobesent.size() == 0) {
                                    avl.setVisibility(View.GONE);
                                    notfound.setVisibility(View.VISIBLE);
                                }
                                for (String s : tobesent.keySet()) {

                                    FirebaseDatabase.getInstance().getReference("Question").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            avl.setVisibility(View.GONE);
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                Question q = dataSnapshot.getValue(Question.class);

                                                if (q != null) {
                                                    data.add(q);

                                                    Collections.sort(data, new Comparator<Question>() {
                                                        @Override
                                                        public int compare(Question lhs, Question rhs) {
                                                            return rhs.date.compareTo(lhs.date);
                                                        }
                                                    });
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
                        } else {
                            avl.setVisibility(View.GONE);
                            notfound.setVisibility(View.VISIBLE);
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
