package com.example.eventco.Attendee.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventco.Attendee.Adapters.CategoryAdapter;
import com.example.eventco.Attendee.Models.Category;
import com.example.eventco.Attendee.Models.Event;
import com.example.eventco.R;
import com.example.eventco.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding ;
    RecyclerView catergoryRV;
    ArrayList<Category>categories;
    CategoryAdapter adapter;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentHomeBinding.inflate(inflater , container , false);
        catergoryRV = binding.categoryRv;
        categories = new ArrayList<>();
        adapter = new CategoryAdapter(getActivity() , categories);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        catergoryRV.setLayoutManager(layoutManager);
        catergoryRV.setAdapter(adapter);
        Category category =  new Category("Music");
        Category category1 =  new Category("Movies");
        Category category2 =  new Category("Plays");
        Category category3 =  new Category("Comedy");
        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        adapter.notifyDataSetChanged();

        return binding.getRoot();
    }
}