package com.example.saddam.petsofasiav2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.saddam.petsofasiav2.R;

public class LogInActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setText(Html.fromHtml("Forgot your <u>Password?</u>"));

        TextView newMember = (TextView) findViewById(R.id.newMember);
        newMember.setText(Html.fromHtml("Not a <u>Member?</u>"));
    }

    public void OnClickCreateAccount(View view){
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }

    public void OnClickForgotPassword(View view){
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        startActivity(i);
    }

    public void OnClickLoggedIn(View view)
    {
        Intent i = new Intent(this, UserLoggedInFragmentHolderActivity.class);
        startActivity(i);
    }
}
