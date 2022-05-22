package com.example.eventco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.eventco.databinding.ActivityLogin2Binding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLogin2Binding binding ;
    private String email;
    private  String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.email.getText().toString();
                password = binding.email.getText().toString();

                if(email.isEmpty()){
                    binding.email.setError("Please enter an valid email address");
                }
                if(password.isEmpty()){
                    binding.password.setError("Please Enter Password");
                }
                if(!email.isEmpty() || !password.isEmpty()){

                }
            }
        });
    }
}