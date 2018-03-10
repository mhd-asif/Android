package com.asif.linkkin;

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

import com.asif.linkkin.utils.Font;
import com.asif.linkkin.utils.SharedDataSaveLoad;

/**
 * Created by kanchan on 12/27/2015.
 */
public class LockActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    LinearLayout linearRoot;
    RelativeLayout relativeRoot;
    EditText edtPin;
    TextView txtOk;

    String pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        linearRoot=(LinearLayout)findViewById(R.id.linearRoot);
        relativeRoot=(RelativeLayout)findViewById(R.id.relativeRoot);
        edtPin = (EditText) findViewById(R.id.edtPin);
        txtOk=(TextView)findViewById(R.id.txtOk);
        edtPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        Font.LATO_Regular.apply(this,edtPin);
        Font.LATO_Bold.apply(this,txtOk);
        Font.LATO_Bold.apply(this, (TextView) findViewById(R.id.txtDesc));

        edtPin.setOnEditorActionListener(this);


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
                pin="";
                pin=edtPin.getText().toString();
                if(pin.equals("")) Toast.makeText(getApplicationContext(),"Please type your secret pin",Toast.LENGTH_SHORT).show();
                else
                {
                    String originalPin= SharedDataSaveLoad.load(LockActivity.this, getResources().getString(R.string.shared_pref_lock_pin));
                    if(!pin.equals(originalPin))
                    {
                        Toast.makeText(getApplicationContext(),"Wrong secret pin",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        finish();
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
            pin="";
            pin=edtPin.getText().toString();
            if(pin.equals("")) Toast.makeText(getApplicationContext(),"Please type your secret pin",Toast.LENGTH_SHORT).show();
            else
            {
                String originalPin= SharedDataSaveLoad.load(LockActivity.this, getResources().getString(R.string.shared_pref_lock_pin));
                if(!pin.equals(originalPin))
                {
                    Toast.makeText(getApplicationContext(),"Wrong secret pin",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    finish();
                }
            }
        }
        return true;
    }




}
