package com.kidsupervisor;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class FirebaseService {

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private Context context;

    public FirebaseService(Context context) {
        this.auth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
        this.context = context;


    }

    public void addUser(User user) {
        this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String id = auth.getUid();
                    user.setId(id);
                    databaseRef.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "User was added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
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

    public void updateEmail(String oldPassword, String newEmail) {
        if (auth.getCurrentUser() != null) {


            AuthCredential credential = EmailAuthProvider
                    .getCredential(auth.getCurrentUser().getEmail(), oldPassword); // Current Login Credentials \\
            auth.getCurrentUser().reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            auth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("email");

                                        databaseRef.setValue(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                        Toast.makeText(context, "Email was modified", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Email was not modified", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });
        } else {
            Toast.makeText(context, "Email was not modified", Toast.LENGTH_SHORT).show();
        }


    }


    public void updatePassword(String oldPassword, String newPassword) {
        Toast.makeText(context, "UPDATE PASS", Toast.LENGTH_SHORT).show();

        if (auth.getCurrentUser() != null) {

            AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(), oldPassword);
            auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Password was modified", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "Incorrect Password! Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(context, "NOT WORKING", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateFullName(String fullName) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("fullName");
            databaseRef.setValue(fullName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Your name was modified", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Your name was not modified", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    public void addDate(String year, String month, String day, String time, Boolean isSleeping) {
//        if (auth.getCurrentUser() != null) {
//            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Schedules").child(year).child(month).child(day).child(time).child("isSleeping");
//            this.databaseRef.setValue(isSleeping);
//
//        }
//
//
//    }
public void addDate(String year, String month, String day, String time, Boolean isSleeping) {
    SharedPreferences sharedPreferences = context.getSharedPreferences("kid" , Context.MODE_PRIVATE);
    String docId = sharedPreferences.getString("doc" ,"");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    String currentDateandTime = sdf.format(new Date());
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime).child(docId);

    Date date =  new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int hours = cal.get(Calendar.HOUR_OF_DAY);
    int mint  = cal.get(Calendar.MINUTE);
//        databaseReference.child("start_hour").setValue(hours);
//        databaseReference.child("start_min").setValue(mint);

    databaseReference.child("end_hour").setValue(hours);
    databaseReference.child("end_min").setValue(mint);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
//        String currentDateandTime = sdf.format(new Date());
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Schedules").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentDateandTime).push();
//
//        Date date =  new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int hours = cal.get(Calendar.HOUR_OF_DAY);
//        int mint  = cal.get(Calendar.MINUTE);
//        databaseReference.child("start_hour").setValue(hours);
//        databaseReference.child("start_min").setValue(mint);
////
//        databaseReference.child("end_hour").setValue(hours+3);
//        databaseReference.child("end_min").setValue(mint);


}



    public ArrayList<Schedule> getDate(String year, String month, String day) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Schedules").child(year).child(month).child(day);
            this.databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String time = snapshot.getKey().toString();
                        Boolean isSleeping = (Boolean) snapshot.child("isSleeping").getValue();
                        Schedule schedule = new Schedule(time, isSleeping);
                        schedules.add(schedule);
                    }
                }
            });
        }
        return schedules;
    }

    public void addEvent(Event event) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Events");
            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        databaseRef.child(event.getId()).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Event added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void editEvent(Event event) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Events").child(event.getId());
            databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        databaseRef.setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Event was changed successfully", Toast.LENGTH_SHORT).show();
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

    public void deleteEvent(String id) {
        if (auth.getCurrentUser() != null) {
            this.databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Events").child(id);
            databaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Event was removed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void setBabyState(boolean state){
        this.databaseRef = FirebaseDatabase.getInstance().getReference().child("isSleeping");
        this.databaseRef.setValue(state).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}