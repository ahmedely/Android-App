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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditUserProfile  extends AppCompatDialogFragment {

    private TextView email,oldPassword,newPassword,confirmPassword;
    private FirebaseService firebaseService;
    private String currentEmail;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        firebaseService = new FirebaseService(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, null);

        email=view.findViewById(R.id.email);
        oldPassword=view.findViewById(R.id.oldPass);
        newPassword=view.findViewById(R.id.newPass);
        confirmPassword=view.findViewById(R.id.confirmPass);

        if(getArguments()!=null){
            if(getArguments().getString("email")!=null){
                currentEmail=getArguments().getString("email");
                email.setText(currentEmail);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("EDIT USER INFO")
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if((!newPassword.getText().toString().isEmpty()) && newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                            if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                firebaseService.updatePassword(oldPassword.getText().toString(), newPassword.getText().toString());
                            }
                        }
                 //change email
                        if(!email.getText().toString().isEmpty() && !oldPassword.getText().toString().isEmpty() && !email.getText().toString().equals(currentEmail)){
                            firebaseService.updateEmail(oldPassword.getText().toString(),email.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
