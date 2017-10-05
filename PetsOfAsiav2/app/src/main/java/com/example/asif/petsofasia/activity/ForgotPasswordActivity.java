package com.example.asif.petsofasia.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.asif.petsofasia.R;

public class ForgotPasswordActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setText(Html.fromHtml("<b>Forgot your Password?</b>" +
                "<br>Type your e-mail address above and click</br>" +
                "<br>the button below.</b>"+
                "<br><br>You will receive an e-mail with instructions.</br</br>"));
    }

    public void onBackPressed(View view)
    {
        finish();
    }
}
