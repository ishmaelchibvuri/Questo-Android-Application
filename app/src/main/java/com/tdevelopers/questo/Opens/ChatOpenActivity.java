package com.tdevelopers.questo.Opens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Adapters.ChatAdapter;
import com.tdevelopers.questo.Objects.Message;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatOpenActivity extends AppCompatActivity {

    String id;
    ImageButton send;
    EditText input;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_open);
        send = (ImageButton) findViewById(R.id.send);
        input = (EditText) findViewById(R.id.input);
        recyclerView = (RecyclerView) findViewById(R.id.chatopenrv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);


        ImageView back = (ImageView) findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        if (toolbar != null) {
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(ChatOpenActivity.this, User.class);
                    i.putExtra("id", id);
                    startActivity(i);
                }
            });
        }
        CircleImageView userdp = (CircleImageView) findViewById(R.id.userdp);
        id = getIntent().getExtras().getString("id");
        if (id != null && !id.isEmpty()) {
            String urlImage = "https://graph.facebook.com/" + id + "/picture?type=large";

            Picasso.with(this)
                    .load(urlImage)
                    .fit()
                    .transform(PaletteTransformation.instance())
                    .into(userdp, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {


                        }
                    });
            FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    setSupportActionBar(toolbar);


                    TextView t = (TextView) findViewById(R.id.username);
                    t.setText(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chat");
        if (Profile.getCurrentProfile().getId().compareTo(id) < 0) {
            recyclerView.setAdapter(new ChatAdapter(ref.child(Profile.getCurrentProfile().getId() + ":" + id).orderByPriority(), Message.class, recyclerView));
            recyclerView.scrollToPosition(0);
        } else {

            recyclerView.setAdapter(new ChatAdapter(ref.child(id + ":" + Profile.getCurrentProfile().getId()).orderByPriority(), Message.class, recyclerView));
            recyclerView.scrollToPosition(0);

        }
        final View contentView = findViewById(R.id.cc);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                // Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened

                    recyclerView.scrollToPosition(0);
                    //Toast.makeText(ChatOpenActivity.this, "something happened", Toast.LENGTH_SHORT).show();
                } else {
                    // keyboard is closed

                    //  recyclerView.scrollToPosition(0);

                    // Toast.makeText(ChatOpenActivity.this, "something happened", Toast.LENGTH_SHORT).show();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input != null && input.getText().toString().trim().length() != 0) {


                    final String s = input.getText().toString();

                    DatabaseReference abs = FirebaseDatabase.getInstance().getReference("abstract");
                    if (Profile.getCurrentProfile().getId().compareTo(id) < 0) {

                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chat").child(Profile.getCurrentProfile().getId() + ":" + id).push();

                        ref.child("content").setValue(s);
                        long l = new Date().getTime();
                        ref.child("date").setValue(l);
                        ref.child("userid").setValue(Profile.getCurrentProfile().getId());
                        ref.setPriority(l * -1);


                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("content").setValue(s);
                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("content").setValue(s);
                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("date").setValue(l * -1);
                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("date").setValue(l * -1);

                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("userid").setValue(id);

                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("userid").setValue(Profile.getCurrentProfile().getId());

                        input.setText("");

                        View view = getCurrentFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (view != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    String gcmid = (String) dataSnapshot.getValue();
                                    String link = "Message:" + ref.getKey();
                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), s, gcmid + "", link, id);
                                } catch (Exception e) {
                                    Toast.makeText(ChatOpenActivity.this, e.toString() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        recyclerView.scrollToPosition(0);

                    } else {
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chat").child(id + ":" + Profile.getCurrentProfile().getId()).push();

                        ref.child("content").setValue(s);
                        long l = new Date().getTime();
                        ref.child("date").setValue(l);
                        ref.setPriority(l * -1);
                        ref.child("userid").setValue(Profile.getCurrentProfile().getId());
                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("content").setValue(s);

                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("content").setValue(s);

                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("date").setValue(l * -1);

                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("date").setValue(l * -1);


                        abs.child(Profile.getCurrentProfile().getId()).child(id).child("userid").setValue(id);

                        abs.child(id).child(Profile.getCurrentProfile().getId()).child("userid").setValue(Profile.getCurrentProfile().getId());

                        input.setText("");
                        View view = getCurrentFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (view != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    String gcmid = (String) dataSnapshot.getValue();
                                    String link = "Message:" + ref.getKey();
                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), s, gcmid + "", link, id);

                                } catch (Exception e) {
                                    Toast.makeText(ChatOpenActivity.this, e.toString() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        recyclerView.scrollToPosition(0);

                    }
                } else if (input.getText().toString().trim().length() == 0) {

                }

            }
        });

    }

}
