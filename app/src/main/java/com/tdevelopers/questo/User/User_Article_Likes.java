package com.tdevelopers.questo.User;


import android.annotation.SuppressLint;
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
import com.tdevelopers.questo.Adapters.ArticleListAdapter;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_Article_Likes extends Fragment {
    String id;
    RecyclerView recyclerView;

    GifImageView avl;
    ImageView notfound;

    public User_Article_Likes() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public User_Article_Likes(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static User_Article_Likes newInstance(String id) {

        Bundle args = new Bundle();

        User_Article_Likes fragment = new User_Article_Likes(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__article__likes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.userarticlelikes);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notfound = (ImageView) view.findViewById(R.id.notfound);
        recyclerView.setNestedScrollingEnabled(false);
        if (id != null && id.trim().length() != 0)
            FirebaseDatabase.getInstance().getReference("userlikes").child(id).child("articlelikes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    final ArrayList<Article> dataList = new ArrayList<Article>();
                    final ArticleListAdapter adapter = new ArticleListAdapter(dataList);
                    recyclerView.setAdapter(adapter);

                    if (MyData.getLiketab() != null) {
                        MyData.getLiketab().getTabAt(1).setText(data.size() + "\nArticles");
                    }
                    if (data.size() == 0) {
                        avl.setVisibility(View.GONE);

                        notfound.setVisibility(View.VISIBLE);
                    }
                    for (String s : data.keySet()) {
                        FirebaseDatabase.getInstance().getReference("Article").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                avl.setVisibility(View.GONE);
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                    Article q = dataSnapshot.getValue(Article.class);


                                    if (q != null) {
                                        dataList.add(q);
                                        adapter.notifyDataSetChanged();
                                        //textView.setText(dataList.size() + " Likes");
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
}
