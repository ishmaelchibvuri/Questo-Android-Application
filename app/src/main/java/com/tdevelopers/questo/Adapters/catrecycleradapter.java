package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.categories;
import com.tdevelopers.questo.Opens.CatOpenActivity;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.libraries.PaletteTransformation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class catrecycleradapter extends RecyclerView.Adapter<catrecycleradapter.catViewHolder> implements Filterable {

    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_NO_HEAD = 1;
    public ArrayList<categories> datalist;
    public ArrayList<categories> full;
    Context context;
    ValueFilter valueFilter;


    public catrecycleradapter(ArrayList<categories> datalist) {
        this.datalist = datalist;
        full = new ArrayList<>(datalist);
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

        if (datalist.get(position).tags != null && datalist.get(position).tags.size() != 0)
            return VIEW_TYPE_HEAD;
        else
            return VIEW_TYPE_NO_HEAD;
    }

    @Override
    public catViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_tile, parent, false);
            context = view.getContext();
            return new catViewHolder(view);
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_tile_nobadge, parent, false);
            context = view.getContext();
            return new catViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final catViewHolder holder, final int position) {
        final categories c = datalist.get(position);


        if (c != null) {

            try {


                if (c.tags != null && c.tags.size() != 0) {
                    int min = 0;
                    int max = c.tags.size() - 1;
                    Random rand = new Random();
                    int randomNum = rand.nextInt((max - min) + 1) + min;

                    //   Toast.makeText(context, randomNum + "", Toast.LENGTH_SHORT).show();
                    ArrayList<String> pic_data = new ArrayList<>(c.tags.keySet());
                    FirebaseDatabase.getInstance().getReference("Tag").child(pic_data.get(randomNum)).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url;
                            if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                                url = (String) dataSnapshot.getValue();
                                if (url != null && !url.equals("")) {
                                    Picasso.with(context).load(url).into(holder.avatar);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            } catch (Exception e) {

            }
            holder.subtext.setText(c.tags.size() + " tags");
            holder.catname.setText(c.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), CatOpenActivity.class);
                    i.putExtra("id", c.name);
                    v.getContext().startActivity(i);
                }

            });


            if (c.pic != null && !c.pic.equals("")) {

                Picasso.with(context)
                        .load(c.pic)
                        .fit().centerCrop()
                        .transform(PaletteTransformation.instance())
                        .into(holder.catpic, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                                Palette palette = null;
                                Bitmap bitmap = ((BitmapDrawable) holder.catpic.getDrawable()).getBitmap(); // Ew!
                                if (bitmap != null)
                                    palette = PaletteTransformation.getPalette(bitmap);
                                if (palette != null && holder.rl != null)
                                    holder.rl.setBackgroundColor(palette.getVibrantColor(Color.GRAY));

                            }
                        });
            }


        }


    }

    @Override
    public int getItemCount() {
        return datalist != null ? datalist.size() : 0;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            //   if (constraint != null && constraint.length() > 0) {
            LinkedList<categories> filterList;
            LinkedHashSet<categories> tempdata = new LinkedHashSet<>();
            for (int i = 0; i < full.size(); i++) {
                if ((full.get(i).name.toUpperCase().contains(constraint.toString().toUpperCase()))) {


                    //      filterList.add(full.get(i));
                    tempdata.add(full.get(i));
                }
                if (full.get(i).tags != null && full.get(i).tags.size() != 0)
                    for (String s : full.get(i).tags.keySet()) {
                        if (s.toUpperCase().contains(constraint.toString().toUpperCase())) {
                            //     filterList.add(full.get(i));

                            tempdata.add(full.get(i));
                        }
                    }
            }
            // Log.v("Filtertrack", full.toString());
            //  Toast.makeText(context, filterList.toString(), Toast.LENGTH_SHORT).show();
            filterList = new LinkedList<>(tempdata);
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

            LinkedList<categories> linkedList = (LinkedList<categories>) results.values;
            datalist = new ArrayList<>();

            for (int i = 0; i < linkedList.size(); i++) {
                datalist.add(linkedList.get(i));
            }

            notifyDataSetChanged();

        }

    }

    public class catViewHolder extends RecyclerView.ViewHolder {
        TextView catname;

        ImageView catpic;
        LinearLayout rl;
        TextView subtext;
        CircleImageView avatar;

        catViewHolder(View itemView) {
            super(itemView);
            subtext = (TextView) itemView.findViewById(R.id.subtext);
            catname = (TextView) itemView.findViewById(R.id.cattext);
            catpic = (ImageView) itemView.findViewById(R.id.catdp);
            rl = (LinearLayout) itemView.findViewById(R.id.rl);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);

        }

    }
}
