package com.example.eventco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventco.Attendee.MainAttendeeActivity;
import com.example.eventco.Organiser.OrganiserMainActivity;
import com.example.eventco.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private Spinner spinner;
    private String name,email,password,role;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    String [] input = {"user","organiser","security"};
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        spinner = binding.spinner;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait");
        firestore = FirebaseFirestore.getInstance();
        String [] roles= {"Attend Events","Organise Events","Security Volunteer"};
        setContentView(binding.getRoot());

        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,roles);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = input[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = binding.newName.getText().toString();
                email = binding.newEmail.getText().toString();
                password = binding.newPassword.getText().toString();

                if(name.isEmpty()){
                    binding.newName.setError("Please Enter Name");
                }

                if(email.isEmpty()){
                    binding.newEmail.setError("Please enter a valid email");
                }
                if(password.isEmpty()){
                    binding.newPassword.setError("Please enter a password");
                }
                if(!name.isEmpty() || !email.isEmpty() || !password.isEmpty()){
                    pDialog.show();
                    mAuth.createUserWithEmailAndPassword(email , password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        User user1 = new User(name,email,role,user.getUid());
                                        firestore.collection("Users").document(user.getUid()).set(user1);
                                        Intent intent ;

                                        if (role == "user"){
                                            pDialog.dismiss();
                                            intent = new Intent(SignUpActivity.this , MainAttendeeActivity.class );
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                        else if(role == "organiser"){
                                            pDialog.dismiss();
                                            intent = new Intent(SignUpActivity.this , OrganiserMainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                        else if(role == "security"){
                                            pDialog.dismiss();
                                            intent = new Intent(SignUpActivity.this , OrganiserMainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.dismiss();
                            Log.e("Failure",e.toString());
                        }
                    });
                }


            }
        });

        binding.loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}