package com.kidsupervisor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private Button btnAddSchedule, btnAddKid, changeBabyState;
    private TextView baby_state;
    private User user;
    ListView scheduleList;
    ListView kidsListView;
    private ArrayList<String> kidsList, eventsList, schedulesList;

    private ArrayAdapter<String> adapter, adapter2;
    private ArrayList<Event> events;
    private DatabaseReference databaseRef;
    private DatabaseReference eventDatabaseRef;
    private String currentValue = "";
    private FirebaseService firebaseService;
    private FirebaseAuth auth;
    private Bundle bundle; // Used to share variable between activities/fragments

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = new Bundle();
        firebaseService = new FirebaseService(getContext());
        auth = FirebaseAuth.getInstance();
        user = new User();

        kidsListView = view.findViewById(R.id.kidsList);
        btnAddSchedule = view.findViewById(R.id.btnAddSchedule);
        scheduleList = view.findViewById(R.id.scheduleList);
        btnAddKid = view.findViewById(R.id.btnAddKid);
        changeBabyState = view.findViewById(R.id.babyStateBtn);
        baby_state = view.findViewById(R.id.baby_state);

        schedulesList = new ArrayList<>();
        kidsList = new ArrayList<>();
        eventsList = new ArrayList<>();
        events = new ArrayList<>();


        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.getKids().clear();
                if (snapshot.exists()) {
                    user.setId(snapshot.child("id").getValue().toString());
                    user.setFullName(snapshot.child("fullName").getValue().toString());
                    user.setEmail(snapshot.child("email").getValue().toString());
                    if (snapshot.child("kids").exists()) {
                        for (DataSnapshot kidSnapshot : snapshot.child("kids").getChildren()) {
                            Kid kid = new Kid();
                            kid.setId(kidSnapshot.child("id").getValue().toString());
                            kid.setFullName(kidSnapshot.child("fullName").getValue().toString());
                            kid.setAge(kidSnapshot.child("age").getValue().toString());
                            kid.setWeight(kidSnapshot.child("weight").getValue().toString());
                            kid.setHeight(kidSnapshot.child("height").getValue().toString());
                            user.addKid(kid);
                        }

                    }
                }
                kidsList.removeAll(kidsList);
                kidsList = new ArrayList<>();
                for (Kid kid : user.getKids()) {
                    kidsList.add(kid.getFullName());
                }
                adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, kidsList);
                kidsListView.setAdapter(adapter2);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eventDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Events");
        eventDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events.clear();
                events = new ArrayList<>();
                eventsList.clear();
                eventsList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = new Event();
                        event.setId(eventSnapshot.child("id").getValue().toString());
                        event.setTitle(eventSnapshot.child("title").getValue().toString());
                        event.setDescription(eventSnapshot.child("description").getValue().toString());

                        events.add(event);
                        eventsList.add(" ➤ " + event.getTitle() + ": " + event.getDescription());
                    }

                }

                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, eventsList);
                scheduleList.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnAddKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KidInfoActivity dialogFragment = new KidInfoActivity();
                bundle.putBoolean("edit", false);
                bundle.putString("kid", null);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");

            }
        });

        changeBabyState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baby_state.getText().toString().equals("AWAKE")) {
                    baby_state.setText("SLEEPING");
                    baby_state.setBackgroundResource(R.drawable.btn_3);
                } else {
                    baby_state.setText("AWAKE");
                    baby_state.setBackgroundResource(R.drawable.btn_bg);
                }
                addStartSleepTime();
            }
        });

        kidsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                KidInfoActivity dialogFragment = new KidInfoActivity();
                Gson gson = new Gson();
                String kid = gson.toJson(user.getKids().get(i));
                bundle.putString("kid", kid); // Stores kid to be displayed in dialogFragment
                bundle.putBoolean("edit", true); // Stores Boolean to know if edit button should be visible
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //save last clicked
                Gson gson = new Gson();
                String event = gson.toJson(events.get(position));
                bundle.putString("event", event);
                bundle.putBoolean("edit", true);
                EventFragment eventFragment = new EventFragment();
                eventFragment.setArguments(bundle);
                eventFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        //add event
        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventFragment eventFragment = new EventFragment();
                //  eventFragment.setArguments(bundle);
                eventFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });


    }

    private void addStartSleepTime() {
        String docId = String.valueOf(System.currentTimeMillis());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("kid" , Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("doc", docId).apply();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime).child(docId);

        Date date =  new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int mint  = cal.get(Calendar.MINUTE);
        databaseReference.child("start_hour").setValue(hours);
        databaseReference.child("start_min").setValue(mint);

        databaseReference.child("end_hour").setValue(000);
        databaseReference.child("end_min").setValue(000);
    }

    //========================
    private void fetch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime);

        FirebaseRecyclerOptions<ModelD> options
                = new FirebaseRecyclerOptions.Builder<ModelD>()
                .setQuery(query, ModelD.class)
                .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ModelD, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_s, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position, ModelD model) {


                holder.tvTitle.setText(model.getStart_hour()+":"+model.getStart_min()+" ➤ "+ model.end_hour+":"+ model.end_min);


                holder.tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Alert!");
                        builder.setMessage("Is your Baby awake ?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());

                                SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
                                String strHOur = sdfHour.format(new Date());

                                SimpleDateFormat sdfMin = new SimpleDateFormat("mm");
                                String strMin = sdfMin.format(new Date());
                                DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime).child(getRef(position).getKey());
                                query.child("end_hour").setValue(Integer.parseInt(strHOur));
                                query.child("end_min").setValue(Integer.parseInt(strMin));

                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                });


            }

        };
        adapter.startListening();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        TextView tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            mView =itemView;

            tvTitle = mView.findViewById(R.id.tvTitle);





        }


    }




}