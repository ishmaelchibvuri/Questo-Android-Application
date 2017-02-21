package com.tdevelopers.questo.HomeScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.facebook.Profile;
import com.tdevelopers.questo.Add.AddPage;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.PageFragments.FollowingPages;
import com.tdevelopers.questo.PageFragments.MyPages;
import com.tdevelopers.questo.PageFragments.PageFragment;
import com.tdevelopers.questo.R;

public class PageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager mpager;
    SearchView mSearchView;
    public static CoordinatorLayout rootlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tagtablayout);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        mpager = (ViewPager) findViewById(R.id.mpager);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Profile.getCurrentProfile() != null) {
                        if (MyData.haveNetworkConnection())
                            startActivity(new Intent(PageActivity.this, AddPage.class));
                        else
                            Toast.makeText(PageActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                    } else {

                        Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(PageActivity.this, Login.class));
                                    }
                                }).show();
                    }
                }
            });
        }
        setTitle("Pages");

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
        if (mpager != null && tabLayout != null) {
            mpager.setOffscreenPageLimit(2);
            mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    switch (position % 3) {
                        case 0:
                            return PageFragment.newInstance();
                        case 1:
                            return FollowingPages.newInstance();
                        case 2:
                            return MyPages.newInstance();
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
                            return "My Pages";
                        case 1:
                            return "Liked";

                        case 0:
                            return "All";


                    }
                    return "";
                }
            });

            tabLayout.setupWithViewPager(mpager);


            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(mpager) {
                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            RecyclerView recyclerView;
                            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                            int numTab = tab.getPosition();
                            if (floatingActionButton != null) {
                                    floatingActionButton.show();
                            }
                            switch (numTab) {
                                case 0:
                                    recyclerView = (RecyclerView) findViewById(R.id.pagesrv);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 1:

                                    recyclerView = (RecyclerView) findViewById(R.id.pagesfollowingrv);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 2:

                                    recyclerView = (RecyclerView) findViewById(R.id.mypagesrv);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;

                            }
                            if (appBarLayout != null)
                                appBarLayout.setExpanded(true, true);
                        }

                    });

            mpager.setCurrentItem(MyData.pageflag);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tagactivitymenu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setQueryHint("Search Pages");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);


                if (PageFragment.pageadapter != null)
                    PageFragment.pageadapter.getFilter().filter(query);
                if (FollowingPages.pageadapter != null)
                    FollowingPages.pageadapter.getFilter().filter(query);

                if (MyPages.pageadapter != null)
                    MyPages.pageadapter.getFilter().filter(query);
                //  myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);


                if (PageFragment.pageadapter != null)
                    PageFragment.pageadapter.getFilter().filter(query);
                if (FollowingPages.pageadapter != null)
                    FollowingPages.pageadapter.getFilter().filter(query);

                if (MyPages.pageadapter != null)
                    MyPages.pageadapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }

}
