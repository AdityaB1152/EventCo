package com.example.eventco.Organiser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.FaceApi;
import com.example.eventco.R;
import com.example.eventco.databinding.ActivityNewEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class NewEventActivity extends AppCompatActivity {

    private ActivityNewEventBinding binding;
    private FirebaseFirestore firestore;
    private Button createEvent;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private EditText newTitle , newVenue , newTime , newDesc;
    private String title,venue,desc,category;
    private Date date;
    private ProgressDialog pDialog;
    private Spinner categorySpinner;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String [] categories = {"Music","Plays","Movies","Stand-up-comedy"};


        categorySpinner = findViewById(R.id.category_spinner);
        newTitle = findViewById(R.id.new_title);
        newVenue = findViewById(R.id.new_venue);
        newTime = findViewById(R.id.new_date);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait");
        newDesc = findViewById(R.id.new_description);
        date = new Date();
        createEvent = findViewById(R.id.create_new_event);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        ArrayAdapter adapter = new ArrayAdapter(this , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,categories);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = newTitle.getText().toString();
                venue = newVenue.getText().toString();
                desc = newDesc.getText().toString();

                if(selectedImage!=null){
                    pDialog.show();
                    StorageReference reference = storage.getReference().child("Event Banners").child(new Date().getTime()+"");
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    String eventId = auth.getUid()+date.getTime();
                                    Event event = new Event(eventId,title , category, downloadUrl,venue,desc,date, auth.getUid(),"ongoing");
                                    FaceApi api = new FaceApi();
                                    api.createPersonGroup(NewEventActivity.this,title,eventId);
                                    firestore.collection("Events").document(eventId)
                                            .set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pDialog.dismiss();
                                            Intent intent = new Intent(NewEventActivity.this,OrganiserMainActivity.class);
                                            startActivity(intent);
                                            finish();
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
                                    Log.e("FAILURE",e.toString());
                                }
                            });
                        }
                    });

                }



            }
        });


        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent , 25);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        binding.newBanner.setImageURI(data.getData());
        selectedImage = data.getData();
    }
}