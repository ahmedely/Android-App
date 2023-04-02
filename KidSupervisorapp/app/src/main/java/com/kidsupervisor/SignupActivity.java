package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseService firebaseService;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseService = new FirebaseService(this);
        pref = new Pref(this);
        if (!pref.getBoolean("Switch")) {
            setTheme(R.style.lighttheme);
        } else {
            setTheme(R.style.darktheme);
        }
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.hideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    if (!pref.getBoolean("Switch")) {

                        binding.hideshow.setImageResource(R.drawable.hide);
                        binding.hideshow.setImageTintList(getResources().getColorStateList(R.color.black));
                    } else {
                        binding.hideshow.setImageResource(R.drawable.hide);
                        binding.hideshow.setImageTintList(getResources().getColorStateList(R.color.white));
                    }


                } else {

                    if (!pref.getBoolean("Switch")) {
                        binding.hideshow.setImageResource(R.drawable.ic_eye);
                    } else {
                        binding.hideshow.setImageResource(R.drawable.ic_eye_dark);


                    }
                    binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                binding.password.setSelection(binding.password.length());
            }
        });
        binding.hideshow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.confirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    if (!pref.getBoolean("Switch")) {

                        binding.hideshow1.setImageResource(R.drawable.hide);
                        binding.hideshow1.setImageTintList(getResources().getColorStateList(R.color.black));
                    } else {
                        binding.hideshow1.setImageResource(R.drawable.hide);
                        binding.hideshow1.setImageTintList(getResources().getColorStateList(R.color.white));
                    }


                } else {

                    if (!pref.getBoolean("Switch")) {

                        binding.hideshow1.setImageResource(R.drawable.ic_eye);
                    } else {
                        binding.hideshow1.setImageResource(R.drawable.ic_eye_dark);


                    }
                    binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                binding.confirmPassword.setSelection(binding.confirmPassword.length());
            }
        });


        auth = FirebaseAuth.getInstance();
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
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Must complete everything", Toast.LENGTH_SHORT).show();
                } else if (!confirmPassword.equals(password)) {
                    Toast.makeText(SignupActivity.this, "Passwords don t match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be more than 5 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(name,email, password);
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

    private void signUp(String fullName,String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(fullName,email);
                    firebaseService.addUser(user);
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    Toast.makeText(SignupActivity.this, "Account created", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Sign up Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}