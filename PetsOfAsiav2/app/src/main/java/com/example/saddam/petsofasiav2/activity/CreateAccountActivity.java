package com.example.saddam.petsofasiav2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.saddam.petsofasiav2.R;

public class CreateAccountActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        TextView existingAccount = (TextView) findViewById(R.id.existingAccount);
        existingAccount.setText(Html.fromHtml("Already have an <u>Account?</u>"));
    }

    public void OnClickLogIn(View view){
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
}
