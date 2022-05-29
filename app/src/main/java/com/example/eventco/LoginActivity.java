package com.example.eventco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.eventco.Attendee.MainAttendeeActivity;
import com.example.eventco.Organiser.OrganiserMainActivity;
import com.example.eventco.databinding.ActivityLogin2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private ActivityLogin2Binding binding ;
    private String email;
    private  String password;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait");

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
                    pDialog.show();
                    mAuth.signInWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            DocumentReference userDocRef = firestore.collection("Users").document(mAuth.getUid().toString());
                            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        Intent intent;
                                        DocumentSnapshot doc = task.getResult();
                                        String userType = doc.get("userType").toString();
                                        pDialog.dismiss();
                                        if(userType == "user"){
                                            intent = new Intent(LoginActivity.this , MainAttendeeActivity.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        }
                                        else if(userType == "organiser"){
                                            intent = new Intent(LoginActivity.this , OrganiserMainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        }
                                        else if(userType == "security"){
                                            intent = new Intent(LoginActivity.this , MainAttendeeActivity.class);
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

        binding.signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}