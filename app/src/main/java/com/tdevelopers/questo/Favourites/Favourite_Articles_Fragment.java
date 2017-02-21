package com.tdevelopers.questo.Favourites;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
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
public class Favourite_Articles_Fragment extends Fragment {

    RecyclerView recyclerView;
    GifImageView avl;
    ImageView notfound;

    public Favourite_Articles_Fragment() {
        // Required empty public constructor
    }

    public static Favourite_Articles_Fragment newInstance() {

        Bundle args = new Bundle();

        Favourite_Articles_Fragment fragment = new Favourite_Articles_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite__articles_, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.fava);
        notfound = (ImageView) view.findViewById(R.id.notfound);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                    data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                final ArrayList<Article> dataList = new ArrayList<Article>();
                final ArticleListAdapter adapter = new ArticleListAdapter(dataList);
                recyclerView.setAdapter(adapter);


                if (data != null && data.keySet().size() == 0)
                    notfound.setVisibility(View.VISIBLE);
                for (String s : data.keySet()) {
                    FirebaseDatabase.getInstance().getReference("Article").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                Article q = dataSnapshot.getValue(Article.class);
                                avl.setVisibility(View.GONE);
                                if (q != null) {
                                    dataList.add(q);
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
}
