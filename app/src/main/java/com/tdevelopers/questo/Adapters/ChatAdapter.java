package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.Message;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdapter extends FirebaseRecyclerAdapter<RecyclerView.ViewHolder, Message> {
    private static final int VIEW_TYPE_OWN = 0;
    private static final int VIEW_TYPE_OTHER = 1;
    Context context;
    RecyclerView recyclerView;

    public ChatAdapter(Query query, Class itemClass, RecyclerView recyclerView) {
        super(query, itemClass);
        this.recyclerView = recyclerView;

    }

    @Override
    public int getItemViewType(int position) {


        Message item = getItem(position);


        if (item.userid.equals(Profile.getCurrentProfile().getId()))
            return VIEW_TYPE_OWN;
        else
            return VIEW_TYPE_OTHER;


    }


    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_OWN) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ownchat_tile, parent, false);
            context = parent.getContext();
            return new OwnerHolder(view);
        } else {


            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_tile, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder out, final int position) {

        if (out instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) out;
            final Message item = getItem(position);


            holder.content.setText(item.content);
            holder.time.setText(new SimpleDateFormat("hh:mm a").format(item.date));

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
            final Message item = getItem(position);

            holder.time.setText(new SimpleDateFormat("hh:mm a").format(item.date));
            holder.content.setText(item.content);
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
    protected void itemAdded(Message item, String key, int position) {
        try {
            recyclerView.scrollToPosition(0);
        } catch (Exception e) {

        }
    }

    @Override
    protected void itemChanged(Message oldItem, Message newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(Message item, String key, int position) {

    }

    @Override
    protected void itemMoved(Message item, String key, int oldPosition, int newPosition) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CircleImageView img;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            img = (CircleImageView) itemView.findViewById(R.id.userdp);
            time = (TextView) itemView.findViewById(R.id.textview_time);
        }

    }


    public class OwnerHolder extends RecyclerView.ViewHolder {
        TextView content;
        CircleImageView img;
        TextView time;

        OwnerHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            img = (CircleImageView) itemView.findViewById(R.id.userdp);
            time = (TextView) itemView.findViewById(R.id.textview_time);

        }

    }


}