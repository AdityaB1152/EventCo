package com.example.eventco.Attendee.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventco.Attendee.Models.Category;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.R;
import com.example.eventco.databinding.CategoryWiseRvBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categories;

    FirebaseFirestore firestore;

    public CategoryAdapter(Context context , ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.category_wise_rv,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        RecyclerView eventsRv;
        EventAdapter adapter;
        ArrayList<Event> events;
        events = new ArrayList<>();
        adapter = new EventAdapter(context,events);
        eventsRv = holder.binding.eventRV;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        eventsRv.setLayoutManager(layoutManager);
        eventsRv.setAdapter(adapter);
        holder.binding.categoryName.setText(category.getCategory());
        firestore = FirebaseFirestore.getInstance();
        CollectionReference ref = firestore.collection("Events");
        Query query = ref.whereEqualTo("category",category.getCategory());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            Event event = doc.toObject(Event.class);
                            events.add(event);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public int getItemCount() {

        return categories.size();
    }
}
class CategoryViewHolder extends RecyclerView.ViewHolder{
    CategoryWiseRvBinding binding;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = CategoryWiseRvBinding.bind(itemView);

    }
}
