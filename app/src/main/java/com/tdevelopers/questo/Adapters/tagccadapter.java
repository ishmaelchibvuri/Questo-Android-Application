package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.LoginStuff.Login;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.Tag;
import com.tdevelopers.questo.Opens.TagOpenActivity;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class tagccadapter extends RecyclerView.Adapter<tagccadapter.tagholder> implements Filterable {
    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_NO_HEAD = 1;
    public ArrayList<Tag> data;
    public ArrayList<Tag> full; //non volatile
    Context context;
    ValueFilter valueFilter;


    public tagccadapter(ArrayList<Tag> r) {
        data = r;

        full = new ArrayList<>(data);
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    @Override
    public int getItemViewType(int position) {


        Tag item = data.get(position);

        Date endDate = new Date(System.currentTimeMillis());
        if (item.created_at != null && item.created_at.toString().trim().length()!=0) {
            Date startDate = new Date(item.created_at);// Set start date
            // Set end date

            long duration = endDate.getTime() - startDate.getTime();

            //   Log.v("time track", startDate.getTime() + " " + endDate.getTime() + " " + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(endDate.getTime()) + " ");
            long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);


            if (diffInHours < 30)
                return VIEW_TYPE_HEAD;
            else
                return VIEW_TYPE_NO_HEAD;
        }

        return VIEW_TYPE_NO_HEAD;
    }

    @Override
    public tagholder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_tile, parent, false);
            context = view.getContext();
            return new tagholder(view);
        } else {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_nohead_tile, parent, false);
            context = view.getContext();
            return new tagholder(view);
        }
    }

    @Override
    public void onBindViewHolder(final tagholder holder, final int position) {
        final Tag t = data.get(position);
        if (t != null) {
            if (t.pic != null)
                Picasso.with(context)
                        .load(t.pic)
                        .fit().centerCrop()
                        .into(holder.tagpic, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {

                            }
                        });
            if (Profile.getCurrentProfile() != null)
                FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId() ).child("tagsfollowing").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();

                            if (data != null && data.containsKey(t.name)) {
                                holder.follow.setText("Following");
                                //holder.follow.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.MULTIPLY);
                            } else {
                                holder.follow.setText("Follow");

                                //    holder.follow.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            if (t.name != null)
                holder.tagname.setText(t.name + "");
            if (t.article_count == null)
                t.article_count = 0L;
            if (t.question_count == null)
                t.question_count = 0L;
            if (t.followers != null)
                holder.subtext.setText(t.followers + " followers");
            else
                holder.subtext.setText("0 followers");
            holder.postinfo.setText(t.question_count + " Questions | " + t.article_count + " Articles");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), TagOpenActivity.class);
                    intent.putExtra("id", t.name + "");
                    v.getContext().startActivity(intent);
                }
            });
            holder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (Profile.getCurrentProfile() != null) {
                            if (MyData.haveNetworkConnection()) {
                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("TagFollowers").child(t.name);
                                postRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        //    Post p = mutableData.getValue(Post.class);
                                        try {


                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (mutableData.getValue());
                                            if (p == null) {
                                                p = new HashMap<String, Boolean>();
                                                Log.v("followtrack", "null");
                                                p.put(Profile.getCurrentProfile().getId(), true);
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                // Unstar the post and remove self from stars
                                                //p.starCount = p.starCount - 1;
                                                p.remove(Profile.getCurrentProfile().getId());
                                                Log.v("followtrack", "removed");
                                            } else {
                                                // Star the post and add self to stars
                                                //p.starCount = p.starCount + 1;
                                                p.put(Profile.getCurrentProfile().getId(), true);
                                                Log.v("followtrack", "added");
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
                                        Log.d("followtrack", "postTransaction:onComplete:" + databaseError);

                                        try {
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId() + "").child("tagsfollowing");
                                            DatabaseReference tagref = FirebaseDatabase.getInstance().getReference("Tag").child(t.name).child("followers");
                                            HashMap<String, Boolean> p = (HashMap<String, Boolean>) (dataSnapshot.getValue());
                                            if (p != null) {
                                                if (p.containsKey(Profile.getCurrentProfile().getId())) {
                                                    holder.follow.setText("Following");
                                                    // holder.follow.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.MULTIPLY);
                                                    ref.child(t.name).setValue(true);

                                                } else {
                                                    holder.follow.setText("Follow");
                                                    ref.child(t.name).setValue(null);
                                                    //         holder.follow.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.MULTIPLY);

                                                }

                                                holder.subtext.setText(p.size() + " followers");
                                                tagref.setValue(p.size());
                                            }
                                            if (p == null) {
                                                holder.follow.setText("Follow");
                                                ref.child(t.name).setValue(null);
                                                holder.subtext.setText("0 followers");
                                                tagref.setValue(0);
                                                //   holder.follow.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.blue), PorterDuff.Mode.MULTIPLY);

                                            }
                                        } catch (Exception e)

                                        {
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
                                Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Snackbar.make(holder.itemView, "Please Login", Snackbar.LENGTH_LONG)
                                    .setAction("Login", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(context, Login.class));
                                        }
                                    }).show();

                        }
                    } catch (Exception e) {
                        Toast.makeText(context,e.toString()+ "", Toast.LENGTH_SHORT).show();
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


    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
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
            // Log.v("Filtertrack", full.toString());
            //  Toast.makeText(context, filterList.toString(), Toast.LENGTH_SHORT).show();
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
            data = new ArrayList<>();

            for (int i = 0; i < linkedList.size(); i++) {
                data.add(linkedList.get(i));
            }

            notifyDataSetChanged();

        }

    }

    class tagholder extends RecyclerView.ViewHolder {
        CircleImageView tagpic;
        TextView tagname;
        TextView postinfo;
        TextView subtext;
        Button follow;

        public tagholder(View itemView) {
            super(itemView);
            tagpic = (CircleImageView) itemView.findViewById(R.id.tagpic);
            tagname = (TextView) itemView.findViewById(R.id.tagname);
            subtext = (TextView) itemView.findViewById(R.id.subtext);
            follow = (Button) itemView.findViewById(R.id.follow);
            postinfo = (TextView) itemView.findViewById(R.id.postinfo);

        }
    }

}
