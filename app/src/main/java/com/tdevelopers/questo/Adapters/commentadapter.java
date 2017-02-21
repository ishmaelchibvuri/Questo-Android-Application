package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.comment;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class commentadapter extends FirebaseRecyclerAdapter<RecyclerView.ViewHolder, comment> {
    private static final int VIEW_TYPE_OWN = 0;
    private static final int VIEW_TYPE_OTHER = 1;
    Context context;

    public commentadapter(Query query, Class itemClass) {
        super(query, itemClass);

    }

    @Override
    public int getItemViewType(int position) {


        comment item = getItem(position);


        if (Profile.getCurrentProfile()!=null&&item.userid.equals(Profile.getCurrentProfile().getId()))
            return VIEW_TYPE_OWN;
        else
            return VIEW_TYPE_OTHER;


    }


    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_OWN) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.owner_comment, parent, false);
            context = parent.getContext();
            return new OwnerHolder(view);
        } else {


            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_layout, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder out, final int position) {

        if (out instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) out;
            final comment item = getItem(position);
            holder.username.setText(item.username);
            holder.content.setText(item.content);

            Date endDate = new Date(System.currentTimeMillis());
            if (item.date != null && item.date != 0L) {
                Date startDate = new Date(item.date);// Set start date
                // Set end date

                long duration = endDate.getTime() - startDate.getTime();

                long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

                if (diffInHours == 0) {
                    if (diffInMinutes == 0 && diffInSeconds != 0)
                        holder.date.setText(diffInSeconds + " seconds ago");
                    else if (diffInMinutes == 0 && diffInSeconds == 0)
                        holder.date.setText("Just now");
                    else
                        holder.date.setText(diffInMinutes + " minutes ago");

                } else if (diffInHours > 0 && diffInHours < 24) {
                    if (diffInHours == 1)
                        holder.date.setText(diffInHours + " hour ago");
                    else
                        holder.date.setText(diffInHours + " hours ago");

                } else if (diffInHours > 24)
                    holder.date.setText(diffInHours / 24 + " days ago");

                if (diffInHours < 0 || diffInMinutes < 0 || diffInSeconds < 0) {


                    holder.date.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(startDate.getTime()));

                }
            }

            String urlImage = "https://graph.facebook.com/" + item.userid + "/picture?type=large";
            Picasso.with(context)
                    .load(urlImage).fit()
                    .into(holder.img, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {


                        }
                    });


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), User.class);
                    intent.putExtra("id", item.userid);
                    v.getContext().startActivity(intent);
                }
            });
        }
        if (out instanceof OwnerHolder) {
            final OwnerHolder holder = (OwnerHolder) out;
            final comment item = getItem(position);
            holder.username.setText(item.username);
            holder.content.setText(item.content);

            Date endDate = new Date(System.currentTimeMillis());
            if (item.date != null && !item.date.equals("")) {
                Date startDate = new Date(item.date);// Set start date
                // Set end date

                long duration = endDate.getTime() - startDate.getTime();

                long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

                if (diffInHours == 0) {
                    if (diffInMinutes == 0 && diffInSeconds != 0)
                        holder.date.setText(diffInSeconds + " seconds ago");
                    else if (diffInMinutes == 0 && diffInSeconds == 0)
                        holder.date.setText("Just now");
                    else
                        holder.date.setText(diffInMinutes + " minutes ago");

                } else if (diffInHours > 0 && diffInHours < 24) {
                    if (diffInHours == 1)
                        holder.date.setText(diffInHours + " hour ago");
                    else
                        holder.date.setText(diffInHours + " hours ago");

                } else if (diffInHours > 24)
                    holder.date.setText(diffInHours / 24 + " days ago");
                else if (diffInHours < 0 || diffInMinutes < 0 || diffInSeconds < 0) {


                    holder.date.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(startDate.getTime()));

                }
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.path != null && item.path.trim().length() != 0) {


                        AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                                .setTitle("Delete")
                                .setMessage("Are you sure to delete this comment")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference del = FirebaseDatabase.getInstance().getReferenceFromUrl(item.path);
                                        del.removeValue();
                                        FirebaseDatabase.getInstance().getReference("Question").child(del.getParent().getKey()).child("comment_count").runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                Long p = mutableData.getValue(Long.class);
                                                if (p == null) {
                                                    p = 0L;
                                                } else p -= 1L;
                                                // Set value and report transaction success
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean b,
                                                                   DataSnapshot dataSnapshot) {

                                            }
                                        });


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
                }
            });
            String urlImage = "https://graph.facebook.com/" + item.userid + "/picture?type=large";
            Picasso.with(context)
                    .load(urlImage).fit()
                    .into(holder.img, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {


                        }
                    });


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), User.class);
                    intent.putExtra("id", item.userid);
                    v.getContext().startActivity(intent);
                }
            });
        }


    }

    @Override
    protected void itemAdded(comment item, String key, int position) {

    }

    @Override
    protected void itemChanged(comment oldItem, comment newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(comment item, String key, int position) {

    }

    @Override
    protected void itemMoved(comment item, String key, int oldPosition, int newPosition) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CircleImageView img;
        TextView username;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.commentcontent);
            img = (CircleImageView) itemView.findViewById(R.id.userdp);
            username = (TextView) itemView.findViewById(R.id.username);
        }

    }


    public class OwnerHolder extends RecyclerView.ViewHolder {
        TextView content;
        CircleImageView img;
        TextView username;
        TextView date;
        ImageView delete;

        OwnerHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            content = (TextView) itemView.findViewById(R.id.commentcontent);
            img = (CircleImageView) itemView.findViewById(R.id.userdp);
            username = (TextView) itemView.findViewById(R.id.username);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

    }


}