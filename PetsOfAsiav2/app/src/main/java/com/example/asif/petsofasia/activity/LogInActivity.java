package com.example.asif.petsofasia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.asif.petsofasia.R;

public class LogInActivity extends ActionBarActivity {

    TextView forgotPassword;
    TextView newMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setText(Html.fromHtml("Forgot your <u>Password?</u>"));

        newMember = (TextView) findViewById(R.id.newMember);
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

    public void OnClickLogInFB(View view)
    {
//        Toast.makeText(this, "Log In clicked!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, UserLoggedInFragmentHolderActivity.class);
        startActivity(i);
    }
}
