package com.tdevelopers.questo.Add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.HomeScreens.PageActivity;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPage extends AppCompatActivity implements View.OnClickListener {

    String name;
    EditText nameedittext, website, desc;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("New Page");
        website = (EditText) findViewById(R.id.website);
        desc = (EditText) findViewById(R.id.desc);


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
        nameedittext = (EditText) findViewById(R.id.name);
        dp = (CircleImageView) findViewById(R.id.dp);
        dp.setOnClickListener(this);
        findViewById(R.id.dp2).setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.addnewtag, menu);

        return true;
    }


    @Override
    public void onClick(View v) {
        try {
            MyData.verifyStoragePermissions(AddPage.this);

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (MyData.haveNetworkConnection())
            switch (item.getItemId()) {

                case R.id.done:

                case R.id.donetext:
                    name = nameedittext.getText().toString();
                    if (name != null && name.trim().length() != 0) {
                        if (bitmap != null) {
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

                        } else {
                            Toast.makeText(AddPage.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                        }
                    } else

                    {
                        Toast.makeText(AddPage.this, "Page name cant be empty", Toast.LENGTH_SHORT).show();
                    }


                    return true;


                default:
                    return super.onOptionsItemSelected(item);
            }


        else

        {
            Toast.makeText(AddPage.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        }

        return super.

                onOptionsItemSelected(item);

    }

    MaterialDialog dialog;
    Bitmap bitmap = null;
    CircleImageView dp;
    private static final int SELECT_PHOTO = 100;

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
                            Toast.makeText(AddPage.this, "Error occcured :( please try again !", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                        } catch (Error e) {
                            Toast.makeText(AddPage.this, "File too big to process", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        } catch (Error e) {
            Toast.makeText(AddPage.this, "File too big to process", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }


    public void uploadPic() {
        // Create a storage reference from our app
        try {
            if (bitmap == null) {
                Toast.makeText(AddPage.this, "Please Select Image", Toast.LENGTH_SHORT).show();

            } else {
                if (bitmap != null) {

                    final DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Page").push();
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://questo-1f35e.appspot.com");
                    // Create a reference to 'images/mountains.jpg'
                    StorageReference mountainImagesRef = storageRef.child("PageImages").child(newref.getKey() + "");
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




                                final String s = name;
                                if (s.trim().length() != 0) {

                                    if(MainActivity.rootLayout!=null)
                                    Snackbar.make(MainActivity.rootLayout, "Successfully created page " + s, Snackbar.LENGTH_LONG)
                                            .setAction("Show", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {


                                                    Intent intent = new Intent(AddPage.this, PageOpenActivity.class);
                                                    intent.putExtra("id", s);
                                                    startActivity(intent);

                                                }
                                            }).show();



                                    if(PageActivity.rootlayout!=null)
                                        Snackbar.make(PageActivity.rootlayout, "Successfully created page " + s, Snackbar.LENGTH_LONG)
                                                .setAction("Show", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        Intent intent = new Intent(AddPage.this, PageOpenActivity.class);
                                                        intent.putExtra("id", s);
                                                        startActivity(intent);

                                                    }
                                                }).show();





                                }

                                // FirebaseAuth.getInstance().signOut();
//                       finish()

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

                if (name.trim().length() != 0) {
                    name = name.trim();


                    DatabaseReference pageref = FirebaseDatabase.getInstance().getReference("Page").push();
                    pageref.child("admins").child(Profile.getCurrentProfile().getId()).setValue(true);
                    pageref.child("name").setValue(name);
                    if (website.getText() != null && website.getText().toString().trim().length() != 0)
                        pageref.child("website").setValue(website.getText().toString());

                    if (desc.getText() != null && desc.getText().toString().trim().length() != 0)
                        pageref.child("desc").setValue(desc.getText().toString());

                    pageref.child("pic").setValue(down);
                    pageref.child("id").setValue(pageref.getKey());
                    pageref.child("question_count").setValue(0);
                    pageref.child("article_count").setValue(0);
                    pageref.child("created_at").setValue(new Date().getTime());
                    FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").child(pageref.getKey()).setValue(name);


                }


            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }

    }


}
