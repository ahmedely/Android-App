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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;


public class HomeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener{

    private Button addBtn, rmBtn;

    //5 schedules max
    short activated=1;
    private TextView schedule1, schedule2, schedule3, schedule4, schedule5;
    private TextView error;
    boolean firstTime=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBtn = view.findViewById(R.id.addSchedule);
        rmBtn = view.findViewById(R.id.removeSchedule);
        schedule1 = view.findViewById(R.id.schd1);
        schedule2 = view.findViewById(R.id.schd2);
        schedule3 = view.findViewById(R.id.schd3);
        schedule4 = view.findViewById(R.id.schd4);
        schedule5 = view.findViewById(R.id.schd5);
        error=view.findViewById(R.id.errorTxt);

        error.setVisibility(View.GONE);
        schedule1.setVisibility(View.GONE);
        schedule2.setVisibility(View.GONE);
        schedule3.setVisibility(View.GONE);
        schedule4.setVisibility(View.GONE);
        schedule5.setVisibility(View.GONE);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (schedule5.getVisibility() == View.VISIBLE) {
                    error.setVisibility(View.VISIBLE);
                }
                else {

                    error.setVisibility(View.GONE);
                    showTimePickerDialog();
                    showNewSchedule();
                    showTimePickerDialog();
                    showNewSchedule();
                }
            }
        });

        rmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (schedule5.getVisibility() == View.VISIBLE) {
                    schedule5.setVisibility(View.GONE);
                    schedule5.setEnabled(false);

                } else if (schedule4.getVisibility() == View.VISIBLE) {
                    schedule4.setVisibility(View.GONE);
                    schedule4.setEnabled(false);

                } else if (schedule3.getVisibility() == View.VISIBLE) {
                    schedule3.setVisibility(View.GONE);
                    schedule3.setEnabled(false);

                } else if (schedule2.getVisibility() == View.VISIBLE) {
                    schedule2.setVisibility(View.GONE);
                    schedule2.setEnabled(false);

                } else if (schedule1.getVisibility() == View.VISIBLE) {
                    schedule1.setVisibility(View.GONE);
                    schedule1.setEnabled(false);
                }
            }
        });

    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                this,
                hour,
                minute,
                true
        );
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                if (schedule5.getVisibility() == View.VISIBLE) {
                    schedule5.setVisibility(View.GONE);
                    schedule5.setEnabled(false);

                } else if (schedule4.getVisibility() == View.VISIBLE) {
                    schedule4.setVisibility(View.GONE);
                    schedule4.setEnabled(false);

                } else if (schedule3.getVisibility() == View.VISIBLE) {
                    schedule3.setVisibility(View.GONE);
                    schedule3.setEnabled(false);

                } else if (schedule2.getVisibility() == View.VISIBLE) {
                    schedule2.setVisibility(View.GONE);
                    schedule2.setEnabled(false);

                } else if (schedule1.getVisibility() == View.VISIBLE) {
                    schedule1.setVisibility(View.GONE);
                    schedule1.setEnabled(false);
                }
            }
        });
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

        if(activated==1) {
            System.out.println(firstTime);
            if (firstTime == true) {
                schedule1.setText(timeString);
                firstTime=false;
            }
            else {
                schedule1.setText("➊ " + schedule1.getText().toString() + " ➤ " + timeString);
                schedule1.setVisibility(View.VISIBLE);
                schedule1.setEnabled(true);
                firstTime=true;
            }
        }
        else if(activated==2){
            if(firstTime==true) {
                schedule2.setText(timeString);
                firstTime = false;
            }
            else{
                schedule2.setText("➋ " + schedule2.getText().toString() + " ➤ " + timeString);
                schedule2.setVisibility(View.VISIBLE);
                schedule2.setEnabled(true);
                firstTime=true;
            }
        }
        else if(activated==3){
            if(firstTime==true) {
                schedule3.setText(timeString);
                firstTime = false;
            }
            else{
                schedule3.setText("➌ "+schedule3.getText().toString()+ " ➤ " + timeString);
                schedule3.setVisibility(View.VISIBLE);
                schedule3.setEnabled(true);
                firstTime=true;
            }
        }
        else if(activated==4) {
            if (firstTime == true){
                schedule4.setText(timeString);
                firstTime = false;
            }
            else{
                schedule4.setText("➍ " + schedule4.getText().toString()+ " ➤ " + timeString);
                schedule4.setVisibility(View.VISIBLE);
                schedule4.setEnabled(true);
                firstTime=true;
            }
        }
        else if(activated==5){
            if(firstTime==true) {
                schedule5.setText(timeString);
                firstTime = false;
            }
            else {
                schedule5.setText("➎ "+schedule5.getText().toString()+ " ➤ " + timeString);
                schedule5.setVisibility(View.VISIBLE);
                schedule5.setEnabled(true);
                firstTime=true;
            }
        }
    }

    public void showNewSchedule(){

        if (schedule1.getVisibility() != View.VISIBLE) {
            activated=1;
        } else if (schedule2.getVisibility() != View.VISIBLE) {
            activated=2;
        } else if (schedule3.getVisibility() != View.VISIBLE) {
            activated=3;
        } else if (schedule4.getVisibility() != View.VISIBLE) {
            activated=4;
        } else if (schedule5.getVisibility() != View.VISIBLE) {
            activated=5;
        }
    }
}
