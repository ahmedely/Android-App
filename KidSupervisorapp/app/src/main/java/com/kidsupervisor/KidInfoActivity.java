package com.kidsupervisor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.gson.Gson;

public class KidInfoActivity extends AppCompatDialogFragment {

    private TextView editTextKidName;
    private TextView editTextKidAge;
    private TextView editTextNumberDecimal;
    private TextView editTextHeight;
    private FirebaseService firebaseService;
    private Boolean isVisible;
    private Kid kid;
    private AppCompatImageView btnRemoveKid;
    Pref pref;
    private Button positiveButton;
    private Button negativeButton;
    private Button neutralButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        kid = new Kid();
        firebaseService = new FirebaseService(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_kid_info, null);
        btnRemoveKid = view.findViewById(R.id.btnRemoveKid);
        editTextKidName = view.findViewById(R.id.editTextKidName);
        editTextKidAge = view.findViewById(R.id.editTextKidAge);
        editTextNumberDecimal = view.findViewById(R.id.editTextNumberDecimal);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        Gson gson = new Gson();


        if (getArguments() != null) {
            kid = gson.fromJson(getArguments().getString("kid"), Kid.class);
            if (kid != null) {
                editTextKidName.setText(kid.getFullName());
                editTextKidAge.setText(kid.getAge());
                editTextNumberDecimal.setText(kid.getWeight());
                editTextHeight.setText(kid.getHeight());
                editTextKidName.setEnabled(false);
                editTextKidAge.setEnabled(false);
                editTextNumberDecimal.setEnabled(false);
                editTextHeight.setEnabled(false);
            }
        }

        if (getArguments().get("edit").equals(true)) {
            isVisible = true;
            btnRemoveKid.setVisibility(View.VISIBLE);
        } else {
            isVisible = false;
            btnRemoveKid.setVisibility(View.GONE);


        }


        pref = new Pref(getContext());

        if (!pref.getBoolean("Switch")) {
            getActivity().setTheme(R.style.lighttheme);
        } else {
            getActivity().setTheme(R.style.darktheme);
        }


        if (isVisible == true) {


            builder.setPositiveButton("EDIT", null);

        }
        builder.setNegativeButton("CANCEL", null);
        builder.setNeutralButton("SAVE", null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                if (getArguments().get("edit").equals(true)) {

                    positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setVisibility(View.VISIBLE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editTextKidName.setEnabled(true);
                            editTextKidAge.setEnabled(true);
                            editTextNumberDecimal.setEnabled(true);
                            editTextHeight.setEnabled(true);
                        }
                    });
                }
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextKidName.setEnabled(false);
                        editTextKidAge.setEnabled(false);
                        editTextNumberDecimal.setEnabled(false);
                        editTextHeight.setEnabled(false);
                        dialog.dismiss();
                    }
                });
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextKidName.setEnabled(false);
                        editTextKidAge.setEnabled(false);
                        editTextNumberDecimal.setEnabled(false);
                        editTextHeight.setEnabled(false);

                        if (
                                TextUtils.isEmpty(editTextKidName.getText().toString()) ||
                                        TextUtils.isEmpty(editTextKidName.getText().toString()) ||
                                        TextUtils.isEmpty(editTextKidName.getText().toString()) ||
                                        TextUtils.isEmpty(editTextKidName.getText().toString())) {
                        } else {

                            if (isVisible == false) {
                                Kid tempKid = new Kid(
                                        editTextKidName.getText().toString(),
                                        editTextKidAge.getText().toString(),
                                        editTextNumberDecimal.getText().toString(),
                                        editTextHeight.getText().toString()
                                );
                                firebaseService.addKid(tempKid);
                            } else {
                                kid.setFullName(editTextKidName.getText().toString());
                                kid.setAge(editTextKidAge.getText().toString());
                                kid.setWeight(editTextNumberDecimal.getText().toString());
                                kid.setHeight(editTextHeight.getText().toString());
                                firebaseService.editKid(kid);
                            }
                            dialog.dismiss();
                        }

                    }
                });


                btnRemoveKid.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {


                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Alert!");
                        builder.setMessage("Do you want to remove this kid ?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseService.deleteKid(kid.getId());
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

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
                layoutParams.weight = 15;
                negativeButton.setLayoutParams(layoutParams);
                if (isVisible == true) {
                    positiveButton.setLayoutParams(layoutParams);
                }
                neutralButton.setLayoutParams(layoutParams);
            }
        });
        return dialog;
    }
}

