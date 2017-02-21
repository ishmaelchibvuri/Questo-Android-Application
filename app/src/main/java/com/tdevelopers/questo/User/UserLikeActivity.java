package com.tdevelopers.questo.User;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserLikeActivity extends AppCompatActivity {
    ViewPager mpager;
    TabLayout tabLayout;
    String id;
    String name;
    public static CoordinatorLayout rootlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_like);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);

        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mpager = (ViewPager) findViewById(R.id.viewpagermain);
        MyData.setLiketab(tabLayout);
        name = getIntent().getExtras().getString("name");
        setTitle("");
        setSupportActionBar(toolbar);
        TextView h = (TextView) findViewById(R.id.name);
        h.setText(name + "");

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


                    if (mpager != null && tabLayout != null) {
                        mpager.setOffscreenPageLimit(1);

                        mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                            @Override
                            public Fragment getItem(int position) {
                                switch (position % 3) {
                                    case 0:
                                        return UserLikes.newInstance(id);

                                    case 1:
                                        return User_Article_Likes.newInstance(id);


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
                                        return "Articles";


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
                                        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.mainappbar);
                                        int numTab = tab.getPosition();
                                        //  if (fab != null)
                                        //    animateIn(fab);

                                        switch (numTab) {
                                            case 0:
                                                recyclerView = (RecyclerView) findViewById(R.id.userlikesrv);

                                                if (recyclerView != null) {
                                                    recyclerView.smoothScrollToPosition(0);
                                                }
                                                break;
                                            case 1:

                                                recyclerView = (RecyclerView) findViewById(R.id.userarticlelikes);

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
        } catch (Exception e) {

        } catch (Error e) {

        }


    }

}
