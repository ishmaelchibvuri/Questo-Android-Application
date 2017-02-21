package com.tdevelopers.questo.HomeScreens;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Profile;
import com.tdevelopers.questo.Friends.AllFriendsFragment;
import com.tdevelopers.questo.Friends.FollowersFragment;
import com.tdevelopers.questo.Friends.FollowingFragment;
import com.tdevelopers.questo.R;

public class MyFriends extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager fpager;
    SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tagactivitymenu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);


                if (AllFriendsFragment.alladapter != null)
                    AllFriendsFragment.alladapter.getFilter().filter(query);
                if (FollowersFragment.followeradapter != null)
                    FollowersFragment.followeradapter.getFilter().filter(query);
                if (FollowingFragment.followingadapter != null)
                    FollowingFragment.followingadapter.getFilter().filter(query);

                //  myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // UserFeedback.show( "FOnQueryTextChanged: " + s);


                if (AllFriendsFragment.alladapter != null)
                    AllFriendsFragment.alladapter.getFilter().filter(query);
                if (FollowersFragment.followeradapter != null)
                    FollowersFragment.followeradapter.getFilter().filter(query);
                if (FollowingFragment.followingadapter != null)
                    FollowingFragment.followingadapter.getFilter().filter(query);

                return false;
            }
        });
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.friendsTabLayout);
        fpager = (ViewPager) findViewById(R.id.fpager);
        setTitle("People");
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

        if (fpager != null && tabLayout != null) {
            fpager.setOffscreenPageLimit(2);
            fpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    switch (position % 3) {
                        case 0:
                            return AllFriendsFragment.newInstance();
                        case 1:
                            return FollowersFragment.newInstance(Profile.getCurrentProfile() != null ? Profile.getCurrentProfile().getId() : "0");
                        case 2:
                            return FollowingFragment.newInstance(Profile.getCurrentProfile() != null ? Profile.getCurrentProfile().getId() : "0");

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
                        case 0:
                            return "All";
                        case 1:
                            return "Followers";
                        case 2:
                            return "Following";


                    }
                    return "";
                }
            });
            tabLayout.setupWithViewPager(fpager);


            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(fpager) {
                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            RecyclerView recyclerView;
                            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                            int numTab = tab.getPosition();
                            switch (numTab) {
                                case 0:
                                    recyclerView = (RecyclerView) findViewById(R.id.rvfbfriends);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 1:

                                    recyclerView = (RecyclerView) findViewById(R.id.rvfollowers);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 2:

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