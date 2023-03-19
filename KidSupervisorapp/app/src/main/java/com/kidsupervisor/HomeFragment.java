package com.kidsupervisor;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private Button addBtn, rmBtn, modifyBtn;

    int nmb_of_schedules = 1;
    ListView listView;

    Spinner spinner;
    private ArrayList<String> myArr,myArr2;
    private ArrayAdapter<String> adapter,adapter2;
    private int hrs, min;

    String currentValue="";
    int lastClicked=-1;
    boolean is_first=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBtn = view.findViewById(R.id.addSchedule);
        rmBtn = view.findViewById(R.id.rmBtn);
        spinner=view.findViewById(R.id.spinner);
        modifyBtn = view.findViewById(R.id.modifyBtn);
        listView = view.findViewById(R.id.list);


        myArr = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myArr);
        listView.setAdapter(adapter);


        myArr2=new ArrayList<>();
        myArr2.add("Add a kid");
        myArr2.add("abdoullah");
        myArr2.add("ayadi");
        myArr2.add("ayadi2");
        myArr2.add("ayadi3");

        adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myArr2);
        spinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(myArr2.get(i).equals("Add a kid")) {
                    DialogFragment registerKid = new DialogFragment(){
                    @Override
                    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
                        return inflater.inflate(R.layout.activity_kid_info, container, false);
                    }
                };
                //    registerKid.show(getParentFragmentManager(), "Register");
                 //add kid
                    //   myArr2.add(0,);
                    adapter2.notifyDataSetChanged();
                }
                else{
                    //show data for correct kid
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //save last clicked
                lastClicked=position;
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClicked == -1 || lastClicked == -2) {
                    Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                }
                else {
                    showTimePicker();
                    lastClicked=-1;
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClicked=-2;
                showTimePicker();
            }
        });

        rmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                    if (lastClicked == -1 || lastClicked == -2) {
                        Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(getContext(), Integer.toString(lastClicked), Toast.LENGTH_SHORT).show();
                        myArr.remove(lastClicked);
                        adapter.notifyDataSetChanged();
                        lastClicked=-1;
                        nmb_of_schedules--;
                    }
                }
        });

    }


    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hrs = hourOfDay;
                min = minute;

                if(is_first==true) {

                    if(lastClicked==-2) {
                        currentValue=Integer.toString(nmb_of_schedules) + ". ";
                        currentValue +=String.format(Locale.getDefault(), "%02d:%02d", hrs, min);
                    }
                    else{
                        currentValue=Integer.toString(lastClicked+1) + ". ";
                        currentValue +=String.format(Locale.getDefault(), "%02d:%02d", hrs, min);
                    }
                    is_first=false;
                    showTimePicker();
                }
                else {
                    currentValue +=" ➤ " + String.format(Locale.getDefault(), "%02d:%02d", hrs, min);
                    if(lastClicked==-2) {
                        myArr.add(currentValue);
                        nmb_of_schedules++;
                    }
                    else{
                        myArr.set(lastClicked,currentValue);
                    }
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
                    lastClicked=-1;
                    is_first=true;
                }
            }
        });

        timePickerDialog.show();

    }
}