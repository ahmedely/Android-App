package com.kidsupervisor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kidsupervisor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    ActivityMainBinding binding;

    Pref pref;

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment camraFragment = new CameraFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private final Fragment statsFragment = new StatsFragment();
    private Fragment activeFragment = homeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new Pref(this);
        if (!pref.getBoolean("Switch")) {
            setTheme(R.style.lighttheme);
        } else {
            setTheme(R.style.darktheme);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager.beginTransaction().add(R.id.fragmentLayout, settingsFragment, "4").hide(settingsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, statsFragment, "3").hide(statsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, camraFragment, "2").hide(camraFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentLayout, homeFragment, "1").commit();


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);


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

}