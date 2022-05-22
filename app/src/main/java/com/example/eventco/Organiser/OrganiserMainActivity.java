package com.example.eventco.Organiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.eventco.Attendee.Fragments.HomeFragment;
import com.example.eventco.Organiser.Adapters.VPFragmentAdapter;
import com.example.eventco.Organiser.Fragments.OngoingFragment;
import com.example.eventco.databinding.ActivityOrganiserMainBinding;
import com.google.android.material.tabs.TabLayout;

public class OrganiserMainActivity extends AppCompatActivity {
    ActivityOrganiserMainBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrganiserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPager = binding.viewPager;
        tabLayout = binding.tabs;

        tabLayout.setupWithViewPager(viewPager);
        VPFragmentAdapter vpFragmentAdapter = new VPFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpFragmentAdapter.addFragment(new OngoingFragment(),"ONGOING EVENTS");
        vpFragmentAdapter.addFragment(new HomeFragment(),"PREVIOUS EVENTS");
        viewPager.setAdapter(vpFragmentAdapter);


    }
}