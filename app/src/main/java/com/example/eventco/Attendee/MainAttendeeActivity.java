package com.example.eventco.Attendee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.eventco.Attendee.Fragments.HistoryFragment;
import com.example.eventco.Attendee.Fragments.HomeFragment;
import com.example.eventco.Attendee.Fragments.ProfileFragment;
import com.example.eventco.R;
import com.example.eventco.databinding.ActivityMainAttendeeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainAttendeeActivity extends AppCompatActivity {
    private ActivityMainAttendeeBinding binding ;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainAttendeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNav = binding.bottonNav;
        bottomNav.getMenu().findItem(R.id.home).setChecked(true);
        FragmentTransaction homeTransaction =  getSupportFragmentManager().beginTransaction();
        homeTransaction.replace(R.id.frame_layout , new HomeFragment());
        homeTransaction.commit();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        FragmentTransaction homeTransaction =  getSupportFragmentManager().beginTransaction();
                        homeTransaction.replace(R.id.frame_layout , new HomeFragment());
                        homeTransaction.commit();
                        break;
                    case R.id.history:
                        FragmentTransaction historyTransaction =  getSupportFragmentManager().beginTransaction();
                        historyTransaction.replace(R.id.frame_layout , new HistoryFragment());
                        historyTransaction.commit();
                        break;
                    case R.id.profile:
                        FragmentTransaction profileTransaction =  getSupportFragmentManager().beginTransaction();
                        profileTransaction.replace(R.id.frame_layout , new ProfileFragment());
                        profileTransaction.commit();
                        break;
                }
                return false;
            }
        });


    }
}