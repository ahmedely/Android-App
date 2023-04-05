package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button btnResetPassword;
    private TextView txtEmail;
    private String email;
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = new Pref(this);

        if (!pref.getBoolean()) {
            setTheme(R.style.lighttheme);
        } else {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_reset_password);
        auth = FirebaseAuth.getInstance();
        btnResetPassword = findViewById(R.id.btnResetPassword);
        txtEmail = findViewById(R.id.txtEmail);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString();
                auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        Toast.makeText(ResetPasswordActivity.this, "Email was sent.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}