package com.tdevelopers.questo.Opens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Adapters.LikerDialogAdapter;
import com.tdevelopers.questo.Adapters.LikersAdapter;
import com.tdevelopers.questo.Adapters.commentadapter;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.comment;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleOpenActivity extends AppCompatActivity {

    String id;
    Toolbar toolbar;
    TextView content;
    ImageView dp;
    FloatingActionButton share;
    BottomSheet sheet;
    FloatingActionButton commentadd;
    RecyclerView recyclerView;
    EditText nc;
    TextView date;
    TextView title, name, nameinfo;
    CircleImageView userdp;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Article a;
    LikeButton like;
    TextView likes_count;
    ImageView star, report;
    RecyclerView viewersrv;
    NestedScrollView nestedScrollView;
    AppBarLayout mAppBarLayout;
    SwitchCompat mute;
    Bitmap bitmap;
    CircleImageView cdp;
    ImageView link;
    CoordinatorLayout rootlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_open);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        link = (ImageView) findViewById(R.id.link);
        cdp = (CircleImageView) findViewById(R.id.dp);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
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
        mute = (SwitchCompat) findViewById(R.id.mute);
        nestedScrollView = (NestedScrollView) findViewById(R.id.ns);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
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
        like = (LikeButton) findViewById(R.id.like);
        likes_count = (TextView) findViewById(R.id.likes_count);
        recyclerView = (RecyclerView) findViewById(R.id.commentsrv);
        recyclerView.setNestedScrollingEnabled(false);
        viewersrv = (RecyclerView) findViewById(R.id.viewersrv);
        star = (ImageView) findViewById(R.id.starflag);
        userdp = (CircleImageView) findViewById(R.id.userdp);
        name = (TextView) findViewById(R.id.name);
        nameinfo = (TextView) findViewById(R.id.nameinfo);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        nc = (EditText) findViewById(R.id.newc);
        share = (FloatingActionButton) findViewById(R.id.sharearticle);
        id = getIntent().getExtras().getString("id");
        report = (ImageView) findViewById(R.id.report);
        viewersrv.setNestedScrollingEnabled(false);
        viewersrv.setLayoutManager(new LinearLayoutManager(ArticleOpenActivity.this, LinearLayoutManager.HORIZONTAL, false));


        if (Profile.getCurrentProfile() != null) {
            String urlImage = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";

            Picasso.with(this).load(urlImage).into(cdp);
        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitleEnabled(false);
        }

        if (id != null && id.trim().length() != 0) {
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), MyData.buildDeepLink("article", id).toString());
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(v, "Link copied", Snackbar.LENGTH_LONG)
                            .setAction("Share", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (a != null) {
                                        if (bitmap != null) {

                                            if (content.getText().toString().trim().length() != 0) {
                                                sheet = shareBitmap(bitmap, "myImage", a.title, content.getText().toString()).title("Sharing Article ").build();
                                                sheet.show();
                                            } else {

                                                sheet = shareBitmap(bitmap, "myImage", a.title, "").title("Sharing Article ").build();
                                                sheet.show();
                                            }
                                        } else {

                                            if (content.getText().toString().trim().length() != 0) {
                                                sheet = getShareActions(a.title, content.getText().toString()).title("Sharing Article ").build();
                                                sheet.show();
                                            } else {

                                                StringBuilder to = new StringBuilder("Explore Article\n\n#Questo\n\n");

                                                to.append("\n\nCheck out :\n").append(MyData.buildDeepLink("article", id));
                                                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("text/plain");
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, to.toString());
                                                BottomSheetHelper.shareAction(ArticleOpenActivity.this, shareIntent).build().show();

                                            }
                                        }
                                    } else {

                                        StringBuilder to;
                                        if (content.getText().toString().trim().length() != 0) {
                                            to = new StringBuilder("Explore Article\n\n#Questo\n\n" + content.getText().toString() + "\n\n");

                                            to.append("\n\nCheck out :\n").append(MyData.buildDeepLink("article", id));
                                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                            shareIntent.setType("text/plain");
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, to.toString());
                                            BottomSheetHelper.shareAction(ArticleOpenActivity.this, shareIntent).build().show();


                                        } else

                                        {

                                            to = new StringBuilder("Explore Article\n\n#Questo\n\n");

                                            to.append("\n\nCheck out :\n").append(MyData.buildDeepLink("article", id));
                                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                            shareIntent.setType("text/plain");
                                            shareIntent.putExtra(Intent.EXTRA_TEXT, to.toString());
                                            BottomSheetHelper.shareAction(ArticleOpenActivity.this, shareIntent).build().show();

                                        }
                                    }
                                }
                            }).show();

                }
            });
            Query query = FirebaseDatabase.getInstance().getReference("Article").child(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        a = dataSnapshot.getValue(Article.class);

                        if (a != null) {

                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Profile.getCurrentProfile() != null) {
                                        MaterialDialog dialog = new MaterialDialog.Builder(ArticleOpenActivity.this)
                                                .title("Report Article")
                                                .customView(R.layout.report_layout, true)
                                                .positiveText("Report")

                                                .negativeColor(Color.BLACK)
                                                .negativeText(android.R.string.cancel)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        //showToast("Password: " + passwordInput.getText().toString());


                                                        EditText reason = (EditText) dialog.getCustomView().findViewById(R.id.reason);
                                                        Toast.makeText(ArticleOpenActivity.this, "Article Reported", Toast.LENGTH_SHORT).show();

                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reports").child(Profile.getCurrentProfile().getId()).child("ArticleReports").child(a.id);
                                                        ref.child("reason").setValue(reason.getText().toString().trim());
                                                        ref.child("id").setValue(a.id);

                                                    }
                                                }).build();
                                        dialog.show();
                                    } else {

                                        Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                .setAction("Login", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ArticleOpenActivity.this.startActivity(new Intent(ArticleOpenActivity.this, Login.class));

                                                    }
                                                }).show();
                                    }
                                }
                            });
                            final DatabaseReference re = FirebaseDatabase.getInstance().getReference("Comments").child("article_comments").child(a.id);
                            if (Profile.getCurrentProfile() != null) {
                                DatabaseReference checkref = FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Articles");
                                checkref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                            data = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                        if (data.containsKey(a.id))
                                            star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                                        else

                                            star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            DatabaseReference likerref = FirebaseDatabase.getInstance().getReference("Likers").child("ArticleLikers").child(a.id);
                            likerref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                        data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                    }
                                    if (Profile.getCurrentProfile() != null && data.containsKey(Profile.getCurrentProfile().getId()))
                                        like.setLiked(true);

                                    else
                                        like.setLiked(false);
                                    likes_count.setText(data.size() + " likes");

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            FirebaseDatabase.getInstance().getReference("Article").child(a.id).child("likes_count").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                        likes_count.setText(dataSnapshot.getValue() + " likes");
                                    else
                                        likes_count.setText("0 likes");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            likes_count.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final MaterialDialog out = new MaterialDialog.Builder(ArticleOpenActivity.this)
                                            .title("Liked By").titleColor(Color.RED)
                                            .customView(R.layout.article_likers_dialog, true).build();
                                    final RecyclerView recyclerView = (RecyclerView) out.findViewById(R.id.likers);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(ArticleOpenActivity.this));
                                    recyclerView.setNestedScrollingEnabled(false);
                                    FirebaseDatabase.getInstance().getReference("Likers").child("ArticleLikers").child(a.id).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                            ArrayList<String> datalist = new ArrayList<String>();
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                data = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                            }
                                            datalist = new ArrayList<String>(data.keySet());
                                            recyclerView.setAdapter(new LikerDialogAdapter(datalist));
                                            out.show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            });
                            if (MyData.haveNetworkConnection())
                                if (id != null && id.trim().length() != 0)
                                    FirebaseDatabase.getInstance().getReference("Article_Viewers").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                            if (Profile.getCurrentProfile() != null && !data.containsKey(Profile.getCurrentProfile().getId())) {
                                                FirebaseDatabase.getInstance().getReference("Article_Viewers").child(id).child(Profile.getCurrentProfile().getId()).setValue(true);
                                                FirebaseDatabase.getInstance().getReference("myUsers").child(a.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String gcmid = new String();
                                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                            gcmid = (String) dataSnapshot.getValue();
                                                            if (gcmid != null && gcmid.trim().length() != 0) {
                                                                String link = "Article:" + a.id;
                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "viewed your Article\n\n" + a.title, gcmid + "", link, a.uploaded_by);

                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                FirebaseDatabase.getInstance().getReference("Article").child(id).child("views_count").runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        try {
                                                            Long p = mutableData.getValue(Long.class);
                                                            if (p == null) {
                                                                p = -1L;
                                                            } else p -= 1L;
                                                            // Set value and report transaction success
                                                            mutableData.setValue(p);
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                                           DataSnapshot dataSnapshot) {
                                                        // Transaction completed


                                                    }
                                                });

                                            }
                                            viewersrv.setVisibility(View.VISIBLE);
                                            viewersrv.setAdapter(new LikersAdapter(new ArrayList<String>(data.keySet())));
                                            nameinfo.setText(data.size() + " views");


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                            like.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    try {
                                        if (Profile.getCurrentProfile() != null) {
                                            if (MyData.haveNetworkConnection()) {
                                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Likers").child("ArticleLikers").child(a.id);
                                                postRef.runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        //    Post p = mutableData.getValue(Post.class);

                                                        try {
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
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                                           DataSnapshot dataSnapshot) {
                                                        // Transaction completed
                                                        Log.d("followtrack", "postTransaction:onComplete:" + databaseError);
                                                        try {
                                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("articlelikes");
                                                            DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Article").child(a.id).child("likes_count");
                                                            DatabaseReference nlikecount = FirebaseDatabase.getInstance().getReference("Article").child(a.id).child("nlikes_count");
                                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                                            if (p != null) {
                                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                                    like.setLiked(true);
                                                                    ref.child(a.id).setValue(true);

                                                                    FirebaseDatabase.getInstance().getReference("myUsers").child(a.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                String gcmid = (String) (dataSnapshot.getValue());


                                                                                String link = "Article:" + a.id;
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "liked your article\n\n" + a.title, gcmid + "", link, a.uploaded_by);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                } else {
                                                                    like.setLiked(false);
                                                                    ref.child(a.id).setValue(null);
                                                                }

                                                                likes_count.setText(p.size() + " likes");
                                                                likecount.setValue(p.size());
                                                                nlikecount.setValue(-1 * p.size());


                                                            }
                                                            if (p == null) {
                                                                like.setLiked(false);
                                                                ref.child(a.id).setValue(null);
                                                                likes_count.setText("0 likes");
                                                                likecount.setValue(0);
                                                                nlikecount.setValue(0);


                                                            }
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(ArticleOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {

                                            Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                    .setAction("Login", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ArticleOpenActivity.this.startActivity(new Intent(ArticleOpenActivity.this, Login.class));

                                                        }
                                                    }).show();
                                        }
                                    } catch (Exception e) {
                                        if (Profile.getCurrentProfile() != null)
                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                    }
                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    try {
                                        if (Profile.getCurrentProfile() != null) {
                                            if (MyData.haveNetworkConnection()) {
                                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Likers").child("ArticleLikers").child(a.id);
                                                postRef.runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        //    Post p = mutableData.getValue(Post.class);
                                                        try {

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
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                                           DataSnapshot dataSnapshot) {
                                                        // Transaction completed
                                                        Log.d("followtrack", "postTransaction:onComplete:" + databaseError);
                                                        try {
                                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("articlelikes");
                                                            DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Article").child(a.id).child("likes_count");
                                                            DatabaseReference nlikecount = FirebaseDatabase.getInstance().getReference("Article").child(a.id).child("nlikes_count");
                                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                                            if (p != null) {
                                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                                    like.setLiked(true);
                                                                    ref.child(a.id).setValue(true);

                                                                    FirebaseDatabase.getInstance().getReference("myUsers").child(a.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                String gcmid = (String) (dataSnapshot.getValue());


                                                                                String link = "Article:" + a.id;
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "liked your article\n\n" + a.title, gcmid + "", link, a.uploaded_by);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                } else {
                                                                    like.setLiked(false);
                                                                    ref.child(a.id).setValue(null);
                                                                }

                                                                likes_count.setText(p.size() + " likes");
                                                                likecount.setValue(p.size());
                                                                nlikecount.setValue(-1 * p.size());


                                                            }
                                                            if (p == null) {
                                                                like.setLiked(false);
                                                                ref.child(a.id).setValue(null);
                                                                likes_count.setText("0 likes");
                                                                likecount.setValue(0);
                                                                nlikecount.setValue(0);


                                                            }
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(ArticleOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {

                                            Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                    .setAction("Login", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ArticleOpenActivity.this.startActivity(new Intent(ArticleOpenActivity.this, Login.class));

                                                        }
                                                    }).show();
                                        }
                                    } catch (Exception e) {
                                        if (Profile.getCurrentProfile() != null)
                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                    }

                                }
                            });
                            star.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        if (Profile.getCurrentProfile() != null) {
                                            if (MyData.haveNetworkConnection()) {
                                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Articles");

                                                postRef.runTransaction(new Transaction.Handler() {
                                                    @Override
                                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                                        //    Post p = mutableData.getValue(Post.class);

                                                        try {
                                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                                            if (p == null) {
                                                                p = new HashMap<String, Boolean>();
                                                                Log.v("followtrack", "null");
                                                                p.put(a.id, true);
                                                                mutableData.setValue(p);
                                                                return Transaction.success(mutableData);
                                                            }

                                                            if (p.containsKey(a.id)) {
                                                                // Unstar the post and remove self from stars
                                                                //p.starCount = p.starCount - 1;
                                                                p.remove(a.id);
                                                                Log.v("followtrack", "removed");
                                                            } else {
                                                                // Star the post and add self to stars
                                                                //p.starCount = p.starCount + 1;
                                                                p.put(a.id, true);
                                                                Log.v("followtrack", "added");
                                                            }

                                                            // Set value and report transaction success
                                                            mutableData.setValue(p);
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                        return Transaction.success(mutableData);
                                                    }

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                                           DataSnapshot dataSnapshot) {
                                                        try {
                                                            HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                                            if (databaseError == null && dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                                            }

                                                            if (data.containsKey(a.id)) {
                                                                Toast.makeText(ArticleOpenActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                                                                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_white_24dp));

                                                            } else {
                                                                Toast.makeText(ArticleOpenActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                                                                star.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                                                            }
                                                        } catch (Exception e) {
                                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                        }
                                                    }

                                                });
                                            } else {
                                                Toast.makeText(ArticleOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                            Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                    .setAction("Login", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            ArticleOpenActivity.this.startActivity(new Intent(ArticleOpenActivity.this, Login.class));

                                                        }
                                                    }).show();
                                        }
                                    } catch (Exception e) {
                                        if (Profile.getCurrentProfile() != null)
                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());
                                    }
                                }
                            });


                            share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sheet = getShareActions(a.title, content.getText().toString()).title("Sharing Article ").build();
                                    sheet.show();
                                }
                            });
                            title.setText(a.title + "");
                            if (a.date != null)
                                date.setText("Originally posted on " + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(a.date));
                            dp = (ImageView) findViewById(R.id.adp);

                            Typeface font = Typeface.createFromAsset(getAssets(), "proxima.ttf");
                            content.setTypeface(font);
                            //  content.setText(a.description + "");

                            name.setText(a.username + "");
                            if (a.tags_here != null) {
                                TagContainerLayout tagContainerLayout = (TagContainerLayout) findViewById(R.id.tag_container);
                                tagContainerLayout.setVisibility(View.VISIBLE);
                                tagContainerLayout.setTags(new ArrayList<String>(a.tags_here.keySet()));
                                tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                                    @Override
                                    public void onTagClick(final int position, final String text) {
                                        // ...
                                        //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(ArticleOpenActivity.this, TagOpenActivity.class);
                                        i.putExtra("id", text);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onTagLongClick(final int position, String text) {
                                        // ...

                                        Intent i = new Intent(ArticleOpenActivity.this, TagOpenActivity.class);
                                        i.putExtra("id", text);
                                        startActivity(i);
                                    }
                                });
                            }
                            String urlImage = "https://graph.facebook.com/" + a.uploaded_by + "/picture?type=large";

                            Picasso.with(ArticleOpenActivity.this)
                                    .load(urlImage)
                                    .fit()
                                    .transform(PaletteTransformation.instance())
                                    .into(userdp, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {


                                        }
                                    });
                            userdp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(v.getContext(), User.class);
                                    intent.putExtra("id", a.uploaded_by);
                                    v.getContext().startActivity(intent);

                                }
                            });
                            name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(v.getContext(), User.class);
                                    intent.putExtra("id", a.uploaded_by);
                                    v.getContext().startActivity(intent);

                                }
                            });
                            if (a.image != null && !a.image.equals("")) {
                                Picasso.with(ArticleOpenActivity.this)
                                        .load(a.image)
                                        .fit().centerCrop()
                                        .transform(PaletteTransformation.instance())
                                        .into(dp, new Callback.EmptyCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Palette palette = null;
                                                bitmap = ((BitmapDrawable) dp.getDrawable()).getBitmap(); // Ew!
                                                // circleImageView = (CircleImageView) findViewById(R.id.userdp);
                                                // circleImageView.setImageBitmap(bitmap);
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

                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        Window window = ArticleOpenActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                        window.setStatusBarColor(palette.getDarkVibrantColor(ArticleOpenActivity.this.getResources().getColor(R.color.colorPrimaryDark)));
                                                    }
                                                    collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(ArticleOpenActivity.this.getResources().getColor(R.color.color_dark_material_motion)));
                                                    // dp.setBackground(ArticleOpenActivity.this.getResources().getDrawable(R.drawable.gradient));
                                                }
                                                if (bitmap != null) {

                                                    share.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //          Toast.makeText(ArticleOpenActivity.this, "Share ", Toast.LENGTH_SHORT).show();
                                                            sheet = shareBitmap(bitmap, "myImage", a.title, content.getText().toString()).title("Sharing Article ").build();
                                                            sheet.show();
                                                        }
                                                    });
                                                }
                                                if (bitmap != null) {

                                                    share.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //      Toast.makeText(ArticleOpenActivity.this, "Share ", Toast.LENGTH_SHORT).show();
                                                            sheet = shareBitmap(bitmap, "myImage", a.title, content.getText().toString()).title("Sharing Article ").build();
                                                            sheet.show();
                                                        }
                                                    });
                                                }


                                            }
                                        });

                            }
                            recyclerView = (RecyclerView) findViewById(R.id.commentsrv);
                            if (recyclerView != null) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(ArticleOpenActivity.this));
                                recyclerView.setNestedScrollingEnabled(false);

                            }
                            commentadd = (FloatingActionButton) findViewById(R.id.commentadd);
                            nc = (AppCompatEditText) findViewById(R.id.newc);
                            if (id != null && id.trim().length() != 0) {

                                final DatabaseReference muted = FirebaseDatabase.getInstance().getReference("Muted").child("Article").child(id);

                                muted.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap<String, Boolean> data = new HashMap<String, Boolean>();

                                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                            data = (HashMap<String, Boolean>) dataSnapshot.getValue();


                                        if (data != null && Profile.getCurrentProfile() != null && data.containsKey(Profile.getCurrentProfile().getId())) {
                                            mute.setChecked(true);
                                        } else
                                            mute.setChecked(false);


                                        final HashMap<String, Boolean> finalData = data;
                                        mute.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (finalData != null && Profile.getCurrentProfile() != null && finalData.containsKey(Profile.getCurrentProfile().getId())) {
                                                    // remove the user
                                                    muted.child(Profile.getCurrentProfile().getId()).removeValue();
                                                } else if (Profile.getCurrentProfile() != null) {
                                                    // mute.setChecked(true);
                                                    muted.child(Profile.getCurrentProfile().getId()).setValue(true);
                                                }
                                            }
                                        });


                                        final HashMap<String, Boolean> finalData1 = data;
                                        commentadd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (Profile.getCurrentProfile() != null) {
                                                    if (MyData.haveNetworkConnection()) {
                                                        if (nc != null)
                                                            nc.addTextChangedListener(new TextWatcher() {
                                                                @Override
                                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                }

                                                                @Override
                                                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                                    if (nc.getText().toString().length() > 0) {
                                                                        nc.setError(null);
                                                                    }
                                                                }

                                                                @Override
                                                                public void afterTextChanged(Editable s) {

                                                                }
                                                            });
                                                        if (nc != null && nc.getText().toString().trim().length() == 0) {
                                                            nc.setError(getString(R.string.error_field_required));
                                                        } else if (nc != null && nc.getText().toString().trim().length() != 0) {

                                                            // nc.setError(null);
                                                            final String content = nc.getText().toString();
                                                            DatabaseReference ref = re.push();
                                                            ref.child("content").setValue(content);
                                                            ref.child("userid").setValue(Profile.getCurrentProfile().getId());
                                                            ref.child("username").setValue(Profile.getCurrentProfile().getName());
                                                            ref.child("path").setValue(ref.toString());
                                                            Date d = new Date();
                                                            ref.child("date").setValue(d.getTime());
                                                            ref.setPriority(d.getTime() * -1);
                                                            Toast.makeText(ArticleOpenActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                                            nc.setText("");
                                                            View view = ArticleOpenActivity.this.getCurrentFocus();
                                                            if (view != null) {
                                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                            }

                                                            DatabaseReference root = ref.getParent();
                                                            root.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    // ArrayList<comment>data=new ArrayList<comment>();
                                                                    final HashSet<String> u = new HashSet<String>();
                                                                    if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                                                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                                            final comment c = d.getValue(comment.class);
                                                                            if (!(finalData1 != null && finalData1.containsKey(c.userid)))
                                                                                if (c != null && c.userid != null && c.userid.trim().length() != 0 && !c.userid.equals(Profile.getCurrentProfile().getId()) && !c.userid.equals(a.uploaded_by)) {
                                                                                    if (!u.contains(c.userid)) {
                                                                                        u.add(c.userid);
                                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child(c.userid).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                String gcmid = new String();
                                                                                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                                                                    gcmid = (String) dataSnapshot.getValue();

                                                                                                if (gcmid.trim().length() != 0) {
                                                                                                    String link = "Article:" + a.id;
                                                                                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "also commented on " + a.username + " article\n\n" + content, gcmid + "", link, c.userid);

                                                                                                    Log.v("pushers", c.userid);
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                            // Toast.makeText(QuestionOpenActivity.this, reference.toString(), Toast.LENGTH_SHORT).show();
                                                            if (!(finalData1 != null && finalData1.containsKey(a.uploaded_by)))
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child(a.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                            String gcmid = (String) (dataSnapshot.getValue());
                                                                            String link = "Article:" + a.id;
                                                                            MyData.pushNotification(Profile.getCurrentProfile().getName(), "commented on your article\n\n" + content, gcmid + "", link, a.uploaded_by);

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                        }

                                                    } else {
                                                        Toast.makeText(ArticleOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {

                                                    Snackbar.make(rootlayout, "Please login to continue", Snackbar.LENGTH_LONG)
                                                            .setAction("Login", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    ArticleOpenActivity.this.startActivity(new Intent(ArticleOpenActivity.this, Login.class));

                                                                }
                                                            }).show();
                                                }

                                            }

                                        });


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                Query q = re;
                                if (recyclerView != null) {

                                    recyclerView.setAdapter(new commentadapter(q, comment.class));
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("descriptions").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            String desc = (String) dataSnapshot.getValue();

                            if (desc != null && desc.trim().length() != 0) {

                                Typeface font = Typeface.createFromAsset(getAssets(), "proxima.ttf");
                                content.setTypeface(font);
                                content.setText(desc);

                            }
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private BottomSheet.Builder getShareActions(String title, String text) {
        Intent shareIntent = null;
        try {
            StringBuilder to = new StringBuilder(title + "\n\n");
            if (a.tags_here != null && a.tags_here.size() != 0) {
                for (String s : a.tags_here.keySet())
                    to.append("#").append(s).append(" ");
            }
            if (text.length() > 500)
                to.append("\n\n").append(text.substring(0, 500));
            else
                to.append("\n\n").append(text);


            to.append("\n\nread more at @Questo");
            to.append("\n\nCheck out :\n" + MyData.buildDeepLink("article", a.id));
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, to.toString());
        } catch (Exception e) {

        }
        return BottomSheetHelper.shareAction(this, shareIntent);
    }

    private BottomSheet.Builder shareBitmap(Bitmap bitmap, String fileName, String title, String text) {
        Intent intent = null;
        try {

            StringBuilder to = new StringBuilder(title + "\n\n");
            if (a.tags_here != null && a.tags_here.size() != 0) {
                for (String s : a.tags_here.keySet())
                    to.append("#").append(s).append(" ");
            }
            if (text.length() > 500)
                to.append("\n\n").append(text.substring(0, 500));
            else

                to.append("\n\n").append(text);

            to.append("\n\nread more at @Questo");
            to.append("\n\nCheck out :\n" + MyData.buildDeepLink("article", a.id));
            File file = new File(this.getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_TEXT, to.toString());
            intent.setType("image/png");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BottomSheetHelper.shareAction(this, intent);

    }


}
