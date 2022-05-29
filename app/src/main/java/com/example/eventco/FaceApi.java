package com.example.eventco;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FaceApi {
    String endpoint = "https://event-co.cognitiveservices.azure.com";
    String apiKey = "40ac89417add49e7ae5be7eaafe81e3f";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public FaceApi(){

    }

    public void createPersonGroup(Context context , String name, String eventId  ){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",name);
            jsonObject.put("recognitionModel","recognition_04");
            jsonObject.put("userData",eventId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                endpoint + "/face/v1.0/persongroups/"+eventId+"f",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("APIResponse",response.toString());
                return;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APIError",error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> param = new HashMap<String ,String>();
                param.put("Ocp-Apim-Subscription-Key",apiKey);
                return param;
            }
        };
        queue.add(request);
    }
    public void addPerson(Context context , String name,String eventId,String uid,String downloadUrl){

        String url = endpoint+"/face/v1.0/persongroups/"+eventId+"/persons";
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name",name);
            requestBody.put("userData",uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String personId = (String) response.get("personId");
                    Log.e("ID",personId);
                    addPersonFace(context ,eventId, personId , uid , downloadUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> param = new HashMap<String ,String>();
                param.put("Ocp-Apim-Subscription-Key",apiKey);

                return param;
            }
        };

        queue.add(request);


    }

    public void addPersonFace(Context context ,String eventId, String personId , String uid , String downloadUrl){
        RequestQueue queue= Volley.newRequestQueue(context);
        String url = endpoint+"/face/v1.0/persongroups/"+eventId+"/persons/"+personId+"/persistedfaces";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("url",downloadUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("CHECK",response.toString());
                Map<String,String> object = new HashMap<>();

                try {
                    String pId = response.getString("persistedFaceId");
                    object.put("uId",uid);
                    object.put("pId",pId);
                    object.put("status","registered");
                    object.put("imageUrl",downloadUrl);
                    Toast.makeText(context, pId, Toast.LENGTH_SHORT).show();
                    firestore.collection("Events/"+eventId+"/registeredUsers/")
                            .document(uid).set(object).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            JSONObject object1 = new JSONObject();
                            try {
                                object1.put("eventId", eventId);
                                object1.put("date", new Date());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e("CHECK", "User Registed Successfully");
                            firestore.collection("Users/" + uid + "/bookings").document(eventId)
                                    .set(object1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e("CHECK", "DONE");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("FAIL", e.toString());
                                }
                            });


                        }

                });
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                queueTraining(context , eventId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> param = new HashMap<String ,String>();
                param.put("Ocp-Apim-Subscription-Key",apiKey);
                return param;
            }
        };

        queue.add(request);
    }

    public void queueTraining(Context context , String eventId){
        RequestQueue queue = Volley.newRequestQueue(context);

        String url = endpoint+"/face/v1.0/persongroups/"+eventId+"/train";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TrainingResp",response.toString());
                Toast.makeText(context,"DONE",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> param = new HashMap<String ,String>();
                param.put("Ocp-Apim-Subscription-Key",apiKey);
                return param;
            }
        };

        queue.add(request);
    }




}
