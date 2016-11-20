package com.example.saddam.petsofasiav2.template;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.saddam.petsofasiav2.R;
import com.example.saddam.petsofasiav2.fragment.AboutUsFragment;
import com.example.saddam.petsofasiav2.fragment.PetsFragment;
import com.example.saddam.petsofasiav2.fragment.PrivacyPolicyFragment;
import com.example.saddam.petsofasiav2.fragment.ProfileFragment;
import com.example.saddam.petsofasiav2.fragment.ServicesFragment;
import com.example.saddam.petsofasiav2.fragment.TermsAndConditionsFragment;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class SlidingMenuTemplate  extends SlidingActivity
{
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};

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

    /*Fragments*/
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new PetsFragment()).commit();
    }

    public void clickedItemServices(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Services", Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new ServicesFragment()).commit();
    }

    public void clickedItemProfile(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new ProfileFragment()).commit();
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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new TermsAndConditionsFragment()).commit();
    }

    public void clickedItemPrivacyPolicy(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "Privacy Policy", Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new PrivacyPolicyFragment()).commit();
    }

    public void clickedItemAboutThisApp(View view)
    {
        if(getSlidingMenu().isMenuShowing())
            getSlidingMenu().toggle(true);
        //Toast.makeText(getApplicationContext(), "About This App", Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new AboutUsFragment()).commit();
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
        Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
    }
}
