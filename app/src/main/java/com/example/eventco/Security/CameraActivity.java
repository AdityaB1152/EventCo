package com.example.eventco.Security;

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

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eventco.Attendee.RegisterActivity;
import com.example.eventco.FaceApi;
import com.example.eventco.RegisteredUser;
import com.example.eventco.databinding.ActivityCameraBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivity extends AppCompatActivity {
    ActivityCameraBinding binding;
    PreviewView previewView;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextureView textureView;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ImageCapture imageCapture;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String eventId = getIntent().getStringExtra("eventId");
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        previewView = binding.preview;
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait!");

        firestore = FirebaseFirestore.getInstance();

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


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraActivity.this , CameraActivity.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
                finish();
            }
        });


        binding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    capture(eventId);
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

    public void capture(String eventId) throws IOException {
        binding.preview.setVisibility(View.GONE);
        binding.capture.setVisibility(View.GONE);
        binding.card1.setVisibility(View.GONE);
        File outputDir = this.getCacheDir();
        File outputFile = File.createTempFile("temp",".jpeg",outputDir);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(outputFile).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        progressDialog.show();
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
                                                    RequestQueue queue = Volley.newRequestQueue(CameraActivity.this);
                                                    JSONObject object = new JSONObject();

                                                    try {
                                                        object.put("downloadUrl",url);
                                                        object.put("eventId","test1152");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Log.e("Calling API","NOW");
                                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                                                            "https://facefunctions.azurewebsites.net/api/FaceIdentification?code=XdqLx648miZd-MFRkl-T2kdylUn26BA5Q6H43YRIB3GBAzFuV37Nbg==",
                                                            object, new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            Log.e("CHECK",response.toString());
                                                            try {
                                                                if(response.getBoolean("verify")){
                                                                    progressDialog.dismiss();
                                                                    binding.verifiedAnimation.setVisibility(View.VISIBLE);
                                                                    binding.textV.setVisibility(View.VISIBLE);
                                                                    binding.back.setVisibility(View.VISIBLE);
                                                                }
                                                                else{

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Log.e("Error",error.toString());
                                                        }
                                                    });

                                                    queue.add(request);

                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CameraActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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