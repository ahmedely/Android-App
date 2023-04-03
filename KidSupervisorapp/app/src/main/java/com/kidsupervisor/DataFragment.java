package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DataFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.RecycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CalendarView calendarView = v.findViewById(R.id.calendarView);

        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String dayString = String.valueOf(day);
                if (day < 10) {
                    dayString = "0" + dayString;
                }

                String monthString = String.valueOf(month + 1);
                if ((month + 1) < 10) {
                    monthString = "0" + monthString;
                }

                String newDate = year + "/" + monthString + "/" + dayString;

                new Handler().postDelayed(() -> {

                    fetch(newDate);

                }, 2000);

                adapter.startListening();

            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        Log.e("ooo2o", "onSelectedDayChange: "+currentDateandTime );

        fetch(currentDateandTime);


        return v;
    }


    private void fetch(String date) {

        DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date);



        FirebaseRecyclerOptions<ModelD> options
                = new FirebaseRecyclerOptions.Builder<ModelD>()
                .setQuery(query, ModelD.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ModelD, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ModelD model) {
                holder.tvTitle.setText(model.getStart_hour()+":"+model.getStart_min()+" -- "+
                        model.getEnd_hour()+":"+model.getEnd_min());
                holder.tvDate.setText(date);

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intent = new Intent(getActivity(),GraphActivity.class);
                        intent.putExtra("date",date);
                        startActivity(intent);
                    }
                });
            }


            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list, parent, false);

                return new ViewHolder(view);
            }




        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        TextView tvTitle,tvDate;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

            card = mView.findViewById(R.id.card);


            tvDate = mView.findViewById(R.id.tvDate);
            tvTitle = mView.findViewById(R.id.tvTitle);



        }


    }
}