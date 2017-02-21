package com.tdevelopers.questo.User;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Adapters.EndlessRecyclerViewScrollListener;
import com.tdevelopers.questo.Adapters.QuestionRecyclerAdapter;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class UserQuestionsActivity extends AppCompatActivity {


    public static QuestionRecyclerAdapter adapter;
    public static ArrayList<Question> tobesenter;
    RecyclerView recyclerView;
    String id;
    GifImageView avl;
    ImageView notfound;
    String name;

    public static CoordinatorLayout rootlayout;

    public void LoadData() {
        final Query q = FirebaseDatabase.getInstance().getReference("Question").orderByChild("uploaded_by").equalTo(id).limitToFirst(10);
        final LinearLayoutManager l = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(l);
        //  avl.setVisibility(View.VISIBLE);
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

                        adapter = new QuestionRecyclerAdapter(tobesenter);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(l) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                final Query extra = FirebaseDatabase.getInstance().getReference("Question").orderByChild("uploaded_by").equalTo(id).limitToFirst(totalItemsCount + 5);
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


                    } else {

                        notfound.setVisibility(View.VISIBLE);
                        avl.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        name = getIntent().getExtras().getString("name");
        setTitle("");
        setSupportActionBar(toolbar);
        TextView h = (TextView) findViewById(R.id.name);
        h.setText(name + "");
        avl = (GifImageView) findViewById(R.id.avloading);
        recyclerView = (RecyclerView) findViewById(R.id.userquestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        notfound = (ImageView) findViewById(R.id.notfound);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

        try {

            if (getIntent().getExtras() != null && getIntent().getExtras().getString("id") != null) {

                id = getIntent().getExtras().getString("id");
                if (id != null && id.trim().length() != 0) {


                    CircleImageView circleImageView = (CircleImageView) findViewById(R.id.dp);

                    String urlImage = "https://graph.facebook.com/" + id + "/picture?type=large";

                    Picasso.with(this).load(urlImage).into(circleImageView);
                    LoadData();

                }

            }
        } catch (Exception e) {

        } catch (Error e) {

        }

    }

}
