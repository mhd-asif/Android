package com.example.asif.petsofasia.activity;

import android.os.Bundle;

import com.example.asif.petsofasia.R;
import com.example.asif.petsofasia.fragment.PetsFragment;
import com.example.asif.petsofasia.template.SlidingMenuTemplate;

public class UserLoggedInFragmentHolderActivity extends SlidingMenuTemplate
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in_fragment_holder);

        //Set starting fragment to show
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, new PetsFragment()).commit();
    }
}
