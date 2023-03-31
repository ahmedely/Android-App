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

    private TextView email,name,oldPassword,newPassword,confirmPassword;
    private FirebaseService firebaseService;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        firebaseService = new FirebaseService(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, null);

        name = view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        oldPassword=view.findViewById(R.id.oldPass);
        newPassword=view.findViewById(R.id.newPass);
        confirmPassword=view.findViewById(R.id.confirmPass);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("EDIT USER INFO")
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//change password
                        if((!newPassword.toString().isEmpty()) && newPassword.toString().equals(confirmPassword)){
                            Toast.makeText(getContext(), "password changed", Toast.LENGTH_SHORT).show();
                            firebaseService.updatePassword(oldPassword.toString(),newPassword.toString());
                        }
                 //change email
                        if(!email.toString().isEmpty()){
                            firebaseService.updateEmail(email.toString());
                            Toast.makeText(getContext(), "email changed", Toast.LENGTH_SHORT).show();
                        }
                        //change name
                        if(!name.toString().isEmpty()){
                            firebaseService.updateFullName(name.toString());
                            Toast.makeText(getContext(), "name changed", Toast.LENGTH_SHORT).show();

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
