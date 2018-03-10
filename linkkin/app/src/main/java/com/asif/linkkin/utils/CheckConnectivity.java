package com.asif.linkkin.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnectivity
{
  Activity activity;
  
  public CheckConnectivity(Activity paramActivity)
  {
    this.activity = paramActivity;
  }
  
  public boolean isConnected()
  {
   // return (isConnectingInternet()||isWifiActive());
    return (isConnectingInternet());
  }
  
  public boolean isConnectingInternet()
  {
    Object localObject = (ConnectivityManager)this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (localObject != null)
    {
      localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
      if ((localObject != null) && (((NetworkInfo)localObject).isConnected())) {
        return true;
      }
    }
    return false;
  }

  /*public boolean isWifiActive()
  {
    WifiManager manager=(WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
    return manager.isWifiEnabled();
  }*/

}

