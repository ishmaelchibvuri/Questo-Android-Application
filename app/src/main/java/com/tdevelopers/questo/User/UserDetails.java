package com.tdevelopers.questo.User;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Adapters.PageAdapter;
import com.tdevelopers.questo.HomeScreens.PageActivity;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Page;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.ScoreChart;
import com.tdevelopers.questo.User.FollowStuff.FollowStuffActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetails extends Fragment {

    public static String id;
    public static LinearLayout l1, l2, l3, l4, l5, l6, l7;
    public static TextView t1, t2, t3, t4, t5, t6, t7;
    public static user userobj;
    public static TabLayout tabLayout;
    public static TextView about_me, header;
    public static CircleImageView leveldp;
    public static TextView levelname, points;
    public static View liner1;
    public static TextView mailhead, mail;
    static Context context;
    static View line;
    long likes_count = 0;

    public static RecyclerView rv;
    public static TextView pageheader;

    public UserDetails() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public UserDetails(String id) {
        // Required empty public constructor
        UserDetails.id = id;
    }

    public static void setCurrent(user current) {

        UserDetails.userobj = current;


        try {
            {

                if (userobj != null) {


                    if (userobj.email != null && userobj.email.trim().length() != 0) {
                        liner1.setVisibility(View.VISIBLE);
                        mailhead.setVisibility(View.VISIBLE);
                        mail.setVisibility(View.VISIBLE);
                        mail.setText(userobj.email);
                    }
                    if (current.score < 0)
                        current.score *= -1;
                    points.setText(current.score + " points");
                    if (current.score < 0) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
                        levelname.setText("Negative");
                    } else if (current.score >= 0 && current.score < 100) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.noobie));
                        levelname.setText("Noobie");
                    } else if (current.score >= 100 && current.score < 500) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.geek));
                        levelname.setText("Geek");
                    } else if (current.score >= 500 && current.score < 1000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.ninja));
                        levelname.setText("Ninja");
                    } else if (current.score >= 1000 && current.score < 2000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.captain));
                        levelname.setText("Captain");
                    } else if (current.score >= 2000 && current.score < 3000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.batman));
                        levelname.setText("Bat Man");
                    } else if (current.score >= 3000 && current.score < 4000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.ironman));
                        levelname.setText("Iron Man");
                    } else if (current.score >= 4000 && current.score < 5000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.joker));
                        levelname.setText("Joker");
                    } else if (current.score >= 5000 && current.score < 7000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.sherlock));
                        levelname.setText("Sherlock");
                    } else if (current.score >= 7000 && current.score < 10000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.genious));
                        levelname.setText("Genious");
                    } else if (current.score >= 10000) {
                        leveldp.setImageDrawable(context.getResources().getDrawable(R.drawable.superman));
                        levelname.setText("Super Man");
                    }


                    if (userobj.work != null && userobj.work.trim().length() != 0) {
                        about_me.setVisibility(View.VISIBLE);
                        header.setVisibility(View.VISIBLE);
                        about_me.setText(userobj.work);
                        line.setVisibility(View.VISIBLE);

                    }

                    if (current.pagesfollowing != null && current.pagesfollowing.size() != 0) {

                        rv.setVisibility(View.VISIBLE);
                        pageheader.setVisibility(View.VISIBLE);
                        final ArrayList<Page> data = new ArrayList<Page>();
                        final PageAdapter pageadapter = new PageAdapter(data);
                        rv.setAdapter(pageadapter);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Page");
                        if (current.pagesfollowing != null && current.pagesfollowing.keySet().size() != 0) {
                            for (String s : current.pagesfollowing.keySet()) {
                                ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                                            Page t = dataSnapshot.getValue(Page.class);
                                            if (t != null) {

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

                    }
                    if (Profile.getCurrentProfile() != null && Profile.getCurrentProfile().getId().equals(userobj.id)) {
                        l7.setVisibility(View.VISIBLE);

                        l7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyData.pageflag = 2;
                                Intent i = new Intent(v.getContext(), PageActivity.class);
                                v.getContext().startActivity(i);
                            }
                        });
                    }
                    l4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), FollowStuffActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("name", userobj.name);
                            v.getContext().startActivity(i);
                        }
                    });

                    l5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), FollowStuffActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("name", userobj.name);
                            v.getContext().startActivity(i);
                        }
                    });

                    l1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), UserQuestionsActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("name", userobj.name);
                            v.getContext().startActivity(i);
                        }
                    });

                    l2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), UserArticlesActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("name", userobj.name);
                            v.getContext().startActivity(i);
                        }
                    });


                    l3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), UserLikeActivity.class);
                            i.putExtra("id", id);
                            i.putExtra("name", userobj.name);
                            v.getContext().startActivity(i);
                        }
                    });

                    if (userobj.question_count != null)
                        t1.setText(userobj.question_count + " Questions");
                    else
                        t1.setText(0 + " Questions");


                    if (userobj.article_count != null)
                        t2.setText(userobj.article_count + " Articles");
                    else

                        t2.setText(0 + " Articles");

                    if (userobj.tagsfollowing != null)
                        t6.setText(userobj.tagsfollowing.size() + " Tags Following");
                    else

                        t6.setText(0 + " Tags Following");

                    if (userobj.followers != null)
                        t4.setText(userobj.followers.size() + " Followers");
                    else

                        t4.setText(0 + " Followers");


                    if (userobj.following != null)
                        t5.setText(userobj.following.size() + " Following");
                    else

                        t5.setText(0 + " Following");

                    if (userobj.page_admin != null)
                        t7.setText(userobj.page_admin.size() + " Pages");
                    else

                        t7.setText(0 + " Pages");


                }
            }
        } catch (Exception e) {

        }


    }

    public static UserDetails newInstance(String id) {

        Bundle args = new Bundle();

        UserDetails fragment = new UserDetails(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        l1 = (LinearLayout) view.findViewById(R.id.l1);
        l2 = (LinearLayout) view.findViewById(R.id.l2);
        l3 = (LinearLayout) view.findViewById(R.id.l3);
        l4 = (LinearLayout) view.findViewById(R.id.l4);
        l5 = (LinearLayout) view.findViewById(R.id.l5);
        l6 = (LinearLayout) view.findViewById(R.id.l6);

        l7 = (LinearLayout) view.findViewById(R.id.l7);
        t1 = (TextView) view.findViewById(R.id.t1);
        t2 = (TextView) view.findViewById(R.id.t2);
        t3 = (TextView) view.findViewById(R.id.t3);
        t4 = (TextView) view.findViewById(R.id.t4);
        t5 = (TextView) view.findViewById(R.id.t5);
        t6 = (TextView) view.findViewById(R.id.t6);
        t7 = (TextView) view.findViewById(R.id.t7);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabl);
        about_me = (TextView) view.findViewById(R.id.about_me);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "proxima.ttf");
        t1.setTypeface(font);
        t2.setTypeface(font);
        line = view.findViewById(R.id.liner);
        t3.setTypeface(font);
        t4.setTypeface(font);
        t5.setTypeface(font);
        t6.setTypeface(font);
        t7.setTypeface(font);
        pageheader = (TextView) view.findViewById(R.id.pageheader);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        points = (TextView) view.findViewById(R.id.points);
        levelname = (TextView) view.findViewById(R.id.aname);
        mailhead = (TextView) view.findViewById(R.id.mailheader);
        mail = (TextView) view.findViewById(R.id.mail);
        liner1 = view.findViewById(R.id.liner1);
        leveldp = (CircleImageView) view.findViewById(R.id.adp);
        levelname.setTypeface(font);
        header = (TextView) view.findViewById(R.id.workheader);
        mail.setTypeface(font);
        header.setTypeface(font);
        points.setTypeface(font);
        about_me.setTypeface(font);
        mailhead.setTypeface(font);
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        rv.setNestedScrollingEnabled(false);
        leveldp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), ScoreChart.class));
            }
        });
        levelname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.getContext().startActivity(new Intent(v.getContext(), ScoreChart.class));
            }
        });
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.getContext().startActivity(new Intent(v.getContext(), ScoreChart.class));
            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (tabLayout != null)
                        tabLayout.getTabAt(2).select();
                } catch (Exception e) {

                }
            }
        });

        try {
            if (id != null && id.trim().length() != 0) {


                FirebaseDatabase.getInstance().getReference("userlikes").child(id).child("questionlikes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                                HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                likes_count = data.size();

                            }

                            FirebaseDatabase.getInstance().getReference("userlikes").child(id).child("articlelikes").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                        HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                        likes_count += data.size();


                                    }

                                    t3.setText(likes_count + " Likes");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {

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
