package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.user;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderShipAdapter extends FirebaseRecyclerAdapter<LeaderShipAdapter.scoreViewHolder, user> {

    Context context;

    public LeaderShipAdapter(Query query, Class itemClass) {

        super(query, itemClass);


    }

    @Override
    public scoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scoreitem, parent, false);
        context = view.getContext();
        return new scoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(scoreViewHolder holder, int position) {

        final user item = getItem(position);
        if (item != null) {
            holder.name.setText(item.name);
            if(item.score<0)
                item.score*=-1;
            holder.score.setText(item.score + " points");
        }

        String urlImage = "https://graph.facebook.com/" + item.id + "/picture?type=large";
        Picasso.with(context)
                .load(urlImage)
                .fit().centerCrop()
                .into(holder.pic, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), User.class);
                intent.putExtra("id", item.id);
                v.getContext().startActivity(intent);
            }
        });
        user t = item;

        if (t.score != null) {

            if (t.score < 0)
                t.score *= -1;

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

    @Override
    protected void itemAdded(user item, String key, int position) {

    }

    @Override
    protected void itemChanged(user oldItem, user newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(user item, String key, int position) {

    }

    @Override
    protected void itemMoved(user item, String key, int oldPosition, int newPosition) {

    }

    public class scoreViewHolder extends RecyclerView.ViewHolder {


        TextView name, score;
        CircleImageView pic;
        CircleImageView avatar;

        public scoreViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            pic = (CircleImageView) itemView.findViewById(R.id.pic);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            score = (TextView) itemView.findViewById(R.id.score);
        }
    }
}
