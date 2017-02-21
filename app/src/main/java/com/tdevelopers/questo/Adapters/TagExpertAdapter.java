package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.TagUser;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class TagExpertAdapter extends FirebaseRecyclerAdapter<TagExpertAdapter.PeopleViewHolder, TagUser> {

    Context context;
    String tagname;

    public TagExpertAdapter(Query query, Class itemClass, String tagname) {

        super(query, itemClass);
        this.tagname = tagname;

    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_user_tile, parent, false);
        context = view.getContext();
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PeopleViewHolder holder, int position) {
        try {
            final TagUser item = getItem(position);
            if (item != null) {

                String urlImage = "https://graph.facebook.com/" + item.id + "/picture?type=large";
                Picasso.with(context).load(urlImage).into(holder.tagpic);
                FirebaseDatabase.getInstance().getReference("myUsers").child(item.id).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String x = new String();
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                x = (String) dataSnapshot.getValue();
                            holder.tagname.setText(x);
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("myUsers").child(item.id).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Long score = 0L;
                            if (dataSnapshot != null && dataSnapshot.getValue() != null)
                                score = (Long) dataSnapshot.getValue();
                            score = score * -1L;

                            holder.postinfo.setText(score + " total points");


                            if (score >= 0 && score < 100)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.noobie));
                            else if (score >= 100 && score < 500)

                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.geek));

                            else if (score >= 500 && score < 1000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ninja));

                            else if (score >= 1000 && score < 2000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.captain));

                            else if (score >= 2000 && score < 3000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.batman));

                            else if (score >= 3000 && score < 4000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.ironman));

                            else if (score >= 4000 && score < 5000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.joker));
                            else if (score >= 5000 && score < 7000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.sherlock));
                            else if (score >= 7000 && score < 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.genious));

                            else if (score >= 10000)
                                holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.superman));

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                holder.subtext.setText(item.score * -1 + " correct in " + tagname);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), User.class);
                        intent.putExtra("id", item.id);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void itemAdded(TagUser item, String key, int position) {

    }

    @Override
    protected void itemChanged(TagUser oldItem, TagUser newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(TagUser item, String key, int position) {

    }

    @Override
    protected void itemMoved(TagUser item, String key, int oldPosition, int newPosition) {

    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {


        CircleImageView tagpic;
        TextView tagname;
        TextView postinfo;
        TextView subtext;

        CircleImageView avatar;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            tagpic = (CircleImageView) itemView.findViewById(R.id.tagpic);
            tagname = (TextView) itemView.findViewById(R.id.tagname);
            subtext = (TextView) itemView.findViewById(R.id.subtext);

            postinfo = (TextView) itemView.findViewById(R.id.postinfo);

        }
    }
}
