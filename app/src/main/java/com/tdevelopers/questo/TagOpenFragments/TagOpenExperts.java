package com.tdevelopers.questo.TagOpenFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tdevelopers.questo.Adapters.TagExpertAdapter;
import com.tdevelopers.questo.Objects.TagUser;
import com.tdevelopers.questo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class
TagOpenExperts extends Fragment {

    RecyclerView recyclerView;
    String id;

    //AVLoadingIndicatorView avl;
    public TagOpenExperts() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TagOpenExperts(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static TagOpenExperts newInstance(String id) {

        Bundle args = new Bundle();

        TagOpenExperts fragment = new TagOpenExperts(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag_open_experts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.tagexpertsrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //  avl=(AVLoadingIndicatorView)view.findViewById(R.id.avloading);
        if (id != null && id.trim().length() != 0) {
            Query query = FirebaseDatabase.getInstance().getReference("tag_analysis").child(id).orderByChild("score");


            recyclerView.setAdapter(new TagExpertAdapter(query, TagUser.class, id));
        }
    }
}
