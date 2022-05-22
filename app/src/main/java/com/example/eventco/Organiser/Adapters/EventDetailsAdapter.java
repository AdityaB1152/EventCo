package com.example.eventco.Organiser.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.R;
import com.example.eventco.databinding.SampleOngoingEventsBinding;

import java.util.ArrayList;

public class EventDetailsAdapter extends RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder> {

     ArrayList<Event> events;
     Context context;
    @NonNull
    @Override
    public EventDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_ongoing_events,parent,false);
        return new EventDetailsViewHolder(view);
    }

    public EventDetailsAdapter(Context context , ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }

    @Override
    public void onBindViewHolder(@NonNull EventDetailsViewHolder holder, int position) {
        Event event = events.get(position);
        holder.binding.ongoingTitle.setText(event.getTitle());
        Glide.with(context).load(event.getBannerUrl()).into(holder.binding.ongoingBanner);
        holder.binding.ongoingDate.setText("21st Feb 2022 6 PM");
        holder.binding.ongoingVenue.setText(event.getVenue());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventDetailsViewHolder extends RecyclerView.ViewHolder{
        SampleOngoingEventsBinding binding;
        public EventDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleOngoingEventsBinding.bind(itemView);
        }
    }
}

