package com.tdevelopers.questo.Add;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.tdevelopers.questo.Adapters.Account_Switch_Adapter;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.Objects.Account;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.Opens.QuestionOpenActivity;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;


public class AddQuestion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final int SELECT_PHOTO = 100;
    static RadioButton ch0;
    static RadioButton ch1;
    static RadioButton ch2;
    static RadioButton ch3;
    static AppCompatEditText e0, e1, e2, e3, question;
    static Integer answer = 0;
    static EditText Explanation;
    static Question p;
    public TextView close_choices;
    public RelativeLayout closerl;
    public boolean choice_flag = true;
    public String account_switch = Profile.getCurrentProfile().getId();
    TagContainerLayout tags;
    TagContainerLayout tagContainerLayout;
    HashSet<String> filterTags = new HashSet<>();
    MaterialDialog outdialog;
    SearchView searchView;
    String id;
    ArrayList<Tag> tagList = new ArrayList<>();
    AutoCompleteTextView auto;
    Bitmap bitmap;
    ImageView adp, close;
    FrameLayout fl;
    AppCompatSpinner account;
    String pageid;

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
                            fl.setVisibility(View.VISIBLE);
                            adp.setImageBitmap(bitmap);
                            View v = findViewById(R.id.g);
                            if (v != null) {
                                v.setVisibility(View.VISIBLE);
                            }
                        } catch (Error e) {

                            Toast.makeText(AddQuestion.this, "File too big to process", Toast.LENGTH_SHORT).show();

                        }
                    }
            }
        } catch (Exception e) {
            Toast.makeText(AddQuestion.this, "Error occcured :( please try again !", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_question, menu);
        return true;

    }

    public void fetchTag() {


        if (tagList != null && tagList.size() > 0) {

            outdialog = new MaterialDialog.Builder(AddQuestion.this)
                    .title("Add Tags")
                    .customView(R.layout.choosetagdialog, true)
                    .positiveText("Ok").negativeText("cancel").neutralText("Add New Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(AddQuestion.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddQuestion.this, AddNewTag.class);

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

                                        AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
                                                .setTitle("Delete " + text)
                                                .setMessage("Remove " + text + " tag from this question ?")
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
                                Toast.makeText(AddQuestion.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).build();
            tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
            if (filterTags != null) {
                tags.setTags(new ArrayList<String>(filterTags));
                tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                    @Override
                    public void onTagClick(final int position, final String text) {

                        AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
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
            rv.setLayoutManager(new LinearLayoutManager(AddQuestion.this));
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


                        outdialog = new MaterialDialog.Builder(AddQuestion.this)
                                .title("Add Tags")
                                .customView(R.layout.choosetagdialog, true)
                                .positiveText("Ok").negativeText("cancel").neutralText("Add Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Toast.makeText(AddQuestion.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(AddQuestion.this, AddNewTag.class);

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

                                                    AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
                                                            .setTitle("Delete " + text)
                                                            .setMessage("Remove " + text + " tag from this question ?")
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
                                            Toast.makeText(AddQuestion.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).build();
                        tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
                        if (filterTags != null) {
                            tags.setTags(new ArrayList<String>(filterTags));
                            tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                                @Override
                                public void onTagClick(final int position, final String text) {

                                    AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
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
                        rv.setLayoutManager(new LinearLayoutManager(AddQuestion.this));
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

        if (MyData.haveNetworkConnection())
            switch (item.getItemId()) {
                case R.id.tags:
                    fetchTag();
                    break;

                case R.id.gallery:


                    MyData.verifyStoragePermissions(AddQuestion.this);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    break;
                case R.id.done:
                    //   case R.id.donetext:

                    if (question.getText().toString().trim().length() != 0) {
                        if (choice_flag) //choices are open
                        {

                            if (e0.getText().toString().length() != 0 && e1.getText().toString().length() != 0 && e2.getText().toString().length() != 0 && e3.getText().toString().length() != 0) {
                                submit();
                            } else {
                                Toast.makeText(AddQuestion.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                            }
                        } else { //choices are closed
                            submit();
                        }
                    } else {
                        Toast.makeText(AddQuestion.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();

                    }

                    break;
            }
        else
            Toast.makeText(AddQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void upload(final Question question) {
        final LinkedHashMap<String, String> q = new LinkedHashMap<>();

        if (account_switch.equals(Profile.getCurrentProfile().getId())) {
            DatabaseReference userr = FirebaseDatabase.getInstance().getReference("uploads").child(Profile.getCurrentProfile().getId()).child("question_uploads");
            userr.child(question.id).setValue(true);
        } else {
            DatabaseReference userr = FirebaseDatabase.getInstance().getReference("pageuploads").child(account_switch).child("question_uploads");
            userr.child(question.id).setValue(true);
        }

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Question").child(question.id);
        if (question.explanation != null && question.explanation.trim().length() != 0)
            q.put("explanation", question.explanation);

        if (question.media != null && question.media.trim().length() != 0)
            q.put("media", question.media);


        q.put("question", question.question);

        if (choice_flag) {
            if (question.choice0 != null && question.choice0.trim().length() != 0)
                q.put("choice0", question.choice0);

            if (question.choice1 != null && question.choice1.trim().length() != 0)
                q.put("choice1", question.choice1);

            if (question.choice2 != null && question.choice2.trim().length() != 0)
                q.put("choice2", question.choice2);

            if (question.choice3 != null && question.choice3.trim().length() != 0)
                q.put("choice3", question.choice3);

        }


        q.put("uploaded_by", question.uploaded_by);
        q.put("username", question.username);
        q.put("id", question.id);
        ref.setValue(q);


        ref.child("likes_count").setValue(0L);
        ref.child("viewcount").setValue(0L);
        ref.child("date").setValue(question.date);
        ref.child("comment_count").setValue(0L);


        if (choice_flag) {


            try {
                answer = 0;

                if (ch0.isChecked()) {
                    answer = 0;

                } else if (ch1.isChecked()) {
                    answer = 1;

                } else if (ch2.isChecked()) {
                    answer = 2;

                } else if (ch3.isChecked()) {
                    answer = 3;
                }

                ref.child("answer").setValue(answer);


            } catch (Exception e) {

            }
        }


        if (!Profile.getCurrentProfile().getId().equals(account_switch))

        {


            FirebaseDatabase.getInstance().getReference("Page").child(account_switch).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ref.child("pagepic").setValue(dataSnapshot.getValue() + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        DatabaseReference tagref;
        ArrayList<String> tagsdata = new ArrayList<>();
        if (filterTags != null && filterTags.size() != 0) {
            for (String s : filterTags) {
                if (s != null) {
                    ref.child("tags_here").child(s + "").setValue(true);
                    tagsdata.add(s);
                }
                tagref = FirebaseDatabase.getInstance().getReference("TagUploads").child(s + "").child("Questions").child(question.id);
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
                            final String key = entry.getKey();
                            // String value = entry.getValue();
                            FirebaseDatabase.getInstance().getReference("myUsers").child(key + "").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {


                                        String link = "Question:" + question.id;
                                        MyData.pushNotification(s, "added question to tag " + s + "\n\n" + question.question, (String) dataSnapshot.getValue(), link, key);
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

        if (account_switch.equals(Profile.getCurrentProfile().getId()))
            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                    for (final String s : data.keySet())

                        FirebaseDatabase.getInstance().getReference("myUsers").child(s + "").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {


                                    String link = "Question:" + question.id;
                                    MyData.pushNotification(Profile.getCurrentProfile().getName(), "added question\n\n" + question.question, (String) dataSnapshot.getValue(), link, s);
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
        else

        {

            FirebaseDatabase.getInstance().getReference("Page").child(account_switch).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String pagepic = (String) dataSnapshot.getValue();

                    FirebaseDatabase.getInstance().getReference("PageFollowers").child(account_switch).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                            for (final String s : data.keySet())
                                FirebaseDatabase.getInstance().getReference("myUsers").child(s + "").child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {


                                            final String link = "Question:" + question.id;
                                            MyData.pushNotification(account_name, "added question\n\n" + question.question, (String) dataSnapshot.getValue(), link, s, account_name, account_switch, pagepic);

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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        ref.setPriority(0 - question.date);


        FirebaseDatabase.getInstance().getReference("questions_count").runTransaction(new Transaction.Handler() {
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

        if (account_switch.equals(Profile.getCurrentProfile().getId()))
            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("question_count").runTransaction(new Transaction.Handler() {
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
        else {
            FirebaseDatabase.getInstance().getReference("Page").child(account_switch).child("question_count").runTransaction(new Transaction.Handler() {
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

        for (String s : tagsdata) {

            FirebaseDatabase.getInstance().getReference("Tag").child(s + "").child("question_count").runTransaction(new Transaction.Handler() {
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


        if (MainActivity.springFloatingActionMenu != null) {
            MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
        }


        try {
            if (MainActivity.rootLayout != null)
                Snackbar.make(MainActivity.rootLayout, "Successfully uploaded question", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (question.id != null && question.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddQuestion.this, QuestionOpenActivity.class);
                                    intent.putExtra("id", question.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();


            if (TagOpenActivity.rootlayout != null)
                Snackbar.make(TagOpenActivity.rootlayout, "Successfully uploaded question", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (question.id != null && question.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddQuestion.this, QuestionOpenActivity.class);
                                    intent.putExtra("id", question.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();


            if (PageOpenActivity.rootlayout != null)
                Snackbar.make(PageOpenActivity.rootlayout, "Successfully uploaded question", Snackbar.LENGTH_LONG)
                        .setAction("Show", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (question.id != null && question.id.trim().length() != 0) {
                                    Intent intent = new Intent(AddQuestion.this, QuestionOpenActivity.class);
                                    intent.putExtra("id", question.id);
                                    startActivity(intent);
                                }
                            }
                        }).show();
        } catch (Exception e) {

        }

        finish();

    }

    public void init() {
        account = (AppCompatSpinner) findViewById(R.id.account);
        tagContainerLayout = (TagContainerLayout) findViewById(R.id.tag_container);
        question = (AppCompatEditText) findViewById(R.id.questionadd);
        ch1 = (RadioButton) findViewById(R.id.ch1add);
        closerl = (RelativeLayout) findViewById(R.id.closerl);
        ch2 = (RadioButton) findViewById(R.id.ch2add);
        ch3 = (RadioButton) findViewById(R.id.ch3add);
        ch0 = (RadioButton) findViewById(R.id.ch0add);
        close = (ImageView) findViewById(R.id.close);
        close_choices = (TextView) findViewById(R.id.close_choices);
        close_choices.setOnClickListener(this);
        adp = (ImageView) findViewById(R.id.adp);
        Explanation = (EditText) findViewById(R.id.explanationadd);
        e0 = (AppCompatEditText) findViewById(R.id.e0);
        e1 = (AppCompatEditText) findViewById(R.id.e1);
        e2 = (AppCompatEditText) findViewById(R.id.e2);
        e3 = (AppCompatEditText) findViewById(R.id.e3);
        fl = (FrameLayout) findViewById(R.id.fl);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                fl.setVisibility(View.GONE);
            }
        });
        // b = (FloatingActionButton) findViewById(R.id.submit);
        // collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        if (MyData.getTags() != null && MyData.getTags().size() != 0) {
            tagContainerLayout.setVisibility(View.VISIBLE);
            filterTags.addAll(MyData.getTags());
            tagContainerLayout.setTags(new ArrayList<String>(filterTags));
            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

                @Override
                public void onTagClick(final int position, final String text) {
                    // ...
                    //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                    AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
                            .setTitle("Delete " + text)
                            .setMessage("Remove " + text + " tag from this question ")
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


            Toast.makeText(AddQuestion.this, "Tags Included " + filterTags, Toast.LENGTH_SHORT).show();

            MyData.setTags(new HashSet<String>());


        }


    }

    public String account_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        auto = (AutoCompleteTextView) findViewById(R.id.auto);
        setTitle("Add Question");
        account_switch = Profile.getCurrentProfile().getId();
        account_name = Profile.getCurrentProfile().getName();
        setSupportActionBar((toolbar));
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("pageid") != null) {

            pageid = getIntent().getExtras().getString("pageid");
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("id")!=null) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }

        init();


        FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    TextView a = (TextView) findViewById(R.id.aheader);
                    a.setVisibility(View.VISIBLE);
                    account.setVisibility(View.VISIBLE);
                    HashMap<String, String> page_data = (HashMap<String, String>) dataSnapshot.getValue();

                    final ArrayList<Account> data = new ArrayList<Account>();

                    for (Map.Entry<String, String> m : page_data.entrySet()) {

                        data.add(new Account(m.getKey(), m.getValue()));
                    }

                    data.add(new Account(Profile.getCurrentProfile().getId(), Profile.getCurrentProfile().getName()));

                    Account_Switch_Adapter dataAdapter = new Account_Switch_Adapter(AddQuestion.this, R.layout.page_item, data);

                    account.setAdapter(dataAdapter);


                    try {


                        if (pageid != null && pageid.trim().length() != 0) {

                            int index=data.size()-1;
                            Account account1;
                            for(int i=0;i<data.size();i++)
                            {
                                account1=data.get(i);
                                if(account1.id.equals(pageid))
                                {
                                    index=i;
                                    break;
                                }

                            }
                            account.setSelection(index);

                            account_switch = data.get(index % data.size()).id;
                            account_name = data.get(index % data.size()).name;
                        } else

                            account.setSelection(data.size() - 1);
                    } catch (Exception e) {

                    }
                    account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            account_switch = data.get(position % data.size()).id;
                            account_name = data.get(position % data.size()).name;

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ch0.setChecked(true);
        ch0.setOnClickListener(this);
        ch1.setOnClickListener(this);
        ch2.setOnClickListener(this);
        ch3.setOnClickListener(this);
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
                    ArrayAdapter adapter = new ArrayAdapter<String>(AddQuestion.this, android.R.layout.simple_dropdown_item_1line, item);

                    auto.setThreshold(1);

                    auto.setAdapter(adapter);
                    auto.setOnItemSelectedListener(AddQuestion.this);
                    auto.setOnItemClickListener(AddQuestion.this);
                    auto.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void submit() {

        {

            if (ch0.isChecked()) {
                answer = 0;

            } else if (ch1.isChecked()) {
                answer = 1;

            } else if (ch2.isChecked()) {
                answer = 2;

            } else if (ch3.isChecked()) {
                answer = 3;
            }
            final String c[] = new String[4];
            c[0] = e0.getText().toString();

            c[1] = e1.getText().toString();

            c[2] = e2.getText().toString();

            c[3] = e3.getText().toString();

            if (bitmap == null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Question");
                final DatabaseReference newref;
                {
                    newref = ref.push();

                    final Date date = new Date();


                    p = new Question(newref.getKey(), date.getTime(), account_name, question.getText().toString(), Explanation.getText().toString(), c[0], c[1], c[2], c[3], answer, account_switch, null);

                    if (MyData.haveNetworkConnection())


                        upload(p);
                    else
                        Toast.makeText(AddQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                }
            } else if (bitmap != null) {

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

    public void uploadPic() {
        // Create a storage reference from our app

        //   Toast.makeText(AddArticleMaterial.this, "uploaded pic called", Toast.LENGTH_SHORT).show();
        try {


            final MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Loading").theme(Theme.LIGHT)
                    .content("Please Wait")
                    .progress(true, 0)
                    .show();

            final DatabaseReference newref = FirebaseDatabase.getInstance().getReference("Question").push();
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReferenceFromUrl("gs://questo-1f35e.appspot.com");
            // Create a reference to 'images/mountains.jpg'
            StorageReference mountainImagesRef = storageRef.child("QuestionImages").child(newref.getKey() + "");
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

                    if (downloadUrl != null) {


                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Question");
                        DatabaseReference newref;
                        {
                            newref = ref.push();
                            String c[] = new String[4];
                            c[0] = e0.getText().toString();

                            c[1] = e1.getText().toString();

                            c[2] = e2.getText().toString();

                            c[3] = e3.getText().toString();

                            Date date = new Date();
                            p = new Question(newref.getKey(), date.getTime(), account_name, question.getText().toString(), Explanation.getText().toString(), c[0], c[1], c[2], c[3], answer, account_switch, downloadUrl.toString());
                            if (MyData.haveNetworkConnection())
                                upload(p);
                            else
                                Toast.makeText(AddQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                        }

                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        //Toast.makeText(AddArticleMaterial.this, "Successfully Uploaded Article", Toast.LENGTH_SHORT).show();
                        //  FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
            });
        } catch (Error e) {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.close_choices) {

            if (!choice_flag) {
                choice_flag = true;
                close_choices.setText("Remove Choices");
                closerl.setVisibility(View.VISIBLE);
            } else {


                choice_flag = false;
                close_choices.setText("Add Choices");
                closerl.setVisibility(View.GONE);
            }
        }
        if (id == R.id.ch0add)

        {
            if (((RadioButton) view).isChecked()) {
                ch1.setChecked(false);

                ch2.setChecked(false);

                ch3.setChecked(false);
                ((RadioButton) view).setChecked(true);
            } else {
                ch1.setChecked(false);

                ch2.setChecked(false);

                ch3.setChecked(false);

                ((RadioButton) view).setChecked(true);

            }

        } else if (id == R.id.ch1add)

        {
            if (((RadioButton) view).isChecked()) {
                ch0.setChecked(false);

                ch2.setChecked(false);

                ch3.setChecked(false);

                ((RadioButton) view).setChecked(true);

            } else {
                ch0.setChecked(false);

                ch2.setChecked(false);

                ch3.setChecked(false);

                ((RadioButton) view).setChecked(true);

            }

        } else if (id == R.id.ch2add)

        {
            if (((RadioButton) view).isChecked()) {

                ch1.setChecked(false);

                ch0.setChecked(false);

                ch3.setChecked(false);

                ((RadioButton) view).setChecked(true);
            } else {
                ch1.setChecked(false);

                ch0.setChecked(false);

                ch3.setChecked(false);

                ((RadioButton) view).setChecked(true);

            }

        } else if (id == R.id.ch3add)

        {

            if (((RadioButton) view).isChecked()) {

                ch1.setChecked(false);

                ch2.setChecked(false);

                ch0.setChecked(false);

                ((RadioButton) view).setChecked(true);
            } else {
                ch1.setChecked(false);

                ch2.setChecked(false);

                ch0.setChecked(false);

                ((RadioButton) view).setChecked(true);

            }
        }


    }

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
        // TODO Auto-generated method stub

        // Show Alert

        // Log.d("AutocompleteContacts", "Position:" + arg2 + " Month:" + arg0.getItemAtPosition(arg2));

        String s = arg0.getItemAtPosition(arg2).toString();
        filterTags.add(s);
//        tags.setTags(new ArrayList<String>(filterTags));
        tagContainerLayout.setVisibility(View.VISIBLE);
        tagContainerLayout.setTags(new ArrayList<String>(filterTags));
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(final int position, final String text) {
                // ...
                //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                AlertDialog dialog = new AlertDialog.Builder(AddQuestion.this)
                        .setTitle("Delete " + text)
                        .setMessage("Remove " + text + " tag from this question ?")
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




