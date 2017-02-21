package com.tdevelopers.questo.User.FollowStuff;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Friends.FollowersFragment;
import com.tdevelopers.questo.Friends.FollowingFragment;
import com.tdevelopers.questo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowStuffActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager mpager;
    String id = "";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_stuff);
        name = getIntent().getExtras().getString("name");
        id = getIntent().getExtras().getString("id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabfollowstuff);
        mpager = (ViewPager) findViewById(R.id.pagerstuff);
        setTitle("");
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(name + " circle");
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
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.dp);

        String urlImage = "https://graph.facebook.com/" + id + "/picture?type=large";

        Picasso.with(this).load(urlImage).into(circleImageView);
        if (mpager != null && tabLayout != null) {
            mpager.setOffscreenPageLimit(1);

            if (id != null && id.trim().length() != 0) {
                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        switch (position % 2) {
                            case 0:
                                return FollowersFragment.newInstance(id);
                            case 1:
                                return FollowingFragment.newInstance(id);

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
                        switch (position % 2) {
                            case 0:
                                return "Followers";

                            case 1:
                                return "Following";


                        }
                        return "";
                    }
                });
            }
            tabLayout.setupWithViewPager(mpager);


            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(mpager) {
                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            RecyclerView recyclerView;
                            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                            int numTab = tab.getPosition();

                            switch (numTab) {
                                case 0:
                                    recyclerView = (RecyclerView) findViewById(R.id.rvfollowers);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 1:

                                    recyclerView = (RecyclerView) findViewById(R.id.rvfollowing);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;

                            }
                            if (appBarLayout != null)
                                appBarLayout.setExpanded(true, true);
                        }

                    });
        }

    }
}
