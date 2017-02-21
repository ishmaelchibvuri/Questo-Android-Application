package com.tdevelopers.questo.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.Article;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Opens.ArticleOpenActivity;
import com.tdevelopers.questo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_NO_IMAGE = 1;
    Context context;
    ArrayList<Article> data = new ArrayList<>();

    public ArticleListAdapter(ArrayList<Article> data) {
        this.data = data;


    }

    private static int getRandomColor(Context context) {
        int[] colors;
        if (Math.random() >= 0.6) {
            colors = context.getResources().getIntArray(R.array.note_accent_colors);
        } else {
            colors = context.getResources().getIntArray(R.array.note_neutral_colors);
        }
        return colors[((int) (Math.random() * colors.length))];
    }

    @Override
    public int getItemViewType(int position) {

        if (data.get(position).image != null && !data.get(position).image.equals(""))
            return VIEW_TYPE_IMAGE;
        else
            return VIEW_TYPE_NO_IMAGE;

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article, parent, false);
            context = view.getContext();
            return new ViewHolder(view);
        } else {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noimagearticle, parent, false);
            context = view.getContext();
            return new NoImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder out, int position) {
        try {
            if (out instanceof ViewHolder) {

                if (data != null && data.get(position % data.size()) != null) {
                    ViewHolder holder = (ViewHolder) out;
                    final Article noteModel = data.get(position);
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), ArticleOpenActivity.class);
                            i.putExtra("id", noteModel.id);
                            v.getContext().startActivity(i);
                        }
                    });
                    String title = noteModel.title;
                    String note = noteModel.description;
                    String info = noteModel.username;

                    // Set text

                    holder.likecount.setText(noteModel.likes_count + "");
                    holder.viewcount.setText(noteModel.views_count * -1 + "");
                    holder.titleTextView.setText(title);

                    holder.infoTextView.setText(info);

                    String pattern = "MMM - dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    if (noteModel.date != null)
                        holder.date.setText(simpleDateFormat.format(noteModel.date));

                    // Set image
                    holder.infoImageView.setProfileId(noteModel.uploaded_by);
                    // Set visibilities
                    holder.titleTextView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);

                    holder.infoLayout.setVisibility(TextUtils.isEmpty(info) ? View.GONE : View.VISIBLE);

                    if (noteModel.image != null && !noteModel.image.equals("")) {

                        Picasso.with(context)
                                .load(noteModel.image)
                                .fit().centerCrop()
                                .into(holder.dp, new Callback.EmptyCallback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                });
                    } else {
                        holder.dp.setVisibility(View.INVISIBLE);
                        holder.rll.removeView(holder.dp);
                    }
                    // Set padding
                    // Set background color
                    ((CardView) holder.itemView).setCardBackgroundColor(getRandomColor(holder.itemView.getContext()));


                }

            } else if (out instanceof NoImageViewHolder)

            {


                if (data != null && data.get(position % data.size()) != null) {
                    NoImageViewHolder holder = (NoImageViewHolder) out;
                    final Article noteModel = data.get(position);
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(v.getContext(), ArticleOpenActivity.class);
                            i.putExtra("id", noteModel.id);
                            v.getContext().startActivity(i);
                        }
                    });
                    String title = noteModel.title;
                    String note = noteModel.description;
                    String info = noteModel.username;

                    // Set text
                    holder.titleTextView.setText(title);

                    holder.infoTextView.setText(info);


                    holder.likecount.setText(noteModel.likes_count + "");
                    holder.viewcount.setText(noteModel.views_count * -1 + "");
                    String pattern = "MMM - dd";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    if (noteModel.date != null)
                        holder.date.setText(simpleDateFormat.format(noteModel.date));
                    // Set image
                    holder.infoImageView.setProfileId(noteModel.uploaded_by);
                    // Set visibilities
                    holder.titleTextView.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);

                    holder.infoLayout.setVisibility(TextUtils.isEmpty(info) ? View.GONE : View.VISIBLE);

                    // Set padding
                    int paddingTop = (holder.titleTextView.getVisibility() != View.VISIBLE) ? 0
                            : holder.itemView.getContext().getResources()
                            .getDimensionPixelSize(R.dimen.note_content_spacing);
                    // Set background color
                    ((CardView) holder.itemView).setCardBackgroundColor(getRandomColor(holder.itemView.getContext()));


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

        }
    }


    public static class NoImageViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public RelativeLayout infoLayout;
        public TextView infoTextView;
        public ProfilePictureView infoImageView;
        public TextView date;

        public View view;

        public TextView likecount, viewcount;


        public NoImageViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            date = (TextView) itemView.findViewById(R.id.date);
            titleTextView = (TextView) itemView.findViewById(R.id.note_title);

            infoLayout = (RelativeLayout) itemView.findViewById(R.id.note_info_layout);
            infoTextView = (TextView) itemView.findViewById(R.id.note_info);

            likecount = (TextView) itemView.findViewById(R.id.like_count);
            viewcount = (TextView) itemView.findViewById(R.id.view_count);
            infoImageView = (ProfilePictureView) itemView.findViewById(R.id.note_info_image);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public RelativeLayout infoLayout;
        public TextView infoTextView;
        public ProfilePictureView infoImageView;
        public TextView date;
        public ImageView dp;
        public RelativeLayout rll;
        public View view;
        public TextView likecount, viewcount;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            rll = (RelativeLayout) itemView.findViewById(R.id.rll);
            dp = (ImageView) itemView.findViewById(R.id.catdp);
            date = (TextView) itemView.findViewById(R.id.date);
            titleTextView = (TextView) itemView.findViewById(R.id.note_title);
            likecount = (TextView) itemView.findViewById(R.id.like_count);
            viewcount = (TextView) itemView.findViewById(R.id.view_count);
            infoLayout = (RelativeLayout) itemView.findViewById(R.id.note_info_layout);
            infoTextView = (TextView) itemView.findViewById(R.id.note_info);
            infoImageView = (ProfilePictureView) itemView.findViewById(R.id.note_info_image);
        }
    }

}
