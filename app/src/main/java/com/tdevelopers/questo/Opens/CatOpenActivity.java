package com.tdevelopers.questo.Opens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Adapters.tagccadapter;
import com.tdevelopers.questo.Add.AddNewTag;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Objects.categories;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// circleImageView.setImageBitmap(bitmap);

public class CatOpenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String id = "";
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView nestedScrollView;
   public static CoordinatorLayout rootlayout;
    SearchView mSearchView;
    tagccadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cat_open);

        id = getIntent().getExtras().getString("id");
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView img = (ImageView) findViewById(R.id.catdp);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        nestedScrollView = (NestedScrollView) findViewById(R.id.ns);

        setTitle(R.string.app_name);
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
        final TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(id + "");
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        recyclerView = (RecyclerView) findViewById(R.id.catopenrv);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (nestedScrollView.computeVerticalScrollOffset() >= 0 && nestedScrollView.computeVerticalScrollOffset() <= (nestedScrollView.computeVerticalScrollOffset() / 2))

                {

                    mAppBarLayout.setExpanded(true);
                    //   Toast.makeText(CatOpenActivity.this, "crazy happend", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (id != null && id.trim().length() != 0) {
            Query query = FirebaseDatabase.getInstance().getReference("categories").child(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        final categories c = dataSnapshot.getValue(categories.class);
                        if (c != null) {
                            if (fab != null) {
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if (Profile.getCurrentProfile() != null) {
                                            Intent intent = new Intent(CatOpenActivity.this, AddNewTag.class);

                                            if (c.name != null && c.name.trim().length() != 0)
                                                intent.putExtra("id", c.name);
                                            startActivity(intent);
                                        } else {

                                            Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                                    .setAction("Login", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(CatOpenActivity.this, Login.class));
                                                        }
                                                    }).show();
                                        }


                                    }
                                });
                            }


                            if (c.tags != null) {
                                final ArrayList<Tag> data = new ArrayList<Tag>();
                                adapter = new tagccadapter(data);
                                //recyclerView.setAdapter(new tagrecycleradapter(c.tags, CatOpenActivity.this));
                                recyclerView.setAdapter(adapter);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tag");

                                for (String s : c.tags.keySet()) {
                                    ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                Tag t = dataSnapshot.getValue(Tag.class);
                                                if (t != null) {
                                                    data.add(t);
                                                    Collections.sort(data, new Comparator<Tag>() {
                                                        @Override
                                                        public int compare(Tag lhs, Tag rhs) {
                                                            return lhs.name.compareToIgnoreCase(rhs.name);
                                                        }
                                                    });
                                                    adapter.full.add(t);
                                                    CardView c = (CardView) findViewById(R.id.ccv);
                                                    c.setVisibility(View.VISIBLE);
                                                    adapter.notifyDataSetChanged();

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                            if (c.pic != null && c.pic.trim().length() != 0) {


                                Picasso.with(CatOpenActivity.this)
                                        .load(c.pic)
                                        .fit().centerCrop()
                                        .transform(PaletteTransformation.instance())
                                        .into(img, new Callback.EmptyCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Palette palette = null;
                                                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap(); // Ew!
                                                // circleImageView = (CircleImageView) findViewById(R.id.userdp);
                                                if (bitmap != null)
                                                    palette = PaletteTransformation.getPalette(bitmap);
                                                if (palette != null && bitmap != null && collapsingToolbarLayout != null && toolbar != null) {
                                                    //
                                                    //
                                                    // toolbar.setBackgroundColor(palette.getVibrantColor(Color.BLUE));
                                                    View view = findViewById(R.id.g);
                                                    if (view != null) {
                                                        view.setVisibility(View.VISIBLE);
                                                    }

                                                    collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(CatOpenActivity.this.getResources().getColor(R.color.colorPrimary)));
                                                    img.setBackground(CatOpenActivity.this.getResources().getDrawable(R.drawable.gradient));


// finally change the color
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        Window window = CatOpenActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                                        window.setStatusBarColor(palette.getDarkVibrantColor(CatOpenActivity.this.getResources().getColor(R.color.colorPrimaryDark)));
                                                    }

                                                }

                                            }
                                        });
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.tagactivitymenu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setQueryHint("Search Tags");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  UserFeedback.show( "SearchOnQueryTextSubmit: " + query);

                if (adapter != null)
                    adapter.getFilter().filter(query);
                //  myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);


                if (adapter != null)
                    adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
