package com.kidsupervisor;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GraphActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
                        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                            String start_hour = dsp.child("start_hour").getValue().toString();
                            Integer start_hour_int = Integer.parseInt(start_hour);


                            String start_min = dsp.child("start_min").getValue().toString();
                            Integer start_min_int = Integer.parseInt(start_min);

                            String end_hour = dsp.child("end_hour").getValue().toString();
                            Integer end_hour_int = Integer.parseInt(end_hour);

                            String end_min = dsp.child("end_min").getValue().toString();
                            Integer end_min_int = Integer.parseInt(end_min);



                            BarEntry v1e1 = new BarEntry(start_min_int, start_hour_int); // Jan
                            valueSet1.add(v1e1);

                            BarEntry v2e2 = new BarEntry(end_min_int, end_hour_int); // Jan
                            valueSet1.add(v2e2);
                        }
                        ArrayList<BarDataSet> dataSets = null;

                        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Red to Green - Sleeping");
                        barDataSet1.setColors(new int[] {Color.RED, Color.GREEN});

                        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "End");
                        barDataSet2.setColor(Color.rgb(0, 0, 155));

                        dataSets = new ArrayList<>();
                        dataSets.add(barDataSet1);
                        //dataSets.add(barDataSet2);


                        BarChart chart = (BarChart) findViewById(R.id.barchart);
                        BarData data = new BarData(getXAxisValues(), dataSets);
                        chart.setData(data);

                        chart.getXAxis().setAdjustXLabels(false);
                        //chart.getXAxis().setValues(getXAxisValues());

                        XAxis xAxis = chart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);

                        
                        chart.setMaxVisibleValueCount(23);
                        chart.setDescription("Sleep ");
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }

                });






//        BarChart chart = (BarChart) findViewById(R.id.barchart);
//        BarData data = new BarData(getXAxisValues(), getDataSet());
//        chart.setData(data);
//        chart.setDescription("My Chart");
//        chart.animateXY(2000, 2000);
//        chart.invalidate();
    }
    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(50f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(55f, 0); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(03.30f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(04.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(23.000f, 4); // May
        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(23.000f, 5); // Jun
//        valueSet1.add(v1e6);
        BarEntry v1e77 = new BarEntry(23.000f, 6); // Jun
        valueSet1.add(v1e77);
        BarEntry v1e7 = new BarEntry(23.000f, 7); // Jun
        valueSet1.add(v1e7);
        BarEntry v1e8 = new BarEntry(100.000f, 8); // Jun
        valueSet1.add(v1e8);
        BarEntry v1e9 = new BarEntry(100.000f, 9); // Jun
        valueSet1.add(v1e9);
        BarEntry v1e10 = new BarEntry(100.000f, 10); // Jun
        valueSet1.add(v1e10);
        BarEntry v1e11 = new BarEntry(100.000f, 11); // Jun
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(100.000f, 12); // Jun
        valueSet1.add(v1e12);
        BarEntry v1e13 = new BarEntry(100.000f, 13); // Jun
        valueSet1.add(v1e13);
        BarEntry v1e14 = new BarEntry(100.000f, 14); // Jun
        valueSet1.add(v1e14);
        BarEntry v1e15 = new BarEntry(100.000f, 15); // Jun
        valueSet1.add(v1e15);
        BarEntry v1e16 = new BarEntry(100.000f, 16); // Jun
        valueSet1.add(v1e16);
        BarEntry v1e17 = new BarEntry(100.000f, 17); // Jun
        valueSet1.add(v1e17);
        BarEntry v1e18 = new BarEntry(100.000f, 18); // Jun
        valueSet1.add(v1e18);
        BarEntry v1e19 = new BarEntry(100.000f, 19); // Jun
        valueSet1.add(v1e19);
        BarEntry v1e20 = new BarEntry(100.000f, 20); // Jun
        valueSet1.add(v1e20);
        BarEntry v1e21 = new BarEntry(100.000f, 21); // Jun
        valueSet1.add(v1e21);
        BarEntry v1e22 = new BarEntry(100.000f, 22); // Jun
        valueSet1.add(v1e22);
        BarEntry v1e23 = new BarEntry(100.000f, 23); // Jun
        valueSet1.add(v1e23);

        //============

//        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);
//        BarEntry v2e7 = new BarEntry(50.000f, 6); // Jun
//        valueSet2.add(v2e7);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("0");
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        xAxis.add("4");
        xAxis.add("5");
        xAxis.add("6");
        xAxis.add("7");
        xAxis.add("8");
        xAxis.add("9");
        xAxis.add("10");

        xAxis.add("11");
        xAxis.add("12");
        xAxis.add("13");
        xAxis.add("14");
        xAxis.add("15");
        xAxis.add("16");
        xAxis.add("17");
        xAxis.add("18");
        xAxis.add("19");
        xAxis.add("20");

        xAxis.add("21");
        xAxis.add("22");
        xAxis.add("23");

        return xAxis;
    }

}
