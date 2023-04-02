package com.kidsupervisor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kidsupervisor.databinding.ActivityLoginBinding;
import com.kidsupervisor.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private String email;
    private String password;
    private TextView txt_email;
    private TextView txt_password;
    private FirebaseAuth auth;
    public static final String CHANNEL_1 = "channel1";
    private DatabaseReference databaseRef;
    private FirebaseService firebaseService;
    static int counter = 0;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = new Pref(this);
        firebaseService = new FirebaseService(LoginActivity.this);

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
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Sensors").child("0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1, "Channel 1 :)", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txt_email.getText().toString();
                password = txt_password.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
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
        // Notification part
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists() && (snapshot.getKey().equals("movementSensor") || snapshot.getKey().equals("soundSensor"))) {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("menuFragment", "MyFragment");
                    PendingIntent pendingIntent = PendingIntent.getActivity(LoginActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(LoginActivity.this, CHANNEL_1);
                    builder.setContentTitle("Movement and sound detect");
                    builder.setContentText("Click to monitor your child");
                    builder.setSmallIcon(R.drawable.ic_mail);
                    builder.setAutoCancel(true);
                    builder.setContentIntent(pendingIntent);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(LoginActivity.this);
                    managerCompat.notify(counter, builder.build());
                    counter++;
                    Toast.makeText(LoginActivity.this, "Data was modified", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(pref.getUserStatus()==true){
                        startActivity(new Intent(LoginActivity.this, TutorialActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                else {
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