package com.example.eventco.Attendee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eventco.databinding.ActivitySetupProfileBinding;

public class SetupProfileActivity extends AppCompatActivity {
    private ActivitySetupProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        setContentView(binding.getRoot());
    }
}