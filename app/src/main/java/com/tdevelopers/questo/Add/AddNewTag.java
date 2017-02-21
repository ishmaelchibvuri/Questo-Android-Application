package com.tdevelopers.questo.Add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tdevelopers.questo.Adapters.tagccadapter;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Objects.categories;
import com.tdevelopers.questo.Opens.CatOpenActivity;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tdevelopers.questo.HomeScreens.MainActivity.rootLayout;

public class AddNewTag extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PHOTO = 100;
    CircleImageView dp;
    ImageView dp2;
    Bitmap bitmap;
    AppCompatEditText name;
    String currentcat;
    String id = "";
    RecyclerView recyclerView;
    ArrayList<Tag> data = new ArrayList<>();
    ArrayList<Tag> filtereddata = new ArrayList<>();
    TextView header;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {

                case SELECT_PHOTO:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2;
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            if (imageBitmap != null) {
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            }
                            final byte[] byteArray = baos.toByteArray();
                            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
                            dp.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddNewTag.this, "Error occcured :( please try again !", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                        } catch (Error e) {
                            Toast.makeText(AddNewTag.this, "File too big to process", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        } catch (Error e) {
            Toast.makeText(AddNewTag.this, "File too big to process", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Add New Tag");
        setSupportActionBar(toolbar);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("id"))
            id = getIntent().getExtras().getString("id");
        dp = (CircleImageView) findViewById(R.id.dp);
        dp2 = (ImageView) findViewById(R.id.dp2);
        dp.setOnClickListener(this);
        dp2.setOnClickListener(this);
        name = (AppCompatEditText) findViewById(R.id.name);
        header = (TextView) findViewById(R.id.header);
        recyclerView = (RecyclerView) findViewById(R.id.present);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }
        try {
            FirebaseDatabase.getInstance().getReference("Tag").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                            data = new ArrayList<Tag>();
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                Tag tag = d.getValue(Tag.class);
                                if (tag != null) {
                                    data.add(tag);
                                }
                            }
                            name.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    if (s.toString().trim().length() != 0) {
                                        filtereddata = new ArrayList<Tag>();

                                        for (Tag t : data) {
                                            if (t != null && t.name.toLowerCase().contains(s.toString().toLowerCase())) {
                                                filtereddata.add(t);
                                            }
                                        }
                                        header.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        recyclerView.setAdapter(new tagccadapter(filtereddata));
                                    } else if (s.toString().trim().length() == 0) {


                                        header.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }

                            });

                            FirebaseDatabase.getInstance().getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> data = new ArrayList<String>();
                                    if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            categories cat = d.getValue(categories.class);
                                            if (cat != null) {
                                                data.add(cat.name);
                                            }
                                        }
                                    }


                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNewTag.this, android.R.layout.select_dialog_item, data);


                                    AppCompatSpinner actv = (AppCompatSpinner) findViewById(R.id.catspinner);
                                    if (actv != null) {
                                        actv.setAdapter(adapter);
                                    }


                                    if (actv != null) {
                                        if (id != null && id.trim().length() != 0)
                                            actv.setSelection(adapter.getPosition(id + ""));
                                        actv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                currentcat = parent.getItemAtPosition(position).toString();


                                                Toast.makeText(AddNewTag.this, currentcat + " selected", Toast.LENGTH_SHORT).show();
                                            }


                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

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
                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.addnewtag, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (MyData.haveNetworkConnection())
                switch (item.getItemId()) {

                    case R.id.done:

                    case R.id.donetext:
                        if (data != null && name != null) {
                            if (currentcat != null && currentcat.trim().length() == 0)
                                Toast.makeText(AddNewTag.this, "Please Choose Category", Toast.LENGTH_SHORT).show();
                            else {
                                name.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (name.getText().toString().length() > 0) {
                                            name.setError(null);
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });


                                if (name.getText().toString().trim().length() == 0) {
                                    name.setError(getString(R.string.error_field_required));
                                } else if (!name.getText().toString().matches("[a-zA-Z ]*")) {
                                    name.setError("Only Alphabets are accepted");


                                } else if (name.getText().toString().trim().length() != 0 && name.getText().toString().matches("[a-zA-Z ]*")) {

                                    String custom = MyData.toCamelCase(name.getText().toString());
                                    name.setText(custom);
                                    ArrayList<String> tagname = new ArrayList<>();
                                    for (Tag z : data) {
                                        tagname.add(MyData.toCamelCase(z.name));
                                    }

                                    if (tagname.contains(custom)) {
                                        Toast.makeText(AddNewTag.this, "Already Tag with same name exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dialog = new MaterialDialog.Builder(this)
                                                .title("Loading").theme(Theme.LIGHT)
                                                .content("Please Wait")
                                                .progress(true, 0)
                                                .show();
                                        if (FirebaseAuth.getInstance().getCurrentUser() == null)
                                            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    uploadPic();
                                                }
                                            });
                                        else
                                            uploadPic();
                                    }


                                }
                            }
                        }
                        return true;


                    default:
                        return super.onOptionsItemSelected(item);
                }
            else {
                Toast.makeText(AddNewTag.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
            }

            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());
            return super.onOptionsItemSelected(item);
        }
    }

    MaterialDialog dialog;

    public void uploadPic() {
        // Create a storage reference from our app
        try {
            if (bitmap == null) {
                Toast.makeText(AddNewTag.this, "Please Select Image", Toast.LENGTH_SHORT).show();

            } else {
                if (bitmap != null && currentcat != null) {


                    final DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Article").push();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://questo-1f35e.appspot.com");
                    // Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child("TagUploads").child(newref.getKey() + "");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = mountainImagesRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }

                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            if (downloadUrl != null) {
                                //  article = new Article(newref.getKey(), date.getTime(), title.getText().toString(), input.getText().toString(), MyData.getMyUser().id, MyData.getMyUser().name, downloadUrl.toString());
                                if (dialog != null && dialog.isShowing())
                                    dialog.dismiss();

                                upload(downloadUrl.toString());


//                                Toast.makeText(AddNewTag.this, "Successfully added new  Tag", Toast.LENGTH_SHORT).show();
                                //          FirebaseAuth.getInstance().signOut();
//                       finish()

                                final String s = name.getText().toString();
                                if (s.trim().length() != 0) {


                                    if (rootLayout != null)
                                        Snackbar.make(rootLayout, "Successfully added tag " + s, Snackbar.LENGTH_LONG)
                                                .setAction("Show", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        Intent intent = new Intent(AddNewTag.this, TagOpenActivity.class);
                                                        intent.putExtra("id", s);
                                                        startActivity(intent);

                                                    }
                                                }).show();

                                    if (CatOpenActivity.rootlayout != null)
                                        Snackbar.make(CatOpenActivity.rootlayout, "Successfully added tag " + s, Snackbar.LENGTH_LONG)
                                                .setAction("Show", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        Intent intent = new Intent(AddNewTag.this, TagOpenActivity.class);
                                                        intent.putExtra("id", s);
                                                        startActivity(intent);

                                                    }
                                                }).show();


                                }


                                finish();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }

    public void upload(String down) {
        try {
            if (name != null) {

                if (name.getText().toString().trim().length() != 0) {
                    String custom = MyData.toCamelCase(name.getText().toString());
                    DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(custom);

                    tagref.child("name").setValue(custom);
                    tagref.child("pic").setValue(down);
                    tagref.child("category").setValue(currentcat);
                    tagref.child("followers").setValue(0);
                    tagref.child("question_count").setValue(0);
                    tagref.child("article_count").setValue(0);
                    tagref.child("created_at").setValue(new Date().getTime());
                    FirebaseDatabase.getInstance().getReference("categories").child(currentcat).child("tags").child(custom).setValue(true);
                }
            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }

    @Override
    public void onClick(View v) {
        try {

            MyData.verifyStoragePermissions(AddNewTag.this);
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }
}
