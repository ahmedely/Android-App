package com.kidsupervisor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.UUID;

public class EventFragment extends AppCompatDialogFragment {
    private Button positiveButton;
    private Button negativeButton;
    private FirebaseService firebaseService;
    private EditText txtTitle, txtDescription;
    private Boolean edit = false;
    private Event displayedEvent;
    private AppCompatImageView btnRemoveEvent;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        firebaseService = new FirebaseService(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_event_fragment, null);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtDescription = view.findViewById(R.id.txtDescription);
        btnRemoveEvent = view.findViewById(R.id.btnRemoveEvent);
        displayedEvent = new Event();
        Gson gson = new Gson();
        if (getArguments() != null) {
            displayedEvent = gson.fromJson(getArguments().getString("event"), Event.class);
            edit = getArguments().getBoolean("edit");
            if (displayedEvent != null) {
                txtTitle.setText(displayedEvent.getTitle());
                txtDescription.setText(displayedEvent.getDescription());
            }

        }

        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("SAVE", null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                if (edit == true) {
                    btnRemoveEvent.setVisibility(View.VISIBLE);
                    btnRemoveEvent.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Alert!");
                            builder.setMessage("Do you want to remove this event ?");
                            builder.setCancelable(true);
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    firebaseService.deleteEvent(displayedEvent.getId());
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                            return false;
                        }
                    });
                } else {
                    btnRemoveEvent.setVisibility(View.GONE);
                }
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edit == true) {
                            displayedEvent = new Event(displayedEvent.getId(), txtTitle.getText().toString(), txtDescription.getText().toString());
                            firebaseService.editEvent(displayedEvent);
                        } else {
                            String id = UUID.randomUUID().toString();
                            Event event = new Event(id, txtTitle.getText().toString(), txtDescription.getText().toString());
                            firebaseService.addEvent(event);
                        }
                        dialog.dismiss();
                    }
                });
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                layoutParams.weight = 15;
                negativeButton.setLayoutParams(layoutParams);
                positiveButton.setLayoutParams(layoutParams);
            }
        });


        return dialog;
    }
}