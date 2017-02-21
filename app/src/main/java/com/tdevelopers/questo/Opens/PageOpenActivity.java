package com.tdevelopers.questo.Opens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Add.AddAdmin;
import com.tdevelopers.questo.Add.AddQuestion;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Page;
import com.tdevelopers.questo.PageOpenTabs.PageOpenFollowers;
import com.tdevelopers.questo.PageOpenTabs.PageOpenQuestions;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.libraries.BottomSheetFragment;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PageOpenActivity extends AppCompatActivity implements View.OnClickListener {
    public static CoordinatorLayout rootlayout;
    TabLayout tabLayout;
    ViewPager mpager;
    String id = "";
    Toolbar toolbar;
    TextView name, followers, stuff;
    Button follow;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CircleImageView circle;
    Page item;
    FloatingActionButton add;
    boolean adminflag = false;
    ImageView admin, website, desc;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.page_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (id != null && id.trim().length() != 0)
            switch (item.getItemId()) {

                case R.id.share:

                    String text;
                    if (PageOpenActivity.this.item != null)
                        text = "Explore question, articles  of " + PageOpenActivity.this.item.name + " page\n #Questo\n\n" + MyData.buildDeepLink("page", PageOpenActivity.this.id);
                    else
                        text = "Explore question, articles  of  page\n #Questo\n\n" + MyData.buildDeepLink("page", PageOpenActivity.this.id);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    BottomSheet sheet = BottomSheetHelper.shareAction(this, shareIntent).title("Share App").build();
                    sheet.show();
                    return true;

                case R.id.sharelink:

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), MyData.buildDeepLink("page", id).toString());
                    clipboard.setPrimaryClip(clip);

                    Snackbar.make(rootlayout, "Link copied", Snackbar.LENGTH_LONG)
                            .setAction("Share", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String text;
                                    if (PageOpenActivity.this.item != null)
                                        text = "Explore question, articles  of " + PageOpenActivity.this.item.name + " page\n #Questo\n\n" + MyData.buildDeepLink("page", PageOpenActivity.this.id);
                                    else
                                        text = "Explore question, articles  of  page\n #Questo\n\n" + MyData.buildDeepLink("page", PageOpenActivity.this.id);

                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    BottomSheet sheet = BottomSheetHelper.shareAction(PageOpenActivity.this, shareIntent).title("Share App").build();
                                    sheet.show();
                                }
                            }).show();


                    return true;


                default:
                    return super.onOptionsItemSelected(item);
            }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_open);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        admin = (ImageView) findViewById(R.id.admin);
        website = (ImageView) findViewById(R.id.website);
        desc = (ImageView) findViewById(R.id.desc);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        add = (FloatingActionButton) findViewById(R.id.add);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = PageOpenActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS fl
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        id = getIntent().getExtras().getString("id");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mpager = (ViewPager) findViewById(R.id.pager);
        name = (TextView) findViewById(R.id.name);
        follow = (Button) findViewById(R.id.follow);
        followers = (TextView) findViewById(R.id.followers);
        stuff = (TextView) findViewById(R.id.stuff);
        circle = (CircleImageView) findViewById(R.id.pic);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        setTitle("Page");
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

        if (id != null && id.trim().length() != 0) {
            if (mpager != null && tabLayout != null) {
                mpager.setOffscreenPageLimit(2);
                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {
                        switch (position % 2) {
                            case 0:
                                return PageOpenQuestions.newInstance(id);
                            case 1:
                                return PageOpenFollowers.newInstance(id);

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
                                return "Likes";

                        }
                        return "";
                    }
                });
                tabLayout.setupWithViewPager(mpager);
                tabLayout.setOnTabSelectedListener(
                        new TabLayout.ViewPagerOnTabSelectedListener(mpager) {


                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                super.onTabSelected(tab);
                                RecyclerView recyclerView;
                                switch (mpager.getCurrentItem()) {
                                    case 0:
                                        if (Profile.getCurrentProfile() != null) {


                                            if (adminflag)
                                                add.setVisibility(View.VISIBLE);

                                            else {
                                                recyclerView = (RecyclerView) findViewById(R.id.tagopenquestionrv);
                                                if (recyclerView != null) {
                                                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                        @Override
                                                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                            super.onScrollStateChanged(recyclerView, newState);
                                                            add.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                                add.setVisibility(View.GONE);

                                            }

                                        } else {
                                            recyclerView = (RecyclerView) findViewById(R.id.tagopenquestionrv);
                                            if (recyclerView != null) {
                                                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                    @Override
                                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                        super.onScrollStateChanged(recyclerView, newState);
                                                        add.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
                                            add.setVisibility(View.GONE);
                                        }
                                        break;


                                    case 1:
                                        recyclerView = (RecyclerView) findViewById(R.id.tagfollowersrv);
                                        if (recyclerView != null) {
                                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                @Override
                                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                    super.onScrollStateChanged(recyclerView, newState);
                                                    add.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                        add.setVisibility(View.GONE);
                                        break;
                                }
                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {
                                super.onTabSelected(tab);
                                RecyclerView recyclerView;
                                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                                int numTab = tab.getPosition();

                                switch (numTab) {
                                    case 0:
                                        recyclerView = (RecyclerView) findViewById(R.id.tagopenquestionrv);

                                        if (recyclerView != null) {
                                            recyclerView.smoothScrollToPosition(0);
                                        }
                                        if (add != null && Profile.getCurrentProfile() != null && adminflag)
                                            add.show();
                                        break;
                                    case 1:

                                        recyclerView = (RecyclerView) findViewById(R.id.tagfollowersrv);

                                        if (recyclerView != null) {
                                            recyclerView.smoothScrollToPosition(0);
                                        }
                                        //   if (add != null)
                                        //      add.show();
                                        break;

                                }
                                if (appBarLayout != null)
                                    appBarLayout.setExpanded(true, true);
                            }

                        });
            }
            Query query = FirebaseDatabase.getInstance().getReference("Page").child(id);
            final Toolbar finalToolbar = toolbar;
            query.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //     Toast.makeText(TagOpenActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                    item = dataSnapshot.getValue(Page.class);
                                                    if (item != null) {


                                                        if (item.admins != null && Profile.getCurrentProfile() != null && item.admins.keySet().contains(Profile.getCurrentProfile().getId())) {


                                                            add.setVisibility(View.VISIBLE);
                                                            add.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent i = new Intent(PageOpenActivity.this, AddQuestion.class);
                                                                    i.putExtra("pageid", item.id);
                                                                    startActivity(i);
                                                                }
                                                            });
                                                            adminflag = true;
                                                            if (admin != null) {
                                                                admin.setVisibility(View.VISIBLE);
                                                                admin.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Intent intent = new Intent(PageOpenActivity.this, AddAdmin.class);
                                                                        intent.putExtra("id", PageOpenActivity.this.item.id);
                                                                        intent.putExtra("name", PageOpenActivity.this.item.name);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                            }
                                                        } else {

                                                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tagopenquestionrv);
                                                            if (recyclerView != null) {
                                                                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                                    @Override
                                                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                                        super.onScrollStateChanged(recyclerView, newState);
                                                                        add.setVisibility(View.GONE);
                                                                    }
                                                                });
                                                            }
                                                            add.setVisibility(View.GONE);
                                                        }


                                                        if (item.website != null && item.website.trim().length() != 0) {
                                                            if (website != null) {
                                                                website.setVisibility(View.VISIBLE);
                                                                website.setOnClickListener(PageOpenActivity.this);
                                                            }
                                                        }
                                                        if (item.desc != null && item.desc.trim().length() != 0) {
                                                            if (desc != null) {
                                                                desc.setVisibility(View.VISIBLE);
                                                                desc.setOnClickListener(PageOpenActivity.this);
                                                            }
                                                        }

                                                        if (Profile.getCurrentProfile() != null)
                                                            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("pagesfollowing").addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                        HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                                                        if (data != null && data.containsKey(item.id)) {
                                                                            follow.setText("Unlike");

                                                                            //   follow.getBackground().setColorFilter(ContextCompat.getColor(TagOpenActivity.this, R.color.green), PorterDuff.Mode.MULTIPLY);
                                                                        } else {
                                                                            follow.setText("Like");

                                                                            //  follow.getBackground().setColorFilter(ContextCompat.getColor(TagOpenActivity.this, R.color.blue), PorterDuff.Mode.MULTIPLY);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                                        follow.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (Profile.getCurrentProfile() != null) {
                                                                    if (MyData.haveNetworkConnection()) {
                                                                        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("PageFollowers").child(item.id);
                                                                        postRef.runTransaction(new Transaction.Handler() {
                                                                            @Override
                                                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                //    Post p = mutableData.getValue(Post.class);


                                                                                HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                                                                if (p == null) {
                                                                                    p = new HashMap<String, Boolean>();

                                                                                    p.put(Profile.getCurrentProfile().getId(), true);
                                                                                    mutableData.setValue(p);
                                                                                    return Transaction.success(mutableData);
                                                                                }

                                                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                                                    // Unstar the post and remove self from stars
                                                                                    //p.starCount = p.starCount - 1;
                                                                                    p.remove(Profile.getCurrentProfile().getId());

                                                                                } else {
                                                                                    // Star the post and add self to stars
                                                                                    //p.starCount = p.starCount + 1;
                                                                                    p.put(Profile.getCurrentProfile().getId(), true);
                                                                                }

                                                                                // Set value and report transaction success
                                                                                mutableData.setValue(p);
                                                                                return Transaction.success(mutableData);
                                                                            }

                                                                            @Override
                                                                            public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                   DataSnapshot dataSnapshot) {


                                                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId() + "").child("pagesfollowing");
                                                                                DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Page").child(item.id).child("followers");
                                                                                HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                                                                if (p != null) {
                                                                                    if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                                                        follow.setText("Unlike");
                                                                                        // follow.getBackground().setColorFilter(ContextCompat.getColor(TagOpenActivity.this, R.color.green), PorterDuff.Mode.MULTIPLY);
                                                                                        ref.child(item.id).setValue(true);

                                                                                        if (item != null && item.admins != null)
                                                                                            for (final String s : item.admins.keySet())
                                                                                                FirebaseDatabase.getInstance().getReference("myUsers").child(s).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                                            String gcmid = (String) (dataSnapshot.getValue());


                                                                                                            String link = "Page:" + item.id;

                                                                                                            MyData.pushNotification(Profile.getCurrentProfile().getName(), "liked your page\n\n" + item.name, gcmid + "", link, s);
                                                                                                        }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                                    }
                                                                                                });


                                                                                    } else {
                                                                                        follow.setText("Like");
                                                                                        ref.child(item.id).setValue(null);
                                                                                        //  follow.getBackground().setColorFilter(ContextCompat.getColor(TagOpenActivity.this, R.color.blue), PorterDuff.Mode.MULTIPLY);

                                                                                    }

                                                                                    followers.setText(p.size() + " Likes");
                                                                                    tagref.setValue(p.size());
                                                                                }
                                                                                if (p == null) {
                                                                                    follow.setText("Like");
                                                                                    ref.child(item.id).setValue(null);
                                                                                    followers.setText("0 Likes");
                                                                                    tagref.setValue(0);
                                                                                    //  follow.getBackground().setColorFilter(ContextCompat.getColor(TagOpenActivity.this, R.color.blue), PorterDuff.Mode.MULTIPLY);

                                                                                }

                                                                            }
                                                                        });
                                                                    } else {
                                                                        Toast.makeText(PageOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {

                                                                    Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                                            .setAction("Login", new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    PageOpenActivity.this.startActivity(new Intent(PageOpenActivity.this, Login.class));

                                                                                }
                                                                            }).show();
                                                                }
                                                            }
                                                        });


                                                        if (item.followers != null)
                                                            followers.setText(item.followers + " Likes");
                                                        else
                                                            followers.setText("0 Likes");
                                                        name.setText(item.name + "");
                                                        stuff.setText(item.question_count + " Questions | " + item.article_count + " Articles");
                                                        if (item.pic != null && item.pic.trim().length() != 0)
                                                            Picasso.with(PageOpenActivity.this)
                                                                    .load(item.pic)
                                                                    .fit().centerCrop()
                                                                    .transform(PaletteTransformation.instance())
                                                                    .into(circle, new Callback.EmptyCallback() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            Palette palette = null;
                                                                            Bitmap bitmap = ((BitmapDrawable) circle.getDrawable()).getBitmap(); // Ew!
                                                                            if (bitmap != null)
                                                                                palette = PaletteTransformation.getPalette(bitmap);
                                                                            if (palette != null && collapsingToolbarLayout != null && tabLayout != null && finalToolbar != null) {

                                                                                int primary = palette.getVibrantColor(palette.getVibrantColor(getResources().getColor(R.color.color_material_motion)));
                                                                                int primaryDark = palette.getDarkVibrantColor(getResources().getColor(R.color.color_dark_material_motion));

                                                                                if (primary != getResources().getColor(R.color.color_material_motion) && primaryDark != getResources().getColor(R.color.color_dark_material_motion)) {
                                                                                    collapsingToolbarLayout.setBackgroundColor(primaryDark);
                                                                                    finalToolbar.setBackgroundColor(primaryDark);
                                                                                    tabLayout.setBackgroundColor(primary);
// finally change the color
                                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                                        Window window = PageOpenActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS fl
                                                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                                                                                        window.setStatusBarColor(palette.getDarkVibrantColor(PageOpenActivity.this.getResources().getColor(R.color.colorPrimaryDark)));
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }

            );
        }


    }

    @Override
    public void onClick(View v) {
        if (item != null)
            switch (v.getId()) {

                case R.id.website:

                    if (PageOpenActivity.this.item.website != null && PageOpenActivity.this.item.website.trim().length() != 0) {
                        String url = PageOpenActivity.this.item.website;

                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                    break;

                case R.id.desc:

                    if (PageOpenActivity.this.item.desc != null && PageOpenActivity.this.item.desc.trim().length() != 0)
                        new BottomSheetFragment(PageOpenActivity.this.item.desc, "About").show(getSupportFragmentManager(), "About");
                    break;

            }
    }
}
