package com.tdevelopers.questo.Add;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Opens.ArticleOpenActivity;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class AddArticleMaterial extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final int SELECT_PHOTO = 100;
    AppCompatEditText input;
    AppCompatEditText title;
    ImageView adp;
    HashSet<String> filterTags = new HashSet<>();
    MaterialDialog outdialog;
    Bitmap bitmap;
    TagContainerLayout tagContainerLayout, tags;
    CollapsingToolbarLayout collapsingToolbarLayout;
    SearchView searchView;

    ArrayList<Tag> tagList = new ArrayList<>();
    AutoCompleteTextView auto;
    MaterialDialog dialog;

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        //Log.d("AutocompleteContacts", "onItemSelected() position " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Toast.makeText(getBaseContext(), arg0.getItemAtPosition(arg2) + "",
                Toast.LENGTH_LONG).show();


        String s = arg0.getItemAtPosition(arg2).toString();
        filterTags.add(s);
        tagContainerLayout.setVisibility(View.VISIBLE);
        tagContainerLayout.setTags(new ArrayList<String>(filterTags));
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(final int position, final String text) {
                // ...
                //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                        .setTitle("Delete " + text)
                        .setMessage("Remove " + text + " tag from this article ?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tagContainerLayout.removeTag(position);
                                filterTags.remove(text);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }
        });

        auto.setText("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article_material);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Add Feed");
        auto = (AutoCompleteTextView) findViewById(R.id.auto);

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("id").equals("id")) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        tagContainerLayout = (TagContainerLayout) findViewById(R.id.tag_container);
        adp = (ImageView) findViewById(R.id.adp);
        title = (AppCompatEditText) findViewById(R.id.title);

        input = (AppCompatEditText) findViewById(R.id.takearticle);
        FirebaseDatabase.getInstance().getReference("Tag").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                    tagList = new ArrayList<Tag>();
                    for (DataSnapshot d : dataSnapshot.getChildren())
                        tagList.add(d.getValue(Tag.class));


                    //Create adapter
                    String item[] = new String[tagList.size()];
                    int i = 0;
                    for (Tag t : tagList)
                        item[i++] = t.name;
                    ArrayAdapter adapter = new ArrayAdapter<String>(AddArticleMaterial.this, android.R.layout.simple_dropdown_item_1line, item);

                    auto.setThreshold(1);

                    //Set adapter to AutoCompleteTextView
                    auto.setAdapter(adapter);
                    auto.setOnItemSelectedListener(AddArticleMaterial.this);
                    auto.setOnItemClickListener(AddArticleMaterial.this);
                    auto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl);
                            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                            if (behavior != null) {
                                behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, v.getBottom(), true);
                            }
                        }
                    });
                }
            }
        });
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitleEnabled(false);
        }
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
            if (MyData.getTags() != null && MyData.getTags().size() != 0) {
                filterTags.addAll(MyData.getTags());
                tagContainerLayout.setVisibility(View.VISIBLE);
                tagContainerLayout.setTags(new ArrayList<String>(filterTags));
                tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                    @Override
                    public void onTagClick(final int position, final String text) {
                        // ...
                        //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                        AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                                .setTitle("Delete " + text)
                                .setMessage("Remove " + text + " tag from this article ? ")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tagContainerLayout.removeTag(position);
                                        filterTags.remove(text);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                    }

                    @Override
                    public void onTagLongClick(final int position, String text) {
                        // ...
                    }
                });

                Toast.makeText(AddArticleMaterial.this, "Tags Included " + filterTags, Toast.LENGTH_SHORT).show();
                MyData.setTags(new HashSet<String>());

            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (input.getText().toString().trim().length() != 0) {

                                if (title.getText().toString().trim().length() != 0) {
                                    DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Article").push();
                                    Date date = new Date();

                                    if (bitmap != null) {
                                        if (MyData.haveNetworkConnection()) {
                                            dialog = new MaterialDialog.Builder(AddArticleMaterial.this)
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
                                        } else
                                            Toast.makeText(AddArticleMaterial.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                    } else {
                                        final Article article = new Article(newref.getKey(), date.getTime(), title.getText().toString(), input.getText().toString(), Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName(), "");
                                        if (MyData.haveNetworkConnection()) {
                                            upload(article);


                                            if (MainActivity.springFloatingActionMenu != null) {
                                                MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                            }

                                            if (MainActivity.rootLayout != null)
                                                Snackbar.make(MainActivity.rootLayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                                        .setAction("Show", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (article.id != null && article.id.trim().length() != 0) {
                                                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                                    intent.putExtra("id", article.id);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }).show();

                                            if (TagOpenActivity.rootlayout != null)
                                                Snackbar.make(TagOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                                        .setAction("Show", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (article.id != null && article.id.trim().length() != 0) {
                                                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                                    intent.putExtra("id", article.id);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }).show();

                                            if (PageOpenActivity.rootlayout != null)
                                                Snackbar.make(PageOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                                        .setAction("Show", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (article.id != null && article.id.trim().length() != 0) {
                                                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                                    intent.putExtra("id", article.id);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }).show();

                                        } else
                                            Toast.makeText(AddArticleMaterial.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(AddArticleMaterial.this, "Please Write Title", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Write Content of Article", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                        }
                    }

                });
            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }

    public void upload(final Article article) {
        try {
            DatabaseReference current = FirebaseDatabase.getInstance().getReference("Article").child(article.id);
            Map<String, String> q = new HashMap<String, String>();
            //  q.put("description", "loading");
            q.put("username", article.username);
            q.put("uploaded_by", article.uploaded_by);
            q.put("title", article.title);
            q.put("id", article.id);
            q.put("image", article.image);
            current.setValue(q);
            current.child("likes_count").setValue(0L);
            current.child("views_count").setValue(0L);
            current.child("nlikes_count").setValue(0L);
            //current.child("nviews_count").setValue(article.nviews_count);
            current.child("date").setValue(article.date);
            current.setPriority(article.date * -1);
            FirebaseDatabase.getInstance().getReference("descriptions").child(article.id).setValue(article.description);

            if (MainActivity.springFloatingActionMenu != null) {
                MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
            }
            if (MainActivity.rootLayout != null)
                Snackbar.make(MainActivity.rootLayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (article.id != null && article.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                    intent.putExtra("id", article.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();

            if (TagOpenActivity.rootlayout != null)
                Snackbar.make(TagOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (article.id != null && article.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                    intent.putExtra("id", article.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();

            if (PageOpenActivity.rootlayout != null)
                Snackbar.make(PageOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (article.id != null && article.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                    intent.putExtra("id", article.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();

            DatabaseReference tagref;
            ArrayList<String> tagsdata = new ArrayList<>();
            if (filterTags != null && filterTags.size() != 0) {
                for (String s : filterTags) {
                    if (s != null) {
                        current.child("tags_here").child(s + "").setValue(true);
                        tagsdata.add(s);
                    }
                    tagref = FirebaseDatabase.getInstance().getReference("TagUploads").child(s + "").child("Articles").child(article.id);
                    tagref.setValue(true);
                }

            }
            for (final String s : tagsdata) {
                FirebaseDatabase.getInstance().getReference("TagFollowers").child(s + "").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            HashMap<String, Boolean> tobesent = new HashMap<String, Boolean>();
                            tobesent = (HashMap<String, Boolean>) dataSnapshot.getValue();
                            for (Map.Entry<String, Boolean> entry : tobesent.entrySet()) {
                                String key = entry.getKey();
                                // String value = entry.getValue();
                                FirebaseDatabase.getInstance().getReference("myUsers").child(key + "").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                                            String link = "Article:" + article.id;
                                            MyData.pushNotification(s, "added article to tag " + s + "\n\n" + article.title, (String) dataSnapshot.getValue(), link, article.uploaded_by);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                // do stuff
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    for (String s : data.keySet())

                        FirebaseDatabase.getInstance().getReference("myUsers").child(s + "").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {


                                    String link = "Article:" + article.id;
                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "added article\n\n" + article.title, (String) dataSnapshot.getValue(), link, article.uploaded_by);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("article_count").runTransaction(new Transaction.Handler() {
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
                }
            });
            FirebaseDatabase.getInstance().getReference("articles_count").runTransaction(new Transaction.Handler() {
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
                }
            });

            for (String s : tagsdata) {

                FirebaseDatabase.getInstance().getReference("Tag").child(s + "").child("article_count").runTransaction(new Transaction.Handler() {
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
                    }
                });

            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_article, menu);
        return true;

    }

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
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            final byte[] byteArray = baos.toByteArray();
                            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
                            adp.setImageBitmap(bitmap);
                            View v = findViewById(R.id.g);
                            if (v != null) {
                                v.setVisibility(View.VISIBLE);
                            }
                        } catch (Error e) {

                            Toast.makeText(AddArticleMaterial.this, "File too big to process", Toast.LENGTH_SHORT).show();

                        }
                    }
            }
        } catch (Exception e) {
            Toast.makeText(AddArticleMaterial.this, "Error occcured :( please try again !", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }

    public void fetchTag() {


        if (tagList != null && tagList.size() > 0) {

            outdialog = new MaterialDialog.Builder(AddArticleMaterial.this)
                    .title("Add Tags")
                    .customView(R.layout.choosetagdialog, true)
                    .positiveText("Ok").negativeText("cancel").neutralText("Add Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(AddArticleMaterial.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddArticleMaterial.this, AddNewTag.class);

                            startActivity(i);
                        }
                    }).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (filterTags != null) {
                                tagContainerLayout.setVisibility(View.VISIBLE);
                                tagContainerLayout.setTags(new ArrayList<>(filterTags));
                                tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                                    @Override
                                    public void onTagClick(final int position, final String text) {
                                        // ...
                                        //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                                        AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                                                .setTitle("Delete " + text)
                                                .setMessage("Remove " + text + " tag from this article ?")
                                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        tagContainerLayout.removeTag(position);
                                                        filterTags.remove(text);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                    }

                                    @Override
                                    public void onTagLongClick(final int position, String text) {
                                        // ...
                                    }
                                });
                                Toast.makeText(AddArticleMaterial.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).build();
            tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
            if (filterTags != null) {
                tags.setTags(new ArrayList<String>(filterTags));
                tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                    @Override
                    public void onTagClick(final int position, final String text) {

                        AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                                .setTitle("Delete " + text)
                                .setMessage("Remove " + text + " tag from filter ?")
                                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tags.removeTag(position);

                                        filterTags.remove(text);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        dialog.show();
                    }

                    @Override
                    public void onTagLongClick(final int position, String text) {
                        // ...
                    }
                });


            }

            final RecyclerView rv = (RecyclerView) outdialog.findViewById(R.id.ctag);
            rv.setLayoutManager(new LinearLayoutManager(AddArticleMaterial.this));
            final FullTagAdapter tagadapter = new FullTagAdapter(tagList);
            rv.setAdapter(tagadapter);
            searchView = (SearchView) outdialog.findViewById(R.id.search_filter);
            searchView.setVisibility(View.VISIBLE);
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(false);
            searchView.setQueryHint("Search Tags");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //        Toast.makeText(AddQuestion.this, "Searched for" + newText, Toast.LENGTH_SHORT).show();
                    tagadapter.getFilter().filter(newText);
                    return true;
                }
            });
            rv.setNestedScrollingEnabled(false);
            outdialog.show();


        } else {


            FirebaseDatabase.getInstance().getReference("Tag").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot != null && dataSnapshot.getChildren() != null) {
                        for (DataSnapshot d : dataSnapshot.getChildren())
                            tagList.add(d.getValue(Tag.class));


                        outdialog = new MaterialDialog.Builder(AddArticleMaterial.this)
                                .title("Add Tags")
                                .customView(R.layout.choosetagdialog, true)
                                .positiveText("Ok").negativeText("cancel").neutralText("Add Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Toast.makeText(AddArticleMaterial.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(AddArticleMaterial.this, AddNewTag.class);

                                        startActivity(i);
                                    }
                                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (filterTags != null) {
                                            tagContainerLayout.setVisibility(View.VISIBLE);
                                            tagContainerLayout.setTags(new ArrayList<>(filterTags));
                                            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                                                @Override
                                                public void onTagClick(final int position, final String text) {
                                                    // ...
                                                    //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                                                    AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                                                            .setTitle("Delete " + text)
                                                            .setMessage("Remove " + text + " tag from this article ?")
                                                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    tagContainerLayout.removeTag(position);
                                                                    filterTags.remove(text);
                                                                }
                                                            })
                                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .create();
                                                    dialog.show();
                                                }

                                                @Override
                                                public void onTagLongClick(final int position, String text) {
                                                    // ...
                                                }
                                            });
                                            Toast.makeText(AddArticleMaterial.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).build();
                        tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
                        if (filterTags != null) {
                            tags.setTags(new ArrayList<String>(filterTags));
                            tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                                @Override
                                public void onTagClick(final int position, final String text) {

                                    AlertDialog dialog = new AlertDialog.Builder(AddArticleMaterial.this)
                                            .setTitle("Delete " + text)
                                            .setMessage("Remove " + text + " tag from filter ?")
                                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    tags.removeTag(position);

                                                    filterTags.remove(text);
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }

                                @Override
                                public void onTagLongClick(final int position, String text) {
                                    // ...
                                }
                            });


                        }

                        final RecyclerView rv = (RecyclerView) outdialog.findViewById(R.id.ctag);
                        rv.setLayoutManager(new LinearLayoutManager(AddArticleMaterial.this));
                        final FullTagAdapter tagadapter = new FullTagAdapter(tagList);
                        rv.setAdapter(tagadapter);
                        searchView = (SearchView) outdialog.findViewById(R.id.search_filter);
                        searchView.setVisibility(View.VISIBLE);
                        searchView.setIconifiedByDefault(false);
                        searchView.setSubmitButtonEnabled(false);
                        searchView.setQueryHint("Search Tags");
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                //        Toast.makeText(AddQuestion.this, "Searched for" + newText, Toast.LENGTH_SHORT).show();
                                tagadapter.getFilter().filter(newText);
                                return true;
                            }
                        });
                        rv.setNestedScrollingEnabled(false);
                        outdialog.show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            switch (item.getItemId()) {
                case R.id.done:
                    try {
                        if (input.getText().toString().trim().length() != 0) {

                            if (title.getText().toString().trim().length() != 0) {
                                DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Article").push();
                                Date date = new Date();

                                if (bitmap != null) {
                                    if (MyData.haveNetworkConnection()) {
                                        dialog = new MaterialDialog.Builder(this)
                                                .title("Loading").theme(Theme.LIGHT)
                                                .content("Please Wait")
                                                .progress(true, 0)
                                                .show();
                                        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                uploadPic();
                                            }
                                        });
                                    } else
                                        Toast.makeText(AddArticleMaterial.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                } else {
                                    final Article article = new Article(newref.getKey(), date.getTime(), title.getText().toString(), input.getText().toString(), Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName(), "");
                                    if (MyData.haveNetworkConnection()) {
                                        upload(article);


                                        if (MainActivity.springFloatingActionMenu != null) {
                                            MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                        }

                                    } else
                                        Toast.makeText(AddArticleMaterial.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddArticleMaterial.this, "Please Write Title", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Write Content of Article", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                    }
                    break;

                case R.id.gallery:


                    MyData.verifyStoragePermissions(AddArticleMaterial.this);

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    break;
                case R.id.tags:
                    if (MyData.haveNetworkConnection())
                        fetchTag();
                    else
                        Toast.makeText(AddArticleMaterial.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                    break;

            }
        } catch (Exception e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
        return false;

    }

    public void uploadPic() {
        // Create a storage reference from our app

        //   Toast.makeText(AddArticleMaterial.this, "uploaded pic called", Toast.LENGTH_SHORT).show();
        try {


            final DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Article").push();
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReferenceFromUrl("gs://questo-1f35e.appspot.com");
            // Create a reference to 'images/mountains.jpg'
            StorageReference mountainImagesRef = storageRef.child("Images").child(newref.getKey() + "");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] data = baos.toByteArray();
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

                    Date date = new Date();
                    Article article = null;
                    if (downloadUrl != null) {
                        article = new Article(newref.getKey(), date.getTime(), title.getText().toString(), input.getText().toString(), Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName(), downloadUrl.toString());
                        upload(article);
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();

                        if (MainActivity.springFloatingActionMenu != null) {
                            MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                        }

                        final Article finalArticle = article;
                        if (MainActivity.rootLayout != null) {
                            final Article finalArticle1 = article;
                            Snackbar.make(MainActivity.rootLayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                    .setAction("Show", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (finalArticle1.id != null && finalArticle1.id.trim().length() != 0) {
                                                Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                intent.putExtra("id", finalArticle1.id);
                                                startActivity(intent);
                                            }
                                        }
                                    }).show();
                        }

                        if (TagOpenActivity.rootlayout != null) {
                            final Article finalArticle2 = article;
                            Snackbar.make(TagOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                    .setAction("Show", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (finalArticle2.id != null && finalArticle2.id.trim().length() != 0) {
                                                Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                intent.putExtra("id", finalArticle2.id);
                                                startActivity(intent);
                                            }
                                        }
                                    }).show();
                        }

                        if (PageOpenActivity.rootlayout != null) {
                            final Article finalArticle3 = article;
                            Snackbar.make(PageOpenActivity.rootlayout, "Successfully uploaded article", Snackbar.LENGTH_LONG)
                                    .setAction("Show", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (finalArticle3.id != null && finalArticle3.id.trim().length() != 0) {
                                                Intent intent = new Intent(AddArticleMaterial.this, ArticleOpenActivity.class);
                                                intent.putExtra("id", finalArticle3.id);
                                                startActivity(intent);
                                            }
                                        }
                                    }).show();
                        }
                        // FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
            });
        } catch (Error e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }


    public class FullTagAdapter extends RecyclerView.Adapter<FullTagAdapter.tagholder> implements Filterable {

        public ArrayList<Tag> full;
        ArrayList<Tag> datalist;
        ValueFilter valueFilter;

        public FullTagAdapter(ArrayList<Tag> data) {

            datalist = data;
            full = new ArrayList<>(datalist);
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        @Override
        public tagholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtertagiteem, parent, false);
            return new tagholder(view);
        }

        @Override
        public void onBindViewHolder(final tagholder holder, final int position) {
            if (datalist != null && datalist.get(position % datalist.size()) != null) {
                Tag t = datalist.get(position % datalist.size());
                if (t != null) {
                    if (t.name != null)
                        holder.cc.setText(t.name);

                    if (filterTags != null) {
                        if (filterTags.contains(t.name)) {
                            holder.cc.setChecked(true);
                        } else
                            holder.cc.setChecked(false);
                    }
                    holder.cc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!holder.cc.isChecked()) {
                                holder.cc.setChecked(false);
                                filterTags.remove((datalist.get(position % datalist.size())).name);
                                tagContainerLayout.setTags(new ArrayList<>(filterTags));
                                tags.setTags(new ArrayList<>(filterTags));
                            } else {
                                holder.cc.setChecked(true);
                                filterTags.add((datalist.get(position % datalist.size())).name);
                                tagContainerLayout.setTags(new ArrayList<>(filterTags));
                                tags.setTags(new ArrayList<>(filterTags));
                            }
                        }
                    });

                }

            }

        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                //   if (constraint != null && constraint.length() > 0) {
                LinkedList<Tag> filterList = new LinkedList<>();

                for (int i = 0; i < full.size(); i++) {
                    if ((full.get(i).name.toUpperCase().contains(constraint.toString().toUpperCase()))) {


                        filterList.add(full.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
                // } else {
                //       results.count = dataTrue.size();
                //       results.values = dataTrue;
                //   }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                LinkedList<Tag> linkedList = (LinkedList<Tag>) results.values;
                datalist = new ArrayList<>();

                for (int i = 0; i < linkedList.size(); i++) {
                    datalist.add(linkedList.get(i));
                }

                notifyDataSetChanged();

            }

        }

        class tagholder extends RecyclerView.ViewHolder {

            AppCompatCheckBox cc;

            public tagholder(View itemView) {
                super(itemView);
                cc = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);

            }
        }
    }

}
