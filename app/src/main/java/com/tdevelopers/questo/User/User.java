package com.tdevelopers.questo.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Edit_My_Profile;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Opens.ChatOpenActivity;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.ScoreChart;
import com.tdevelopers.questo.User.FollowStuff.FollowStuffActivity;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class User extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    String id = new String();
    TextView username, uploadsinfo;

    TextView followersfollowing, levelname;
    DatabaseReference user;
    MaterialDialog progress;
    com.tdevelopers.questo.Objects.user current;
    TextView title;
    CircleImageView circleImageView, leveldp;
    Button followbutton;
    ImageView coverpic;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    public static CoordinatorLayout rootlayout;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyData.pageflag = 0;
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        id = getIntent().getExtras().getString("id");
        if (id != null && !id.equals(""))
            user = FirebaseDatabase.getInstance().getReference("myUsers").child(id);
        title = (TextView) findViewById(R.id.titlex);
        circleImageView = (CircleImageView) findViewById(R.id.userdp);
        uploadsinfo = (TextView) findViewById(R.id.uploadsinfo);
        levelname = (TextView) findViewById(R.id.levelname);
        leveldp = (CircleImageView) findViewById(R.id.leveldp);
        followbutton = (Button) findViewById(R.id.followbutton);
        coverpic = (ImageView) findViewById(R.id.main_imageview_placeholder);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        ImageView newm = (ImageView) findViewById(R.id.newm);
        if (newm != null) {
            newm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Profile.getCurrentProfile() != null) {
                        Intent i = new Intent(User.this, ChatOpenActivity.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    } else {

                        Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        User.this.startActivity(new Intent(User.this, Login.class));

                                    }
                                }).show();
                    }
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS fl
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        String urlImage = "https://graph.facebook.com/" + id + "/picture?type=large";
        if (id != null && id.trim().length() != 0 && Profile.getCurrentProfile() != null && id.equals(Profile.getCurrentProfile().getId()) && AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            Log.v("expiry", AccessToken.getCurrentAccessToken().isExpired() + "");
            Bundle bundle = new Bundle();
            bundle.putString("fields", "cover");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "me/", bundle, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    try {// in case you want to get inner data
                        Log.v("apicall", response.toString());

                        JSONObject json = response.getJSONObject();
                        JSONObject json_cover = json.getJSONObject("cover");
                        String source = (String) json_cover.get("source");
                        Picasso.with(User.this).load(source).into(coverpic);

                    } catch (Exception e) {
                        e.printStackTrace();
                        coverpic.setImageDrawable(User.this.getResources().getDrawable(R.drawable.b));
                    }
                }
            }).executeAsync();
        }
        //  request.executeAsync();


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabl);
        if (!urlImage.isEmpty()) {

            Picasso.with(this)
                    .load(urlImage)
                    .fit().centerCrop()
                    .transform(PaletteTransformation.instance())
                    .into(circleImageView, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            Palette palette = null;
                            Bitmap bitmap = ((BitmapDrawable) circleImageView.getDrawable()).getBitmap(); // Ew!
                            // circleImageView = (CircleImageView) findViewById(R.id.userdp);
                            FrameLayout back = (FrameLayout) findViewById(R.id.fram);

                            // circleImageView.setImageBitmap(bitmap);
                            if (bitmap != null)
                                palette = PaletteTransformation.getPalette(bitmap);
                            if (palette != null && back != null && tabLayout != null) {
                                //Toast.makeText(User.this, "Reached color changed", Toast.LENGTH_SHORT).show();

                                int primary = palette.getVibrantColor(palette.getVibrantColor(getResources().getColor(R.color.md_material_blue_800)));
                                int primaryDark = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));

                                //   if (primary != getResources().getColor(R.color.color_material_bold) && primaryDark != getResources().getColor(R.color.color_dark_material_bold)) {
                                {
                                    back.setBackgroundColor(primary);
                                    tabLayout.setBackgroundColor(primary);
                                    mToolbar.setBackgroundColor(primary);
// finally change the color
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Window window = User.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        window.setStatusBarColor(primaryDark);
                                    }
                                }

                            }

                        }
                    });

        }


        progress = new MaterialDialog.Builder(this)
                .title("Loading").theme(Theme.LIGHT)
                .content("Please Wait")
                .progress(true, 0)
                .show();
        final ViewPager mpager = (ViewPager) findViewById(R.id.mpager);

        if (id != null && id.trim().length() != 0) {
            if (mpager != null && tabLayout != null) {
                mpager.setOffscreenPageLimit(2);

                mpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

                    @Override
                    public Fragment getItem(int position) {

                        switch (position % 3) {

                            case 0:
                                return UserDetails.newInstance(id);
                            case 1:
                                return User_Analysis.newInstance(id);

                            case 2:
                                return User_Tags_Following.newInstance(id);

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
                                return "Details";

                            case 1:
                                return "Interests";
                            case 2:
                                return "Tags Following";
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
                                NestedScrollView ns;
                                int numTab = tab.getPosition();

                                switch (numTab) {
                                    case 0:
                                        ns = (NestedScrollView) findViewById(R.id.ns0);
                                        if (ns != null) {
                                            ns.smoothScrollTo(0, 0);
                                        }
                                        break;

                                    case 2:

                                        ns = (NestedScrollView) findViewById(R.id.ns2);
                                        if (ns != null) {
                                            ns.smoothScrollTo(0, 0);
                                        }
                                        break;


                                }
                            }

                        });

            }
        }
        followersfollowing = (TextView) findViewById(R.id.followersfollowing);
        username = (TextView) findViewById(R.id.username);
        //   final ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.userdp);
        if (user != null)
            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        current = dataSnapshot.getValue(com.tdevelopers.questo.Objects.user.class);
                        if (current != null) {

                            UserDetails.setCurrent(current);
                            User_Tags_Following.setCurrent(current);
                            leveldp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(User.this, ScoreChart.class));

                                }
                            });
                            levelname.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    startActivity(new Intent(User.this, ScoreChart.class));
                                }
                            });
                            if (current.score < 0)
                                current.score *= -1;
                            if (current.score < 0) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                                levelname.setText("Negative");
                            } else if (current.score >= 0 && current.score < 100) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.noobie));
                                levelname.setText("Noobie");
                            } else if (current.score >= 100 && current.score < 500) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.geek));
                                levelname.setText("Geek");
                            } else if (current.score >= 500 && current.score < 1000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.ninja));
                                levelname.setText("Ninja");
                            } else if (current.score >= 1000 && current.score < 2000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.captain));
                                levelname.setText("Captain");
                            } else if (current.score >= 2000 && current.score < 3000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.batman));
                                levelname.setText("Bat Man");
                            } else if (current.score >= 3000 && current.score < 4000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.ironman));
                                levelname.setText("Iron Man");
                            } else if (current.score >= 4000 && current.score < 5000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.joker));
                                levelname.setText("Joker");
                            } else if (current.score >= 5000 && current.score < 7000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.sherlock));
                                levelname.setText("Sherlock");
                            } else if (current.score >= 7000 && current.score < 10000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.genious));
                                levelname.setText("Genious");
                            } else if (current.score >= 10000) {
                                leveldp.setImageDrawable(getResources().getDrawable(R.drawable.superman));
                                levelname.setText("Super Man");
                            }


                            if (current.followers == null)
                                current.followers = new HashMap<String, Boolean>();
                            if (current.following == null)
                                current.following = new HashMap<String, Boolean>();

                            followersfollowing.setText(current.followers.size() + " followers | " + current.following.size() + " following");

                            followersfollowing.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(User.this, FollowStuffActivity.class);
                                    intent.putExtra("id", id + "");
                                    intent.putExtra("name", current.name + "");
                                    startActivity(intent);
                                }
                            });
                            //  profilePictureView.setProfileId(current.id);
                            title.setText(current.name);
                            username.setText(current.name);
                            if (current.article_count == null)
                                current.article_count = 0L;
                            if (current.question_count == null)
                                current.question_count = 0L;
//                holder.subtext.setText(t.followers.size() + " followers | " + t.following.size() + " following");

                            if (current.score == null)
                                current.score = 0L;
                            uploadsinfo.setText(current.score + " Points");


                            if (Profile.getCurrentProfile() != null && current.id.equals(Profile.getCurrentProfile().getId())) {
                                followbutton.setText("Edit");
                                followbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        v.getContext().startActivity(new Intent(v.getContext(), Edit_My_Profile.class));
                                    }
                                });
                            }

                            if (Profile.getCurrentProfile() != null && !current.id.equals(Profile.getCurrentProfile().getId())) {


                                if (current.followers != null && Profile.getCurrentProfile() != null && current.followers.containsKey(Profile.getCurrentProfile().getId()))
                                    followbutton.setText("Following");

                                else
                                    followbutton.setText("Follow");


                                followbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (Profile.getCurrentProfile() != null) {
                                            if (MyData.haveNetworkConnection()) {
                                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("myUsers").child(current.id).child("followers");
                                                postRef.runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        //    Post p = mutableData.getValue(Post.class);


                                                        HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                                        if (p == null) {
                                                            p = new HashMap<String, Boolean>();
                                                            Log.v("followtrack", "null");
                                                            p.put(Profile.getCurrentProfile().getId(), true);
                                                            mutableData.setValue(p);
                                                            return Transaction.success(mutableData);
                                                        }

                                                        if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                            // Unstar the post and remove self from stars
                                                            //p.starCount = p.starCount - 1;
                                                            p.remove(Profile.getCurrentProfile().getId());
                                                            Log.v("followtrack", "removed");
                                                        } else {
                                                            // Star the post and add self to stars
                                                            //p.starCount = p.starCount + 1;
                                                            p.put(Profile.getCurrentProfile().getId(), true);
                                                            Log.v("followtrack", "added");
                                                        }

                                                        // Set value and report transaction success
                                                        mutableData.setValue(p);
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                                           DataSnapshot dataSnapshot) {
                                                        // Transaction completed
                                                        Log.d("followtrack", "postTransaction:onComplete:" + databaseError);

                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId() + "").child("following");
                                                        //DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(t.name).child("followers");
                                                        HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                                        if (p != null) {
                                                            if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                                followbutton.setText("Following");
                                                                ref.child(current.id).setValue(true);

                                                                String link = "myUsers:" + Profile.getCurrentProfile().getId();
                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "started following you", current.gcmid, link, current.id);

                                                            } else {
                                                                followbutton.setText("Follow");
                                                                ref.child(current.id).setValue(null);
                                                            }


                                                            //   tagref.setValue(p.size());
                                                        }
                                                        if (p == null) {
                                                            followbutton.setText("Follow");
                                                            ref.child(current.id).setValue(null);
                                                            //  tagref.setValue(0);
                                                        }

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(User.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                            Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                                    .setAction("Login", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            User.this.startActivity(new Intent(User.this, Login.class));

                                                        }
                                                    }).show();
                                        }
                                    }


                                });
                            }



                            progress.dismiss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        bindActivity();
        mToolbar.setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);

        setSupportActionBar(mToolbar);


        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.titlex);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }


}
