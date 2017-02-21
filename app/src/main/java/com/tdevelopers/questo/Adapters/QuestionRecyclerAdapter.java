package com.tdevelopers.questo.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;
import com.facebook.Profile;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.EditStuff.EditQuestion;
import com.tdevelopers.questo.Favourites.Favorite_Activity;
import com.tdevelopers.questo.HomeScreens.MainActivity;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Question;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.Opens.QuestionOpenActivity;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.Reactions.Reactions_Activity;
import com.tdevelopers.questo.User.User;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.ArrayList;
import java.util.HashMap;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.CLIPBOARD_SERVICE;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TAG = 0;
    private static final int VIEW_TYPE_NO_TAG = 1;
    private static final int VIEW_TYPE_TAG_IMAGE = 3;
    private static final int VIEW_TYPE_NO_TAG_IMAGE = 2;
    private static final int VIEW_TYPE_SUPER_TAG = 4;
    private static final int VIEW_TYPE_SUPER_NO_TAG = 5;
    private static final int VIEW_TYPE_SUPER_NO_TAG_IMAGE = 7;
    private static final int VIEW_TYPE_SUPER_TAG_IMAGE = 6;
    ArrayList<Question> data;
    Context context;

    public QuestionRecyclerAdapter(ArrayList<Question> recieved) {
        data = recieved;
    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position).choice0 != null && data.get(position).choice0.trim().length() != 0) {
            if (data.get(position).media != null && data.get(position).media.trim().length() != 0) {
                if (data.get(position).tags_here != null)
                    return VIEW_TYPE_TAG_IMAGE;
                else
                    return VIEW_TYPE_NO_TAG_IMAGE;
            } else {
                if (data.get(position).tags_here != null)
                    return VIEW_TYPE_TAG;
                else
                    return VIEW_TYPE_NO_TAG;
            }
        } else //  no choice
        {
            if (data.get(position).media != null && data.get(position).media.trim().length() != 0) {
                if (data.get(position).tags_here != null)
                    return VIEW_TYPE_SUPER_TAG_IMAGE;
                else
                    return VIEW_TYPE_SUPER_NO_TAG_IMAGE;
            } else {
                if (data.get(position).tags_here != null)
                    return VIEW_TYPE_SUPER_TAG;
                else
                    return VIEW_TYPE_SUPER_NO_TAG;
            }


        }
    }

    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_TAG)

        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lquestion, parent, false);
            context = view.getContext();

            return new ViewHolder(view);

        } else if (viewType == VIEW_TYPE_NO_TAG) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notaglquestion, parent, false);
            context = view.getContext();

            return new ParentViewHolder(view);
        } else if (viewType == VIEW_TYPE_TAG_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imagetagquestion, parent, false);
            context = view.getContext();

            return new ImageTagHolder(view);
        } else if (viewType == VIEW_TYPE_NO_TAG_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imagenotagquestion, parent, false);
            context = view.getContext();

            return new ImageNoTagHolder(view);
        } else if (viewType == VIEW_TYPE_SUPER_TAG) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.super_parent_tag, parent, false);
            context = view.getContext();

            return new SuperParentTag(view);
        } else if (viewType == VIEW_TYPE_SUPER_NO_TAG) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.super_parent_no_tag, parent, false);
            context = view.getContext();

            return new SuperParent(view);
        } else if (viewType == VIEW_TYPE_SUPER_TAG_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.super_parent_tag_image, parent, false);
            context = view.getContext();

            return new SuperParentImageTagHolder(view);
        } else if (viewType == VIEW_TYPE_SUPER_NO_TAG_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.super_parent_no_tag_image, parent, false);
            context = view.getContext();

            return new SuperParentImageNoTagHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.super_parent_no_tag, parent, false);
            context = view.getContext();
            return new ParentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder out, int position) {

        try {

            final Question item = data.get(position);
            if (item != null) {

                if (out instanceof SuperParent) {

                    final SuperParent holder = (SuperParent) out;


                    FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("likes_count").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                holder.likecount.setText(((Long) dataSnapshot.getValue()) * -1 + "");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("comment_count").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                holder.commentcount.setText(dataSnapshot.getValue() + "");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("viewcount").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                holder.viewcount.setText((long) dataSnapshot.getValue() * -1 + "");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (Profile.getCurrentProfile() != null) {
                        FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, Boolean> r = new HashMap<String, Boolean>();
                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                    r = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                if (r.containsKey(item.id))
                                    holder.like.setLiked(true);
                                else
                                    holder.like.setLiked(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Questions").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, Boolean> r = new HashMap<String, Boolean>();
                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                    r = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                if (r.containsKey(item.id))
                                    holder.favourite.setLiked(true);
                                else
                                    holder.favourite.setLiked(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    String urlImage;
                    holder.username.setText(item.username);
                    if (item.pagepic != null && item.pagepic.trim().length() != 0) {


                        Picasso.with(context).load(item.pagepic).into(holder.userdp);
                    } else {

                        if (item.uploaded_by.matches("\\d*")) {
                            urlImage = "https://graph.facebook.com/" + item.uploaded_by + "/picture?type=large";


                            Picasso.with(context)
                                    .load(urlImage)
                                    .fit()
                                    .transform(PaletteTransformation.instance())
                                    .into(holder.userdp, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {


                                        }
                                    });
                        } else {

                            FirebaseDatabase.getInstance().getReference("Page").child(item.uploaded_by).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                        Picasso.with(context)
                                                .load(dataSnapshot.getValue().toString())
                                                .fit()
                                                .transform(PaletteTransformation.instance())
                                                .into(holder.userdp, new Callback.EmptyCallback() {
                                                    @Override
                                                    public void onSuccess() {


                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                    }


                    holder.likecount.setText(-1 * item.likes_count + "");
                    holder.commentcount.setText(item.comment_count + "");
                    holder.viewcount.setText(-1 * item.viewcount + "");
                    holder.question.setText(item.question);


                }
                if (out instanceof SuperParentTag) {

                    SuperParentTag holder = (SuperParentTag) out;
                    ArrayList<String> lister = new ArrayList<>();

                    if (item.tags_here != null) {
                        for (Object key : item.tags_here.keySet()) {
                            lister.add(key.toString());
                            holder.chipView.setTags(lister);
                        }
                    }
                }

                if (out instanceof SuperParentImageTagHolder) {


                    SuperParentImageTagHolder holder = (SuperParentImageTagHolder) out;

                    if (item.media != null && item.media.trim().length() != 0)
                        Picasso.with(context).load(item.media).into(holder.pic);


                }
                if (out instanceof SuperParentImageNoTagHolder) {
                    SuperParentImageNoTagHolder holder = (SuperParentImageNoTagHolder) out;


                    if (item.media != null && item.media.trim().length() != 0)
                        Picasso.with(context).load(item.media).into(holder.pic);

                }


                if (out instanceof ParentViewHolder) {


                    final ParentViewHolder holder = (ParentViewHolder) out;
                    holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.r[0].setTextColor(context.getResources().getColor(R.color.black));
                    holder.r[1].setTextColor(context.getResources().getColor(R.color.black));
                    holder.r[2].setTextColor(context.getResources().getColor(R.color.black));
                    holder.r[3].setTextColor(context.getResources().getColor(R.color.black));
                    holder.r[0].setTypeface(null, Typeface.NORMAL);
                    holder.r[1].setTypeface(null, Typeface.NORMAL);
                    holder.r[2].setTypeface(null, Typeface.NORMAL);
                    holder.r[3].setTypeface(null, Typeface.NORMAL);
                    holder.llExpandArea.setExpanded(false);
                    if (holder.llExpandArea.isExpanded())
                        holder.expand.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                    else
                        holder.expand.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);


                    holder.r[0].setText(item.choice0);
                    holder.r[1].setText(item.choice1);
                    holder.r[2].setText(item.choice2);
                    holder.r[3].setText(item.choice3);

                }


                if (out instanceof ViewHolder) {
                    ViewHolder holder = (ViewHolder) out;
                    ArrayList<String> lister = new ArrayList<>();

                    if (item.tags_here != null) {
                        for (Object key : item.tags_here.keySet()) {
                            lister.add(key.toString());
                            holder.chipView.setTags(lister);
                        }


                    }
                }
                if (out instanceof ImageTagHolder) {

                    ImageTagHolder holder = (ImageTagHolder) out;

                    if (item.media != null && item.media.trim().length() != 0) {
                        Picasso.with(context).load(item.media).into(holder.pic);
                    }

                } else if (out instanceof ImageNoTagHolder) {
                    ImageNoTagHolder holder = (ImageNoTagHolder) out;
                    if (item.media != null && item.media.trim().length() != 0) {
                        Picasso.with(context).load(item.media).into(holder.pic);
                    }

                }


            }


        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {

        return data != null ? data.size() : 0;
    }

    private BottomSheet.Builder getShareActions(String text) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        return BottomSheetHelper.shareAction((Activity) context, shareIntent);
    }


    public class ParentViewHolder extends SuperParent implements View.OnClickListener {

        public TextView r[] = new TextView[4];
        public ImageView expand;

        public ExpandableLinearLayout llExpandArea;


        public ParentViewHolder(View itemView) {
            super(itemView);
            try {
                expand = (ImageView) itemView.findViewById(R.id.expand);
                r[0] = (TextView) itemView.findViewById(R.id.ch1);
                r[1] = (TextView) itemView.findViewById(R.id.ch2);
                r[2] = (TextView) itemView.findViewById(R.id.ch3);
                r[3] = (TextView) itemView.findViewById(R.id.ch4);
                llExpandArea = (ExpandableLinearLayout) itemView.findViewById(R.id.ll);
                expand.setOnClickListener(this);
                r[0].setOnClickListener(this);
                r[1].setOnClickListener(this);
                r[2].setOnClickListener(this);
                r[3].setOnClickListener(this);
            } catch (Exception e) {

            }

        }

        @Override
        public void onClick(final View v) {

            try {
                Intent intent;
                ParentViewHolder holder = this;
                final Question item = data.get(getLayoutPosition());
                switch (v.getId()) {

                    case R.id.ll1:

                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(0);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.ll2:

                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(1);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.ll3:
                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(2);
                        v.getContext().startActivity(intent);
                        break;

                    case R.id.username:
                    case R.id.userdp:
                        if (data.get(getLayoutPosition()).uploaded_by.matches("\\d*")) {
                            intent = new Intent(v.getContext(), User.class);

                        } else {
                            intent = new Intent(v.getContext(), PageOpenActivity.class);
                        }
                        intent.putExtra("id", data.get(getLayoutPosition()).uploaded_by);
                        v.getContext().startActivity(intent);
                        break;

                    case R.id.menu:


                        final Question q = data.get(getLayoutPosition());


                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        popup.inflate(R.menu.question_item_menu_owner);
                        popup.setOnMenuItemClickListener(ParentViewHolder.this);
                        popup.show();

                        final MenuItem edit = popup.getMenu().findItem(R.id.edit);

                        if (Profile.getCurrentProfile() != null && q.uploaded_by.equals(Profile.getCurrentProfile().getId())) {

                            edit.setVisible(true);
                        } else {
                            if (Profile.getCurrentProfile() != null)
                                FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                            HashMap<String, String> data = (HashMap<String, String>) dataSnapshot.getValue();

                                            if (data != null && data.keySet().contains(q.uploaded_by)) {
                                                edit.setVisible(true);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                        }

                        break;


                    case R.id.pic:
                    case R.id.rll:
                    case R.id.contentquestion:

                        intent = new Intent(v.getContext(), QuestionOpenActivity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        v.getContext().startActivity(intent);
                        break;

                    case R.id.expand:
                        holder.llExpandArea.toggle();

                        if (Profile.getCurrentProfile() != null && item.uploaded_by.equals(Profile.getCurrentProfile().getId()))
                            if (item.answer != null)
                                switch (item.answer) {
                                    case 0:
                                        holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[0].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[0].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 1:

                                        holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[1].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[1].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 2:

                                        holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[2].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[2].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 3:

                                        holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[3].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[3].setTypeface(null, Typeface.BOLD);
                                        break;
                                    default:
                                        break;
                                }

                        if (!holder.llExpandArea.isExpanded()) {
                            if (MainActivity.appBarLayout != null) {
                                MainActivity.appBarLayout.setExpanded(false, true);

                                if (MainActivity.springFloatingActionMenu != null) {
                                    MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                }
                            }

                            holder.expand.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);


                        } else {

                            if (MainActivity.springFloatingActionMenu != null) {
                                MainActivity.springFloatingActionMenu.setVisibility(View.VISIBLE);
                            }
                            holder.expand.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        }
                        break;
                    case R.id.ch1:
                    case R.id.ch2:
                    case R.id.ch3:
                    case R.id.ch4:
                        if (Profile.getCurrentProfile() != null) {
                            int id = v.getId();
                            int selected = 0;
                            if (id == R.id.ch1) {
                                selected = 0;
                            } else if (id == R.id.ch2) {
                                selected = 1;

                            } else if (id == R.id.ch3) {
                                selected = 2;

                            } else if (id == R.id.ch4) {
                                selected = 3;

                            }

                            try {
                                int flag = 0;
                                holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.r[0].setTextColor(context.getResources().getColor(R.color.black));
                                holder.r[1].setTextColor(context.getResources().getColor(R.color.black));
                                holder.r[2].setTextColor(context.getResources().getColor(R.color.black));
                                holder.r[3].setTextColor(context.getResources().getColor(R.color.black));
                                holder.r[0].setTypeface(null, Typeface.NORMAL);
                                holder.r[1].setTypeface(null, Typeface.NORMAL);
                                holder.r[2].setTypeface(null, Typeface.NORMAL);
                                holder.r[3].setTypeface(null, Typeface.NORMAL);
                                if (selected == item.answer) {
                                    flag = 1;
                                }
                                final int f = flag; //flag will be 1 for correctly answered case
                                if (f != 1) {

                                    holder.r[selected].setBackgroundColor(context.getResources().getColor(R.color.red));
                                    holder.r[selected].setTextColor(context.getResources().getColor(R.color.white));
                                    holder.r[selected].setTypeface(null, Typeface.BOLD);
                                }
                                switch (item.answer) {
                                    case 0:
                                        holder.r[0].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[0].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[0].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 1:
                                        holder.r[1].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[1].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[1].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 2:


                                        holder.r[2].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[2].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[2].setTypeface(null, Typeface.BOLD);
                                        break;
                                    case 3:


                                        holder.r[3].setBackgroundColor(context.getResources().getColor(R.color.green));
                                        holder.r[3].setTextColor(context.getResources().getColor(R.color.white));
                                        holder.r[3].setTypeface(null, Typeface.BOLD);
                                        break;
                                    default:
                                        break;
                                }

                                if (!item.uploaded_by.equals(Profile.getCurrentProfile().getId())) {
                                    if (MyData.haveNetworkConnection()) {

                                        final DatabaseReference ownerref = FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("score");
                                        final DatabaseReference userref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("score");
                                        final DatabaseReference a = FirebaseDatabase.getInstance().getReference("Attempts").child(item.id);
                                        a.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                             @Override
                                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                 try {
                                                                                     HashMap<String, Boolean> attempts = new HashMap<String, Boolean>();
                                                                                     if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                                                         attempts = (HashMap<String, Boolean>) dataSnapshot.getValue();

                                                                                     if (!attempts.containsKey(Profile.getCurrentProfile().getId())) {

                                                                                         if (f == 1) {

                                                                                             if (item.tags_here != null && item.tags_here.size() != 0) {
                                                                                                 DatabaseReference useranal = FirebaseDatabase.getInstance().getReference("user_analysis").child(Profile.getCurrentProfile().getId());
                                                                                                 for (String s : item.tags_here.keySet()) {
                                                                                                     useranal.child(s).runTransaction(new Transaction.Handler() {
                                                                                                         @Override
                                                                                                         public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                             try {
                                                                                                                 Long p = mutableData.getValue(Long.class);
                                                                                                                 if (p == null) {
                                                                                                                     p = 1L;
                                                                                                                 } else
                                                                                                                     p += 1L;
                                                                                                                 // Set value and report transaction success
                                                                                                                 mutableData.setValue(p);
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

                                                                                                             }
                                                                                                             return Transaction.success(mutableData);
                                                                                                         }

                                                                                                         @Override
                                                                                                         public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                                DataSnapshot dataSnapshot) {
                                                                                                             // Transaction completed
                                                                                                             try {

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

                                                                                                             }
                                                                                                         }
                                                                                                     });
                                                                                                 }

                                                                                                 DatabaseReference taganal;
                                                                                                 for (String k : item.tags_here.keySet()) {
                                                                                                     taganal = FirebaseDatabase.getInstance().getReference("tag_analysis").child(k).child(Profile.getCurrentProfile().getId());
                                                                                                     taganal.child("id").setValue(Profile.getCurrentProfile().getId());
                                                                                                     taganal.child("score").runTransaction(new Transaction.Handler() {
                                                                                                         @Override
                                                                                                         public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                             try {
                                                                                                                 Long p = mutableData.getValue(Long.class);
                                                                                                                 if (p == null) {
                                                                                                                     p = -1L;
                                                                                                                 } else
                                                                                                                     p -= 1L;
                                                                                                                 // Set value and report transaction success
                                                                                                                 mutableData.setValue(p);
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

                                                                                                             }
                                                                                                             return Transaction.success(mutableData);
                                                                                                         }

                                                                                                         @Override
                                                                                                         public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                                DataSnapshot dataSnapshot) {
                                                                                                             // Transaction completed
                                                                                                             try {

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

                                                                                                             }
                                                                                                         }
                                                                                                     });

                                                                                                 }

                                                                                             }
                                                                                             item.buildRef().child("viewcount").runTransaction(new Transaction.Handler() {
                                                                                                 @Override
                                                                                                 public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                     try {
                                                                                                         Long p = mutableData.getValue(Long.class);
                                                                                                         if (p == null) {
                                                                                                             p = -1L;
                                                                                                         } else p -= 1L;
                                                                                                         // Set value and report transaction success
                                                                                                         mutableData.setValue(p);
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

                                                                                                     }
                                                                                                     return Transaction.success(mutableData);
                                                                                                 }

                                                                                                 @Override
                                                                                                 public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                        DataSnapshot dataSnapshot) {
                                                                                                     // Transaction completed
                                                                                                     try {

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

                                                                                                     }
                                                                                                 }
                                                                                             });

                                                                                             a.child(Profile.getCurrentProfile().getId()).setValue(true);

                                                                                             userref.runTransaction(new Transaction.Handler() {
                                                                                                 @Override
                                                                                                 public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                     try {


                                                                                                         Long p = mutableData.getValue(Long.class);
                                                                                                         if (p == null) {
                                                                                                             p = -1L;
                                                                                                         } else p -= 1L;
                                                                                                         // Set value and report transaction success
                                                                                                         mutableData.setValue(p);
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

                                                                                                     }
                                                                                                     return Transaction.success(mutableData);
                                                                                                 }

                                                                                                 @Override
                                                                                                 public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                        DataSnapshot dataSnapshot) {
                                                                                                     // Transaction completed
                                                                                                     try {
                                                                                                         if (databaseError == null)
                                                                                                             Toast.makeText(v.getContext(), "Score Updated " + (Long) dataSnapshot.getValue() * -1L, Toast.LENGTH_SHORT).show();
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

                                                                                                     }
                                                                                                 }
                                                                                             });


                                                                                         } else {

                                                                                             item.buildRef().child("viewcount").runTransaction(new Transaction.Handler() {
                                                                                                 @Override
                                                                                                 public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                     try {


                                                                                                         Long p = mutableData.getValue(Long.class);
                                                                                                         if (p == null) {
                                                                                                             p = -1L;
                                                                                                         } else p -= 1L;
                                                                                                         // Set value and report transaction success
                                                                                                         mutableData.setValue(p);
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

                                                                                                     }
                                                                                                     return Transaction.success(mutableData);
                                                                                                 }

                                                                                                 @Override
                                                                                                 public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                        DataSnapshot dataSnapshot) {
                                                                                                     // Transaction completed
                                                                                                     try {

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

                                                                                                     }
                                                                                                 }
                                                                                             });
                                                                                             a.child(Profile.getCurrentProfile().getId()).setValue(false);
                                                                                             userref.runTransaction(new Transaction.Handler() {
                                                                                                 @Override
                                                                                                 public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                     try {
                                                                                                         Long p = mutableData.getValue(Long.class);
                                                                                                         if (p == null) {
                                                                                                             p = +1L;
                                                                                                         } else p += 1L;
                                                                                                         // Set value and report transaction success
                                                                                                         mutableData.setValue(p);
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

                                                                                                     }
                                                                                                     return Transaction.success(mutableData);
                                                                                                 }

                                                                                                 @Override
                                                                                                 public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                        DataSnapshot dataSnapshot) {
                                                                                                     // Transaction completed
                                                                                                     try {
                                                                                                         if (databaseError == null)
                                                                                                             Toast.makeText(v.getContext(), "Score Updated " + (Long) dataSnapshot.getValue() * -1L, Toast.LENGTH_SHORT).show();
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

                                                                                                     }
                                                                                                 }
                                                                                             });
                                                                                             ownerref.runTransaction(new Transaction.Handler() {
                                                                                                 @Override
                                                                                                 public Transaction.Result doTransaction(MutableData mutableData) {
                                                                                                     try {
                                                                                                         Long p = mutableData.getValue(Long.class);
                                                                                                         if (p == null) {
                                                                                                             p = -1L;
                                                                                                         } else p -= 1L;
                                                                                                         // Set value and report transaction success
                                                                                                         mutableData.setValue(p);
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

                                                                                                     }
                                                                                                     return Transaction.success(mutableData);
                                                                                                 }

                                                                                                 @Override
                                                                                                 public void onComplete(DatabaseError databaseError, boolean b,
                                                                                                                        DataSnapshot dataSnapshot) {
                                                                                                     // Transaction completed
                                                                                                     try {
                                                                                                         if (databaseError == null)

                                                                                                             FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                 @Override
                                                                                                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                     if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                                                                         String gcmid = (String) (dataSnapshot.getValue());

                                                                                                                         String link = "Question:" + item.id;
                                                                                                                         MyData.pushNotification(Profile.getCurrentProfile().getName(), "failed to crack your question\n\n" + item.question, gcmid + "", link, item.uploaded_by);
                                                                                                                     }
                                                                                                                 }

                                                                                                                 @Override
                                                                                                                 public void onCancelled(DatabaseError databaseError) {

                                                                                                                 }
                                                                                                             });
                                                                                                     } catch (Exception e) {
                                                                                                         if (Profile.getCurrentProfile() != null) {
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
                                                                                                         }
                                                                                                     }
                                                                                                 }
                                                                                             });

                                                                                         }
                                                                                     } else {


                                                                /*Activity at = (Activity) context;
                                                                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                                                                View layout = inflater.inflate(R.layout.toast,
                                                                        (ViewGroup) at.findViewById(R.id.toast_layout_root));
                                                                ((ImageView) layout.findViewById(R.id.image)).setImageDrawable((context.getResources().getDrawable(R.drawable.wrong)));
                                                                ((TextView) layout.findViewById(R.id.result)).setText("Already Attempted");
                                                                Toast toast = new Toast(context);
                                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                                toast.setDuration(Toast.LENGTH_SHORT);
                                                                toast.setView(layout);
                                                                toast.show();         Already attempted         */
                                                                                     }

                                                                                 } catch (Exception e) {
                                                                                     if (Profile.getCurrentProfile() != null) {
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
                                                                                     }
                                                                                 }
                                                                             }

                                                                             @Override
                                                                             public void onCancelled(DatabaseError databaseError) {

                                                                             }
                                                                         }

                                        );

                                    } else {

                                        Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (Exception e) {

                                if (Profile.getCurrentProfile() != null)
                                    FirebaseDatabase.getInstance().getReference("Exceptions").child(Profile.getCurrentProfile().getId()).push().setValue(e.toString());

                            }

                        } else {

                            if (MainActivity.springFloatingActionMenu != null) {
                                MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                            }

                            Snackbar.make(itemView, "Please Login", Snackbar.LENGTH_LONG)
                                    .setAction("Login", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(context, Login.class));
                                        }
                                    }).show();

                        }

                        break;


                }


            } catch (Exception e) {

            }


        }
    }

    public class ViewHolder extends ParentViewHolder implements TagView.OnTagClickListener {
        public TagContainerLayout chipView;


        public ViewHolder(View itemView) {

            super(itemView);
            chipView = (TagContainerLayout) itemView.findViewById(R.id.tag_container);
            chipView.setOnTagClickListener(this);


        }


        @Override
        public void onTagClick(int position, String text) {
            // ...
            Intent i = new Intent(context, TagOpenActivity.class);
            i.putExtra("id", text);
            context.startActivity(i);
        }

        @Override
        public void onTagLongClick(final int position, String text) {
            // ...

            Intent i = new Intent(context, TagOpenActivity.class);
            i.putExtra("id", text);
            context.startActivity(i);
        }
    }

    public class SuperParentImageTagHolder extends SuperParentTag {
        ImageView pic;

        SuperParentImageTagHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            pic.setOnClickListener(this);
        }
    }

    public class SuperParentImageNoTagHolder extends SuperParent {
        ImageView pic;

        public SuperParentImageNoTagHolder(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.pic);
            pic.setOnClickListener(this);
        }
    }

    public class SuperParent extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, OnLikeListener {

        public TextView likecount;
        public TextView viewcount;
        public TextView commentcount;
        public AppCompatTextView username;
        public TextView question;

        public RelativeLayout rll;
        public CircleImageView userdp;
        public LinearLayout ll1, ll2, ll3;
        public LikeButton like;
        public ImageView menu;
        public LikeButton favourite;

        public SuperParent(View itemView) {
            super(itemView);
            try {
                ll1 = (LinearLayout) itemView.findViewById(R.id.ll1);
                ll2 = (LinearLayout) itemView.findViewById(R.id.ll2);
                ll3 = (LinearLayout) itemView.findViewById(R.id.ll3);
                menu = (ImageView) itemView.findViewById(R.id.menu);
                rll = (RelativeLayout) itemView.findViewById(R.id.rll);
                like = (LikeButton) itemView.findViewById(R.id.likedp);
                favourite = (LikeButton) itemView.findViewById(R.id.star);
                userdp = (CircleImageView) itemView.findViewById(R.id.userdp);
                question = (TextView) itemView.findViewById(R.id.contentquestion);
                likecount = (TextView) itemView.findViewById(R.id.likecount);
                viewcount = (TextView) itemView.findViewById(R.id.viewcount);
                commentcount = (TextView) itemView.findViewById(R.id.commentcount);
                username = (AppCompatTextView) itemView.findViewById(R.id.username);
                userdp.setOnClickListener(this);
                question.setOnClickListener(this);
                username.setOnClickListener(this);
                menu.setOnClickListener(this);
                likecount.setOnClickListener(this);
                ll1.setOnClickListener(this);
                ll2.setOnClickListener(this);
                ll3.setOnClickListener(this);
                rll.setOnClickListener(this);
                favourite.setOnLikeListener(this);
                like.setOnLikeListener(this);
            } catch (Exception e) {

            }

        }

        @Override
        public void onClick(final View v) {


            try {
                Intent intent;

                SuperParent holder = this;
                final Question item = data.get(getLayoutPosition());
                switch (v.getId()) {
                    case R.id.ll1:

                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(0);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.ll2:

                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(1);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.ll3:
                        intent = new Intent(v.getContext(), Reactions_Activity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        MyData.setType(2);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.pic:
                    case R.id.rll:
                    case R.id.contentquestion:

                        intent = new Intent(v.getContext(), QuestionOpenActivity.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.username:
                    case R.id.userdp:
                        if (data.get(getLayoutPosition()).uploaded_by.matches("\\d*")) {
                            intent = new Intent(v.getContext(), User.class);

                        } else {
                            intent = new Intent(v.getContext(), PageOpenActivity.class);
                        }
                        intent.putExtra("id", data.get(getLayoutPosition()).uploaded_by);
                        v.getContext().startActivity(intent);
                        break;

                    case R.id.menu:


                        final Question q = data.get(getLayoutPosition());


                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        popup.inflate(R.menu.question_item_menu_owner);
                        popup.setOnMenuItemClickListener(SuperParent.this);
                        popup.show();

                        final MenuItem edit = popup.getMenu().findItem(R.id.edit);

                        if (Profile.getCurrentProfile() != null && q.uploaded_by.equals(Profile.getCurrentProfile().getId())) {

                            edit.setVisible(true);
                        } else {
                            if (Profile.getCurrentProfile() != null)
                                FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("page_admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                            HashMap<String, String> data = (HashMap<String, String>) dataSnapshot.getValue();

                                            if (data != null && data.keySet().contains(q.uploaded_by)) {
                                                edit.setVisible(true);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                        }

                        break;

                }
            } catch (Exception e) {

            }

        }

        @Override

        public boolean onMenuItemClick(MenuItem item) {
            try {
                int id = item.getItemId();
                if (id == R.id.share) {
                    Question currentObject = data.get(getLayoutPosition());
                    StringBuilder toShare = new StringBuilder(currentObject.question + "\n\n");
                    if (currentObject.tags_here != null && currentObject.tags_here.size() != 0) {
                        for (String s : currentObject.tags_here.keySet())
                            toShare.append("#").append(s).append(" ");
                    }
                    if (currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)
                        toShare.append("\n\nChoice 1:").append(currentObject.choice0).append("\nChoice 2:").append(currentObject.choice1).append("\nChoice 3:").append(currentObject.choice2).append("\nChoice 4:").append(currentObject.choice3);

                    toShare.append("\n\n#Questo");

                    BottomSheet sheet = getShareActions(toShare.toString() + "\n\n" + MyData.buildDeepLink("question", currentObject.id)).title("Sharing Question").build();
                    sheet.show();
                } else if (id == R.id.report) {
                    if (Profile.getCurrentProfile() != null) {
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Report Question")
                                .customView(R.layout.report_layout, true)
                                .positiveText("Report")

                                .negativeColor(Color.BLACK)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //showToast("Password: " + passwordInput.getText().toString());


                                        EditText reason = (EditText) dialog.getCustomView().findViewById(R.id.reason);
                                        Toast.makeText(context, "Question Reported", Toast.LENGTH_SHORT).show();

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Reports").child(Profile.getCurrentProfile().getId()).child("QuestionReports").child(data.get(getLayoutPosition()).id);
                                        ref.child("reason").setValue(reason.getText().toString().trim());
                                        ref.child("id").setValue(data.get(getLayoutPosition()).id);

                                    }
                                }).build();
                        dialog.show();

                    } else {

                        if (MainActivity.springFloatingActionMenu != null) {
                            MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                        }
                        Snackbar.make(itemView, "Please Login", Snackbar.LENGTH_LONG)
                                .setAction("Login", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, Login.class));
                                    }
                                }).show();


                    }

                } else if (id == R.id.edit) {
                    if (Profile.getCurrentProfile() != null) {
                        Intent intent = new Intent(context, EditQuestion.class);
                        intent.putExtra("id", data.get(getLayoutPosition()).id);
                        context.startActivity(intent);
                    }

                } else if (id == R.id.sharelink) {


                    if (MainActivity.springFloatingActionMenu != null) {
                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                    }

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(context.getResources().getString(R.string.app_name), MyData.buildDeepLink("question", data.get(getLayoutPosition()).id).toString());
                    clipboard.setPrimaryClip(clip);


                    Snackbar.make(itemView, "Link copied", Snackbar.LENGTH_LONG)
                            .setAction("Share", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Question currentObject = data.get(getLayoutPosition());
                                    StringBuilder toShare = new StringBuilder(currentObject.question + "\n\n");
                                    if (currentObject.tags_here != null && currentObject.tags_here.size() != 0) {
                                        for (String s : currentObject.tags_here.keySet())
                                            toShare.append("#").append(s).append(" ");
                                    }
                                    if (currentObject.choice0 != null && currentObject.choice0.trim().length() != 0)
                                        toShare.append("\n\nChoice 1:").append(currentObject.choice0).append("\nChoice 2:").append(currentObject.choice1).append("\nChoice 3:").append(currentObject.choice2).append("\nChoice 4:").append(currentObject.choice3);

                                    toShare.append("\n\n#Questo");

                                    BottomSheet sheet = getShareActions(toShare.toString() + "\n\n" + MyData.buildDeepLink("question", currentObject.id)).title("Sharing Question").build();
                                    sheet.show();

                                }
                            }).show();

                }
            } catch (Exception e) {

            }
            return true;

        }


        @Override
        public void liked(LikeButton likeButton) {

            try {
                if (Profile.getCurrentProfile() != null) {
                    final Question item = data.get(getLayoutPosition());
                    final SuperParent holder = this;
                    if (MyData.haveNetworkConnection()) {
                        DatabaseReference postRef;
                        switch (likeButton.getId()) {
                            case R.id.star:

                                postRef = FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Questions");
                                postRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        //    Post p = mutableData.getValue(Post.class);
                                        try {

                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                            if (p == null) {
                                                p = new HashMap<String, Boolean>();

                                                p.put(item.id, true);
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            if (p.containsKey(item.id)) {
                                                // Unstar the post and remove self from stars
                                                //p.starCount = p.starCount - 1;
                                                p.remove(item.id);
                                            } else {
                                                // Star the post and add self to stars
                                                //p.starCount = p.starCount + 1;
                                                p.put(item.id, true);
                                            }

                                            // Set value and report transaction success
                                            mutableData.setValue(p);
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


                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        try {
                                            if (databaseError == null) {
                                                HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                    data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                                if (data.containsKey(item.id)) {
                                                    if (MainActivity.springFloatingActionMenu != null) {
                                                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                                    }
                                                    Snackbar.make(holder.itemView, "Added to Favorites", Snackbar.LENGTH_LONG)
                                                            .setAction("Show", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    context.startActivity(new Intent(context, Favorite_Activity.class));
                                                                }
                                                            }).show();

                                                } else {
                                                    if (MainActivity.springFloatingActionMenu != null) {
                                                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                                    }
                                                    Snackbar.make(holder.itemView, "Removed from Favorites", Snackbar.LENGTH_LONG)
                                                            .setAction("Show", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    context.startActivity(new Intent(context, Favorite_Activity.class));
                                                                }
                                                            }).show();
                                                }

                                            }
                                        } catch (Exception e) {
                                            if (Profile.getCurrentProfile() != null) {
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

                                            }
                                        }
                                    }
                                });
                                break;

                            case R.id.likedp:
                                postRef = FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(item.id);
                                postRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        //    Post p = mutableData.getValue(Post.class);

                                        try {


                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                            if (p == null) {
                                                p = new HashMap<String, Boolean>();

                                                p.put(Profile.getCurrentProfile().getId(), true);
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                // Unstar the post and remove self from stars
                                                //p.starCount = p.starCount - 1;
                                                p.remove(Profile.getCurrentProfile().getId());

                                            } else {
                                                // Star the post and add self to stars
                                                //p.starCount = p.starCount + 1;
                                                p.put(Profile.getCurrentProfile().getId(), true);

                                            }

                                            // Set value and report transaction success
                                            mutableData.setValue(p);
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


                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        // Transaction completed
                                        try {
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes");
                                            DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("likes_count");
                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                            if (p != null) {
                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                    holder.like.setLiked(true);
                                                    ref.child(item.id).setValue(true);

                                                    FirebaseDatabase.getInstance().getReference("myUsers").child(item.uploaded_by).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                                String gcmid = (String) (dataSnapshot.getValue());


                                                                String link = "Question:" + item.id;
                                                                MyData.pushNotification(Profile.getCurrentProfile().getName(), "liked your question\n\n" + item.question, gcmid + "", link, item.uploaded_by);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                } else {
                                                    holder.like.setLiked(false);
                                                    ref.child(item.id).setValue(null);
                                                }

                                                holder.likecount.setText(p.size() + "");
                                                likecount.setValue(-1 * p.size());


                                            }
                                            if (p == null) {
                                                holder.like.setLiked(false);
                                                ref.child(item.id).setValue(null);
                                                holder.likecount.setText("0");
                                                likecount.setValue(0);


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


                                        }
                                    }
                                });
                                break;
                        }
                    } else {
                        Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (MainActivity.springFloatingActionMenu != null) {
                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                    }

                    Snackbar.make(itemView, "Please Login", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(new Intent(context, Login.class));
                                }
                            }).show();
                }
            } catch (Exception e) {

                if (Profile.getCurrentProfile() != null) {
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
                }
            }
        }


        @Override
        public void unLiked(LikeButton likeButton) {

            try {
                if (Profile.getCurrentProfile() != null) {
                    final Question item = data.get(getLayoutPosition());
                    final SuperParent holder = this;
                    if (MyData.haveNetworkConnection()) {
                        DatabaseReference postRef;
                        switch (likeButton.getId()) {
                            case R.id.star:
                                postRef = FirebaseDatabase.getInstance().getReference("Favourites").child(Profile.getCurrentProfile().getId()).child("Questions");
                                postRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        //    Post p = mutableData.getValue(Post.class);
                                        try {

                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                            if (p == null) {
                                                p = new HashMap<String, Boolean>();

                                                p.put(item.id, true);
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            if (p.containsKey(item.id)) {
                                                // Unstar the post and remove self from stars
                                                //p.starCount = p.starCount - 1;
                                                p.remove(item.id);
                                            } else {
                                                // Star the post and add self to stars
                                                //p.starCount = p.starCount + 1;
                                                p.put(item.id, true);
                                            }

                                            // Set value and report transaction success
                                            mutableData.setValue(p);
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


                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        try {


                                            if (databaseError == null) {
                                                HashMap<String, Boolean> data = new HashMap<String, Boolean>();
                                                if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                                    data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                                                if (data.containsKey(item.id)) {
                                                    if (MainActivity.springFloatingActionMenu != null) {
                                                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                                    }
                                                    Snackbar.make(holder.itemView, "Added to Favorites", Snackbar.LENGTH_LONG)
                                                            .setAction("Show", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    context.startActivity(new Intent(context, Favorite_Activity.class));
                                                                }
                                                            }).show();
                                                }
                                                //   Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                                                else {
                                                    if (MainActivity.springFloatingActionMenu != null) {
                                                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                                                    }
                                                    Snackbar.make(holder.itemView, "Removed from Favorites", Snackbar.LENGTH_LONG)
                                                            .setAction("Show", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    context.startActivity(new Intent(context, Favorite_Activity.class));
                                                                }
                                                            }).show();
                                                }
                                                //    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();

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


                                        }
                                    }
                                });
                                break;
                            case R.id.likedp:

                                postRef = FirebaseDatabase.getInstance().getReference("Likers").child("QuestionLikers").child(item.id);
                                postRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        //    Post p = mutableData.getValue(Post.class);
                                        try {


                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                            if (p == null) {
                                                p = new HashMap<String, Boolean>();

                                                p.put(Profile.getCurrentProfile().getId(), true);
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                // Unstar the post and remove self from stars
                                                //p.starCount = p.starCount - 1;
                                                p.remove(Profile.getCurrentProfile().getId());

                                            } else {
                                                // Star the post and add self to stars
                                                //p.starCount = p.starCount + 1;
                                                p.put(Profile.getCurrentProfile().getId(), true);

                                            }

                                            // Set value and report transaction success
                                            mutableData.setValue(p);
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


                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        // Transaction completed

                                        try {


                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userlikes").child(Profile.getCurrentProfile().getId()).child("questionlikes");
                                            DatabaseReference likecount = FirebaseDatabase.getInstance().getReference("Question").child(item.id).child("likes_count");
                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                            if (p != null) {
                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                    holder.like.setLiked(true);
                                                    ref.child(item.id).setValue(true);

                                                } else {
                                                    holder.like.setLiked(false);
                                                    ref.child(item.id).setValue(null);
                                                }

                                                holder.likecount.setText(p.size() + "");
                                                likecount.setValue(-1 * p.size());


                                            }
                                            if (p == null) {
                                                holder.like.setLiked(false);
                                                ref.child(item.id).setValue(null);
                                                holder.likecount.setText("0");
                                                likecount.setValue(0);


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


                                        }
                                    }
                                });
                                break;
                        }
                    } else {
                        Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (MainActivity.springFloatingActionMenu != null) {
                        MainActivity.springFloatingActionMenu.setVisibility(View.INVISIBLE);
                    }
                    Snackbar.make(itemView, "Please Login", Snackbar.LENGTH_LONG)
                            .setAction("Login", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    context.startActivity(new Intent(context, Login.class));
                                }
                            }).show();
                }

            } catch (Exception e) {

                if (Profile.getCurrentProfile() != null) {
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
                }
            }
        }


    }

    public class SuperParentTag extends SuperParent implements TagView.OnTagClickListener {

        TagContainerLayout chipView;

        SuperParentTag(View itemView) {
            super(itemView);

            chipView = (TagContainerLayout) itemView.findViewById(R.id.tag_container);

            chipView.setOnTagClickListener(this);
        }

        @Override
        public void onTagClick(int position, String text) {
            // ...
            Intent i = new Intent(context, TagOpenActivity.class);
            i.putExtra("id", text);
            context.startActivity(i);
        }

        @Override
        public void onTagLongClick(final int position, String text) {
            // ...

            Intent i = new Intent(context, TagOpenActivity.class);
            i.putExtra("id", text);
            context.startActivity(i);
        }
    }

    public class ImageNoTagHolder extends ParentViewHolder {

        ImageView pic;

        ImageNoTagHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            pic.setOnClickListener(this);

        }


    }

    public class ImageTagHolder extends ViewHolder {

        ImageView pic;

        ImageTagHolder(View itemView) {
            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.pic);
            pic.setOnClickListener(this);
        }


    }
}
