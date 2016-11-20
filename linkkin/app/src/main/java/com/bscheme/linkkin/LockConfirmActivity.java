package com.bscheme.linkkin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

/**
 * Created by kanchan on 12/27/2015.
 */
public class LockConfirmActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    LinearLayout linearRoot;
    RelativeLayout relativeRoot;
    EditText edtPin,edtConfirm,edtPass;
    TextView txtOk;

    String pin,confirm,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_confirm);
        linearRoot=(LinearLayout)findViewById(R.id.linearRoot);
        relativeRoot=(RelativeLayout)findViewById(R.id.relativeRoot);
        edtPin = (EditText) findViewById(R.id.edtPin);
        edtConfirm = (EditText) findViewById(R.id.edtConfirm);
        edtPass = (EditText) findViewById(R.id.edtPass);
        txtOk=(TextView)findViewById(R.id.txtOk);

        Font.LATO_Regular.apply(this,edtPin);
        Font.LATO_Regular.apply(this,edtConfirm);
        Font.LATO_Regular.apply(this,edtPass);
        Font.LATO_Bold.apply(this,txtOk);
        Font.LATO_Bold.apply(this, (TextView) findViewById(R.id.txtDesc));

        edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        edtPin.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtConfirm.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtConfirm.setOnEditorActionListener(this);


        ViewTreeObserver observer = linearRoot.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                int rootHeight = linearRoot.getHeight();
                relativeRoot.getLayoutParams().height = rootHeight;
                linearRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });


        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin=confirm=pass="";
                pin=edtPin.getText().toString();
                confirm=edtConfirm.getText().toString();
                pass=edtPass.getText().toString();
                if(pin.equals("")||confirm.equals("")||pass.equals("")) Toast.makeText(getApplicationContext(),"Please fill up all options",Toast.LENGTH_SHORT).show();
                else
                {
                    String originalPass= SharedDataSaveLoad.load(LockConfirmActivity.this, getResources().getString(R.string.shared_pref_password));
                    if(!originalPass.equals(pass)) Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                    else
                    {
                        if(!pin.equals(confirm)) Toast.makeText(getApplicationContext(),"Pin not matched",Toast.LENGTH_SHORT).show();
                        else
                        {
                            SharedDataSaveLoad.save(LockConfirmActivity.this,getResources().getString(R.string.shared_pref_lock_pin),pin);
                            finish();
                        }
                    }
                }
            }
        });


    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    public void onBackPressed() {
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            pin=confirm=pass="";
            pin=edtPin.getText().toString();
            confirm=edtConfirm.getText().toString();
            pass=edtPass.getText().toString();
            if(pin.equals("")||confirm.equals("")||pass.equals("")) Toast.makeText(getApplicationContext(),"Please fill up all options",Toast.LENGTH_SHORT).show();
            else
            {
                String originalPass= SharedDataSaveLoad.load(LockConfirmActivity.this, getResources().getString(R.string.shared_pref_password));
                if(!originalPass.equals(pass)) Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                else
                {
                    if(!pin.equals(confirm)) Toast.makeText(getApplicationContext(),"Pin not matched",Toast.LENGTH_SHORT).show();
                    else
                    {
                        SharedDataSaveLoad.save(LockConfirmActivity.this,getResources().getString(R.string.shared_pref_lock_pin),pin);
                        finish();
                    }
                }
            }
        }
        return true;
    }




}
