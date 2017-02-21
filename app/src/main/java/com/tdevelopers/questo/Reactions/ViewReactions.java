package com.tdevelopers.questo.Reactions;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdevelopers.questo.R;
import com.tdevelopers.questo.Reactions.ReactionsOpen.ReactedUsersActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewReactions extends Fragment implements OnChartValueSelectedListener {

    PieChart mChart;
    Long correct = 0L, wrong = 0L;
    String id;
    Context context;

    public ViewReactions() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ViewReactions(String id) {
        // Required empty public constructor
        this.id = id;
    }

    public static ViewReactions newInstance(String id) {

        Bundle args = new Bundle();

        ViewReactions fragment = new ViewReactions(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        try {
            if (id != null && id.trim().length() != 0) {


                FirebaseDatabase.getInstance().getReference("Attempts").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String, Boolean> data = (HashMap<String, Boolean>) dataSnapshot.getValue();
                        if (data != null) {


                            Long correct_count = 0L;
                            Long wrong_count = 0L;
                            for (Boolean s : data.values()) {
                                if (s) {
                                    correct_count++;
                                } else {
                                    wrong_count++;
                                }

                            }


                            correct = correct_count;
                            wrong = wrong_count;

                            mChart = (PieChart) view.findViewById(R.id.chartdata);
                            //    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            //       WindowManager.LayoutParams.FLAG_FULLSCREEN);


                            mChart.setUsePercentValues(true);
                            mChart.setDescription("Question Analysis");
                            mChart.setExtraOffsets(5, 10, 5, 5);

                            mChart.setDragDecelerationFrictionCoef(0.95f);


                            mChart.setDrawHoleEnabled(true);
                            mChart.setHoleColor(Color.WHITE);

                            mChart.setTransparentCircleColor(Color.WHITE);
                            mChart.setTransparentCircleAlpha(110);

                            mChart.setHoleRadius(58f);
                            mChart.setTransparentCircleRadius(61f);

                            mChart.setDrawCenterText(true);

                            mChart.setRotationAngle(0);
                            // enable rotation of the chart by touch
                            mChart.setRotationEnabled(true);
                            mChart.setHighlightPerTapEnabled(true);

                            // mChart.setUnit(" €");
                            // mChart.setDrawUnitsInChart(true);

                            // add a selection listener
                            mChart.setOnChartValueSelectedListener(ViewReactions.this);

                            if (!(correct == 0 && wrong == 0)) {
                                setData(correct, wrong);
                                mChart.setCenterText(generateCenterSpannableText(correct + wrong));
                            }

                            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                            // mChart.spin(2000, 0, 360);

                            Legend l = mChart.getLegend();
                            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
                            l.setXEntrySpace(7f);
                            l.setYEntrySpace(0f);
                            l.setYOffset(0f);
                            // l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        } catch (Exception e) {

        }
    }


    private SpannableString generateCenterSpannableText(long count) {

        SpannableString s = new SpannableString(count + " Views");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length(), 0);
        return s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_reactions, container, false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {


        if (e == null)
            return;
        if (context != null && id != null && id.trim().length() != 0) {
            Intent i = new Intent(context, ReactedUsersActivity.class);
            i.putExtra("id", id + "");
            startActivity(i);
        }

    }

    @Override
    public void onNothingSelected() {

    }


    private void setData(float correct, float wrong) {


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        yVals1.add(new Entry(correct, 0));
        yVals1.add(new Entry(wrong, 1));
        ArrayList<String> xVals = new ArrayList<>();


        xVals.add("Correct : " + (int) correct);
        xVals.add("Wrong   : " + (int) wrong);
        PieDataSet dataSet = new PieDataSet(yVals1, "Question Analysis");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();


        colors.add(ColorTemplate.rgb("#8BC34A"));

        colors.add(ColorTemplate.rgb("#F44336"));

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

}
