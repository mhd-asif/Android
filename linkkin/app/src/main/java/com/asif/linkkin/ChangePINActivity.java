package com.asif.linkkin;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.SharedDataSaveLoad;

public class ChangePINActivity extends AppCompatActivity {

    EditText oldPINOrPassword, newPIN, confirmNewPIN;
    Button btnSave, btnResetOrChange;
    TextView titleChangeOrReset,txtOldPinOrPassword;
    TextInputLayout oldPinOrPasswordIL;
    ImageView imgBack;

    boolean modeInChange;
    String stringOldPINOrPassword;
    String stringNewPIN;
    String stringConfirmPIN;
    String originalPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        DisconnectClass disconnectClass=new DisconnectClass(this);

        modeInChange = true;
//        txtOldPinOrPassword = (TextView) findViewById(R.id.old)
        oldPinOrPasswordIL = (TextInputLayout) findViewById(R.id.OldPINOrPassword);
        oldPINOrPassword = (EditText) findViewById(R.id.edtOldPINOrPassword);
        newPIN = (EditText) findViewById(R.id.edtNewPIN);
        confirmNewPIN = (EditText) findViewById(R.id.edtConfirmPIN);
        titleChangeOrReset = (TextView) findViewById(R.id.titleChangeOrResetPIN);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hh=new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },100);
            }
        });

        originalPIN = SharedDataSaveLoad.load(ChangePINActivity.this, getResources().getString(R.string.shared_pref_lock_pin));
        //If there is no Original PIN and User went to settings

        btnResetOrChange = (Button) findViewById(R.id.btnResetOrChange);
        btnResetOrChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if Mode is in 'Change' Then change Mode to Reset and Change View accordingly
                if (modeInChange == true) {
                    modeInChange = false; // changed to 'Reset'
                    /*btnResetOrChange.setText("Change PIN");
                    titleChangeOrReset.setText("Add new PIN");
                    oldPinOrPasswordIL.setHint("Password");
                    oldPINOrPassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);*/
                    oldPINOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    SharedDataSaveLoad.remove(ChangePINActivity.this, getResources().getString(R.string.shared_pref_lock_pin));

                    startActivity(new Intent(ChangePINActivity.this,LockConfirmActivity.class));
                    finish();
                }
                else {
                    modeInChange = true; // changed to 'Change'
                    btnResetOrChange.setText("Reset PIN");
                    titleChangeOrReset.setText("Change PIN");
                    oldPinOrPasswordIL.setHint("Old PIN");
                    oldPINOrPassword.setTransformationMethod(null);
                    oldPINOrPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                oldPINOrPassword.setText("");
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringOldPINOrPassword = oldPINOrPassword.getText().toString();
                stringNewPIN = newPIN.getText().toString();
                stringConfirmPIN = confirmNewPIN.getText().toString();

                if(stringOldPINOrPassword.equals(""))
                {
                    if (modeInChange) oldPINOrPassword.setError("Please Enter Old PIN");
                    else oldPINOrPassword.setError("Please Enter Your Password");
                }
                else if(stringNewPIN.equals(""))
                {
                    newPIN.setError("Please Enter New PIN");
                }
                else if(stringConfirmPIN.equals(""))
                {
                    confirmNewPIN.setError("Please Confirm Your New PIN");
                }
                else
                {
                    boolean check = true;

                    if(modeInChange) { //user trying to input OldPin
                        if (!originalPIN.equals(stringOldPINOrPassword)) {
                            oldPINOrPassword.setError("Wrong PIN Entered");
                            check = false;
                        }
                    }
                    else // user trying to input Password
                    {
                        String originalPassword = SharedDataSaveLoad.load(ChangePINActivity.this, getResources().getString(R.string.shared_pref_password));
                        if (!originalPassword.equals(stringOldPINOrPassword)) {
                            oldPINOrPassword.setError("Wrong Password Entered");
                            check = false;
                        }
                    }

                    if(!stringConfirmPIN.equals(stringNewPIN)){
                        confirmNewPIN.setError("PIN Did Not Match");
                        check = false;
                    }

                    if(check)
                    {
                        SharedDataSaveLoad.save(ChangePINActivity.this,getResources().getString(R.string.shared_pref_lock_pin),stringNewPIN);
                        Toast.makeText(getApplicationContext(),"Your PIN Changed Successfully !",Toast.LENGTH_SHORT).show();

                        Handler hh=new Handler();
                        hh.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 100);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_pin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // DisconnectTimer Override Methods
    @Override
    protected void onResume() {
        super.onResume();
        DisconnectClass.resetDisconnectTimer();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        DisconnectClass.resetDisconnectTimer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // DisconnectClass.stopDisconnectTimer();
        DisconnectClass.removeActivity(this);
    }
}
