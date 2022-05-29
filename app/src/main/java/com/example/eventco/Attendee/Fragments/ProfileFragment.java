package com.example.eventco.Attendee.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventco.R;
import com.example.eventco.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentProfileBinding binding;
        binding = FragmentProfileBinding.inflate(inflater , container , false);
        // Inflating the layout for this fragment

        return binding.getRoot();
    }
}