package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.MyData;
import com.tdevelopers.questo.Objects.notifications;
import com.tdevelopers.questo.Opens.ArticleOpenActivity;
import com.tdevelopers.questo.Opens.PageOpenActivity;
import com.tdevelopers.questo.Opens.QuestionOpenActivity;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Gordon Wong on 7/18/2015.
 * <p>
 * Adapter for the all items screen.
 */
public class NotificationAdapter extends FirebaseRecyclerAdapter<NotificationAdapter.ViewHolder, notifications> {

    Context context;

    public NotificationAdapter(Query query, Class itemClass, Context context) {

        super(query, itemClass);

        this.context = context;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public ArrayList<notifications> getItems() {
        return super.getItems();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final notifications item = getItem(position);
        if (item != null) {


            if (item.pagepic != null && item.pagepic.trim().length() != 0)

            {
                Picasso.with(context)
                        .load(item.pagepic).fit()
                        .into(holder.infoImageView, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {


                            }
                        });


                holder.infoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(v.getContext(), PageOpenActivity.class);
                        intent.putExtra("id", item.userid);
                        v.getContext().startActivity(intent);
                    }
                });
            } else {

                String urlImage = "https://graph.facebook.com/" + item.userid + "/picture?type=large";

                Picasso.with(context)
                        .load(urlImage).fit()
                        .into(holder.infoImageView, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {


                            }
                        });


                holder.infoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(v.getContext(), User.class);
                        intent.putExtra("id", item.userid);
                        v.getContext().startActivity(intent);
                    }
                });

            }


            holder.infoTextView.setText(item.message);
            Date endDate = new Date(System.currentTimeMillis());
            if (item.created_at != null && !item.created_at.equals("")) {
                Date startDate = new Date(item.created_at);// Set start date
                // Set end date

                long duration = endDate.getTime() - startDate.getTime();
                long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                if (diffInHours == 0) {
                    if (diffInMinutes == 0)
                        holder.date.setText(diffInSeconds + " seconds ago");
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

            if (item.link != null && item.link.trim().length() != 0) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.link.contains("Question")) {
                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), QuestionOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);
                        } else if (item.link.contains("Article")) {

                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), ArticleOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        } else if (item.link.contains("myUsers")) {

                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), User.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        } else if (item.link.contains("Page")) {


                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), PageOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        }
                    }
                });

                holder.infoTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.link.contains("Question")) {
                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), QuestionOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);
                        } else if (item.link.contains("Article")) {

                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), ArticleOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        } else if (item.link.contains("myUsers")) {

                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), User.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        } else if (item.link.contains("Page")) {


                            String id = item.link.substring(item.link.indexOf(":") + 1);
                            Intent intent = new Intent(v.getContext(), PageOpenActivity.class);
                            intent.putExtra("id", id);
                            v.getContext().startActivity(intent);

                        }
                    }
                });
            }
        }
    }

    @Override
    protected void itemAdded(notifications item, String key, int position) {

        try {
            MyData.getNotfound().setVisibility(View.GONE);
            // MyData.getLoader().setVisibility(View.GONE);
        } catch (Exception e) {

        }
    }

    @Override
    protected void itemChanged(notifications oldItem, notifications newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(notifications item, String key, int position) {

    }

    @Override
    protected void itemMoved(notifications item, String key, int oldPosition, int newPosition) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView infoTextView;
        public CircleImageView infoImageView;
        public TextView date;
        public RelativeLayout itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView =(RelativeLayout) itemView.findViewById(R.id.rl);
            date = (TextView) itemView.findViewById(R.id.date);
            infoTextView = (TextView) itemView.findViewById(R.id.content);
            infoImageView = (CircleImageView) itemView.findViewById(R.id.userdp);
        }
    }

}
