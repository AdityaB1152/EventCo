package com.example.eventco.Attendee.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.Attendee.RegisterActivity;
import com.example.eventco.MainActivity;
import com.example.eventco.R;
import com.example.eventco.databinding.EventBannerBinding;

import java.util.ArrayList;

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
        holder.binding.eventName.setText(event.getTitle());
        holder.binding.venue.setText(event.getVenue());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , RegisterActivity.class);
               intent.putExtra("title",event.getTitle());
               intent.putExtra("bannerUrl",event.getBannerUrl());
               intent.putExtra("venue",event.getVenue());
               intent.putExtra("date",event.getDate());
               intent.putExtra("desc",event.getDesc());
               context.startActivity(intent);
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
