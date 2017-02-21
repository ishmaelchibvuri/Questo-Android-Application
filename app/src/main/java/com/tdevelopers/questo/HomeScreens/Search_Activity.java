package com.tdevelopers.questo.HomeScreens;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.SearchFragments.PeopleSearch;
import com.tdevelopers.questo.SearchFragments.QuestionSearch;
import com.tdevelopers.questo.SearchFragments.QuoraSearch;
import com.tdevelopers.questo.SearchFragments.StackOverflowSearch;
import com.tdevelopers.questo.SearchFragments.TagSearch;

import java.util.ArrayList;
import java.util.List;

public class Search_Activity extends AppCompatActivity {

    SearchView mSearchView;
    TabLayout tabLayout;
    ViewPager mpager;
    FloatingActionButton fab;
    String data = "";
    private SearchHistoryTable mHistoryDatabase;

    private void getData(String text, int position) {
        if (text != null && text.trim().length() != 0) {
            mHistoryDatabase.addItem(new SearchItem(text));
            if (mSearchView != null)
                mSearchView.setText(text);

            StackOverflowSearch.setQuery(text);
            QuoraSearch.setQuery(text);
            TagSearch.setQuery(text);
            QuestionSearch.setQuery(text);
            PeopleSearch.setQuery(text);
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            if (appBarLayout != null)
                appBarLayout.setExpanded(true, true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusearch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_search:
                mSearchView.open(true);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);
        mSearchView = (com.lapism.searchview.SearchView) findViewById(R.id.searchaa);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            mSearchView.setHint("Search");
            mSearchView.setTextSize(16);
            mSearchView.setVoice(false);
            if (data != null) {
                mSearchView.setText(data + "");

            }
            mSearchView.setDivider(false);
            mSearchView.setAnimationDuration(com.lapism.searchview.SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new com.lapism.searchview.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mSearchView.close(false);
                    getData(query, 0);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new com.lapism.searchview.SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {
                    if (fab != null) {
                        //fab.close(true);
                        fab.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onClose() {
                    if (fab != null) {
                        //  fab.open(true);
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            });

            List<SearchItem> suggestionsList = new ArrayList<>();
            //suggestionsList.add(new SearchItem("search1"));
            // suggestionsList.add(new SearchItem("search2"));
            //suggestionsList.add(new SearchItem("search3"));

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mSearchView.close(false);
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    getData(query, position);
                }
            });
            mSearchView.setAdapter(searchAdapter);
        }
    }

    public void init() {

        mSearchView = (SearchView) findViewById(R.id.searchaa);
        setSearchView();

        if (data != null && data.trim().length() != 0)
            if (mpager != null && tabLayout != null) {
                mpager.setOffscreenPageLimit(4);
                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        switch (position % 5) {
                            case 0:
                                PeopleSearch.setQuery(data + "");
                                return PeopleSearch.newInstance();
                            case 1:
                                TagSearch.setQuery(data + "");
                                return TagSearch.newInstance();
                            case 2:
                                QuestionSearch.setQuery(data + "");
                                return QuestionSearch.newInstance();

                            case 4:
                                StackOverflowSearch.setQuery(data + "");
                                return StackOverflowSearch.newInstance();
                            case 3:
                                QuoraSearch.setQuery(data + "");
                                return QuoraSearch.newInstance();
                            default:
                                return null;
                        }
                    }

                    @Override
                    public int getCount() {
                        return 5;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        switch (position % 5) {
                            case 0:
                                return "People";
                            case 1:
                                return "Tags";
                            case 2:
                                return "Questions";
                            case 4:
                                return "Stack Exchange";
                            case 3:
                                return "Quora";

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
                                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                                int numTab = tab.getPosition();
                                switch (numTab) {
                                    case 0:
                                        recyclerView = (RecyclerView) findViewById(R.id.searchpeoplerv);

                                        if (recyclerView != null) {
                                            recyclerView.smoothScrollToPosition(0);
                                        }
                                        break;
                                    case 1:

                                        recyclerView = (RecyclerView) findViewById(R.id.searchtagrv);

                                        if (recyclerView != null) {
                                            recyclerView.smoothScrollToPosition(0);
                                        }
                                        break;
                                    case 2:

                                        recyclerView = (RecyclerView) findViewById(R.id.searchquestionrv);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_);
        data = getIntent().getExtras().getString("text");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.searchTabLayout);
        mpager = (ViewPager) findViewById(R.id.searchpager);
        setTitle("");
        setSupportActionBar(toolbar);
        init();

    }

}
