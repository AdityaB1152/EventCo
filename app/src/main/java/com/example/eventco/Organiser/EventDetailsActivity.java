package com.example.eventco.Organiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.eventco.databinding.ActivityEventDetailsBinding;

public class EventDetailsActivity extends AppCompatActivity {
    ActivityEventDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("bannerUrl")).into(binding.banner);
        binding.title.setText(intent.getStringExtra("title"));
        binding.about.setText(intent.getStringExtra("desc"));
        binding.venue.setText(intent.getStringExtra("venue"));
        binding.date.setText(intent.getStringExtra("date"));

    }
}