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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    Switch changeTheme;
    Pref pref;
    Button signOutBut;
    FloatingActionButton addKidBut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new Pref(getActivity());

        changeTheme = view.findViewById(R.id.switch_btn);

        changeTheme.setChecked(pref.getBoolean("Switch"));

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
                startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                getActivity().finish();
            }
        });


        addKidBut = (FloatingActionButton) view.findViewById(R.id.add_kidFloat);
        signOutBut = (Button) view.findViewById(R.id.sign_out);

        addKidBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KidInfoActivity.class));
            }
        });
        signOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });


    }
}