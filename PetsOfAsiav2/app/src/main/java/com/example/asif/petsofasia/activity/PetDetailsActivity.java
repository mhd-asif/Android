package com.example.asif.petsofasia.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.example.asif.petsofasia.R;
import com.example.asif.petsofasia.template.SlidingMenuTemplate;

public class PetDetailsActivity extends SlidingMenuTemplate {

    ActionBar homeActionBar;
    ImageView imgDog;
    int dogID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        homeActionBar = getSupportActionBar();
        homeActionBar.setTitle("Pet Details");
        homeActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8C00")));

        Bundle bundle = getIntent().getExtras();
        dogID = bundle.getInt("DOG_ID");


        int id = getResources().getIdentifier("dog" + dogID + "_mid", "drawable", getPackageName());
//        Toast.makeText(getApplicationContext(), id +" ", Toast.LENGTH_SHORT).show();
        imgDog = (ImageView) findViewById(R.id.imgDogDetail);
        imgDog.setImageResource(id);

    }
}
