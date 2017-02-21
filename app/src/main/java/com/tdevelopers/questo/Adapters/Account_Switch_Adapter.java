package com.tdevelopers.questo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tdevelopers.questo.Objects.Account;
import com.tdevelopers.questo.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saitej dandge on 21-09-2016.
 */
public class Account_Switch_Adapter extends ArrayAdapter<Account> {


    public Account_Switch_Adapter(Context context, int resource, ArrayList<Account> objects) {
        super(context, R.layout.page_item, objects);
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // TODO Auto-generated method stub
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Account account = getItem(position);

        if (account != null) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.page_item, parent, false);
            }
            final TextView name = (TextView) convertView.findViewById(R.id.aname);
            final CircleImageView pic = (CircleImageView) convertView.findViewById(R.id.apic);
            name.setText(account.name);
            if (!account.id.equals(Profile.getCurrentProfile().getId())) {

                final View finalConvertView = convertView;
                FirebaseDatabase.getInstance().getReference("Page").child(account.id).child("pic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                            Picasso.with(finalConvertView.getContext()).load(dataSnapshot.getValue() + "").into(pic);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {

                String urlImage = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large";
                Picasso.with(convertView.getContext()).load(urlImage).into(pic);
            }
            return convertView;
        } else {

            return LayoutInflater.from(this.getContext()).inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent,
                    false);
        }

    }

}
