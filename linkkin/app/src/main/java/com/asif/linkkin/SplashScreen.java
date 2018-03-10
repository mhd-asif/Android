package com.asif.linkkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.SharedDataSaveLoad;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SplashScreen extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean notUpdated=false;
    String value="";

    DbHelper dbOpenHelper;
    boolean fromSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ActionBar actionBar= getSupportActionBar();
        if (actionBar != null){
            getSupportActionBar().hide();

        }*/

        setContentView(R.layout.activity_splash_screen);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        fromSettings=false;


       // new TimeSpender().execute();

        pref=getSharedPreferences("database_linkkin",0);
        value=pref.getString("load", "no");
        if(value.equals("no"))
        {
            notUpdated=true;
        }
        else
        {
            notUpdated=false;
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
        if(fromSettings)
        {
            fromSettings=false;
            timeSpended();
        }
        else
        {
            DisconnectClass.resetDisconnectTimer();
            new TimeSpender().execute();
        }
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




    private void timeSpended()
    {
        if (TextUtils.isEmpty(SharedDataSaveLoad.load(SplashScreen.this, getResources().getString(R.string.shared_pref_employee_id)))){
            startActivity(new Intent(SplashScreen.this, RegActivity.class));
            finish();

        }else {

            CheckConnectivity connectivity=new CheckConnectivity(SplashScreen.this);
            if(connectivity.isConnected())
            {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
            else
            {
                String path_kindividual=getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindividual);
                File file = new File(path_kindividual);
                if(file.exists())
                {
                    SweetAlertDialog dialog = new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Continue in Offline?");
                    dialog.setConfirmText("Settings");
                    dialog.setCancelText("Continue");
                    dialog.setCancelable(false);
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            fromSettings=true;
                            Intent in=new Intent(Settings.ACTION_SETTINGS);
                            startActivity(in);
                        }
                    });
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Intent in = new Intent(SplashScreen.this, MainActivity.class);
                            //in.putExtra("type","");
                            // in.putExtra("category", "");
                            startActivity(in);
                            finish();
                        }
                    });

                    dialog.show();
                }
                else
                {
                    SweetAlertDialog dialog = new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Internet Connection Required");
                    dialog.setConfirmText("Settings");
                    dialog.setCancelText("Cancel");
                    dialog.setCancelable(false);
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            fromSettings=true;
                            Intent in=new Intent(Settings.ACTION_SETTINGS);
                            startActivity(in);
                        }
                    });
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                            System.exit(1);
                        }
                    });

                    dialog.show();
                }

            }

        }
    }





    class TimeSpender extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(2000);
            }
            catch (Exception e) {}

            try {
                if(notUpdated)
                {
                    dbOpenHelper = new DbHelper(SplashScreen.this, Constants.DATABASE_NAME);
                    editor=pref.edit();
                    editor.putString("load","yes");
                    editor.commit();
                }

            }
            catch (Exception e) {}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            timeSpended();
        }
    }


}
