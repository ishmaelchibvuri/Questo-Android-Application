package com.tdevelopers.questo.LoginStuff;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {
    CallbackManager mCallbackManager;
    MaterialDialog dialog;
    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            // Log.v("profile track", (DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(loginResult.getAccessToken().getExpires())));

            dialog = new MaterialDialog.Builder(Login.this)
                    .title("Loading").theme(Theme.LIGHT)
                    .content("Please Wait")
                    .progress(true, 0)
                    .show();
            if (loginResult.getAccessToken() != null && !loginResult.getAccessToken().isExpired()) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    final String name = object.getString("name");
                                    // String email = object.getString("email");
                                    final String id = object.getString("id");


                                    FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {


                                            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("myUsers").child(id);
                                            ref.child("name").setValue(name);
                                            ref.child("id").setValue(id);

                                            FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    //    Long x=0L;
                                                    if (dataSnapshot.getValue() == null) {
                                                        ref.child("score").setValue(0L);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("nscore").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    //    Long x=0L;
                                                    if (dataSnapshot.getValue() == null) {
                                                        ref.child("nscore").setValue(0L);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            //  ref.child("email").setValue(email);


                                            String token = FirebaseInstanceId.getInstance().getToken();
                                            ref.child("gcmid").setValue(token);


                                            String founderid = "1040113529409185";

                                            final String s = "Welcome to Questo \nThank you for downloading and Please share this with your friends and make us grow big ! Like our Facebook\nPage:https://www.facebook.com/questoapps\n@Founder\nSaiTej Dandge";

                                            DatabaseReference abs = FirebaseDatabase.getInstance().getReference("abstract");
                                            if (id.compareTo(founderid) < 0) {

                                                final DatabaseReference refr = FirebaseDatabase.getInstance().getReference("chat").child(id + ":" + founderid).push();
                                                refr.child("content").setValue(s);
                                                long l = new Date().getTime();
                                                refr.child("date").setValue(l);
                                                refr.child("userid").setValue(founderid);
                                                refr.setPriority(l * -1);


                                                abs.child(id).child(founderid).child("content").setValue(s);
                                                abs.child(founderid).child(id).child("content").setValue(s);
                                                abs.child(id).child(founderid).child("date").setValue(l * -1);
                                                abs.child(founderid).child(id).child("date").setValue(l * -1);

                                                abs.child(id).child(founderid).child("userid").setValue(founderid);

                                                abs.child(founderid).child(id).child("userid").setValue(id);

                                                try {
                                                    String link = "Message:" + refr.getKey();
                                                    MyData.pushNotification("Team Questo", s, token + "", link, founderid);
                                                } catch (Exception e) {

                                                    Log.v("Exception occured", e.toString());
                                                    // Toast.makeText(ChatOpenActivity.this, e.toString() + "", Toast.LENGTH_SHORT).show();
                                                }

                                            } else {
                                                final DatabaseReference refr = FirebaseDatabase.getInstance().getReference("chat").child(founderid + ":" + id).push();

                                                // final DatabaseReference refr = FirebaseDatabase.getInstance().getReference("chat").child(id + ":" + founderid).push();
                                                refr.child("content").setValue(s);
                                                long l = new Date().getTime();
                                                refr.child("date").setValue(l);
                                                refr.child("userid").setValue(founderid);
                                                refr.setPriority(l * -1);


                                                abs.child(id).child(founderid).child("content").setValue(s);
                                                abs.child(founderid).child(id).child("content").setValue(s);
                                                abs.child(id).child(founderid).child("date").setValue(l * -1);
                                                abs.child(founderid).child(id).child("date").setValue(l * -1);

                                                abs.child(id).child(founderid).child("userid").setValue(founderid);

                                                abs.child(founderid).child(id).child("userid").setValue(id);

                                                try {
                                                    String link = "Message:" + refr.getKey();
                                                    MyData.pushNotification("Team Questo", s, token + "", link, founderid);
                                                } catch (Exception e) {
                                                    // Toast.makeText(ChatOpenActivity.this, e.toString() + "", Toast.LENGTH_SHORT).show();

                                                    Log.v("Exception occured", e.toString());
                                                }

                                            }

                                            if (dialog != null && dialog.isShowing())
                                                dialog.dismiss();

                                            try {
                                                View Header = MainActivity.Header;
                                                final TextView textView = (TextView) Header.findViewById(R.id.name);
                                                final TextView textView1 = (TextView) Header.findViewById(R.id.sub);
                                                final TextView textView2 = (TextView) Header.findViewById(R.id.score);
                                                final CircleImageView circleImageView = (CircleImageView) Header.findViewById(R.id.CimageView);
                                                DatabaseReference userref;
                                                if (Header != null) {
                                                    Header.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent i = new Intent(Login.this, User.class);
                                                            i.putExtra("id", id);
                                                            startActivity(i);
                                                        }
                                                    });

                                                    textView1.setVisibility(View.VISIBLE);
                                                    textView2.setVisibility(View.VISIBLE);
                                                    userref = FirebaseDatabase.getInstance().getReference("myUsers").child(id);
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
                                                                Activity a = Login.this;
                                                                Picasso.with(a)
                                                                        .load(urlImage).fit().into(circleImageView);


                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });


                                                }

                                            } catch (Exception e) {

                                            }


                                            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);

                                            if (id != null && id.trim().length() != 0) {
                                                if (sharedPreferences.getInt("entry", 0) != 1) {
                                                    Toast.makeText(Login.this, "successfully logged in as " + name, Toast.LENGTH_SHORT).show();


                                                    startActivity(new Intent(Login.this, MainActivity.class));
                                                    startActivity(new Intent(Login.this, Introduction.class));

                                                    // SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("entry", 1);
                                                    editor.apply();
                                                    finish();
                                                } else {
                                                    finish();
                                                }

                                            }

                                        }
                                    });


                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                // parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("entry", 1);
                editor.apply();


                startActivity(new Intent(Login.this, MainActivity.class));
                startActivity(new Intent(Login.this, Introduction.class));
                finish();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);


        if (sharedPreferences.getInt("entry", 0) == 1) {
            skip.setVisibility(View.GONE);
        }

        if (Profile.getCurrentProfile() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else if (sharedPreferences.getInt("entry", 0) == 1) {

            if (MyData.exsist == 0) {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }

        }

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        if (loginButton != null) {
            //
            //  loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(mCallbackManager, callback);


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
