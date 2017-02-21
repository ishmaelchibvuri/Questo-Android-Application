package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PageAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int VIEW_TYPE_ADMIN = 0;
    private static final int VIEW_TYPE_NOT_ADMIN = 1;
    public ArrayList<user> full = new ArrayList<>();
    public ArrayList<user> data = new ArrayList<>();
    Context context;
    ValueFilter valueFilter;
    public HashSet<String> admins = new HashSet<>();
    public String pageid;
    public String pagename;

    public PageAdminAdapter(ArrayList<user> data, HashSet<String> admin, String pageid, String pagename) {
        this.data = data;
        full = new ArrayList<>(data);
        admins = admin;
        this.pageid = pageid;
        this.pagename = pagename;
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


        user item = data.get(position);


        if (admins != null && admins.contains(item.id))


            return VIEW_TYPE_ADMIN;
        else

            return VIEW_TYPE_NOT_ADMIN;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADMIN) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_tile, parent, false);
            context = view.getContext();
            return new ME_PeopleViewHolder(view);
        } else {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.not_admin_tile, parent, false);
            context = view.getContext();
            return new PeopleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder out, int position) {
        try {
            if (out instanceof PeopleViewHolder) {
                final PeopleViewHolder holder = (PeopleViewHolder) out;
                if (data != null && position < data.size()) {
                    final user t = data.get(position);
                    if (t != null) {

                        if (t.id != null) {

                            String urlImage = "https://graph.facebook.com/" + t.id + "/picture?type=large";
                            Picasso.with(context)
                                    .load(urlImage)
                                    .fit().centerCrop()
                                    .into(holder.tagpic, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }
                                    });

                        }


                        if (t.followers != null)
                            holder.subtext.setText(t.followers.size() + " followers");
                        else
                            holder.subtext.setText(0 + " followers");
                        if (t.name != null)
                            holder.tagname.setText(t.name);
                        if (t.article_count == null)
                            t.article_count = 0L;
                        if (t.question_count == null)
                            t.question_count = 0L;
//                holder.subtext.setText(t.followers.size() + " followers | " + t.following.size() + " following");


                        holder.postinfo.setText(t.question_count + " Questions | " + t.article_count + " Articles");


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                                        .setTitle("Add Admin")
                                        .setMessage("Are you sure to add " + t.name + " as admin for " + pagename)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                FirebaseDatabase.getInstance().getReference("Page").child(pageid).child("admins").child(t.id).setValue(true);
                                                FirebaseDatabase.getInstance().getReference("myUsers").child(t.id).child("page_admin").child(pageid).setValue(pagename);

                                                Toast.makeText(context, t.name + " added as Admin for " + pagename, Toast.LENGTH_SHORT).show();


                                                FirebaseDatabase.getInstance().getReference("myUsers").child(t.id).child("gcmid").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                                            String gcmid = (String) (dataSnapshot.getValue());


                                                            String link = "Page:" + pageid;
                                                            MyData.pushNotification(Profile.getCurrentProfile().getName(), "made you as Admin\n\n" + pagename, gcmid + "", link, t.id);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

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
                        });

                        if (t.score < 0)
                            t.score *= -1;
                        if (t.score != null) {
                            if (t.score >= 0 && t.score < 100)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.noobie));
                            else if (t.score >= 100 && t.score < 500)

                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.geek));

                            else if (t.score >= 500 && t.score < 1000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ninja));

                            else if (t.score >= 1000 && t.score < 2000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.captain));

                            else if (t.score >= 2000 && t.score < 3000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.batman));

                            else if (t.score >= 3000 && t.score < 4000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ironman));

                            else if (t.score >= 4000 && t.score < 5000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.joker));
                            else if (t.score >= 5000 && t.score < 7000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.sherlock));
                            else if (t.score >= 7000 && t.score < 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.genious));

                            else if (t.score >= 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.superman));
                        }

                    }


                }
            } else if (out instanceof ME_PeopleViewHolder) {


                ME_PeopleViewHolder holder = (ME_PeopleViewHolder) out;
                if (data != null && position < data.size()) {
                    final user t = data.get(position);
                    if (t != null) {

                        if (t.id != null) {

                            String urlImage = "https://graph.facebook.com/" + t.id + "/picture?type=large";
                            Picasso.with(context)
                                    .load(urlImage)
                                    .fit().centerCrop()
                                    .into(holder.tagpic, new Callback.EmptyCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }
                                    });

                        }


                        if (t.followers != null)
                            holder.subtext.setText(t.followers.size() + " followers");
                        else
                            holder.subtext.setText(0 + " followers");
                        if (t.name != null)
                            holder.tagname.setText(t.name);
                        if (t.article_count == null)
                            t.article_count = 0L;
                        if (t.question_count == null)
                            t.question_count = 0L;
//                holder.subtext.setText(t.followers.size() + " followers | " + t.following.size() + " following");


                        holder.postinfo.setText(t.question_count + " Questions | " + t.article_count + " Articles");
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(context, "Already  admin", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (t.score != null) {
                            if (t.score >= 0 && t.score < 100)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.noobie));
                            else if (t.score >= 100 && t.score < 500)

                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.geek));

                            else if (t.score >= 500 && t.score < 1000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ninja));

                            else if (t.score >= 1000 && t.score < 2000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.captain));

                            else if (t.score >= 2000 && t.score < 3000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.batman));

                            else if (t.score >= 3000 && t.score < 4000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ironman));

                            else if (t.score >= 4000 && t.score < 5000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.joker));
                            else if (t.score >= 5000 && t.score < 7000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.sherlock));
                            else if (t.score >= 7000 && t.score < 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.genious));

                            else if (t.score >= 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.superman));
                        }

                    }


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

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            //   if (constraint != null && constraint.length() > 0) {
            LinkedList<user> filterList = new LinkedList<>();

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

            LinkedList<user> linkedList = (LinkedList<user>) results.values;
            data = new ArrayList<>();

            for (int i = 0; i < linkedList.size(); i++) {
                data.add(linkedList.get(i));
            }

            notifyDataSetChanged();

        }

    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {


        CircleImageView tagpic;
        TextView tagname;
        TextView postinfo;
        TextView subtext;
        Button follow;
        CircleImageView avatar;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            tagpic = (CircleImageView) itemView.findViewById(R.id.tagpic);
            tagname = (TextView) itemView.findViewById(R.id.tagname);
            subtext = (TextView) itemView.findViewById(R.id.subtext);
            follow = (Button) itemView.findViewById(R.id.follow);
            postinfo = (TextView) itemView.findViewById(R.id.postinfo);

        }
    }


    class ME_PeopleViewHolder extends RecyclerView.ViewHolder {


        CircleImageView tagpic;
        TextView tagname;
        TextView postinfo;
        TextView subtext;
        Button edit;
        CircleImageView avatar;

        public ME_PeopleViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            tagpic = (CircleImageView) itemView.findViewById(R.id.tagpic);
            tagname = (TextView) itemView.findViewById(R.id.tagname);
            subtext = (TextView) itemView.findViewById(R.id.subtext);
            edit = (Button) itemView.findViewById(R.id.follow);
            postinfo = (TextView) itemView.findViewById(R.id.postinfo);

        }
    }
}
