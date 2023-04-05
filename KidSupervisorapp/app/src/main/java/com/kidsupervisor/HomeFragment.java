package com.kidsupervisor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
}