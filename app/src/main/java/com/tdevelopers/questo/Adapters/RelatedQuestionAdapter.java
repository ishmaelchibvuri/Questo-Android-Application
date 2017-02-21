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
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.Opens.QuestionOpenActivity;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

public class RelatedQuestionAdapter extends RecyclerView.Adapter<RelatedQuestionAdapter.ViewHolder> {

    ArrayList<String> data;
    Context context;

    public RelatedQuestionAdapter(ArrayList<String> recieved) {
        data = recieved;
    }


    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relatedquestion, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (data != null) {
            FirebaseDatabase.getInstance().getReference("Question").child(data.get(position % data.size())).child("question").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        holder.question.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("Question").child(data.get(position % data.size())).child("likes_count").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        holder.likecount.setText(Long.parseLong(dataSnapshot.getValue().toString()) * -1 + "");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), QuestionOpenActivity.class);
                    i.putExtra("id", data.get(position % data.size()));
                    v.getContext().startActivity(i);
                }
            });
            FirebaseDatabase.getInstance().getReference("Question").child(data.get(position % data.size())).child("viewcount").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null)
                        holder.commentcount.setText((long) dataSnapshot.getValue() * -1 + "");
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView likecount;
        TextView commentcount;

        ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.question);

            likecount = (TextView) itemView.findViewById(R.id.like_count);
            commentcount = (TextView) itemView.findViewById(R.id.comment_count);


        }

    }


}
