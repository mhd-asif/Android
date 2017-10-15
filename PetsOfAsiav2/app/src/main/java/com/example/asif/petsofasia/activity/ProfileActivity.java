package com.example.asif.petsofasia.activity;

import android.os.Bundle;

import com.example.asif.petsofasia.R;
import com.example.asif.petsofasia.template.SlidingMenuTemplate;

public class ProfileActivity extends SlidingMenuTemplate {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
