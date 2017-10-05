package com.example.asif.petsofasia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asif.petsofasia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment()
    {
        //getActivity().setTitle("Profile Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment

        return inflater.inflate(
                R.layout.fragment_profile, container, false);
    }

}
