package com.kidsupervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kidsupervisor.databinding.ActivityMainBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    ActivityMainBinding binding;

    Pref pref;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment camraFragment = new CameraFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private final Fragment statsFragment = new StatsFragment();
    private Fragment activeFragment = homeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    private FirebaseService firebaseService;

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
        pref.setLogInStatus(true);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager.beginTransaction().add(R.id.fragmentLayout, settingsFragment, "4").hide(settingsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, statsFragment, "3").hide(statsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, camraFragment, "2").hide(camraFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, homeFragment, "1").commit();

        // Go to Camera fragment on Notification click
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("menuFragment")) {
                binding.bottomNavigationView.setSelectedItemId(R.id.camera);
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert!");
                builder.setMessage("Is your Baby awake ?");
                builder.setCancelable(true);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String year = dtf.format(now).split("/")[0];
                        String month = dtf.format(now).split("/")[1];
                        String day = dtf.format(now).split("/")[2].split("\\s+")[0];
                        String time = dtf.format(now).split("/")[2].split("\\s+")[1];
                        firebaseService.addDate(year, month, day, time, false);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        builder.show();

                    }
                }, 5000);


            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:

                if (activeFragment != homeFragment) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;

                }
                return true;

            case R.id.camera:
                if (activeFragment != camraFragment) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(camraFragment).commit();
                    activeFragment = camraFragment;

                }
                return true;

            case R.id.stats:
                if (activeFragment != statsFragment) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(statsFragment).commit();
                    activeFragment = statsFragment;

                }
                return true;

            case R.id.settings:
                if (activeFragment != settingsFragment) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(settingsFragment).commit();
                    activeFragment = settingsFragment;

                }
                return true;
        }
        return false;
    }

    // Go to Camera fragment on Notification click
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            if (extras.containsKey("menuFragment")) {
//               // binding.bottomNavigationView.setSelectedItemId(R.id.camera);
//                System.out.println("HELLO");
//                Toast.makeText(this, "HELLo", Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder;
//                builder = new AlertDialog.Builder(this);
//                builder.setTitle("Alert!");
//                builder.setMessage("Is your Baby awake ?");
//                builder.setCancelable(true);
//                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.show();
//            }
//        }
//    }
}