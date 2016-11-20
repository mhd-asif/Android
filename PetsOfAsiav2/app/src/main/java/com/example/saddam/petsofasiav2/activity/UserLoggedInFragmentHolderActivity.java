package com.example.saddam.petsofasiav2.activity;

import android.os.Bundle;
import com.example.saddam.petsofasiav2.R;
import com.example.saddam.petsofasiav2.fragment.PetsFragment;
import com.example.saddam.petsofasiav2.template.SlidingMenuTemplate;

public class UserLoggedInFragmentHolderActivity extends SlidingMenuTemplate
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in_fragment_holder);
        //Set starting fragment to show
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, new PetsFragment()).commit();
    }
}
