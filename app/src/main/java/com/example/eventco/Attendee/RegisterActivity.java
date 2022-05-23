package com.example.eventco.Attendee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.eventco.databinding.ActivityRegisterBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    CollapsingToolbarLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        layout = binding.coordinator;
        layout.setTitle(intent.getStringExtra("title"));
        String downloadUrl = intent.getStringExtra("bannerUrl");
        Glide.with(RegisterActivity.this).load(downloadUrl).into(binding.registerBanner);
        binding.registerAbout.setText(intent.getStringExtra("desc"));


    }
}