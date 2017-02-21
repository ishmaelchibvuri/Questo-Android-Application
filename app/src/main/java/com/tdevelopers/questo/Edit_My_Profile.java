package com.tdevelopers.questo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.MyData;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_My_Profile extends AppCompatActivity {

    AppCompatEditText editname, about_me;
    AutoCompleteTextView editemail;

    public void init() {
        editemail = (AutoCompleteTextView) findViewById(R.id.edit_email);
        editname = (AppCompatEditText) findViewById(R.id.edit_name);
        about_me = (AppCompatEditText) findViewById(R.id.about_me);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
            case R.id.donetext:
                if (MyData.haveNetworkConnection())
                    edit();
                else
                    Toast.makeText(Edit_My_Profile.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public void edit() {
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId());

        editemail = (AutoCompleteTextView) findViewById(R.id.edit_email);
        editname = (AppCompatEditText) findViewById(R.id.edit_name);
        about_me = (AppCompatEditText) findViewById(R.id.about_me);
        if (editname.getText().toString().trim().length() == 0) {
            editname.setError(getString(R.string.error_field_required));

        }


        if ((editemail.getText().toString().trim().length() != 0) && !isEmailValid(editemail.getText().toString().trim()))

        {

            editemail.setError(getString(R.string.error_invalid_email));

        }


        if ((editname.getText().toString().trim().length() != 0)) {
            if (editname != null && editname.getText().toString().trim().length() != 0) {
                userref.child("name").setValue(editname.getText().toString());
            }

            if (editemail != null && editemail.getText().toString().trim().length() != 0 && isEmailValid(editemail.getText().toString())) {
                userref.child("email").setValue(editemail.getText().toString());
            }

            if (about_me.getText().toString().trim().length() != 0) {
                userref.child("work").setValue(about_me.getText().toString());
            }


            Toast.makeText(Edit_My_Profile.this, "Details Updated", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__my__profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Edit My Profile");
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
        init();
        String urlImage = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";
        CircleImageView c = (CircleImageView) findViewById(R.id.edit_dp);
        Picasso.with(this)
                .load(urlImage).into(c);
        FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ex = "";
                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                    ex = (String) dataSnapshot.getValue();

                editname = (AppCompatEditText) findViewById(R.id.edit_name);

                if (ex != null && !ex.equals(""))
                    editname.setText(ex);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String x = "";
                editemail = (AutoCompleteTextView) findViewById(R.id.edit_email);
                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                    x = (String) dataSnapshot.getValue();
                if (x != null && !x.equals(""))
                    editemail.setText(x);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("work").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String x = "";
                about_me = (AppCompatEditText) findViewById(R.id.about_me);
                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                    x = (String) dataSnapshot.getValue();
                if (x != null && !x.equals(""))
                    about_me.setText(x);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.addnewtag, menu);


        return true;
    }
}
