package com.tdevelopers.questo.HomeScreens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.tdevelopers.questo.Add.AddArticleMaterial;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.MainArticles.MostLikedArticles;
import com.tdevelopers.questo.MainArticles.NewArticles;
import com.tdevelopers.questo.MainArticles.TrendingArticles;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class ArticleActivity extends AppCompatActivity {
    public static boolean viewflag = false;
    Menu menu;
    TabLayout tabLayout;
    ViewPager mpager;
    FloatingActionButton floatingActionButton;
    RecyclerView filterrv;
    HashSet<String> filterTags = new HashSet<>();
    TagContainerLayout tagContainerLayout;
    FullTagAdapter fullTagAdapter;
    android.support.v7.widget.SearchView filtersearch;
    CoordinatorLayout rootlayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.articlemenu, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.filter:
                if (MyData.haveNetworkConnection())
                    filter();
                else
                    Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filter() {


        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Filter")
                .customView(R.layout.filterframe, true)
                .positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (filterTags != null) {
                            Toast.makeText(ArticleActivity.this, " Filters Applied " + filterTags.toString(), Toast.LENGTH_SHORT).show();
                            MostLikedArticles.setFilterTags(new ArrayList<String>(filterTags));
                            NewArticles.setFilterTags(new ArrayList<String>(filterTags));
                            TrendingArticles.setFilterTags(new ArrayList<String>(filterTags));
                            if (menu != null) {
                                ActionItemBadge.update(ArticleActivity.this, menu.findItem(R.id.filter), getResources().getDrawable(R.drawable.ic_filter_outline_white_24dp), ActionItemBadge.BadgeStyles.RED, filterTags.size());

                            }

                        }
                    }
                }).neutralText("Cancel")
                .build();
        final LinearLayoutManager l = new LinearLayoutManager(this);

        filterrv = (RecyclerView) dialog.findViewById(R.id.tagsrv);
        filterrv.setLayoutManager(l);
        filterrv.setNestedScrollingEnabled(false);
        tagContainerLayout = (TagContainerLayout) dialog.findViewById(R.id.tag_container);
        if (filterTags != null) {
            tagContainerLayout.setTags(new ArrayList<>(filterTags));
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                @Override
                public void onTagClick(final int position, final String text) {
                    AlertDialog dialog = new AlertDialog.Builder(ArticleActivity.this)
                            .setTitle("Delete " + text)
                            .setMessage("Remove " + text + " tag from filter ?")
                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        tagContainerLayout.removeTag(position);
                                        filterTags.remove(text);
                                        // View v = l.findViewByPosition(position);
                                        //    CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
                                        //    cb.setText("dummy");
                                        //    cb.setChecked(false);
                                    } catch (Exception e) {
                                        Log.e("exception", e.toString());
                                    }

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }

                @Override
                public void onTagLongClick(final int position, String text) {
                    // ...
                }
            });
        }
        Query query = FirebaseDatabase.getInstance().getReference("Tag");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                    ArrayList<Tag> data = new ArrayList<Tag>();
                    for (DataSnapshot c : dataSnapshot.getChildren()) {
                        if (c != null)
                            data.add(c.getValue(Tag.class));
                    }
                    Collections.sort(data, new Comparator<Tag>() {
                        @Override
                        public int compare(Tag lhs, Tag rhs) {
                            return lhs.name.compareTo(rhs.name);
                        }
                    });
                    fullTagAdapter = new FullTagAdapter(data);
                    filterrv.setAdapter(fullTagAdapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        filtersearch = (android.support.v7.widget.SearchView) dialog.findViewById(R.id.filtersearch);
        filtersearch.setVisibility(View.VISIBLE);
        filtersearch.setIconifiedByDefault(false);
        filtersearch.setSubmitButtonEnabled(false);
        filtersearch.setQueryHint("Search Tags");
        filtersearch.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (fullTagAdapter != null)
                    fullTagAdapter.getFilter().filter(newText);
                return true;
            }
        });


        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();


        try {
            ActionItemBadge.update(ArticleActivity.this, menu.findItem(R.id.filter), getResources().getDrawable(R.drawable.ic_filter_outline_white_24dp), ActionItemBadge.BadgeStyles.RED, filterTags.size());

        } catch (Exception e) {
            return;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        mpager = (ViewPager) findViewById(R.id.mpager);
        //  avl = (AVLoadingIndicatorView) findViewById(R.id.avloading);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyData.haveNetworkConnection()) {
                    if (Profile.getCurrentProfile() != null)
                        startActivity(new Intent(ArticleActivity.this, AddArticleMaterial.class));
                    else {

                        Snackbar.make(rootlayout, "Please login to add", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArticleActivity.this.startActivity(new Intent(ArticleActivity.this, Login.class));

                                    }
                                }).show();
                    }
                } else
                    Toast.makeText(ArticleActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

            }
        });
        setTitle("Feed");
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

        init();
    }

    public void init() {
        mpager = (ViewPager) findViewById(R.id.viewpagermain);
        if (mpager != null && tabLayout != null) {
            mpager.setOffscreenPageLimit(2);

            mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    switch (position % 3) {
                        case 0:
                            return MostLikedArticles.newInstance();

                        case 1:
                            return TrendingArticles.newInstance();
                        case 2:
                            return NewArticles.newInstance();

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
                            return "";
                        case 1:
                            return "";
                        case 2:
                            return "";


                    }
                    return "";
                }
            });
            tabLayout.setupWithViewPager(mpager);

            tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite_border_white_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_whatshot_white_24dp);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_fiber_new_white_24dp);

            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(mpager) {
                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            RecyclerView recyclerView;
                            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                            int numTab = tab.getPosition();
                            //  if (fab != null)
                            //    animateIn(fab);

                            switch (numTab) {
                                case 0:
                                    recyclerView = (RecyclerView) findViewById(R.id.rvmostliked);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 1:

                                    recyclerView = (RecyclerView) findViewById(R.id.rvtrending);

                                    if (recyclerView != null) {
                                        recyclerView.smoothScrollToPosition(0);
                                    }
                                    break;
                                case 2:

                                    recyclerView = (RecyclerView) findViewById(R.id.rvnew);

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

    public class FullTagAdapter extends RecyclerView.Adapter<FullTagAdapter.tagholder> implements Filterable {

        public ArrayList<Tag> full;
        ArrayList<Tag> datalist;
        ValueFilter valueFilter;

        public FullTagAdapter(ArrayList<Tag> data) {

            datalist = data;
            full = new ArrayList<>(datalist);
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        @Override
        public tagholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtertagiteem, parent, false);
            return new tagholder(view);
        }

        @Override
        public void onBindViewHolder(final tagholder holder, final int position) {
            if (datalist != null && datalist.get(position % datalist.size()) != null) {
                Tag t = datalist.get(position % datalist.size());
                if (t != null) {
                    if (t.name != null)
                        holder.cc.setText(t.name);

                    if (filterTags != null) {
                        if (filterTags.contains(t.name)) {
                            holder.cc.setChecked(true);
                        } else
                            holder.cc.setChecked(false);
                    }
                    holder.cc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!holder.cc.isChecked()) {
                                holder.cc.setChecked(false);
                                filterTags.remove((datalist.get(position % datalist.size())).name);
                                tagContainerLayout.setTags(new ArrayList<>(filterTags));
                            } else {
                                holder.cc.setChecked(true);
                                filterTags.add((datalist.get(position % datalist.size())).name);
                                tagContainerLayout.setTags(new ArrayList<>(filterTags));
                            }
                        }
                    });

                }

            }

        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                //   if (constraint != null && constraint.length() > 0) {
                LinkedList<Tag> filterList = new LinkedList<>();

                for (int i = 0; i < full.size(); i++) {
                    if ((full.get(i).name.toUpperCase().contains(constraint.toString().toUpperCase()))) {


                        filterList.add(full.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
                // } else {
                //       results.count = dataTrue.size();
                //       results.values = dataTrue;
                //   }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                LinkedList<Tag> linkedList = (LinkedList<Tag>) results.values;
                datalist = new ArrayList<>();

                for (int i = 0; i < linkedList.size(); i++) {
                    datalist.add(linkedList.get(i));
                }

                notifyDataSetChanged();

            }

        }


        class tagholder extends RecyclerView.ViewHolder {

            AppCompatCheckBox cc;

            public tagholder(View itemView) {
                super(itemView);
                cc = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);

            }


        }


    }


}
