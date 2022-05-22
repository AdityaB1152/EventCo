package com.example.eventco.Organiser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.R;
import com.example.eventco.databinding.ActivityNewEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
    private EditText newTitle , newVenue , newTime , newDesc;
    private String title,venue,desc,bannerUrl;
    private Date date;
    Uri selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newTitle = findViewById(R.id.new_title);
        newVenue = findViewById(R.id.new_venue);
        newTime = findViewById(R.id.new_date);
        newDesc = findViewById(R.id.new_description);
        createEvent = findViewById(R.id.create_new_event);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = newTitle.getText().toString();
                venue = newVenue.getText().toString();
                desc = newDesc.getText().toString();

                if(selectedImage!=null){
                    StorageReference reference = storage.getReference().child("Event Banners").child(new Date().getTime()+"");
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    Event event = new Event(title ,downloadUrl,venue,desc,date);

                                    firestore.collection("Events").add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Intent intent = new Intent(NewEventActivity.this , OrganiserMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
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