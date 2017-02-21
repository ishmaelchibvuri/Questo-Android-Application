package com.tdevelopers.questo.User;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class User_Analysis extends Fragment {

    String id;

    HorizontalBarChart mChart;

    public User_Analysis() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public User_Analysis(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static User_Analysis newInstance(String id) {

        Bundle args = new Bundle();

        User_Analysis fragment = new User_Analysis(id);
        fragment.setArguments(args);
        return fragment;
    }

    private static Map<String, Long> sortByComparator(Map<String, Long> unsortMap, final boolean order) {

        List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user__analysis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (id != null && id.trim().length() != 0) {
            mChart = (HorizontalBarChart) view.findViewById(R.id.chartdata);
            mChart.setHighlightPerTapEnabled(false);
            mChart.setPinchZoom(false);
            mChart.setDoubleTapToZoomEnabled(false);
            FirebaseDatabase.getInstance().getReference("user_analysis").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        HashMap<String, Long> data = new HashMap<String, Long>();
                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                            data = (HashMap<String, Long>) dataSnapshot.getValue();

                        XAxis xl = mChart.getXAxis();
                        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xl.setDrawAxisLine(false);
                        xl.setDrawGridLines(false);
                        YAxis yl = mChart.getAxisLeft();
                        yl.setDrawAxisLine(false);
                        yl.setDrawGridLines(false);
                        yl.setEnabled(false);
                        YAxis yr = mChart.getAxisRight();
                        //   yr.setTypeface(tf);
                        yr.setDrawAxisLine(false);
                        yr.setDrawGridLines(false);
                        yr.setEnabled(false);
                        // mChart.setPinchZoom(false);
                        if (data != null && data.size() != 0)
                            setData(data);
                        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                        Legend l = mChart.getLegend();
                        l.setEnabled(true);
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            FirebaseDatabase.getInstance().getReference("myUsers").child(id).child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Long score = 0L;
                    try {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null)
                            score = (Long) dataSnapshot.getValue();
                        mChart.setDescription("Total Score " + score);


                    } catch (Exception e)

                    {
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    private void setData(HashMap<String, Long> odata) {


        try {
            Map<String, Long> data = sortByComparator(odata, true);
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            int count = 0;
            for (Long l : data.values()) {
                yVals1.add(new BarEntry(l.intValue(), count));
                count++;
            }
            ArrayList<String> xVals = new ArrayList<>();

            if (data != null && data.size() != 0)
                for (String x : data.keySet())
                    xVals.add(x);
            BarDataSet barDataSet = new BarDataSet(yVals1, "correctly answered");
            BarData barData = new BarData();
            barData.setXVals(xVals);
            barData.addDataSet(barDataSet);

            mChart.setData(barData);


        } catch (Exception e) {

        }
    }

}
