package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {
    private Switch changeTheme;
    private Pref pref;
    private Button signOutBut,edit_Profile,helpBtn,changeLanguage;

    private TextView profileName;
    private DatabaseReference databaseRef,onChangeDatabaseRef;
    private FirebaseAuth auth;
    private User currentUser;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = new User();
        pref = new Pref(getActivity());
        changeTheme = view.findViewById(R.id.switch_btn);
        edit_Profile = view.findViewById(R.id.editPrfl);
        profileName = view.findViewById(R.id.profile_name);

        bundle = new Bundle();

        changeTheme.setChecked(pref.getBoolean("Switch"));
        auth = FirebaseAuth.getInstance();
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeTheme.isChecked()) {
                    pref.setBoolean("Switch", true);
                    changeTheme.setChecked(true);
                } else {
                    pref.setBoolean("Switch", false);
                    changeTheme.setChecked(false);
                }

            }
        });

        edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to dialogFragment
                EditUserProfile dialogFragment = new EditUserProfile();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });


        signOutBut = (Button) view.findViewById(R.id.sign_out);
        signOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setUserStatus(true);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                pref.setLogInStatus();
                getActivity().finish();
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                    profileName.setText(task.getResult().child("fullName").getValue().toString());
            }
        });

        if (auth.getCurrentUser() != null) {
            onChangeDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
            onChangeDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser.getKids().clear();
                    if (snapshot.exists()) {
                        if (snapshot.child("id").exists()) {
                            currentUser.setId(snapshot.child("id").getValue().toString());
                        }else{
                            currentUser.setId("");
                        }
                        if (snapshot.child("fullName").exists()) {
                            currentUser.setFullName(snapshot.child("fullName").getValue().toString());
                        }else{
                            currentUser.setFullName("");
                        }
                        if (snapshot.child("email").exists()) {
                            currentUser.setEmail(snapshot.child("email").getValue().toString());

                        }else{
                            currentUser.setEmail("");
                        }

                        bundle.putString("fullName",currentUser.getFullName());
                        bundle.putString("email",currentUser.getEmail());

                        profileName.setText(currentUser.getFullName());



                        if(snapshot.child("kids").exists()) {
                            for (DataSnapshot kidSnapshot : snapshot.child("kids").getChildren()) {
                                Kid kid = new Kid();
                                kid.setId(kidSnapshot.child("id").getValue().toString());
                                kid.setFullName(kidSnapshot.child("fullName").getValue().toString());
                                kid.setAge(kidSnapshot.child("age").getValue().toString());
                                kid.setWeight(kidSnapshot.child("weight").getValue().toString());
                                kid.setHeight(kidSnapshot.child("height").getValue().toString());
                                currentUser.addKid(kid);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}