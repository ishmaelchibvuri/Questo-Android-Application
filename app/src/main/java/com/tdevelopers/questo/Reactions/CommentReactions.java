package com.tdevelopers.questo.Reactions;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
import com.tdevelopers.questo.Adapters.commentadapter;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.Objects.comment;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentReactions extends Fragment {

    public String id = "";
    RecyclerView recyclerView;
    FloatingActionButton commentadd;
    EditText nc;
    CircleImageView circleImageView;
    //  ImageView notfound;

    public CommentReactions() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CommentReactions(String id) {
        this.id = id;
    }

    public static CommentReactions newInstance(String id) {

        Bundle args = new Bundle();

        CommentReactions fragment = new CommentReactions(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  notfound=(ImageView)view.findViewById(R.id.notfound);
        recyclerView = (RecyclerView) view.findViewById(R.id.reactionscomment);
        commentadd = (FloatingActionButton) view.findViewById(R.id.commentadd);
        circleImageView = (CircleImageView) view.findViewById(R.id.dp);
        commentadd.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        nc = (EditText) view.findViewById(R.id.newc);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        try {

            if (Profile.getCurrentProfile() != null) {
                String urlImage = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";

                Picasso.with(getContext())
                        .load(urlImage)
                        .fit()
                        .transform(PaletteTransformation.instance())
                        .into(circleImageView, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {


                            }
                        });
            }
            if (id != null && id.trim().length() != 0) {


                Query query = FirebaseDatabase.getInstance().getReference("Comments").child("question_comments").child(id + "");
                final DatabaseReference re = FirebaseDatabase.getInstance().getReference("Comments").child("question_comments").child(id + "");

                recyclerView.setAdapter(new commentadapter(query, comment.class));
                FirebaseDatabase.getInstance().getReference("Question").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Question currentObject = dataSnapshot.getValue(Question.class);
                        if (currentObject != null) {

                            final DatabaseReference muted = FirebaseDatabase.getInstance().getReference("Muted").child("Questions").child(id);

                            muted.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap<String, Boolean> data = new HashMap<String, Boolean>();

                                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                        data = (HashMap<String, Boolean>) dataSnapshot.getValue();

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
                                                        View view = getActivity().getCurrentFocus();
                                                        if (view != null) {
                                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                        }
                                                        Toast.makeText(getContext(), "Comment Added", Toast.LENGTH_SHORT).show();
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
                                                                recyclerView.smoothScrollToPosition(0);


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
                                                    Toast.makeText(getContext(), "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Snackbar.make(Reactions_Activity.rootlayout, "Please Login", Snackbar.LENGTH_LONG)
                                                        .setAction("Login", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                getContext().startActivity(new Intent(getContext(), Login.class));

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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        } catch (Exception e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_reactions, container, false);


    }

}
