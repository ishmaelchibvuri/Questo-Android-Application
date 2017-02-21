package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExsistingAdminAdapter extends RecyclerView.Adapter<ExsistingAdminAdapter.AdminViewHolder> {

    Context context;
    ArrayList<String> data = new ArrayList<>();

    public String pageid;

    public ExsistingAdminAdapter(HashSet<String> dataset, String pageid) {
        super();
        data = new ArrayList<>();
        for (String s : dataset) {
            data.add(s);
        }
        this.pageid = pageid;
    }

    @Override
    public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_cross, parent, false);
        context = view.getContext();
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminViewHolder holder, int position) {
        try {

            String urlImage = "https://graph.facebook.com/" + data.get(position) + "/picture?type=large";

            Picasso.with(context).load(urlImage).into(holder.dp);

        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {


        return data != null ? data.size() : 0;
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView dp;
        ImageView cross;

        public AdminViewHolder(View itemView) {
            super(itemView);
            dp = (CircleImageView) itemView.findViewById(R.id.dp);
            cross = (ImageView) itemView.findViewById(R.id.cross);
            cross.setOnClickListener(this);
            dp.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Remove")
                        .setMessage("Are you sure to remove from admins")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase.getInstance().getReference("Page").child(pageid).child("admins").child(data.get(getLayoutPosition())).setValue(null);
                                FirebaseDatabase.getInstance().getReference("myUsers").child(data.get(getLayoutPosition())).child("page_admin").child(pageid).setValue(null);
                                Toast.makeText(context, "Removed admin", Toast.LENGTH_SHORT).show();

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

            } catch (Exception e) {

            }
        }
    }
}
