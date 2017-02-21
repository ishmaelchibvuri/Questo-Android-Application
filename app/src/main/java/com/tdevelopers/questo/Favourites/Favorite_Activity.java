package com.tdevelopers.questo.Favourites;

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

import com.tdevelopers.questo.R;

public class Favorite_Activity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager mpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Favorites");
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
        tabLayout = (TabLayout) findViewById(R.id.favtab);
        mpager = (ViewPager) findViewById(R.id.mpager);
        if (mpager != null && tabLayout != null) {
            mpager.setOffscreenPageLimit(1);
            mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    switch (position % 2) {
                        case 0:
                            return Favourite_Questions_Fragment.newInstance();
                        case 1:
                            return Favourite_Articles_Fragment.newInstance();

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
                            return "Questions";

                        case 1:
                            return "Feed";


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

                            switch (numTab) {
                                case 0:
                                    recyclerView = (RecyclerView) findViewById(R.id.favq);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 1:

                                    recyclerView = (RecyclerView) findViewById(R.id.fava);

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
