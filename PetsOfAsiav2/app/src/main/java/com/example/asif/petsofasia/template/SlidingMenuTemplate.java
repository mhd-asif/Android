package com.example.asif.petsofasia.template;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.asif.petsofasia.R;
import com.example.asif.petsofasia.activity.AboutUsActivity;
import com.example.asif.petsofasia.activity.HomeActivity;
import com.example.asif.petsofasia.activity.LogInActivity;
import com.example.asif.petsofasia.activity.PrivacyPolicyActivity;
import com.example.asif.petsofasia.activity.ProfileActivity;
import com.example.asif.petsofasia.activity.TACActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class SlidingMenuTemplate  extends SlidingActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.menu_drawer_slide);
        //Initial Setup Done

//        TextView tvMyFavourite = (TextView) findViewById(R.id.txt_my_favourite);
//        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/blogger_sans-medium.otf");
//        tvMyFavourite.setTypeface(tf);

        //Now add some properties to the side slider
        getSlidingMenu().setBehindOffset(100);
    }

    /******************************************* Fragments *****************************************/
    public void clickedItemMyFavourite(View view)
    {
        /*if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        Toast.makeText(getApplicationContext(), "My Favourite", Toast.LENGTH_LONG).show();*/
    }

    public void clickedItemPets(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Pets", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new PetsFragment()).commit();
    }

    public void clickedItemServices(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Services", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new ServicesFragment()).commit();
    }

    public void clickedItemProfile(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new ProfileFragment()).commit();
    }

    public void clickedItemSupport(View view)
    {
        /*if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        Toast.makeText(getApplicationContext(), "Support", Toast.LENGTH_LONG).show();*/
    }

    public void clickedItemTermsAndConditions(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Terms & conditions", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, TACActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new TermsAndConditionsFragment()).commit();
    }

    public void clickedItemPrivacyPolicy(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Privacy Policy", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new PrivacyPolicyFragment()).commit();
    }

    public void clickedItemAboutThisApp(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "About This App", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AboutUsActivity.class);
        startActivity(i);
        finish();
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new AboutUsFragment()).commit();
    }

    /*Functionality*/
    public void clickedItemRateThisApp(View view)
    {
        /*if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);*/
        Toast.makeText(getApplicationContext(), "Rate This App", Toast.LENGTH_LONG).show();
    }

    public void clickedItemLogOut(View view)
    {
        /*if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);*/
        //Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
        finish();
    }
}
