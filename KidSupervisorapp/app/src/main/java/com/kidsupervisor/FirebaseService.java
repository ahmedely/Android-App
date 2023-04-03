package com.kidsupervisor;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class FirebaseService {

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private Context context;

    public FirebaseService(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        context = context;

    }

    public void addKid(Kid kid) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("kids");
            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        String id = UUID.randomUUID().toString();
                        kid.setId(id);
                        databaseRef.child(id).setValue(kid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Kid was added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void editKid(Kid kid) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("kids").child(kid.getId());
            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        databaseRef.setValue(kid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Kid information were changed successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    public void deleteKid(String id) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("kids").child(id);
            databaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Kid was removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
