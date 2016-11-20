package com.bscheme.linkkin.left_menu_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bscheme.linkkin.ChangePINActivity;
import com.bscheme.linkkin.LockConfirmActivity;
import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.RegActivity;
import com.bscheme.linkkin.SymbolActivity;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

import org.json.JSONObject;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

public class SettingFragment extends Fragment
{

  LinearLayout linearLogout, linearChangePIN, linearProfile,linearSymbol;
  SwitchCompat mSwitch;
  TextView txtVersion;

  SharedPreferences prefNotify;
  SharedPreferences.Editor editorNotify;

  
  public void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
  {
    return inflater.inflate(R.layout.fragment_setting, container, false);
  }
  
  public void onViewCreated(View view, Bundle bundle)
  {
    super.onViewCreated(view, bundle);
    this.mSwitch = ((SwitchCompat)view.findViewById(R.id.mSwitch));
    this.linearLogout = ((LinearLayout)view.findViewById(R.id.linearLogout));
    this.linearProfile = ((LinearLayout)view.findViewById(R.id.linearProfile));
    this.linearChangePIN = ((LinearLayout)view.findViewById(R.id.linearChangePIN));
    linearSymbol=(LinearLayout)view.findViewById(R.id.linearSymbol);
    txtVersion=(TextView)view.findViewById(R.id.txtVersion);

    TextView txtPush=(TextView)view.findViewById(R.id.txtPush);
    TextView txtLogout=(TextView)view.findViewById(R.id.txtLogout);
    TextView txtEditProfile=(TextView)view.findViewById(R.id.txtEditProfile);
    TextView txtDefault=(TextView)view.findViewById(R.id.txtDefault);

    Font.LATO_Regular.apply(getActivity(), txtPush);
    Font.LATO_Regular.apply(getActivity(), txtLogout);
    Font.LATO_Regular.apply(getActivity(), txtEditProfile);
    Font.LATO_Regular.apply(getActivity(), txtDefault);

    setVersionName();

    prefNotify=getActivity().getSharedPreferences("Push_Notification", Context.MODE_PRIVATE);
    boolean notify=prefNotify.getBoolean("notify",false);
    mSwitch.setChecked(notify);

/////////////////////////////////////////////////////////////////////////////////////////
    this.linearChangePIN.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
          /*check pin added or not */
        if(TextUtils.isEmpty(SharedDataSaveLoad.load(getActivity(),getResources().getString(R.string.shared_pref_lock_pin)))){
          Intent changePassword = new Intent(getActivity(), LockConfirmActivity.class);
          startActivity(changePassword);
        }else {
          Intent changePassword = new Intent(getActivity(), ChangePINActivity.class);
          startActivity(changePassword);
        }
      }
    });
///////////////////////////////////////////////////////////////////////////////////////////

    this.linearLogout.setOnClickListener(new OnClickListener() {
      public void onClick(View v1) {

        Handler h=new Handler();
        h.postDelayed(new Runnable() {
          @Override
          public void run() {
            CheckConnectivity connectivity=new CheckConnectivity(getActivity());
            if(connectivity.isConnected())
            {
              SweetAlertDialog sDialog = new SweetAlertDialog(SettingFragment.this.getActivity(), SweetAlertDialog.WARNING_MODIFIED_TYPE);
              sDialog.setTitleText("Are you sure?");
              sDialog.setConfirmText("Sign out");
              sDialog.setCancelText("Cancel");
              sDialog.setCancelable(false);
              sDialog.setConfirmClickListener(new OnSweetClickListener() {
                public void onClick(SweetAlertDialog mDialog) {
                  mDialog.dismiss();
                  new LogoutTask().execute();
                }
              });
              sDialog.setCancelClickListener(new OnSweetClickListener() {
                public void onClick(SweetAlertDialog mDialog) {
                  mDialog.dismiss();
                }
              });
              sDialog.show();
            }
            else Toast.makeText(getActivity(),"Internet connection required",Toast.LENGTH_SHORT).show();

          }
        }, 100);

      }
    });



    mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        editorNotify = prefNotify.edit();
        editorNotify.putBoolean("notify", b);
        editorNotify.commit();
      }
    });



    this.linearProfile.setOnClickListener(new OnClickListener() {
      public void onClick(View paramAnonymousView) {

      }
    });




    linearSymbol.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent in = new Intent(getActivity(), SymbolActivity.class);
            startActivity(in);
          }
        },100);

      }
    });




  }



  private void makeLogout()
  {
      clearPin();
    clearAllCache();
    SharedDataSaveLoad.save(getActivity(), getActivity().getResources().getString(R.string.shared_pref_previous_emp_id), SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_employee_id)));
    SharedDataSaveLoad.remove(SettingFragment.this.getActivity(), SettingFragment.this.getResources().getString(R.string.shared_pref_employee_id));
    SharedDataSaveLoad.remove(SettingFragment.this.getActivity(), SettingFragment.this.getResources().getString(R.string.shared_pref_employee_name));
    SettingFragment.this.startActivity(new Intent(SettingFragment.this.getActivity(), RegActivity.class));
    SettingFragment.this.getActivity().finish();
  }




  private void setVersionName()
  {
    PackageInfo pInfo = null;
    try {
      pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
      String version = pInfo.versionName;
      txtVersion.setText("Version: "+version);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }







  private void logoutFailed()
  {
    SweetAlertDialog sDialog = new SweetAlertDialog(SettingFragment.this.getActivity(), SweetAlertDialog.WARNING_MODIFIED_TYPE);
    sDialog.setTitleText("Logout failed");
    sDialog.setConfirmText("Try Again");
    sDialog.setCancelText("Cancel");
    sDialog.setCancelable(false);
    sDialog.setConfirmClickListener(new OnSweetClickListener() {
      public void onClick(SweetAlertDialog mDialog) {
        mDialog.dismiss();
        new LogoutTask().execute();
      }
    });
    sDialog.setCancelClickListener(new OnSweetClickListener() {
      public void onClick(SweetAlertDialog mDialog) {
        mDialog.dismiss();
      }
    });
    sDialog.show();
  }




  private void clearAllCache()
  {
    try
    {
      String path_dir=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.offline_directory);
      File dir = new File(path_dir);
      if(dir.isDirectory())
      {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++)
        {
          new File(dir, children[i]).delete();
        }
      }
    }
    catch (Exception e)
    {

    }
  }

    /////
    private void clearPin() {
        SharedDataSaveLoad.remove(getActivity(), getResources().getString(R.string.shared_pref_lock_pin));
    }
    //////////




  private class LogoutTask extends AsyncTask<String, String, String>
  {
    SweetAlertDialog sweetProgress;

    protected String doInBackground(String... paramVarArgs)
    {
      try
      {
        ConnectionHelper helper = new ConnectionHelper();
        helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
        helper.addData(new Uri.Builder().appendQueryParameter("action", "user_logout")
                .appendQueryParameter("user_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id))));
        return helper.getResponse();
      }
      catch (Exception e)
      {
        return null;
      }

    }

    protected void onPostExecute(String paramString)
    {
        sweetProgress.dismiss();

        if (!TextUtils.isEmpty(paramString)) // check response is null or not

            try
      {
//        Log.e("response logout", paramString);

        JSONObject json = new JSONObject(paramString);
        int success=json.getInt("success");
        if(success==1)
        {
          makeLogout();
        }
        else logoutFailed();
      }
      catch (Exception e)
      {
        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
      }
    }

    protected void onPreExecute()
    {
      sweetProgress = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      sweetProgress.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
      sweetProgress.setTitleText("");
      sweetProgress.setCancelable(false);
      sweetProgress.show();
    }
  }









}


