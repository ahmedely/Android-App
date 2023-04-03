package com.kidsupervisor;

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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    private Button btnAddSchedule, btnDeleteSchedule, btnModifySchedule;
    private Button btnAddKid;
    private User user;
    int nmb_of_schedules = 1;
    ListView scheduleList;
    ListView kidsListView;
    private ArrayList<String> kidsList, schedulesList;
    private ArrayAdapter<String> adapter, adapter2;
    private int hrs, min;
    private DatabaseReference databaseRef;
    private String currentValue = "";
    private int lastClicked = -1;
    boolean is_first = true;
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
        schedulesList = new ArrayList<>();
        kidsList = new ArrayList<>();

        kidsListView = view.findViewById(R.id.kidsList);
        btnAddSchedule = view.findViewById(R.id.btnAddSchedule);
        btnDeleteSchedule = view.findViewById(R.id.btnDeleteSchedule);
        btnModifySchedule = view.findViewById(R.id.btnModifySchedule);
        scheduleList = view.findViewById(R.id.scheduleList);
        btnAddKid = view.findViewById(R.id.btnAddKid);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.getKids().clear();
                if (snapshot.exists()) {
                    user.setId(snapshot.child("id").getValue().toString());
                    user.setFullName(snapshot.child("fullName").getValue().toString());
                    user.setEmail(snapshot.child("email").getValue().toString());
                    if(snapshot.child("kids").exists()) {
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
                kidsList.removeAll(schedulesList);
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


        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, schedulesList);
        scheduleList.setAdapter(adapter);


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
                lastClicked = position;
            }
        });
        btnModifySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClicked == -1 || lastClicked == -2) {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                } else {
                    showTimePicker();
                    lastClicked = -1;
                }
            }
        });
        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClicked = -2;
                showTimePicker();
            }
        });

        btnDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClicked == -1 || lastClicked == -2) {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(getContext(), Integer.toString(lastClicked), Toast.LENGTH_SHORT).show();
                    schedulesList.remove(lastClicked);
                    adapter.notifyDataSetChanged();
                    lastClicked = -1;
                    nmb_of_schedules--;
                }
            }
        });

    }


    int first_time_hour,first_time_min,second_time_hour,second_time_min;
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hrs = hourOfDay;
                min = minute;

                if (is_first == true) {

                    if (lastClicked == -2) {
                        currentValue = Integer.toString(nmb_of_schedules) + ". ";
                        currentValue += String.format(Locale.getDefault(), "%02d:%02d", hrs, min);
                    } else {
                        currentValue = Integer.toString(lastClicked + 1) + ". ";
                        currentValue += String.format(Locale.getDefault(), "%02d:%02d", hrs, min);
                    }
                    is_first = false;

                    first_time_hour = hrs;
                    first_time_min = min;
                    Log.e("aaaa", "onTimeSet: "+first_time_hour );
                    showTimePicker();
                } else {
                    currentValue += " ➤ " + String.format(Locale.getDefault(), "%02d:%02d", hrs, min);

                    //second_time = String.format(Locale.getDefault(), "%02d:%02d", hrs, min);

                    second_time_hour = hrs;
                    second_time_min = min;
                    if (lastClicked == -2) {
                        schedulesList.add(currentValue);
                        nmb_of_schedules++;
                    } else {
                        schedulesList.set(lastClicked, currentValue);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime).push();
                    databaseReference.child("start_hour").setValue(first_time_hour);
                    databaseReference.child("start_min").setValue(first_time_min);

                    databaseReference.child("end_hour").setValue(second_time_hour);
                    databaseReference.child("end_min").setValue(second_time_min);


                    //Log.e("ppo", "onTimeSet: "+ second_time);

                    adapter.notifyDataSetChanged();
                    is_first = true;
                }
            }
        }, hrs, min, true);

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.dismiss();
                    lastClicked = -1;
                    is_first = true;
                }
            }
        });
        timePickerDialog.show();
    }
}