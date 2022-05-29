package com.example.eventco.Organiser.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.LoginActivity;
import com.example.eventco.Organiser.Adapters.EventDetailsAdapter;
import com.example.eventco.R;
import com.example.eventco.Security.SecurityMainActivity;
import com.example.eventco.databinding.FragmentPreviousBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PreviousFragment extends Fragment {

    FragmentPreviousBinding binding;
    FirebaseFirestore firestore;
    RecyclerView ongoingEventsRV;
    FloatingActionButton createEventButton;
    ArrayList<Event> events;
    EventDetailsAdapter adapter;
    FirebaseAuth auth;

    public PreviousFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentPreviousBinding.inflate(inflater,container,false);

        ongoingEventsRV = binding.ongoingRv;
        auth = FirebaseAuth.getInstance();
        events = new ArrayList<>();
        adapter = new EventDetailsAdapter(getActivity() , events);
        ongoingEventsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        ongoingEventsRV.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        CollectionReference eventsRef = firestore.collection("Events");
        Query query = eventsRef.whereEqualTo("organiserId",auth.getUid()).whereEqualTo("status","completed");
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                        Event event = documentSnapshot.toObject(Event.class);
//                        Snackbar.make(getView(),event.getTitle(),Snackbar.LENGTH_LONG).show();
//                        events.add(event);
//                        Log.e("CHECK",event.toString());
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                events.clear();
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();

    }
}