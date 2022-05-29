package com.example.eventco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventco.Attendee.MainAttendeeActivity;
import com.example.eventco.Attendee.RegisterActivity;
import com.example.eventco.Organiser.OrganiserMainActivity;
import com.example.eventco.Security.SecurityMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser()!=null){
            DocumentReference userDocRef = firestore.collection("Users").document(mAuth.getUid().toString());
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Intent intent;
                        DocumentSnapshot doc = task.getResult();
                        String userType = doc.getString("role");
                        Log.e("CHECK",userType);

                        if(userType.equals("user")){
                            intent = new Intent(MainActivity.this , MainAttendeeActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }
                        else if(userType.equals("organiser")){
                            intent = new Intent(MainActivity.this , OrganiserMainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }
                        else if(userType.equals("security")){
                            intent = new Intent(MainActivity.this , SecurityMainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("Failure",e.toString());
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }

        else{
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
}