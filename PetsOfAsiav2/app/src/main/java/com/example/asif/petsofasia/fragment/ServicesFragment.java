package com.example.asif.petsofasia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asif.petsofasia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment
{
    public ServicesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Services");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false);

    }


}
