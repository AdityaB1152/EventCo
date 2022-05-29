package com.example.eventco.Attendee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.eventco.FaceApi;
import com.example.eventco.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    CollapsingToolbarLayout layout;
    PreviewView previewView;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextureView textureView;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ImageCapture imageCapture;
    ProgressDialog progressDialog;
    String eventId = getIntent().getStringExtra("eventId");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        layout = binding.coordinator;
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait!");
        layout.setTitle(intent.getStringExtra("title"));
        firestore = FirebaseFirestore.getInstance();
        binding.title.setText(intent.getStringExtra("title"));
        String downloadUrl = intent.getStringExtra("bannerUrl");
        Glide.with(RegisterActivity.this).load(downloadUrl).into(binding.registerBanner);
        binding.registerAbout.setText(intent.getStringExtra("desc"));
        previewView = binding.preview;

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },getExecutor());

        binding.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.coordinator1.setVisibility(View.GONE);
                binding.book.setVisibility(View.GONE);
                binding.preview.setVisibility(View.VISIBLE);
                binding.capture.setVisibility(View.VISIBLE);
            }
        });


        binding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    capture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector selector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.preview.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        cameraProvider.bindToLifecycle((LifecycleOwner) this,selector,preview,imageCapture);
    }

    public void capture() throws IOException {
        binding.preview.setVisibility(View.GONE);
        binding.capture.setVisibility(View.GONE);
        progressDialog.show();
        File outputDir = this.getCacheDir();
        File outputFile = File.createTempFile("temp",".jpeg",outputDir);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(outputFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        progressDialog.dismiss();
                        binding.verifiedAnimation.setVisibility(View.VISIBLE);
                        binding.textV.setVisibility(View.VISIBLE);
                            Uri uri = Uri.fromFile(outputFile);
                        StorageReference reference = FirebaseStorage.getInstance().getReference("Faces/"+eventId+"/"+ auth.getUid());
                                                reference.putFile(uri)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                    @Override

                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if(task.isSuccessful()){
                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String url = uri.toString();
                                                    FaceApi api =new FaceApi();
                                                    api.addPerson(RegisterActivity.this,auth.getUid(),eventId
                                                            ,auth.getUid(),url);
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                }
        );
    }

}