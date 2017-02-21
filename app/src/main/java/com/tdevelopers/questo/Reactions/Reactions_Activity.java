package com.tdevelopers.questo.Reactions;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.R;

public class Reactions_Activity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager mpager;
    String id = "";
static CoordinatorLayout rootlayout;

    public void init() {
        tabLayout = (TabLayout) findViewById(R.id.reactionTabLayout);
        mpager = (ViewPager) findViewById(R.id.mpager);

        if (id != null && id.trim().length() != 0) {

            if (mpager != null && tabLayout != null) {
                mpager.setOffscreenPageLimit(2);

                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        switch (position % 3) {
                            case 2:
                                return LikeReactions.newInstance(id + "");

                            case 1:
                                return CommentReactions.newInstance(id + "");
                            case 0:
                                return ViewReactions.newInstance(id + "");

                            default:
                                return null;
                        }
                    }

                    @Override
                    public int getCount() {
                        return 3;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        switch (position % 3) {
                            case 2:
                                return "";
                            case 1:
                                return "";
                            case 0:
                                return "";


                        }
                        return "";
                    }
                });
                tabLayout.setupWithViewPager(mpager);
                mpager.setCurrentItem(MyData.getType());
                tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_red_500_24dp);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_comment_blue_500_24dp);
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_people_brown_500_24dp);


            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactions_);
        rootlayout=(CoordinatorLayout)findViewById(R.id.rootlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.id = getIntent().getExtras().getString("id");

        setTitle("Reactions");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.search_ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }
        init();


    }

}
