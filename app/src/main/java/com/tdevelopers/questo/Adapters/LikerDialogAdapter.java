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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikerDialogAdapter extends RecyclerView.Adapter<LikerDialogAdapter.LHolder> {
    Context context;
    DatabaseReference databaseReference;
    ArrayList<String> data = new ArrayList<>();

    public LikerDialogAdapter(ArrayList<String> data) {
        this.data = data;
        databaseReference = FirebaseDatabase.getInstance().getReference("myUsers");
    }

    @Override
    public LHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liker_dialog_item, parent, false);
        context = view.getContext();
        return new LHolder(view);
    }

    @Override
    public void onBindViewHolder(final LHolder holder, final int position) {
        if (data != null && position < data.size()) {
            String urlImage = "https://graph.facebook.com/" + data.get(position % data.size()) + "/picture?type=large";
            Picasso.with(context)
                    .load(urlImage)
                    .fit()
                    .into(holder.dp);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), User.class);
                    intent.putExtra("id", data.get(position % data.size()));
                    v.getContext().startActivity(intent);
                }
            });
            databaseReference.child(data.get(position)).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        holder.name.setText((String) dataSnapshot.getValue());
                    }
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

    public class LHolder extends RecyclerView.ViewHolder {
        CircleImageView dp;
        TextView name;

        public LHolder(View itemView) {
            super(itemView);
            dp = (CircleImageView) itemView.findViewById(R.id.dialogdp);
            name = (TextView) itemView.findViewById(R.id.dialogname);
        }
    }
}
