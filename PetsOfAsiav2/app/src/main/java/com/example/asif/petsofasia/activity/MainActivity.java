package com.example.asif.petsofasia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.asif.petsofasia.R;

public class MainActivity extends AppCompatActivity
{
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Just added a comment

//        viewPager = (ViewPager)findViewById(R.id.view_pager);
//        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(swipeAdapter);
//
//        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(viewPager);
    }

    public void OnClickCreateAccount(View view){
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }

    public void OnClickLogIn(View view){
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
}
