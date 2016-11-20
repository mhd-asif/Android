package com.bscheme.linkkin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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

import com.bscheme.linkkin.db.Constants;
import com.bscheme.linkkin.db.DbHelper;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.helper.DisconnectClass;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;
import com.bscheme.linkkin.utils.Urls;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    LinearLayout linearRoot;
    RelativeLayout relativeRoot;
    EditText etEmployeeId,edtPassword;
    TextView btnNext;
    SweetAlertDialog mDialog;
    String employeeId,employeeName,password,regId="",webId,api_url;
  //  String kincomingDate="",kinformationDate="",kintertainmentDate="",kinteractDate="";
    DbHelper helper;

    String PROJECT_NUMBER =   "1072812948798";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        DisconnectClass disconnectClass=new DisconnectClass(this);

        linearRoot=(LinearLayout)findViewById(R.id.linearRoot);
        relativeRoot=(RelativeLayout)findViewById(R.id.relativeRoot);
        etEmployeeId = (EditText) findViewById(R.id.et_employee_id);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnNext = (TextView) findViewById(R.id.btn_next);

        Font.LATO_Regular.apply(this,etEmployeeId);
        Font.LATO_Regular.apply(this,edtPassword);
        Font.LATO_Bold.apply(this,btnNext);
        Font.LATO_Bold.apply(this, (TextView) findViewById(R.id.tv_active_text));

        etEmployeeId.setHint("Employee Code");
        edtPassword.setHint("Password");
        btnNext.setText("NEXT");

        ((TextView)findViewById(R.id.tv_active_text)).setText("To activate an account use your\nEmployee code & Password");

        edtPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        edtPassword.setOnEditorActionListener(this);


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




        try
        {
            helper=new DbHelper(RegActivity.this,Constants.DATABASE_NAME,1);
        }
        catch (Exception e) {
        }



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        employeeId = "";
                        employeeId = etEmployeeId.getText().toString();
                        password = edtPassword.getText().toString();
                        if (employeeId.equals(""))
                            Toast.makeText(getApplicationContext(), "Please enter your employee ID", Toast.LENGTH_LONG).show();
                        else {
                            CheckConnectivity connectivity = new CheckConnectivity(RegActivity.this);
                            if (connectivity.isConnected()) {
                                registerUser();
                            } else
                                Toast.makeText(getApplicationContext(), "Please turn on internet connection of your device", Toast.LENGTH_SHORT).show();
                        }
                /*        CheckConnectivity connectivity = new CheckConnectivity(RegActivity.this);
                        if (connectivity.isConnected()) {
                            registerUser();
                        } else
                            Toast.makeText(getApplicationContext(), "Please turn on internet connection of your device", Toast.LENGTH_SHORT).show();
  */
                    }
                }, 100);

            }
        });


    }



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
     //   DisconnectClass.stopDisconnectTimer();
        DisconnectClass.removeActivity(this);
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO){
            employeeId="";
            employeeId=etEmployeeId.getText().toString();
            password=edtPassword.getText().toString();
            if(employeeId.equals("")) Toast.makeText(getApplicationContext(),"Please enter your employee ID",Toast.LENGTH_LONG).show();
            else
            {
                CheckConnectivity connectivity = new CheckConnectivity(RegActivity.this);
                if (connectivity.isConnected()) {
                    registerUser();
                } else
                    Toast.makeText(getApplicationContext(), "Please turn on internet connection of your device", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }



    private void registerUser()
    {
        SharedPreferences prefNotify=getSharedPreferences("Push_Notification",MODE_PRIVATE);
        regId=prefNotify.getString("reg_id","");

        mDialog = new SweetAlertDialog(RegActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
        mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
        mDialog.setTitleText("");
        mDialog.setCancelable(false);
        mDialog.show();

        if(regId.equals("")) new GCMRegister().execute();
        else
        {
            new getRes().execute();
        }
       // new getRes().execute();
    }



    private void saveKindomData()
    {
        try {
            String path_dir=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.offline_directory);
            File dir = new File(path_dir);
            dir.mkdir();

            String path_kindom=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindom);
            Log.e("catch path: ",path_kindom);
            File file = new File(path_kindom);
            file.createNewFile();
            new getKindomInfos().execute();
        }
        catch (Exception e) {
            Log.e("file_creation",""+e);
        }

    }


    private void saveKindividualData()
    {
        try {
            String path_kindividual=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindividual);
            File file = new File(path_kindividual);
            file.createNewFile();
            new getKindividualInfos().execute();
        }
        catch (Exception e) {
            Log.e("file_creation","+e");
        }

    }




    private void startNextActivity()
    {
        if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();

        Intent intent = new Intent(RegActivity.this,MainActivity.class);
       // intent.putExtra("type","");
        //intent.putExtra("category", "");
        startActivity(intent);
        finish();
    }





    private class GCMRegister extends AsyncTask<String,String,String>
    {
        boolean gcmSuccess;

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                regId = gcm.register(PROJECT_NUMBER);
                if(!regId.equals("")) gcmSuccess=true;
            }
            catch (Exception e) {
                gcmSuccess=false;
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if(gcmSuccess==false)
            {
               // if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
               // Toast.makeText(getApplicationContext(),"Failed to register with GCM",Toast.LENGTH_LONG).show();
                new getRes().execute();
            }
            else
            {
                SharedPreferences prefNotify=getSharedPreferences("Push_Notification",MODE_PRIVATE);
                SharedPreferences.Editor editorNotify=prefNotify.edit();
                editorNotify.putString("reg_id",regId);
                editorNotify.commit();
                new getRes().execute();
            }
        }

        @Override
        protected void onPreExecute() {
            gcmSuccess=false;
        }
    }



    private class getRes extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            ConnectionHelper ch = new ConnectionHelper();
            ch.createConnection(Urls.URL_COMMON, "POST");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("action","user_login")
                    .appendQueryParameter("user_id", employeeId)
                    .appendQueryParameter("password", password)
                    .appendQueryParameter("gcm_id",regId);
            ch.addData(builder);

           // employeeId=params[0];

            return ch.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("login_response", s);
            try {
                JSONObject jObj = new JSONObject(s);
                int success=jObj.getInt("success");
                if(success==0)
                {
                    if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Employee not found",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //getEmployeeName();
                    employeeName=jObj.getString("name");
                    webId=jObj.getString("webid");
                    api_url=jObj.getString("api_url");
                    SharedDataSaveLoad.save(RegActivity.this, getResources().getString(R.string.shared_pref_web_id), webId);
                    SharedDataSaveLoad.save(RegActivity.this, getResources().getString(R.string.shared_pref_employee_id), employeeId);
                    SharedDataSaveLoad.save(RegActivity.this, getResources().getString(R.string.shared_pref_employee_name), employeeName);
                    SharedDataSaveLoad.save(RegActivity.this, getResources().getString(R.string.shared_pref_password), password);
                    SharedDataSaveLoad.save(RegActivity.this, getResources().getString(R.string.shared_pref_api_url), api_url);
                    saveKindomData();
                }
            } catch (JSONException e) {
                if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }


    }





    private class getKindomInfos extends AsyncTask<String,Void,String> {
        String name,description,logo,banner;
        boolean response;
        ArrayList<String>optionTitles=new ArrayList<>();
        ArrayList<String>optionDetails=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            response=false;
        }

        @Override
        protected String doInBackground(String... params) {
       /*     ConnectionHelper ch = new ConnectionHelper();
            return ch.getDataFromUrlByGET(Urls.KINDOM_LIST);   */

            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(SharedDataSaveLoad.load(RegActivity.this,getResources().getString(R.string.shared_pref_api_url)), "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_kindominfo")
                    .appendQueryParameter("blog_id", SharedDataSaveLoad.load(RegActivity.this, getResources().getString(R.string.shared_pref_web_id))));
            return helper.getResponse();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //mDialog.dismiss();
            String path_kindom=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindom);
            File file = new File(path_kindom);
            if(file.exists())
            {
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    stream.write(response.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stream.close();
                        saveKindividualData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else saveKindividualData();

        }

    }






    private class getKindividualInfos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
          //  ConnectionHelper ch = new ConnectionHelper();
           // return ch.getDataFromUrlByGET(Urls.KINDIVIDUAL_INFO + "/" +employeeId);

            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(Urls.URL_COMMON, "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_user_info")
                    .appendQueryParameter("user_id", SharedDataSaveLoad.load(RegActivity.this, getResources().getString(R.string.shared_pref_employee_id)))
                    .appendQueryParameter("blog_id", SharedDataSaveLoad.load(RegActivity.this, getResources().getString(R.string.shared_pref_web_id))));
            return helper.getResponse();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            //Log.i("response kindividual", s);

            String path_kindom=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindividual);
            File file = new File(path_kindom);
            if(file.exists())
            {
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    stream.write(response.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stream.close();
                        startNextActivity();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else startNextActivity();


        }

    }






}
