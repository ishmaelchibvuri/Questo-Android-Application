package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.AbsModel;
import com.tdevelopers.questo.Opens.ChatOpenActivity;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AbstractChatAdapter extends RecyclerView.Adapter<AbstractChatAdapter.AbsHolder> {

    Context context;
    ArrayList<AbsModel> data;

    public AbstractChatAdapter(ArrayList<AbsModel> data) {
        this.data = data;


    }

    @Override
    public AbsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.abschat_tile, parent, false);
        context = view.getContext();
        return new AbsHolder(view);
    }

    @Override
    public void onBindViewHolder(final AbsHolder holder, int position) {

        final AbsModel current = data.get(position);
        if (current != null) {
            holder.usertext.setText(current.content);
            String urlImage = "https://graph.facebook.com/" + current.userid + "/picture?type=large";
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ChatOpenActivity.class);
                    i.putExtra("id", current.userid);
                    v.getContext().startActivity(i);
                }
            });


            String pattern = "MMM - dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


            Date endDate = new Date(System.currentTimeMillis());
            if (current.date != null && current.date != 0L) {
                Date startDate = new Date(current.date * -1);// Set start date
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


            FirebaseDatabase.getInstance().getReference("myUsers").child(current.userid).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {


                        Long score = (Long) dataSnapshot.getValue();
                        if(score<0)
                            score*=-1;
                        if (score != null) {
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
                        }

                    } catch (Exception e) {

                        //Toast.makeText(context, e.toString()+"", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            Picasso.with(context)
                    .load(urlImage)
                    .fit()
                    .transform(PaletteTransformation.instance())
                    .into(holder.userdp, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {


                        }
                    });

            FirebaseDatabase.getInstance().getReference("myUsers").child(current.userid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        holder.username.setText(dataSnapshot.getValue() + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class AbsHolder extends RecyclerView.ViewHolder {
        CircleImageView userdp;
        TextView usertext;
        TextView username;
        TextView date;
        ImageView avatar;

        public AbsHolder(View itemView) {
            super(itemView);
            userdp = (CircleImageView) itemView.findViewById(R.id.userdp);
            usertext = (TextView) itemView.findViewById(R.id.usertext);
            username = (TextView) itemView.findViewById(R.id.username);
            date = (TextView) itemView.findViewById(R.id.date);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);

        }
    }
}
