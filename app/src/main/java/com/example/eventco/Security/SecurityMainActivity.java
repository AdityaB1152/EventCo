package com.example.eventco.Security;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eventco.databinding.ActivitySecurityMainBinding;

public class SecurityMainActivity extends AppCompatActivity {
    ActivitySecurityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
