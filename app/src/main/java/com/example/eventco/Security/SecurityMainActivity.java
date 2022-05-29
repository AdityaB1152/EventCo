package com.example.eventco.Security;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eventco.databinding.ActivitySecurityMainBinding;

public class SecurityMainActivity extends AppCompatActivity {
    ActivitySecurityMainBinding binding;
    String eventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventId = binding.eventId.getText().toString();
                Intent intent = new Intent(SecurityMainActivity.this,CameraActivity.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
            }
        });

    }
}
