package com.tdevelopers.questo.libraries;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdevelopers.questo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    String s, aboutstring;
    TextView exptext;
    TextView about;

    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public BottomSheetFragment(String s, String about) {
        // Required empty public constructor
        this.s = s;
        this.aboutstring = about;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exptext = (TextView) view.findViewById(R.id.exptext);
        about = (TextView) view.findViewById(R.id.about);
        if (aboutstring != null && aboutstring.trim().length() != 0)
            about.setText(aboutstring);
        if (s != null && s.trim().length() != 0)
            exptext.setText(s);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sheet_dialog, container, false);
    }
}
