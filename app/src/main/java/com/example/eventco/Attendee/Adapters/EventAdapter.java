package com.example.eventco.Attendee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.R;
import com.example.eventco.databinding.EventBannerBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context , ArrayList<Event> events){
        this.context = context;
        this.events = events;
    }
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.event_banner , parent , false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        Glide.with(context).load(event.getBannerUrl()).into(holder.binding.banner);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view , "Working",Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
class EventViewHolder extends RecyclerView.ViewHolder{
    EventBannerBinding binding;
    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        binding = EventBannerBinding.bind(itemView);
    }
}
