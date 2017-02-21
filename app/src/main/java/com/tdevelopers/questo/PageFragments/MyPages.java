package com.tdevelopers.questo.PageFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PageAdapter;
import com.tdevelopers.questo.Objects.Page;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPages extends Fragment {


    public MyPages() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    public static PageAdapter pageadapter;
    ImageView notfound;

    GifImageView avl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pages, container, false);
    }


    public static MyPages newInstance() {

        Bundle args = new Bundle();

        MyPages fragment = new MyPages();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.mypagesrv);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        notfound = (ImageView) view.findViewById(R.id.notfound);
        avl = (GifImageView) view.findViewById(R.id.avloading);
        getData();
    }

    ArrayList<Page> data;

    public void getData() {
        try {

            notfound.setVisibility(View.GONE);
            if (Profile.getCurrentProfile() != null)
                FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                         @Override
                                                                                                                                                                         public void onDataChange(DataSnapshot dataSnapshot) {


                                                                                                                                                                             HashMap<String, String> tags = new HashMap<String, String>();
                                                                                                                                                                             if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                                                                                                                                                 tags = (HashMap<String, String>) dataSnapshot.getValue();

                                                                                                                                                                             if (tags != null) {
                                                                                                                                                                                 data = new ArrayList<Page>();
                                                                                                                                                                                 pageadapter = new PageAdapter(data);
                                                                                                                                                                                 recyclerView.setAdapter(pageadapter);
                                                                                                                                                                                 // adapter.getFilter().filter("a");

                                                                                                                                                                                 if (tags != null && tags.keySet().size() == 0) {
                                                                                                                                                                                     notfound.setVisibility(View.VISIBLE);
                                                                                                                                                                                     avl.setVisibility(View.GONE);
                                                                                                                                                                                 }
                                                                                                                                                                                 DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Page");
                                                                                                                                                                                 if (tags != null && tags.keySet().size() != 0) {
                                                                                                                                                                                     for (String s : tags.keySet()) {
                                                                                                                                                                                         ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                                                             @Override
                                                                                                                                                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                                                 if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                                                                                                                                     avl.setVisibility(View.GONE);

                                                                                                                                                                                                     Page t = dataSnapshot.getValue(Page.class);
                                                                                                                                                                                                     if (t != null) {
                                                                                                                                                                                                         avl.setVisibility(View.GONE);

                                                                                                                                                                                                         boolean flag = false;
                                                                                                                                                                                                         for (Page p : data) {

                                                                                                                                                                                                             if (p.id.equals(t.id)) {
                                                                                                                                                                                                                 flag = true; //duplicate found
                                                                                                                                                                                                                 break;
                                                                                                                                                                                                             }

                                                                                                                                                                                                         }
                                                                                                                                                                                                         if (!flag)
                                                                                                                                                                                                             data.add(t);

                                                                                                                                                                                                         Collections.sort(data, new Comparator<Page>() {
                                                                                                                                                                                                             @Override
                                                                                                                                                                                                             public int compare(Page lhs, Page rhs) {
                                                                                                                                                                                                                 return lhs.name.compareToIgnoreCase(rhs.name);
                                                                                                                                                                                                             }
                                                                                                                                                                                                         });


                                                                                                                                                                                                         pageadapter.full.add(t);
                                                                                                                                                                                                         pageadapter.notifyDataSetChanged();
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
                                                                                                                                                                                 notfound.setVisibility(View.VISIBLE);
                                                                                                                                                                                 avl.setVisibility(View.GONE);
                                                                                                                                                                             }
                                                                                                                                                                         }

                                                                                                                                                                         @Override
                                                                                                                                                                         public void onCancelled(DatabaseError databaseError) {

                                                                                                                                                                         }
                                                                                                                                                                     }

                );
            else {
                notfound.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {

        }

    }
}
