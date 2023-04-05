package com.kidsupervisor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class SettingsFragment extends Fragment {
    private Switch changeTheme;

    private FirebaseService firebaseService;
    private Pref pref;
    ImageView myImage;
    private LinearLayout signOutBut;
    private Button edit_Profile;

    private RelativeLayout guide_btn, aboutUs, reportBugs, btn_FAQs, infosBtn, editEmPass;
    private TextView profileName, emailProfile;
    private DatabaseReference databaseRef, onChangeDatabaseRef;
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


        firebaseService = new FirebaseService(getContext());

        currentUser = new User();
        pref = new Pref(getActivity());
        changeTheme = view.findViewById(R.id.switch_btn);
        edit_Profile = view.findViewById(R.id.editPrfl);
        profileName = view.findViewById(R.id.profile_name);
        emailProfile = view.findViewById(R.id.emailTxt);
        guide_btn = view.findViewById(R.id.guideBtn);
        aboutUs = view.findViewById(R.id.aboutBtn);
        reportBugs = view.findViewById(R.id.reportBugs_btn);
        btn_FAQs = view.findViewById(R.id.faqsBtn);
        infosBtn = view.findViewById(R.id.infos);
        editEmPass = view.findViewById(R.id.editAccount);
        myImage = view.findViewById(R.id.profile_image);
        profileName.setEnabled(false);


        bundle = new Bundle();

        changeTheme.setChecked(false);
        auth = FirebaseAuth.getInstance();
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changeTheme.isChecked()) {
                    pref.setBoolean(true);
                    changeTheme.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    pref.setBoolean(false);
                    changeTheme.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

            }
        });

        edit_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastName = "";
                if (edit_Profile.getText().toString().equals("Edit")) {
                    profileName.setEnabled(true);
                    edit_Profile.setText("Save");
                    lastName = profileName.getText().toString();
                } else {
                    profileName.setEnabled(false);
                    if (!profileName.getText().toString().isEmpty()) {
                        firebaseService.updateFullName(profileName.getText().toString());
                    } else {
                        profileName.setText(lastName);
                    }
                    edit_Profile.setText("Edit");
                }

            }
        });
        editEmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserProfile dialogFragment = new EditUserProfile();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                pickImageActivityResultLauncher.launch(intent);
            }
        });

        signOutBut = view.findViewById(R.id.sign_out_btn);
        signOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setUserStatus(false);
                pref.setPrevFragment(false);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        guide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.setPrevFragment(true);
                startActivity(new Intent(getActivity(), TutorialActivity.class));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutFragment dialogFragment = new AboutFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        reportBugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReportBugDialog dialogFragment = new ReportBugDialog();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");

            }
        });

        btn_FAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQsFragment dialogFragment = new FAQsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        infosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Useful_info dialogFragment = new Useful_info();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    profileName.setText(task.getResult().child("fullName").getValue().toString());
                }
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
                        } else {
                            currentUser.setId("");
                        }
                        if (snapshot.child("fullName").exists()) {
                            currentUser.setFullName(snapshot.child("fullName").getValue().toString());
                        } else {
                            currentUser.setFullName("");
                        }
                        if (snapshot.child("email").exists()) {
                            currentUser.setEmail(snapshot.child("email").getValue().toString());

                        } else {
                            currentUser.setEmail("");
                        }

                        bundle.putString("email", currentUser.getEmail());

                        profileName.setText(currentUser.getFullName());
                        emailProfile.setText(currentUser.getEmail());



                        if (snapshot.child("kids").exists()) {
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

    public String getPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        final String path = getPathFromURI(selectedImageUri);
                        if (path != null) {
                            File f = new File(path);
                            selectedImageUri = Uri.fromFile(f);
                        }
                        myImage.setImageURI(selectedImageUri);
                    }
                }
            });
}