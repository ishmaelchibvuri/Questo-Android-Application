package com.tdevelopers.questo;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Objects.MyData;

public class About_me extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setTitle("SaiTej Dandge");
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

            LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
            if (ll != null) {
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/questoapps/?fref=ts"));
                            startActivity(browserIntent);
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

                        } catch (Error e) {

                        }

                    }
                });
            }
            FloatingActionButton share = (FloatingActionButton) findViewById(R.id.shareapp);
            if (share != null) {
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String text = "Check out new app  Questo : \n " + "https://play.google.com/store/apps/details?id=com.tdevelopers.questo";
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                            BottomSheet sheet = BottomSheetHelper.shareAction(About_me.this, shareIntent).title("Share App").build();
                            sheet.show();
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

                        } catch (Error error) {

                        }
                    }
                });
            }
            TextView textView = (TextView) findViewById(R.id.about_me);
            Typeface font = Typeface.createFromAsset(getAssets(), "proxima.ttf");
            textView.setTypeface(font);
            ImageView gmail, fb, whatsapp;
            gmail = (ImageView) findViewById(R.id.gmailpic);
            fb = (ImageView) findViewById(R.id.fbpic);
            whatsapp = (ImageView) findViewById(R.id.whatsappic);
            if (gmail != null) {
                gmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            emailIntent.setType("vnd.android.cursor.item/email");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"saitejdandge1@gmail.com"});
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Regarding Questo");
                            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                            startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
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

                        } catch (Error e) {

                        }
                    }
                });
            }
            if (whatsapp != null) {
                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //  About_me.this.addActivityListener(someActivityListener);
                        try {
                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

// Just two examples of information you can send to pre-fill out data for the
// user.  See android.provider.ContactsContract.Intents.Insert for the complete
// list.
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, "SaiTej Dandge @Developer");
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, "9177896188");

// Send with it a unique request code, so when you get called back, you can
// check to make sure it is from the intent you launched (ideally should be
// some public static final so receiver can check against it)
                            int PICK_CONTACT = 100;
                            About_me.this.startActivityForResult(intent, PICK_CONTACT);
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

                        } catch (Error e) {

                        }
                    }
                });
                if (fb != null) {
                    fb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sai.tej.94651"));
                                startActivity(browserIntent);
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

                            } catch (Error e) {

                            }
                        }
                    });
                }
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

        } catch (Error e) {

        }
    }
}
