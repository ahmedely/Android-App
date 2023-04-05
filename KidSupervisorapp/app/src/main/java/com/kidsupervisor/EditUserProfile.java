package com.kidsupervisor;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;

public class EditUserProfile  extends AppCompatDialogFragment {

    private TextView email,oldPassword,newPassword,confirmPassword;
    private FirebaseService firebaseService;
    private FirebaseAuth auth;
    private String currentEmail;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        firebaseService = new FirebaseService(getContext());
        this.auth = FirebaseAuth.getInstance();

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
                        if (!oldPassword.getText().toString().isEmpty()) {
                            if (newPassword.length() >= 6) {
                                if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                                    firebaseService.updatePassword(oldPassword.getText().toString(), newPassword.getText().toString());
                                } else {
                                    Toast.makeText(getContext(), "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "Current password missing", Toast.LENGTH_SHORT).show();
                        }


                        if(!email.getText().toString().isEmpty() && !oldPassword.getText().toString().isEmpty()){
                            if(!auth.getCurrentUser().getEmail().equals(email.getText().toString())){
                            firebaseService.updateEmail(oldPassword.getText().toString(),email.getText().toString());
                            }
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
