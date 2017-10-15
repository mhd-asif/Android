package com.example.asif.petsofasia.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.example.asif.petsofasia.R;
import com.example.asif.petsofasia.template.SlidingMenuTemplate;

public class HomeActivity extends SlidingMenuTemplate {
    ActionBar homeActionBar;
    ImageView imgDog1;
    ImageView imgDog2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeActionBar = getSupportActionBar();
        homeActionBar.setTitle("Pets for Adoption");
        homeActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8C00")));

        final Intent i = new Intent(this, PetDetailsActivity.class);

        imgDog1 = (ImageView) findViewById(R.id.imgDog1);
        imgDog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i.putExtra("DOG_ID", 1);
                startActivity(i);
            }
        });
        imgDog2 = (ImageView) findViewById(R.id.imgDog2);
        imgDog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i.putExtra("DOG_ID", 2);
                startActivity(i);
            }
        });

    }
}
