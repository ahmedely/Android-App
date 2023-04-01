package com.kidsupervisor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsFragment extends Fragment {
    private Switch changeTheme;
    private Pref pref;
    private Button signOutBut;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;
    private FirebaseService firebaseService;
    private User currentUser;

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
        changeTheme.setChecked(pref.getBoolean("Switch"));
        auth = FirebaseAuth.getInstance();
        firebaseService = new FirebaseService(getContext());
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

        signOutBut = (Button) view.findViewById(R.id.sign_out);
        signOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        if (auth.getCurrentUser() != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());

            databaseRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    currentUser.getKids().clear();
                    if (snapshot.exists()) {

                        currentUser.setId(snapshot.child("id").getValue().toString());
                        currentUser.setFullName(snapshot.child("fullName").getValue().toString());
                        currentUser.setEmail(snapshot.child("email").getValue().toString());
                        currentUser.setSelectedKid(snapshot.child("selectedKid").getValue().toString());
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
    }

}