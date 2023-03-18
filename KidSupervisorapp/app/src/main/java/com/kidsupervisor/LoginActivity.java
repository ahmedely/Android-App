package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
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
import com.kidsupervisor.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String email;
    private String password;
    private TextView txt_email;
    private TextView txt_password;
    private FirebaseAuth auth;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new Pref(this);

        if (!pref.getBoolean("Switch")) {
            setTheme(R.style.lighttheme);
        } else {
            setTheme(R.style.darktheme);
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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


        auth = FirebaseAuth.getInstance();
        txt_email = findViewById(R.id.email);
        txt_password = findViewById(R.id.password);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txt_email.getText().toString();
                password = txt_password.getText().toString();


                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }else{
                    signIn(email, password);
                }



            }
        });

        binding.forgotPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });


        binding.newAccountTextViw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}