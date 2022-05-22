package com.example.eventco.Organiser.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.Organiser.Adapters.EventDetailsAdapter;
import com.example.eventco.Organiser.NewEventActivity;
import com.example.eventco.databinding.FragmentOngoingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class OngoingFragment extends Fragment {
    FragmentOngoingBinding binding;
   FirebaseFirestore firestore;
    RecyclerView ongoingEventsRV;
     FloatingActionButton createEventButton;
    ArrayList<Event> events;
     EventDetailsAdapter adapter;

    public OngoingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOngoingBinding.inflate(inflater , container ,false);
        ongoingEventsRV = binding.ongoingRv;
        createEventButton = binding.createEventButton;
        events = new ArrayList<>();
        adapter = new EventDetailsAdapter(getActivity() , events);
        ongoingEventsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        ongoingEventsRV.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();

        CollectionReference eventsRef = firestore.collection("Events");
        eventsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                      Event event = documentSnapshot.toObject(Event.class);
                      Snackbar.make(getView(),event.getTitle(),Snackbar.LENGTH_LONG).show();
                      events.add(event);
                      Log.e("CHECK",event.toString());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });






        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , NewEventActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}