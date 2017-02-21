package com.tdevelopers.questo.Opens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Target;
import com.tdevelopers.questo.Adapters.LikersAdapter;
import com.tdevelopers.questo.Adapters.RelatedQuestionAdapter;
import com.tdevelopers.questo.Adapters.commentadapter;
import com.tdevelopers.questo.EditStuff.EditQuestion;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.Objects.comment;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.Reactions.Reactions_Activity;
import com.tdevelopers.questo.User.User;
import com.tdevelopers.questo.libraries.BottomSheetFragment;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class QuestionOpenActivity extends AppCompatActivity implements View.OnClickListener {
    String id = "";
    Question currentObject;
    RecyclerView likersrv;
    TextView r[] = new TextView[4];
    TextView question;
    TextView likescount;
    TextView viewscount;
    TextView commentsCount;
    RecyclerView comments;
    RecyclerView relatedquestions;
    FloatingActionButton commentadd;
    TagContainerLayout tags;
    EditText nc;
    TextView date;
    TextView username;
    CircleImageView userdp;
    LikeButton like;
    LinearLayout ll1, ll2, ll3;
    BottomSheet sheet;
    Context context;
    View itemView;
    DrawerLayout dl;
    WebView webView;
    NavigationView navigationView;
    ImageView close;
    TextView home;
    TextView exp;
    TextView likerhead;
    SwitchCompat mute;
    ImageView pic;
    MenuItem edit;
    LinearLayout llchoice;
    RelativeLayout rl;
    Bitmap bitmap;
    ImageView share, link;

    public void openD(String url) {

        if (currentObject != null && webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);

            StringTokenizer breaker = new StringTokenizer(currentObject.question, " ");
            while (breaker.hasMoreTokens())
                url += breaker.nextToken() + "+";
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            webView.loadUrl(url);

        }

    }

    public void init() {
        if (id != null && id.trim().length() != 0 && currentObject != null) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            //);
            exp = (TextView) findViewById(R.id.exp);
            webView = (WebView) findViewById(R.id.webview);
            home = (TextView) findViewById(R.id.title);
            close = (ImageView) findViewById(R.id.close);
            pic = (ImageView) findViewById(R.id.pic);
            likerhead = (TextView) findViewById(R.id.likerhead);
            mute = (SwitchCompat) findViewById(R.id.mute);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dl != null) {
                        dl.closeDrawer(Gravity.RIGHT);
                    }
                }
            });
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            likersrv = (RecyclerView) findViewById(R.id.likersrv);
            ll1 = (LinearLayout) findViewById(R.id.ll1);
            ll2 = (LinearLayout) findViewById(R.id.ll2);
            ll3 = (LinearLayout) findViewById(R.id.ll3);
            commentadd = (FloatingActionButton) findViewById(R.id.commentadd);
            commentadd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            r[0] = (TextView) findViewById(R.id.ch1);
            r[1] = (TextView) findViewById(R.id.ch2);
            r[2] = (TextView) findViewById(R.id.ch3);
            relatedquestions = (RecyclerView) findViewById(R.id.relatedrv);
            like = (LikeButton) findViewById(R.id.like);
            username = (TextView) findViewById(R.id.username);
            userdp = (CircleImageView) findViewById(R.id.userdp);
            r[3] = (TextView) findViewById(R.id.ch4);
            tags = (TagContainerLayout) findViewById(R.id.tag_container);
            question = (TextView) findViewById(R.id.questionopen);
            likescount = (TextView) findViewById(R.id.likesCount);
            commentsCount = (TextView) findViewById(R.id.commentsCount);
            viewscount = (TextView) findViewById(R.id.viewsCount);
            comments = (RecyclerView) findViewById(R.id.commentsrv);
            comments.setNestedScrollingEnabled(false);
            date = (TextView) findViewById(R.id.date);
            nc = (EditText) findViewById(R.id.newc);

            FirebaseDatabase.getInstance().getReference("Question").child(id).child("viewcount").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    viewscount.setText((long) dataSnapshot.getValue() * -1 + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("Question").child(id).child("likes_count").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    likescount.setText((long) dataSnapshot.getValue() * -1 + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("Question").child(id).child("comment_count").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    commentsCount.setText((long) dataSnapshot.getValue() + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final DatabaseReference re = FirebaseDatabase.getInstance().getReference("Comments").child("question_comments").child(currentObject.id);
            Query q = re;
            if (comments != null) {

                comments.setNestedScrollingEnabled(false);
                comments.setLayoutManager(new LinearLayoutManager(comments.getContext()));
                comments.setAdapter(new commentadapter(q, comment.class));
            }
            putData();
            if (id != null && id.trim().length() != 0) {


                final DatabaseReference muted = FirebaseDatabase.getInstance().getReference("Muted").child("Questions").child(id);

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
                                            nc.setText("");
                                            View view = QuestionOpenActivity.this.getCurrentFocus();
                                            if (view != null) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            }
                                            Toast.makeText(QuestionOpenActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference("Question").child(currentObject.id + "").child("comment_count").runTransaction(new Transaction.Handler() {
                                                @Override
                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                    Long p = mutableData.getValue(Long.class);
                                                    if (p == null) {
                                                        p = 1L;
                                                    } else p += 1L;
                                                    // Set value and report transaction success
                                                    mutableData.setValue(p);
                                                    return Transaction.success(mutableData);
                                                }

                                                @Override
                                                public void onComplete(DatabaseError databaseError, boolean b,
                                                                       DataSnapshot dataSnapshot) {
                                                    // Transaction completed
                                                    comments.smoothScrollToPosition(0);


                                                }
                                            });
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
                                                                if (c != null && c.userid != null && c.userid.trim().length() != 0 && !c.userid.equals(Profile.getCurrentProfile().getId()) && !c.userid.equals(currentObject.uploaded_by)) {
                                                                    if (!u.contains(c.userid)) {
                                                                        u.add(c.userid);
                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child(c.userid).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String gcmid = new String();
                                                                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                                                    gcmid = (String) dataSnapshot.getValue();

                                                                                if (gcmid.trim().length() != 0) {
                                                                                    String link = "Question:" + currentObject.id;
                                                                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "also commented on " + currentObject.username + " question\n\n" + content, gcmid + "", link, c.userid);

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
                                            if (!(finalData1 != null && finalData1.containsKey(currentObject.uploaded_by)))
                                                FirebaseDatabase.getInstance().getReference("myUsers").child(currentObject.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                            String gcmid = (String) (dataSnapshot.getValue());
                                                            String link = "Question:" + currentObject.id;
                                                            MyData.pushNotification(Profile.getCurrentProfile().getName(), "commented on your question\n\n" + content, gcmid + "", link, currentObject.uploaded_by);

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                        }

                                    } else {
                                        Toast.makeText(QuestionOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                    }
                                } else {


                                    Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                            .setAction("Login", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    QuestionOpenActivity.this.startActivity(new Intent(QuestionOpenActivity.this, Login.class));

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


            }

        }
    }

    public void putData() {

        if (currentObject != null && id != null && id.trim().length() != 0) {
            final Question item = currentObject;


            if (currentObject.media != null && currentObject.media.trim().length() != 0) {
                pic.setVisibility(View.VISIBLE);
                Picasso.with(context).load(currentObject.media).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        QuestionOpenActivity.this.bitmap = bitmap;
                        pic.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            ll1.setOnClickListener(this);
            ll2.setOnClickListener(this);
            ll3.setOnClickListener(this);

            if (currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)
                r[0].setText(currentObject.choice0);

            if (currentObject.choice1 != null && currentObject.choice1.trim().length() != 0)
                r[1].setText(currentObject.choice1);

            if (currentObject.choice2 != null && currentObject.choice2.trim().length() != 0)
                r[2].setText(currentObject.choice2);

            if (currentObject.choice3 != null && currentObject.choice3.trim().length() != 0)
                r[3].setText(currentObject.choice3);

            r[0].setOnClickListener(this);
            r[1].setOnClickListener(this);
            r[2].setOnClickListener(this);
            r[3].setOnClickListener(this);

            question.setText(currentObject.question);
            likescount.setText(currentObject.likes_count * -1 + "");
            viewscount.setText(currentObject.viewcount * -1 + "");


            if (Profile.getCurrentProfile() != null && currentObject.uploaded_by.equals(Profile.getCurrentProfile().getId())) {

                if (currentObject.explanation != null && currentObject.explanation.trim().length() != 0) {
                    exp.setText(currentObject.explanation);

                    exp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //openBottomSheet(currentObject.explanation);
                            new BottomSheetFragment(currentObject.explanation, "Explanation").show(getSupportFragmentManager(), "Explanation");
                        }
                    });

                } else {
                    exp.setText("No Explanation Available");
                }
                if (item.answer != null && item.choice0 != null && item.choice0.trim().length() != 0)
                    switch (item.answer) {
                        case 0:
                            r[0].setBackgroundColor(context.getResources().getColor(R.color.green));
                            r[0].setTextColor(context.getResources().getColor(R.color.white));
                            r[0].setTypeface(null, Typeface.BOLD);
                            break;
                        case 1:
                            r[1].setBackgroundColor(context.getResources().getColor(R.color.green));
                            r[1].setTextColor(context.getResources().getColor(R.color.white));
                            r[1].setTypeface(null, Typeface.BOLD);
                            break;
                        case 2:


                            r[2].setBackgroundColor(context.getResources().getColor(R.color.green));
                            r[2].setTextColor(context.getResources().getColor(R.color.white));
                            r[2].setTypeface(null, Typeface.BOLD);
                            break;
                        case 3:


                            r[3].setBackgroundColor(context.getResources().getColor(R.color.green));
                            r[3].setTextColor(context.getResources().getColor(R.color.white));
                            r[3].setTypeface(null, Typeface.BOLD);
                            break;
                        default:
                            break;
                    }


            } else if (Profile.getCurrentProfile() != null) {

                final DatabaseReference a = FirebaseDatabase.getInstance().getReference("Attempts").child(currentObject.id);
                a.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String, Boolean> attempts = new HashMap<String, Boolean>();
                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                            attempts = (HashMap<String, Boolean>) dataSnapshot.getValue();

                        if (attempts.containsKey(Profile.getCurrentProfile().getId())) {

                            if (currentObject.explanation != null && currentObject.explanation.trim().length() != 0) {
                                // exptext.setText(currentObject.explanation);
                                exp.setText(currentObject.explanation);
                                exp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //  openBottomSheet(currentObject.explanation);
                                        new BottomSheetFragment(currentObject.explanation, "Explanation").show(getSupportFragmentManager(), "Explanation");

                                    }
                                });


                            } else {
                                exp.setText("No Explanation Available");
                            }

                            TextView al = (TextView) findViewById(R.id.already_attempted);
                            al.setVisibility(View.VISIBLE);
                            if (item.answer != null && item.choice0 != null && item.choice0.trim().length() != 0)
                                switch (item.answer) {
                                    case 0:
                                        r[0].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        r[0].setTextColor(context.getResources().getColor(R.color.white));
                                        r[0].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 1:
                                        r[1].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        r[1].setTextColor(context.getResources().getColor(R.color.white));
                                        r[1].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 2:


                                        r[2].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        r[2].setTextColor(context.getResources().getColor(R.color.white));
                                        r[2].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 3:


                                        r[3].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        r[3].setTextColor(context.getResources().getColor(R.color.white));
                                        r[3].setTypeface(null, Typeface.BOLD);
                                        break;
                                    default:
                                        break;
                                }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            //if (currentObject.likes_count != 0) {
            FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(currentObject.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Boolean> hm = new HashMap<String, Boolean>();
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                        hm = (HashMap<String, Boolean>) dataSnapshot.getValue();
                        likerhead.setVisibility(View.VISIBLE);
                        likersrv.setVisibility(View.VISIBLE);
                        likersrv.setLayoutManager(new LinearLayoutManager(QuestionOpenActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        //  likersrv.setAdapter(new LikersAdapter(new ArrayList<String>(hm.keySet())));
                    }

                    likersrv.setAdapter(new LikersAdapter(new ArrayList<String>(hm.keySet())));

                    if (hm.size() == 0) {
                        likersrv.setVisibility(View.GONE);
                        likerhead.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //   }
            if (Profile.getCurrentProfile() != null)
                FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Boolean> r = new HashMap<String, Boolean>();
                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                            r = (HashMap<String, Boolean>) dataSnapshot.getValue();

                        if (r.containsKey(currentObject.id))
                            like.setLiked(true);
                        else
                            like.setLiked(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    if (Profile.getCurrentProfile() != null) {
                        if (MyData.haveNetworkConnection()) {
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(item.id);
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

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes");
                                    DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("likes_count");
                                    HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                    if (p != null) {
                                        if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                            like.setLiked(true);
                                            ref.child(item.id).setValue(true);

                                            FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                        String gcmid = (String) (dataSnapshot.getValue());


                                                        String link = "Question:" + item.id;
                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "liked your question", gcmid + "", link, item.uploaded_by);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        } else {
                                            like.setLiked(false);
                                            ref.child(item.id).setValue(null);
                                        }

                                        likescount.setText(p.size() + "");
                                        likecount.setValue(-1 * p.size());


                                    }
                                    if (p == null) {
                                        like.setLiked(false);
                                        ref.child(item.id).setValue(null);
                                        likescount.setText("0");
                                        likecount.setValue(0);


                                    }

                                }
                            });
                        } else {
                            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        QuestionOpenActivity.this.startActivity(new Intent(QuestionOpenActivity.this, Login.class));

                                    }
                                }).show();
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    if (Profile.getCurrentProfile() != null) {
                        if (MyData.haveNetworkConnection()) {
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(item.id);
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

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes");
                                    DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("likes_count");
                                    HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                    if (p != null) {
                                        if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                            like.setLiked(true);
                                            ref.child(item.id).setValue(true);

                                        } else {
                                            like.setLiked(false);
                                            ref.child(item.id).setValue(null);
                                        }

                                        likescount.setText(p.size() + "");
                                        likecount.setValue(-1 * p.size());


                                    }
                                    if (p == null) {
                                        like.setLiked(false);
                                        ref.child(item.id).setValue(null);
                                        likescount.setText("0");
                                        likecount.setValue(0);


                                    }

                                }
                            });
                        } else {
                            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                        }
                    } else {

                        Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        QuestionOpenActivity.this.startActivity(new Intent(QuestionOpenActivity.this, Login.class));

                                    }
                                }).show();
                    }

                }
            });

            if (currentObject.tags_here != null) {
                tags.setVisibility(View.VISIBLE);
                tags.setTags(new ArrayList<String>(currentObject.tags_here.keySet()));

                tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                    @Override
                    public void onTagClick(final int position, final String text) {
                        // ...
                        //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(QuestionOpenActivity.this, TagOpenActivity.class);
                        i.putExtra("id", text);
                        startActivity(i);

                    }

                    @Override
                    public void onTagLongClick(final int position, String text) {
                        // ...
                        Intent i = new Intent(QuestionOpenActivity.this, TagOpenActivity.class);
                        i.putExtra("id", text);
                        startActivity(i);
                    }

                });
                //  appBarLayout.setExpanded(false, false);
                relatedquestions.setNestedScrollingEnabled(false);
                relatedquestions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                final ArrayList<String> related_data = new ArrayList<>();
                final RelatedQuestionAdapter adapter = new RelatedQuestionAdapter(related_data);
                for (String s : currentObject.tags_here.keySet()) {
                    FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                HashMap<String, Boolean> r = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                ArrayList<String> to = new ArrayList<String>(r.keySet());
                                to.remove(currentObject.id);
                                HashSet<String> exp = new HashSet<String>(related_data);
                                exp.addAll(to);
                                related_data.removeAll(related_data);
                                related_data.addAll(exp);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                relatedquestions.setAdapter(adapter);
            }
            date.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(currentObject.date));


            if (item.uploaded_by.matches("\\d*")) {
                String urlImage = "https://graph.facebook.com/" + item.uploaded_by + "/picture?type=large";


                Picasso.with(context)
                        .load(urlImage)
                        .fit()
                        .transform(PaletteTransformation.instance())
                        .into(userdp, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {


                            }
                        });
            } else {

                FirebaseDatabase.getInstance().getReference("Page").child(item.uploaded_by).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            Picasso.with(context)
                                    .load(dataSnapshot.getValue().toString())
                                    .fit()
                                    .transform(PaletteTransformation.instance())
                                    .into(userdp, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {


                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


            username.setText(currentObject.username + "");
            username.setOnClickListener(this);
            userdp.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.quesion_open, menu);
        edit = menu.findItem(R.id.edit);
        //  ActionItemBadge.update(this, menu.findItem(R.id.notifications), getResources().getDrawable(R.drawable.ic_notifications_white_24dp), ActionItemBadge.BadgeStyles.RED, 0);

        return true;
    }

    public void share() {
        if (currentObject != null) {

            if (bitmap == null) {
                StringBuilder toShare = new StringBuilder(currentObject.question + "\n\n");
                if (currentObject.tags_here != null && currentObject.tags_here.size() != 0) {
                    for (String s : currentObject.tags_here.keySet())
                        toShare.append("#").append(s).append(" ");
                }
                if (currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)
                    toShare.append("\n\nChoice 1:").append(currentObject.choice0).append("\nChoice 2:").append(currentObject.choice1).append("\nChoice 3:").append(currentObject.choice2).append("\nChoice 4:").append(currentObject.choice3);
                toShare.append("\n\n#Questo");
                toShare.append("\n\n").append(MyData.buildDeepLink("question", currentObject.id));
                sheet = getShareActions(toShare.toString()).title("Sharing Question ").build();
                sheet.show();
            } else {
                Intent intent = null;
                try {


                    StringBuilder toShare = new StringBuilder(currentObject.question + "\n\n");
                    if (currentObject.tags_here != null && currentObject.tags_here.size() != 0) {
                        for (String s : currentObject.tags_here.keySet())
                            toShare.append("#").append(s).append(" ");
                    }
                    if (currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)
                        toShare.append("\n\nChoice 1:").append(currentObject.choice0).append("\nChoice 2:").append(currentObject.choice1).append("\nChoice 3:").append(currentObject.choice2).append("\nChoice 4:").append(currentObject.choice3);
                    toShare.append("\n\n#Questo");
                    toShare.append("\n\n").append(MyData.buildDeepLink("question", currentObject.id));

                    sheet = getShareActions(toShare.toString()).title("Sharing Question ").build();

                    File file = new File(this.getCacheDir(), "share" + currentObject.id + ".png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.putExtra(Intent.EXTRA_TEXT, toShare.toString());
                    intent.setType("image/png");
                    BottomSheetHelper.shareAction(this, intent).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } else {

            if (id != null && id.trim().length() != 0) {
                StringBuilder toShare = new StringBuilder("Guess the answer of following question" + "\n\n");
                toShare.append("\n\n#Questo");
                toShare.append("\n\n").append(MyData.buildDeepLink("question", id));
                sheet = getShareActions(toShare.toString()).title("Sharing Question ").build();
                sheet.show();
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (MyData.haveNetworkConnection()) {


            switch (item.getItemId()) {

                case R.id.quora:
                    if (currentObject != null) {
                        openD("https://www.quora.com/search?q=");

                        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        dl.openDrawer(Gravity.RIGHT);
                    }
                    return true;

                case R.id.stack:
                    if (currentObject != null) {
                        openD("http://stackexchange.com/search?q=");
                        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        dl.openDrawer(Gravity.RIGHT);
                    }
                    return true;

                case R.id.report:


                    if (Profile.getCurrentProfile() != null) {
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Report Question")
                                .customView(R.layout.report_layout, true)
                                .positiveText("Report")

                                .negativeColor(Color.BLACK)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //showToast("Password: " + passwordInput.getText().toString());


                                        EditText reason = (EditText) dialog.getCustomView().findViewById(R.id.reason);
                                        Toast.makeText(context, "Question Reported", Toast.LENGTH_SHORT).show();

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reports").child(Profile.getCurrentProfile().getId()).child("QuestionReports").child(currentObject.id);
                                        ref.child("reason").setValue(reason.getText().toString().trim());
                                        ref.child("id").setValue(currentObject.id);

                                    }
                                }).build();
                        dialog.show();
                    } else {

                        Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, Login.class));
                                    }
                                }).show();

                    }
                    return true;

                case R.id.edit:
                    if (Profile.getCurrentProfile() != null && Profile.getCurrentProfile().getId().equals(currentObject.uploaded_by)) {
                        Intent intent = new Intent(QuestionOpenActivity.this, EditQuestion.class);
                        intent.putExtra("id", currentObject.id);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(rootlayout, "You dont have access to edit", Snackbar.LENGTH_LONG)
                                .setAction("Close", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();

                    }
                    return true;

                case R.id.share:
                    share();
                    return true;


                case R.id.sharelink:

                    if (id != null && id.trim().length() != 0) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(getString(R.string.app_name), MyData.buildDeepLink("question", id).toString());
                        clipboard.setPrimaryClip(clip);


                        Snackbar.make(rootlayout, "Link copied", Snackbar.LENGTH_LONG)
                                .setAction("Share", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        share();
                                    }
                                }).show();
                    }


                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }


        } else

        {
            Toast.makeText(QuestionOpenActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

        }

        return true;
    }

    CoordinatorLayout rootlayout;

    private BottomSheet.Builder getShareActions(String text) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        return BottomSheetHelper.shareAction(this, shareIntent);
    }

    CircleImageView cdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_open);
        rootlayout = (CoordinatorLayout) findViewById(R.id.rootlayout);
        cdp = (CircleImageView) findViewById(R.id.dp);
        commentadd = (FloatingActionButton) findViewById(R.id.commentadd);
        //   commentadd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_material_shadow)));
        rl = (RelativeLayout) findViewById(R.id.rl);
        itemView = findViewById(R.id.cvopen);
        llchoice = (LinearLayout) findViewById(R.id.llchoice);
        dl = (DrawerLayout) findViewById(R.id.dl);
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        context = this;
        link = (ImageView) findViewById(R.id.link);

        id = getIntent().getExtras().getString("id");


        if (Profile.getCurrentProfile() != null) {
            String urlImage = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";

            Picasso.with(this).load(urlImage).into(cdp);

        }
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id != null && id.trim().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), MyData.buildDeepLink("question", id).toString());
                    clipboard.setPrimaryClip(clip);


                    Snackbar.make(rootlayout, "Link copied", Snackbar.LENGTH_LONG)
                            .setAction("Share", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    share();
                                }
                            }).show();
                }

            }
        });
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

        if (id != null && id.trim().length() != 0)
            FirebaseDatabase.getInstance().getReference("Question").child(id + "").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        currentObject = dataSnapshot.getValue(Question.class);
                        if (currentObject != null) {
                            init();

                            if (edit != null) {
                                if ((currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)) {
                                    llchoice.setVisibility(View.VISIBLE);
                                    rl.setVisibility(View.VISIBLE);
                                }
                                if (Profile.getCurrentProfile() != null && currentObject.uploaded_by.equals(Profile.getCurrentProfile().getId()))
                                    edit.setVisible(true);
                                else if (Profile.getCurrentProfile() != null)
                                    FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                HashMap<String, String> data = (HashMap<String, String>) dataSnapshot.getValue();

                                                if (data != null && data.keySet().contains(currentObject.uploaded_by)) {
                                                    edit.setVisible(true);
                                                }

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

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }


    @Override
    public void onClick(final View v) {

        Intent intent;

        QuestionOpenActivity holder = this;
        final Question item = currentObject;
        switch (v.getId()) {
            case R.id.ll1:

                intent = new Intent(v.getContext(), Reactions_Activity.class);
                intent.putExtra("id", item.id);
                MyData.setType(0);
                v.getContext().startActivity(intent);
                break;
            case R.id.ll2:

                intent = new Intent(v.getContext(), Reactions_Activity.class);
                intent.putExtra("id", item.id);
                MyData.setType(1);
                v.getContext().startActivity(intent);
                break;
            case R.id.ll3:
                intent = new Intent(v.getContext(), Reactions_Activity.class);
                intent.putExtra("id", item.id);
                MyData.setType(2);
                v.getContext().startActivity(intent);
                break;
            case R.id.username:
            case R.id.userdp:
                if (item.uploaded_by.matches("\\d*")) {
                    intent = new Intent(v.getContext(), User.class);

                } else {
                    intent = new Intent(v.getContext(), PageOpenActivity.class);
                }
                intent.putExtra("id", item.uploaded_by);
                v.getContext().startActivity(intent);
                break;
            case R.id.ch1:
            case R.id.ch2:
            case R.id.ch3:
            case R.id.ch4:

                if (Profile.getCurrentProfile() != null) {
                    int id = v.getId();
                    int selected = 0;
                    if (id == R.id.ch1) {
                        selected = 0;
                    } else if (id == R.id.ch2) {
                        selected = 1;

                    } else if (id == R.id.ch3) {
                        selected = 2;

                    } else if (id == R.id.ch4) {
                        selected = 3;

                    }

                    try {
                        int flag = 0;
                        holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.white));
                        holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.white));
                        holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.white));
                        holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.white));
                        holder.r[0].setTextColor(context.getResources().getColor(R.color.black));
                        holder.r[1].setTextColor(context.getResources().getColor(R.color.black));
                        holder.r[2].setTextColor(context.getResources().getColor(R.color.black));
                        holder.r[3].setTextColor(context.getResources().getColor(R.color.black));
                        holder.r[0].setTypeface(null, Typeface.NORMAL);
                        holder.r[1].setTypeface(null, Typeface.NORMAL);
                        holder.r[2].setTypeface(null, Typeface.NORMAL);
                        holder.r[3].setTypeface(null, Typeface.NORMAL);
                        if (selected == item.answer) {
                            flag = 1;
                        }
                        final int f = flag; //flag will be 1 for correctly answered case
                        if (f != 1) {

                            holder.r[selected].setBackgroundColor(context.getResources().getColor(R.color.red));
                            holder.r[selected].setTextColor(context.getResources().getColor(R.color.white));
                            holder.r[selected].setTypeface(null, Typeface.BOLD);
                        }
                        if (item.answer != null && item.choice0 != null && item.choice0.trim().length() != 0)
                            switch (item.answer) {
                                case 0:
                                    holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.green));
                                    holder.r[0].setTextColor(context.getResources().getColor(R.color.white));
                                    holder.r[0].setTypeface(null, Typeface.BOLD);
                                    break;
                                case 1:
                                    holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.green));
                                    holder.r[1].setTextColor(context.getResources().getColor(R.color.white));
                                    holder.r[1].setTypeface(null, Typeface.BOLD);
                                    break;
                                case 2:


                                    holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.green));
                                    holder.r[2].setTextColor(context.getResources().getColor(R.color.white));
                                    holder.r[2].setTypeface(null, Typeface.BOLD);
                                    break;
                                case 3:


                                    holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.green));
                                    holder.r[3].setTextColor(context.getResources().getColor(R.color.white));
                                    holder.r[3].setTypeface(null, Typeface.BOLD);
                                    break;
                                default:
                                    break;
                            }

                        if (!item.uploaded_by.equals(Profile.getCurrentProfile().getId())) {
                            if (MyData.haveNetworkConnection()) {

                                final DatabaseReference ownerref = FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("score");
                                final DatabaseReference userref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("score");
                                final DatabaseReference a = FirebaseDatabase.getInstance().getReference("Attempts").child(item.id);
                                a.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            HashMap<String, Boolean> attempts = new HashMap<String, Boolean>();
                                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                attempts = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                            if (!attempts.containsKey(Profile.getCurrentProfile().getId())) {

                                                if (f == 1) {

                                                    if (item.tags_here != null && item.tags_here.size() != 0) {
                                                        DatabaseReference useranal = FirebaseDatabase.getInstance().getReference("user_analysis").child(Profile.getCurrentProfile().getId());
                                                        for (String s : item.tags_here.keySet()) {
                                                            useranal.child(s).runTransaction(new Transaction.Handler() {
                                                                @Override
                                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                                    try {
                                                                        Long p = mutableData.getValue(Long.class);
                                                                        if (p == null) {
                                                                            p = 1L;
                                                                        } else
                                                                            p += 1L;
                                                                        // Set value and report transaction success
                                                                        mutableData.setValue(p);
                                                                    } catch (Exception e) {
                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String gcmid = (String) dataSnapshot.getValue();
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                                    }
                                                                    return Transaction.success(mutableData);
                                                                }

                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, boolean b,
                                                                                       DataSnapshot dataSnapshot) {
                                                                    // Transaction completed
                                                                    try {

                                                                    } catch (Exception e) {
                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String gcmid = (String) dataSnapshot.getValue();
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                                    }
                                                                }
                                                            });
                                                        }

                                                        DatabaseReference taganal;
                                                        for (String k : item.tags_here.keySet()) {
                                                            taganal = FirebaseDatabase.getInstance().getReference("tag_analysis").child(k).child(Profile.getCurrentProfile().getId());
                                                            taganal.child("id").setValue(Profile.getCurrentProfile().getId());
                                                            taganal.child("score").runTransaction(new Transaction.Handler() {
                                                                @Override
                                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                                    try {
                                                                        Long p = mutableData.getValue(Long.class);
                                                                        if (p == null) {
                                                                            p = -1L;
                                                                        } else
                                                                            p -= 1L;
                                                                        // Set value and report transaction success
                                                                        mutableData.setValue(p);
                                                                    } catch (Exception e) {
                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String gcmid = (String) dataSnapshot.getValue();
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                                    }
                                                                    return Transaction.success(mutableData);
                                                                }

                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, boolean b,
                                                                                       DataSnapshot dataSnapshot) {
                                                                    // Transaction completed
                                                                    try {

                                                                    } catch (Exception e) {
                                                                        FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                String gcmid = (String) dataSnapshot.getValue();
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                                    }
                                                                }
                                                            });

                                                        }

                                                    }
                                                    item.buildRef().child("viewcount").runTransaction(new Transaction.Handler() {
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
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                            return Transaction.success(mutableData);
                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                                               DataSnapshot dataSnapshot) {
                                                            // Transaction completed
                                                            try {

                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                        }
                                                    });

                                                    a.child(Profile.getCurrentProfile().getId()).setValue(true);

                                                    userref.runTransaction(new Transaction.Handler() {
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
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                            return Transaction.success(mutableData);
                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                                               DataSnapshot dataSnapshot) {
                                                            // Transaction completed
                                                            try {
                                                                if (databaseError == null)
                                                                    Toast.makeText(v.getContext(), "Score Updated " + (Long) dataSnapshot.getValue() * -1L, Toast.LENGTH_SHORT).show();
                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                        }
                                                    });


                                                } else {

                                                    item.buildRef().child("viewcount").runTransaction(new Transaction.Handler() {
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
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                            return Transaction.success(mutableData);
                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                                               DataSnapshot dataSnapshot) {
                                                            // Transaction completed
                                                            try {

                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                        }
                                                    });
                                                    a.child(Profile.getCurrentProfile().getId()).setValue(false);
                                                    userref.runTransaction(new Transaction.Handler() {
                                                        @Override
                                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                                            try {
                                                                Long p = mutableData.getValue(Long.class);
                                                                if (p == null) {
                                                                    p = +1L;
                                                                } else p += 1L;
                                                                // Set value and report transaction success
                                                                mutableData.setValue(p);
                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                            return Transaction.success(mutableData);
                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                                               DataSnapshot dataSnapshot) {
                                                            // Transaction completed
                                                            try {
                                                                if (databaseError == null)
                                                                    Toast.makeText(v.getContext(), "Score Updated " + (Long) dataSnapshot.getValue() * -1L, Toast.LENGTH_SHORT).show();
                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                        }
                                                    });
                                                    ownerref.runTransaction(new Transaction.Handler() {
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
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                            return Transaction.success(mutableData);
                                                        }

                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                                               DataSnapshot dataSnapshot) {
                                                            // Transaction completed
                                                            try {
                                                                if (databaseError == null)

                                                                    FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                String gcmid = (String) (dataSnapshot.getValue());

                                                                                String link = "Question:" + item.id;
                                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "failed to crack your question\n\n" + item.question, gcmid + "", link, item.uploaded_by);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                            } catch (Exception e) {
                                                                FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        String gcmid = (String) dataSnapshot.getValue();
                                                                        MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                                                            }
                                                        }
                                                    });

                                                }
                                            } else {


                                                                /*Activity at = (Activity) context;
                                                                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                                                                View layout = inflater.inflate(R.layout.toast,
                                                                        (ViewGroup) at.findViewById(R.id.toast_layout_root));
                                                                ((ImageView) layout.findViewById(R.id.image)).setImageDrawable((context.getResources().getDrawable(R.drawable.wrong)));
                                                                ((TextView) layout.findViewById(R.id.result)).setText("Already Attempted");
                                                                Toast toast = new Toast(context);
                                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                                toast.setView(layout);
                                                                toast.show();         Already attempted         */
                                            }

                                        } catch (Exception e) {
                                            FirebaseDatabase.getInstance().getReference("myUsers").child("1040113529409185").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String gcmid = (String) dataSnapshot.getValue();
                                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "Exception Occured bro", gcmid + "", "test", Profile.getCurrentProfile().getId());

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {

                                Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                            }
                        }

                    } catch (Exception e) {

                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                    }

                } else {

                    Snackbar.make(rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    QuestionOpenActivity.this.startActivity(new Intent(QuestionOpenActivity.this, Login.class));

                                }
                            }).show();
                }
                break;


        }


    }

}
