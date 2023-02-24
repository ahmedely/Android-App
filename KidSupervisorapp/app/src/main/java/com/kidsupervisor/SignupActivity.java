package com.kidsupervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kidsupervisor.databinding.ActivityMainBinding;
import com.kidsupervisor.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtConfirmPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        System.out.println(auth);

        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        txtConfirmPassword = findViewById(R.id.confirmPassword);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();
                confirmPassword = txtConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Must complete everything", Toast.LENGTH_SHORT).show();
                } else if (!confirmPassword.equals(password)) {
                    Toast.makeText(SignupActivity.this, "Passwords don t match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be more than 5 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(email, password);
                }
            }
        });
        binding.newAccountTextViw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    private void signUp(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Sign up Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}