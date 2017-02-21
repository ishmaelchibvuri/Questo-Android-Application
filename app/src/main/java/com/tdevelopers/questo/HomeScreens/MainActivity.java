package com.tdevelopers.questo.HomeScreens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;
import com.facebook.Profile;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.About_me;
import com.tdevelopers.questo.Adapters.NotificationAdapter;
import com.tdevelopers.questo.Add.AddArticleMaterial;
import com.tdevelopers.questo.Add.AddNewTag;
import com.tdevelopers.questo.Add.AddPage;
import com.tdevelopers.questo.Add.AddQuestion;
import com.tdevelopers.questo.ChatStuff.ChatMain;
import com.tdevelopers.questo.ChatStuff.NewChatActivity;
import com.tdevelopers.questo.Dummy_About;
import com.tdevelopers.questo.Explore.Explore_Activity;
import com.tdevelopers.questo.Favourites.Favorite_Activity;
import com.tdevelopers.questo.LeaderShip.Leadershipboard_activity;
import com.tdevelopers.questo.LoginStuff.Introduction;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.MainFragments.MostLiked;
import com.tdevelopers.questo.MainFragments.New;
import com.tdevelopers.questo.MainFragments.Trending;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Objects.notifications;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.Opens.ArticleOpenActivity;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.Opens.QuestionOpenActivity;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.Pushes.FirebaseInstanceIDService;
import com.tdevelopers.questo.Pushes.MyFirebaseMessagingService;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.ScoreChart;
import com.tdevelopers.questo.User.User;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final int REQUEST_INVITE = 0;
    public static CoordinatorLayout rootLayout;
    public static View Header;
    public static AppBarLayout appBarLayout;
    public static SpringFloatingActionMenu springFloatingActionMenu;
    public static com.melnykov.fab.FloatingActionButton fabr;
    private static int[] frameAnimRes = new int[]{
            R.mipmap.compose_anim_1,
            R.mipmap.compose_anim_2,
            R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4,
            R.mipmap.compose_anim_5,
            R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7,
            R.mipmap.compose_anim_8,
            R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10,
            R.mipmap.compose_anim_11,
            R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13,
            R.mipmap.compose_anim_14,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_16,
            R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18,
            R.mipmap.compose_anim_19
    };
    CircleImageView circleImageView;
    TabLayout tabLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView mSearchView;
    TagContainerLayout tagContainerLayout;
    LinearLayout back;
    ViewPager mpager;
    Menu menu;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userref;
    RecyclerView filterrv;
    HashSet<String> filterTags = new HashSet<>();
    android.support.v7.widget.SearchView filtersearch;
    FullTagAdapter fullTagAdapter;
    ImageView notfound;
    private GoogleApiClient mGoogleApiClient;
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;
    private SearchHistoryTable mHistoryDatabase;

    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    public void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemIconTintList(null);

        }
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootlayout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setTitleTextColor(this.getResources().getColor(R.color.invertcolor));
        setTitle(R.string.app_name);
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        fabr = new FloatingActionButton(this);
        fabr.setType(com.melnykov.fab.FloatingActionButton.TYPE_NORMAL);
        fabr.setImageDrawable(getResources().getDrawable(R.mipmap.compose_anim_1));

        fabr.setColorPressedResId(R.color.colorPrimary);
        fabr.setColorNormalResId(R.color.red);
        fabr.setColorRippleResId(R.color.text_color);
        fabr.setShadow(true);


        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fabr)
                .addMenuItem(R.color.quote, R.mipmap.ic_messaging_posttype_quote, "Question", R.color.white, this)
                .addMenuItem(R.color.photo, R.mipmap.ic_messaging_posttype_photo, "Photo", R.color.white, this)
                .addMenuItem(R.color.chat, R.mipmap.ic_messaging_posttype_chat, "Chat", R.color.white, this)
                .addMenuItem(R.color.audio, R.drawable.ic_tag_white_36dp, "Tag", R.color.white, this)
                .addMenuItem(R.color.text, R.mipmap.ic_messaging_posttype_text, "Text", R.color.white, this)
                .addMenuItem(R.color.link, R.drawable.ic_flag_white_24dp, "Page", R.color.white, this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.tback)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fabr.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                        // RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                        //Blurry.with(MainActivity.this).radius(25).sampling(2).onto((ViewGroup) rl);
                    }

                    @Override
                    public void onMenuClose() {
                        fabr.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();

                        //RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                        // Blurry.delete((ViewGroup) rl);
                        //  Blurry.with(MainActivity.this).radius(25).sampling(2).onto((ViewGroup) rl);
                    }
                })
                .build();


        mpager = (ViewPager) findViewById(R.id.viewpagermain);
        if (mpager != null && tabLayout != null) {
            mpager.setOffscreenPageLimit(2);

            mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                @Override
                public Fragment getItem(int position) {
                    switch (position % 3) {
                        case 0:
                            return MostLiked.newInstance();

                        case 1:
                            return Trending.newInstance();
                        case 2:
                            return New.newInstance();

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


        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyData.exsist = 1;
        appBarLayout = (AppBarLayout) findViewById(R.id.mainappbar);
        notfound = (ImageView) findViewById(R.id.notfound);


        MyData.setNotfound(notfound);
        MyData.context = this;
        mSearchView = (SearchView) findViewById(R.id.searchView);
        setSearchView();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
                // would automatically launch the deep link if one is found.
                boolean autoLaunchDeepLink = false;
                AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, MainActivity.this, autoLaunchDeepLink)
                        .setResultCallback(
                                new ResultCallback<AppInviteInvitationResult>() {
                                    @Override
                                    public void onResult(@NonNull AppInviteInvitationResult result) {
                                        if (result.getStatus().isSuccess()) {
                                            // Extract deep link from Intent

                                            try {
                                                Intent intent = result.getInvitationIntent();
                                                String link = AppInviteReferral.getDeepLink(intent);


                                                // Handle the deep link. For example, open the linked
                                                // content, or apply promotional credit to the user's
                                                // account.

                                                // [START_EXCLUDE]
                                                // Display deep link in the UI
                                                // [END_EXCLUDE]


                                                if (link != null && link.trim().length() != 0) {
                                                    String deepLink;


                                                    Intent i = null;

                                                    deepLink = link.substring(link.lastIndexOf("/") + 1);
                                                    if (link.contains("question")) {
                                                        i = new Intent(MainActivity.this, QuestionOpenActivity.class);
                                                        i.putExtra("id", deepLink);

                                                    } else if (link.contains("page")) {

                                                        i = new Intent(MainActivity.this, PageOpenActivity.class);
                                                        i.putExtra("id", deepLink);

                                                    } else if (link.contains("article")) {

                                                        i = new Intent(MainActivity.this, ArticleOpenActivity.class);
                                                        i.putExtra("id", deepLink);

                                                    } else if (link.contains("tag")) {

                                                        i = new Intent(MainActivity.this, TagOpenActivity.class);
                                                        i.putExtra("id", deepLink);


                                                    } else if (link.contains("user")) {

                                                        i = new Intent(MainActivity.this, User.class);
                                                        i.putExtra("id", deepLink);

                                                    }
                                                    if (i != null)
                                                        startActivity(i);
                                                }

                                            } catch (Exception e) {

                                            }


                                        }
                                    }
                                });
                startService(new Intent(MainActivity.this, FirebaseInstanceIDService.class));
                startService(new Intent(MainActivity.this, MyFirebaseMessagingService.class));
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notificationsrv);
                if (recyclerView != null) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    //recyclerView.setNestedScrollingEnabled(false);
                    if (Profile.getCurrentProfile() != null) {
                        Query q = FirebaseDatabase.getInstance().getReference("notifications").child(Profile.getCurrentProfile().getId());
                        NotificationAdapter notificationAdapter = new NotificationAdapter(q, notifications.class, MainActivity.this);

                        recyclerView.setAdapter(notificationAdapter);


                        if (notificationAdapter.getItems().size() == 0) {
                            notfound.setVisibility(View.VISIBLE);
                        } else {
                            notfound.setVisibility(View.GONE);
                        }
                    } else {
                        notfound.setVisibility(View.VISIBLE);
                    }


                    ImageView imageView = (ImageView) findViewById(R.id.sweep);
                    if (imageView != null && Profile.getCurrentProfile() != null) {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Delete")
                                        .setMessage("Are you sure to delete all notifications")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                DatabaseReference del = FirebaseDatabase.getInstance().getReference("notifications").child(Profile.getCurrentProfile().getId());
                                                del.removeValue();


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
                        });
                    }

                }

                createFabFrameAnim();
                createFabReverseFrameAnim();
                init();
                Header = navigationView.getHeaderView(0);

                final TextView textView = (TextView) Header.findViewById(R.id.name);
                final TextView textView1 = (TextView) Header.findViewById(R.id.sub);

                final TextView textView2 = (TextView) Header.findViewById(R.id.score);
                circleImageView = (CircleImageView) Header.findViewById(R.id.CimageView);

                if (Profile.getCurrentProfile() != null) {


                    Header.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this, User.class);
                            i.putExtra("id", Profile.getCurrentProfile().getId());
                            startActivity(i);
                        }
                    });

                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    userref = database.getReference("myUsers").child(Profile.getCurrentProfile().getId());
                    userref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            progress.dismiss();

                            user current = dataSnapshot.getValue(user.class);

                            if (current != null && current.check()) {
                                if (current.score != null)
                                    textView2.setText("Score " + current.score * -1);


                                if (current.following != null && current.followers != null && current.score != null) {

                                    textView1.setText(current.followers.size() + " followers | " + current.following.size() + " following");

                                } else if (current.followers == null && current.following == null) {
                                    textView1.setText(0 + " followers | " + 0 + " following");

                                } else if (current.followers == null) {
                                    textView1.setText(0 + " followers | " + current.following.size() + " following");

                                } else if (current.following == null) {
                                    textView1.setText(current.followers.size() + " followers | " + 0 + " following");

                                }

                                String urlImage = "https://graph.facebook.com/" + current.id + "/picture?type=large";
                                textView.setText(current.name);
                                Activity a = MainActivity.this;
                                Picasso.with(a)
                                        .load(urlImage).fit().into(circleImageView);


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {

                    textView.setText("Login");
                    Header.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, Login.class));
                        }
                    });

                }

            }
        });


    }


    @Override
    public void onClick(View v) {

        if (Profile.getCurrentProfile() != null) {
            if (v instanceof MenuItemView && MyData.haveNetworkConnection()) {
                MenuItemView menuItemView = (MenuItemView) v;
                // Toast.makeText(this, menuItemView.getLabelTextView().getText(), Toast.LENGTH_SHORT).show();
                String s = menuItemView.getLabelTextView().getText().toString();
                if (s.equals("Tag")) {

                    startActivity(new Intent(MainActivity.this, AddNewTag.class));
                } else if (s.equals("Question")) {

                    startActivity(new Intent(MainActivity.this, AddQuestion.class));

                } else if (s.equals("Text")) {

                    startActivity(new Intent(MainActivity.this, AddArticleMaterial.class));

                } else if (s.equals("Page")) {

                    startActivity(new Intent(MainActivity.this, AddPage.class));
                } else if (s.equals("Chat")) {
                    startActivity(new Intent(MainActivity.this, NewChatActivity.class));

                } else if (s.equals("Photo")) {
                    MyData.verifyStoragePermissions(MainActivity.this);

                    Intent i = new Intent(MainActivity.this, AddQuestion.class);
                    i.putExtra("id", "id");
                    startActivity(i);

                }

            } else
                Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

        } else {
            MainActivity.this.startActivity(new Intent(MainActivity.this, Login.class));

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
                            Toast.makeText(MainActivity.this, " Filters Applied " + filterTags.toString(), Toast.LENGTH_SHORT).show();
                            MostLiked.setFilterTags(new ArrayList<String>(filterTags));
                            New.setFilterTags(new ArrayList<String>(filterTags));
                            Trending.setFilterTags(new ArrayList<String>(filterTags));
                            if (menu != null) {
                                ActionItemBadge.update(MainActivity.this, menu.findItem(R.id.filter), getResources().getDrawable(R.drawable.ic_filter_outline_white_24dp), ActionItemBadge.BadgeStyles.RED, filterTags.size());

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
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
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

    private void getData(String text, int position) {
        mHistoryDatabase.addItem(new SearchItem(text));
        Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
        intent.putExtra("text", text);
        startActivity(intent);
    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setTextSize(16);
            mSearchView.setHint("Search Everywhere");
            mSearchView.setDivider(false);
            mSearchView.setVoice(false);
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {

                }

                @Override
                public void onClose() {

                }
            });

            List<SearchItem> suggestionsList = new ArrayList<>();
         /*   suggestionsList.add(new SearchItem("search1"));
            suggestionsList.add(new SearchItem("search2"));
            suggestionsList.add(new SearchItem("search3"));*/

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notifications:

                DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (dl != null)
                    dl.openDrawer(Gravity.RIGHT);

                //  startActivity(new Intent(MainActivity.this, MaterialNotifications.class));
                return true;
            case R.id.filter:
                if (MyData.haveNetworkConnection())
                    filter();
                else
                    Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.action_search:
                if (mSearchView != null)
                    mSearchView.open(true);
                return true;

            case android.R.id.home:
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
                mDrawerLayout.openDrawer(GravityCompat.START); // finish()
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        try {


            ActionItemBadge.update(MainActivity.this, menu.findItem(R.id.filter), getResources().getDrawable(R.drawable.ic_filter_outline_white_24dp), ActionItemBadge.BadgeStyles.RED, filterTags.size());
            //fab.close(false);

            if (springFloatingActionMenu.isMenuOpen()) {
                springFloatingActionMenu.hideMenu();
            }
        } catch (Exception e) {
            return;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.myaccount) {
            if (Profile.getCurrentProfile() != null) {
                Intent intent = new Intent(this, User.class);
                intent.putExtra("id", Profile.getCurrentProfile().getId());
                startActivity(intent);
            } else {

                if (MainActivity.springFloatingActionMenu != null) {
                    MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                }
                Snackbar.make(MainActivity.rootLayout, "Please Login", Snackbar.LENGTH_LONG)
                        .setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, Login.class));
                            }
                        }).show();
            }

        } else if (id == R.id.share) {


            String text = "Check out new app Questo : \n " + "https://play.google.com/store/apps/details?id=com.tdevelopers.questo";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            BottomSheet sheet = BottomSheetHelper.shareAction(this, shareIntent).title("Share App").build();
            sheet.show();


        } else if (id == R.id.invite) {

            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage("Questo App Invite")
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();


            startActivityForResult(intent, REQUEST_INVITE);
        } else if (id == R.id.tagsmain) {
            startActivity(new Intent(this, TagActivity.class));
        } else if (id == R.id.Explore) {
            startActivity(new Intent(this, Explore_Activity.class));
        } else if (id == R.id.Articles) {
            startActivity(new Intent(MainActivity.this, ArticleActivity.class));

        } else if (id == R.id.aboutus) {
            startActivity(new Intent(MainActivity.this, About_me.class));

        } else if (id == R.id.page) {
            startActivity(new Intent(MainActivity.this, PageActivity.class));

        } else if (id == R.id.favourites) {
            if (Profile.getCurrentProfile() != null)
                startActivity(new Intent(MainActivity.this, Favorite_Activity.class));
            else {
                if (MainActivity.springFloatingActionMenu != null) {
                    MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                }
                Snackbar.make(MainActivity.rootLayout, "Please Login", Snackbar.LENGTH_LONG)
                        .setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, Login.class));
                            }
                        }).show();
            }

        } else if (id == R.id.Chat) {
            if (Profile.getCurrentProfile() != null) {
                startActivity(new Intent(MainActivity.this, ChatMain.class));
            } else {

                if (MainActivity.springFloatingActionMenu != null) {
                    MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                }
                Snackbar.make(MainActivity.rootLayout, "Please Login", Snackbar.LENGTH_LONG)
                        .setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, Login.class));
                            }
                        }).show();
            }
        } else if (id == R.id.scorechart) {
            startActivity(new Intent(MainActivity.this, ScoreChart.class));
        } else if (id == R.id.myfriends) {
            Intent i = new Intent(MainActivity.this, MyFriends.class);
            startActivity(i);

        } else if (id == R.id.scoreboard) {

            Intent i = new Intent(MainActivity.this, Leadershipboard_activity.class);
            startActivity(i);

        } else if (id == R.id.how) {
            startActivity(new Intent(MainActivity.this, Introduction.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {

            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        if (springFloatingActionMenu.isMenuOpen()) {
            springFloatingActionMenu.hideMenu();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services Error: " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
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



