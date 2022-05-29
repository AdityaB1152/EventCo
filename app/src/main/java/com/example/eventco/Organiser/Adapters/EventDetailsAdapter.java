package com.example.eventco.Organiser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.Organiser.EventDetailsActivity;
import com.example.eventco.R;
import com.example.eventco.databinding.SampleFoldingCellBinding;
import com.example.eventco.databinding.SampleOngoingEventsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder> {

     ArrayList<Event> events;
     Context context;
     FirebaseFirestore firestore ;
    @NonNull
    @Override
    public EventDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_folding_cell,parent,false);
        return new EventDetailsViewHolder(view);
    }

    public EventDetailsAdapter(Context context , ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public void onBindViewHolder(@NonNull EventDetailsViewHolder holder, int position) {
        Event event = events.get(position);
        firestore = FirebaseFirestore.getInstance();
        holder.binding.title.setText(event.getTitle());
        Glide.with(context).load(event.getBannerUrl()).into(holder.binding.titleImageView);
        Glide.with(context).load(event.getBannerUrl()).into(holder.binding.contentImageView);
        holder.binding.ongoingDate.setText("21st Feb 2022 6 PM");
        holder.binding.contentVenue.setText(event.getVenue());
        holder.binding.ongoingTitle.setText(event.getTitle());
        holder.binding.ongoingVenue.setText(event.getVenue());

        holder.binding.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.foldingCell.toggle(false);
            }
        });

        holder.binding.viewEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("bannerUrl",event.getBannerUrl());
                intent.putExtra("title",event.getTitle());
                intent.putExtra("date",event.getDate());
                intent.putExtra("venue",event.getVenue());
                intent.putExtra("desc",event.getDesc());
                context.startActivity(intent);
            }
        });

        holder.binding.endEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Events").document(event.getEventId())
                        .update("status","completed").addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Event Ended", Toast.LENGTH_SHORT).show();
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventDetailsViewHolder extends RecyclerView.ViewHolder{
        SampleFoldingCellBinding binding;
        public EventDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleFoldingCellBinding.bind(itemView);
        }
    }
}

