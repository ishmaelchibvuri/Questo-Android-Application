package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.User.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saitej dandge on 10-06-2016.
 */
public class LikersAdapter extends RecyclerView.Adapter<LikersAdapter.ViewHolder> {

    ArrayList<String> data;

    Context context;

    public LikersAdapter(ArrayList<String> d) {
        data = d;
    }

    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liker, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (data != null && position < data.size()) {
            String urlImage = "https://graph.facebook.com/" + data.get(position % data.size()) + "/picture?type=large";
            Picasso.with(context)
                    .load(urlImage)
                    .fit()
                    .into(holder.circleImageView);

            holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), User.class);
                    intent.putExtra("id", data.get(position % data.size()));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.likerdp);
        }
    }

}
