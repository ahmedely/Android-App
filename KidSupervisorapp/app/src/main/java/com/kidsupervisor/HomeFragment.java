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
    RecyclerView scheduleList2;
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

        scheduleList2 = view.findViewById(R.id.scheduleList2);
        scheduleList2.setLayoutManager(new LinearLayoutManager(getActivity()));

        kidsListView = view.findViewById(R.id.kidsList);
        btnAddSchedule = view.findViewById(R.id.btnAddSchedule);
        btnDeleteSchedule = view.findViewById(R.id.btnDeleteSchedule);
        btnModifySchedule = view.findViewById(R.id.btnModifySchedule);
        scheduleList = view.findViewById(R.id.scheduleList);
        btnAddKid = view.findViewById(R.id.btnAddKid);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user.getKids().clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals(auth.getUid())) {
                            System.out.println("ID : " + dataSnapshot.child("id").getValue().toString());
                            user.setId(dataSnapshot.child("id").getValue().toString());
                            user.setFullName(dataSnapshot.child("fullName").getValue().toString());
                            user.setEmail(dataSnapshot.child("email").getValue().toString());
//                            user.setSelectedKid(dataSnapshot.child("selectedKid").getValue().toString());
//                            for (DataSnapshot kidSnapshot : dataSnapshot.child("kids").getChildren()) {
//                                Kid kid = new Kid();
//                                kid.setId(kidSnapshot.child("id").getValue().toString());
//                                kid.setFullName(kidSnapshot.child("fullName").getValue().toString());
//                                kid.setAge(kidSnapshot.child("age").getValue().toString());
//                                kid.setWeight(kidSnapshot.child("weight").getValue().toString());
//                                kid.setHeight(kidSnapshot.child("height").getValue().toString());
//                                user.addKid(kid);
//                            }
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

        fetch();
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
        scheduleList2.setAdapter(adapter);
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