package com.tdevelopers.questo.EditStuff;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tdevelopers.questo.Add.AddNewTag;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class EditQuestion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public boolean choice_flag = true;
    private static final int SELECT_PHOTO = 100;
    static RadioButton ch0;
    static RadioButton ch1;
    static RadioButton ch2;
    static RadioButton ch3;
    static AppCompatEditText e0, e1, e2, e3, question;
    static int answer = 0;
    static EditText Explanation;
    static ImageView pic;
    static int count = 0;
    static ImageView close;
    static Bitmap bitmap;
    static FrameLayout fl;
    TagContainerLayout tags;
    TagContainerLayout tagContainerLayout;
    HashSet<String> filterTags = new HashSet<>();
    MaterialDialog outdialog;
    SearchView searchView;
    String id;
    ArrayList<Tag> tagList = new ArrayList<>();
    AutoCompleteTextView auto;
    Question current;
    RelativeLayout choicesrl;

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
        Toast.makeText(getBaseContext(), arg0.getItemAtPosition(arg2) + "",
                Toast.LENGTH_LONG).show();

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

                AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_question, menu);
        return true;

    }

    public void fetchTag() {


        if (tagList != null && tagList.size() > 0) {

            outdialog = new MaterialDialog.Builder(EditQuestion.this)
                    .title("Add Tags")
                    .customView(R.layout.choosetagdialog, true)
                    .positiveText("Ok").negativeText("cancel").neutralText("Add New Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(EditQuestion.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditQuestion.this, AddNewTag.class);

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

                                        AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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
                                Toast.makeText(EditQuestion.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).build();
            tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
            if (filterTags != null) {
                tags.setTags(new ArrayList<String>(filterTags));
                tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                    @Override
                    public void onTagClick(final int position, final String text) {

                        AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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
            rv.setLayoutManager(new LinearLayoutManager(EditQuestion.this));
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


                        outdialog = new MaterialDialog.Builder(EditQuestion.this)
                                .title("Add Tags")
                                .customView(R.layout.choosetagdialog, true)
                                .positiveText("Ok").negativeText("cancel").neutralText("Add Tag").onNeutral(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Toast.makeText(EditQuestion.this, "Add a new tag", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(EditQuestion.this, AddNewTag.class);

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

                                                    AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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
                                            Toast.makeText(EditQuestion.this, "Choosen Tags are" + filterTags.toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).build();
                        tags = (TagContainerLayout) outdialog.findViewById(R.id.tag);
                        if (filterTags != null) {
                            tags.setTags(new ArrayList<String>(filterTags));
                            tags.setOnTagClickListener(new TagView.OnTagClickListener() {

                                @Override
                                public void onTagClick(final int position, final String text) {

                                    AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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
                        rv.setLayoutManager(new LinearLayoutManager(EditQuestion.this));
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
                            pic.setImageBitmap(bitmap);
                            View v = findViewById(R.id.g);
                            if (v != null) {
                                v.setVisibility(View.VISIBLE);
                            }
                        } catch (Error e) {

                            Toast.makeText(EditQuestion.this, "File too big to process", Toast.LENGTH_SHORT).show();

                        }
                    }
            }
        } catch (Exception e) {
            Toast.makeText(EditQuestion.this, "Error occcured :( please try again !", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

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

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    break;
                case R.id.done:


                    if (question.getText().toString().trim().length() != 0) {
                        if (choice_flag) //choices are open
                        {

                            if (e0.getText().toString().length() != 0 && e1.getText().toString().length() != 0 && e2.getText().toString().length() != 0 && e3.getText().toString().length() != 0) {
                                if (bitmap != null) {
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
                                    submit();
                            } else {
                                Toast.makeText(EditQuestion.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                            }
                        } else { //choices are closed
                            if (bitmap != null) {

                                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                                    FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            uploadPic();
                                        }
                                    });
                                else {
                                    uploadPic();
                                }
                            } else
                                submit();
                        }
                    } else {
                        Toast.makeText(EditQuestion.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        else
            Toast.makeText(EditQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        return false;
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
            String c[] = new String[4];
            c[0] = e0.getText().toString();

            c[1] = e1.getText().toString();

            c[2] = e2.getText().toString();

            c[3] = e3.getText().toString();
            {
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Question").child(id);

                    {


                        if (MyData.haveNetworkConnection()) {

                            String explanation = Explanation.getText().toString();
                            if (explanation != null && explanation.trim().length() != 0)
                                ref.child("explanation").setValue(explanation);

                            ref.child("question").setValue(question.getText().toString());

                            if (choice_flag) {
                                if (c[0] != null && c[0].trim().length() != 0)
                                    ref.child("choice0").setValue(c[0]);
                                if (c[1] != null && c[1].trim().length() != 0)
                                    ref.child("choice1").setValue(c[1]);
                                if (c[2] != null && c[2].trim().length() != 0)
                                    ref.child("choice2").setValue(c[2]);
                                if (c[3] != null && c[3].trim().length() != 0)
                                    ref.child("choice3").setValue(c[3]);


                                ref.child("answer").setValue((long) answer);
                            }

                            ref.child("media").setValue(null);


                            if (filterTags != null && filterTags.size() != 0) {


                                for (String s : filterTags) {

                                    if (s != null && s.trim().length() != 0) {

                                        if (current.tags_here != null && current.tags_here.keySet().size() != 0 && current.tags_here.keySet().contains(s)) {

                                            current.tags_here.remove(s);
                                        } else {

                                            FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(true);
                                            final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                            tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Long value;
                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                        value = (Long) dataSnapshot.getValue();

                                                        tagref.setValue(value + 1L);
                                                    } else {

                                                        tagref.setValue(1L);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            ref.child("tags_here").child(s).setValue(true);

                                        }
                                    }
                                }

                                if (current.tags_here != null && current.tags_here.keySet().size() != 0) {
                                    for (String s : current.tags_here.keySet()) {
                                        if (s != null && s.trim().length() != 0) {

                                            ref.child("tags_here").child(s).setValue(null);
                                            FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(null);
                                            final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                            tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Long value;
                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                        value = (Long) dataSnapshot.getValue();

                                                        tagref.setValue(value - 1L);
                                                    } else {

                                                        tagref.setValue(0L);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }

                                }

                            } else {
                                if (current.tags_here != null && current.tags_here.size() != 0) {

                                    for (String s : current.tags_here.keySet()) {
                                        if (s != null && s.trim().length() != 0) {


                                            ref.child("tags_here").child(s).setValue(null);
                                            FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(null);


                                            final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                            tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Long value;
                                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                        value = (Long) dataSnapshot.getValue();

                                                        tagref.setValue(value - 1L);
                                                    } else {

                                                        tagref.setValue(0L);
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


                            finish();
                            Toast.makeText(EditQuestion.this, "Successfully updated", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(EditQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    public void init() {
        auto = (AutoCompleteTextView) findViewById(R.id.auto);
        tagContainerLayout = (TagContainerLayout) findViewById(R.id.tag_container);
        question = (AppCompatEditText) findViewById(R.id.questionadd);
        ch1 = (RadioButton) findViewById(R.id.ch1add);
        ch2 = (RadioButton) findViewById(R.id.ch2add);
        ch3 = (RadioButton) findViewById(R.id.ch3add);
        ch0 = (RadioButton) findViewById(R.id.ch0add);
        Explanation = (EditText) findViewById(R.id.explanationadd);
        e0 = (AppCompatEditText) findViewById(R.id.e0);
        e1 = (AppCompatEditText) findViewById(R.id.e1);
        e2 = (AppCompatEditText) findViewById(R.id.e2);
        e3 = (AppCompatEditText) findViewById(R.id.e3);
        fl = (FrameLayout) findViewById(R.id.fl);
        ch0.setChecked(true);
        ch0.setOnClickListener(this);
        ch1.setOnClickListener(this);
        ch2.setOnClickListener(this);
        ch3.setOnClickListener(this);
        pic = (ImageView) findViewById(R.id.pic);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                fl.setVisibility(View.GONE);

            }
        });
        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(final int position, final String text) {
                // ...
                //Toast.makeText(AddQuestion.this, "Clicked "+text, Toast.LENGTH_SHORT).show();

                AlertDialog dialog = new AlertDialog.Builder(EditQuestion.this)
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
                    ArrayAdapter adapter = new ArrayAdapter<String>(EditQuestion.this, android.R.layout.simple_dropdown_item_1line, item);

                    auto.setThreshold(1);

                    //Set adapter to AutoCompleteTextView
                    auto.setVisibility(View.VISIBLE);
                    auto.setAdapter(adapter);
                    auto.setOnItemSelectedListener(EditQuestion.this);
                    auto.setOnItemClickListener(EditQuestion.this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Edit");
        choicesrl = (RelativeLayout) findViewById(R.id.choices_rl);
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
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("id") != null)
            id = getIntent().getExtras().getString("id");

        if (id != null && id.trim().length() > 0) {

            FirebaseDatabase.getInstance().getReference("Question").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                        current = dataSnapshot.getValue(Question.class);
                        if (current != null) {
                            if (current.choice0 != null && current.choice0.trim().length() != 0)

                            {
                                choicesrl.setVisibility(View.VISIBLE);
                                choice_flag = true;
                            } else {
                                choice_flag = false;
                            }

                            init();

                            if (current.media != null && current.media.trim().length() != 0) {
                                fl.setVisibility(View.VISIBLE);
                                Picasso.with(EditQuestion.this).load(current.media).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        pic.setImageBitmap(bitmap);
                                        EditQuestion.bitmap = bitmap;
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                            }
                            question.setText(current.question);

                            if (choice_flag) {
                                if (current.choice0 != null && current.choice0.trim().length() != 0)
                                    e0.setText(current.choice0);

                                if (current.choice1 != null && current.choice1.trim().length() != 0)
                                    e1.setText(current.choice1);

                                if (current.choice2 != null && current.choice2.trim().length() != 0)
                                    e2.setText(current.choice2);

                                if (current.choice3 != null && current.choice3.trim().length() != 0)
                                    e3.setText(current.choice3);

                                if (current.explanation != null && current.explanation.trim().length() != 0)
                                    Explanation.setText(current.explanation);
                            }
                            if (current.tags_here != null && current.tags_here.size() != 0) {
                                filterTags = new HashSet<String>(current.tags_here.keySet());
                                tagContainerLayout.setVisibility(View.VISIBLE);
                                tagContainerLayout.setTags(new ArrayList<String>(current.tags_here.keySet()));
                            }
                            if (choice_flag && current.answer != null && current.choice0 != null && current.choice0.trim().length() != 0)
                                switch (current.answer) {
                                    case 0:
                                        ch0.setChecked(true);
                                        ch1.setChecked(false);
                                        ch2.setChecked(false);
                                        ch3.setChecked(false);
                                        break;
                                    case 1:

                                        ch0.setChecked(false);
                                        ch1.setChecked(true);
                                        ch2.setChecked(false);
                                        ch3.setChecked(false);
                                        break;
                                    case 2:

                                        ch0.setChecked(false);
                                        ch1.setChecked(false);
                                        ch2.setChecked(true);
                                        ch3.setChecked(false);
                                        break;
                                    case 3:

                                        ch0.setChecked(false);
                                        ch1.setChecked(false);
                                        ch2.setChecked(false);
                                        ch3.setChecked(true);
                                        break;

                                }


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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


                        if (ch0.isChecked()) {
                            answer = 0;

                        } else if (ch1.isChecked()) {
                            answer = 1;

                        } else if (ch2.isChecked()) {
                            answer = 2;

                        } else if (ch3.isChecked()) {
                            answer = 3;
                        }
                        String c[] = new String[4];
                        c[0] = e0.getText().toString();

                        c[1] = e1.getText().toString();

                        c[2] = e2.getText().toString();

                        c[3] = e3.getText().toString();
                        {
                            {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Question").child(id);
                                //   DatabaseReference newref;
                                {


                                    if (MyData.haveNetworkConnection()) {

                                        String explanation = Explanation.getText().toString();
                                        if (explanation != null && explanation.trim().length() != 0)
                                            ref.child("explanation").setValue(explanation);
                                        ref.child("question").setValue(question.getText().toString());

                                        if (choice_flag) {
                                            if (c[0] != null && c[0].trim().length() != 0)
                                                ref.child("choice0").setValue(c[0]);
                                            if (c[1] != null && c[1].trim().length() != 0)
                                                ref.child("choice1").setValue(c[1]);
                                            if (c[2] != null && c[2].trim().length() != 0)
                                                ref.child("choice2").setValue(c[2]);
                                            if (c[3] != null && c[3].trim().length() != 0)
                                                ref.child("choice3").setValue(c[3]);


                                            ref.child("answer").setValue((long) answer);
                                        }

                                        if (filterTags != null && filterTags.size() != 0) {


                                            for (String s : filterTags) {

                                                if (s != null && s.trim().length() != 0) {

                                                    if (current.tags_here != null && current.tags_here.keySet().size() != 0 && current.tags_here.keySet().contains(s)) {

                                                        current.tags_here.remove(s);
                                                    } else {

                                                        FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(true);
                                                        final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                                        tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Long value;
                                                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                    value = (Long) dataSnapshot.getValue();

                                                                    tagref.setValue(value + 1L);
                                                                } else {

                                                                    tagref.setValue(1L);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                        ref.child("tags_here").child(s).setValue(true);

                                                    }
                                                }
                                            }

                                            if (current.tags_here != null && current.tags_here.keySet().size() != 0) {
                                                for (String s : current.tags_here.keySet()) {
                                                    if (s != null && s.trim().length() != 0) {

                                                        ref.child("tags_here").child(s).setValue(null);
                                                        FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(null);
                                                        final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                                        tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Long value;
                                                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                    value = (Long) dataSnapshot.getValue();

                                                                    tagref.setValue(value - 1L);
                                                                } else {

                                                                    tagref.setValue(0L);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }
                                                }

                                            }

                                        } else {
                                            if (current.tags_here != null && current.tags_here.size() != 0) {

                                                for (String s : current.tags_here.keySet()) {
                                                    if (s != null && s.trim().length() != 0) {


                                                        ref.child("tags_here").child(s).setValue(null);
                                                        FirebaseDatabase.getInstance().getReference("TagUploads").child(s).child("Questions").child(id).setValue(null);


                                                        final DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(s).child("question_count");
                                                        tagref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Long value;
                                                                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                    value = (Long) dataSnapshot.getValue();

                                                                    tagref.setValue(value - 1L);
                                                                } else {

                                                                    tagref.setValue(0L);
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


                                        ref.child("media").setValue(downloadUrl.toString());


                                        finish();
                                        Toast.makeText(EditQuestion.this, "Successfully updated", Toast.LENGTH_SHORT).show();

                                    } else
                                        Toast.makeText(EditQuestion.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        //Toast.makeText(AddArticleMaterial.this, "Successfully Uploaded Article", Toast.LENGTH_SHORT).show();
                        //   FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }
            });
        } catch (
                Error e
                )

        {
            FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

        }
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
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

            View itemView;
            AppCompatCheckBox cc;

            public tagholder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                cc = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);

            }
        }
    }


}
