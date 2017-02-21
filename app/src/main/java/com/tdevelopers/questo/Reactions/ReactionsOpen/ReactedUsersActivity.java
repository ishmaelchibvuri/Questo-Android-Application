package com.tdevelopers.questo.Reactions.ReactionsOpen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.R;

import java.util.HashMap;
import java.util.Map;

public class ReactedUsersActivity extends AppCompatActivity {
    ViewPager mpager;
    TabLayout tabLayout;
    String id = "";

    public void init() {
        tabLayout = (TabLayout) findViewById(R.id.reactedtab);
        mpager = (ViewPager) findViewById(R.id.mpager);

        if (id != null && id.trim().length() != 0) {
            if (mpager != null && tabLayout != null) {
                mpager.setOffscreenPageLimit(1);

                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        switch (position % 2) {
                            case 0:
                                return CorrectUsers.newInstance(id + "");

                            case 1:
                                return WrongUsers.newInstance(id + "");

                            default:
                                return null;
                        }
                    }

                    @Override
                    public int getCount() {
                        return 2;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        switch (position % 3) {
                            case 0:
                                return "Correct";
                            case 1:
                                return "Wrong";

                        }
                        return "";
                    }
                });
                tabLayout.setupWithViewPager(mpager);
            }

            FirebaseDatabase.getInstance().getReference("Attempts").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            HashMap<String, Boolean> all = (HashMap<String, Boolean>) dataSnapshot.getValue();
                            HashMap<String, Boolean> correct = new HashMap<String, Boolean>();
                            HashMap<String, Boolean> wrong = new HashMap<String, Boolean>();

                            if (all != null) {


                                for (Map.Entry<String, Boolean> i : all.entrySet()) {
                                    if (i.getValue()) {
                                        correct.put(i.getKey(), i.getValue());
                                    } else {

                                        wrong.put(i.getKey(), i.getValue());
                                    }
                                }

                                CorrectUsers.setPeople(correct);
                                WrongUsers.setPeople(wrong);
                            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reacted_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Attempted Users");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }
        id = getIntent().getExtras().getString("id");
        init();

    }

}
