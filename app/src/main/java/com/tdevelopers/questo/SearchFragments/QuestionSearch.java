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
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.QuestionRecyclerAdapter;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashSet;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionSearch extends Fragment {
    public static GifImageView avl;
    public static HashSet<Question> FullList = new HashSet<>();
    static String tobesearch = "";
    static RecyclerView recyclerView;
    static Context context;
    static ImageView notfound;

    public QuestionSearch() {
        // Required empty public constructor
    }

    public static QuestionSearch newInstance() {

        Bundle args = new Bundle();

        QuestionSearch fragment = new QuestionSearch();
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
            if (FullList.size() == 0)
                FirebaseDatabase.getInstance().getReference("Question").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Question> data = new ArrayList<Question>();
                        avl.setVisibility(View.GONE);
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                Question a = d.getValue(Question.class);


                                if (a != null) {
                                    FullList.add(a);
                                    if (a.choice0 != null && a.choice0.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice1 != null && a.choice1.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice2 != null && a.choice2.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice3 != null && a.choice3.toLowerCase().contains(tobesearch.toLowerCase()) || a.question.toLowerCase().contains(tobesearch.toLowerCase()))
                                        data.add(a);
                                    else {
                                        if (a.tags_here != null && a.tags_here.size() != 0) {
                                            for (String s : a.tags_here.keySet()) {
                                                if (s.toLowerCase().contains(tobesearch.toLowerCase()))
                                                    data.add(a);
                                            }
                                        }
                                    }


                                }
                            }
                            if (data.size() == 0)
                                notfound.setVisibility(View.VISIBLE);
                            else
                                notfound.setVisibility(View.GONE);

                        } else {
                            notfound.setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(new QuestionRecyclerAdapter(data));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            else {
                ArrayList<Question> data = new ArrayList<Question>();
                for (Question a : FullList) {
                    if (a != null) {
                        FullList.add(a);

                        if (a.choice0 != null && a.choice0.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice1 != null && a.choice1.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice2 != null && a.choice2.toLowerCase().contains(tobesearch.toLowerCase()) || a.choice3 != null && a.choice3.toLowerCase().contains(tobesearch.toLowerCase()) || a.question.toLowerCase().contains(tobesearch.toLowerCase()))
                            data.add(a);
                        else {
                            if (a.tags_here != null && a.tags_here.size() != 0) {
                                for (String s : a.tags_here.keySet()) {
                                    if (s.toLowerCase().contains(tobesearch.toLowerCase()))
                                        data.add(a);
                                }
                            }
                        }


                    }
                }


                if (data.size() == 0)
                    notfound.setVisibility(View.VISIBLE);
                else
                    notfound.setVisibility(View.GONE);

                recyclerView.setAdapter(new QuestionRecyclerAdapter(data));


            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.searchquestionrv);
        avl = (GifImageView) view.findViewById(R.id.loading);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        if (tobesearch != null && tobesearch.trim().length() != 0)
            setQuery(tobesearch + "");
    }

}
