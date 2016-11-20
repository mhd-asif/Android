package com.bscheme.linkkin.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bscheme.linkkin.LockActivity;
import com.bscheme.linkkin.LockConfirmActivity;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

import java.util.ArrayList;

/**
 * Created by ASUS on 10/25/2015.
 */
public class DisconnectClass {

    public static ArrayList<Activity>activities=new ArrayList<Activity>();

    public static final long DISCONNECT_TIMEOUT = 60000; // 5 min = 5 * 60 * 1000 ms

    public DisconnectClass(Activity activity)
    {
        activities.add(activity);
       // Toast.makeText(activities.get(0),activities.size()+"",Toast.LENGTH_SHORT).show();
    }

    public static void removeActivity(Activity activity)
    {
        if(activities.contains(activity)) activities.remove(activity);
    }

    private static Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private static Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            if(activities.size()>0)
            {
                disconnectHandler.removeCallbacks(disconnectCallback);
                createLock();
              /*  CheckConnectivity connectivity=new CheckConnectivity(activities.get(0));
                if(connectivity.isConnected())
                {
                    new LogoutTask().execute();
                }  */
            }
        }
    };


    public static void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public static void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }



    private static void createLock()
    {
        Activity mActivity=activities.get(activities.size()-1);
        String lockPin= SharedDataSaveLoad.load(mActivity,mActivity.getResources().getString(R.string.shared_pref_lock_pin));
        if(lockPin.equals(""))
        {
            Intent in=new Intent(mActivity,LockConfirmActivity.class);
            mActivity.startActivity(in);
        }
        else
        {
            Intent in=new Intent(mActivity,LockActivity.class);
            mActivity.startActivity(in);
        }

}




/*
    private static void makeLogout()
    {
        clearAllCache();
        SharedDataSaveLoad.save(activities.get(0),activities.get(0).getResources().getString(R.string.shared_pref_previous_emp_id),SharedDataSaveLoad.load(activities.get(0),activities.get(0).getResources().getString(R.string.shared_pref_employee_id)));
        SharedDataSaveLoad.remove(activities.get(0), activities.get(0).getResources().getString(R.string.shared_pref_employee_id));
        SharedDataSaveLoad.remove(activities.get(0), activities.get(0).getResources().getString(R.string.shared_pref_employee_name));
        stopDisconnectTimer();
        for(int i=0;i<activities.size();i++)
            activities.get(i).finish();
        System.exit(1);
    }  */


    /*
    private static void clearAllCache()
    {
        try
        {
            String path_dir=activities.get(0).getCacheDir().getAbsolutePath()+"/"+activities.get(0).getResources().getString(R.string.offline_directory);
            File file = new File(path_dir);
            if(file.exists()) file.delete();
        }
        catch (Exception e)
        {

        }
    }   */


/*
    private static class LogoutTask extends AsyncTask<String, String, String>
    {


        protected String doInBackground(String... paramVarArgs)
        {
            //return new ConnectionHelper().getDataFromUrlByGET(Urls.GET_EVENT);
            try
            {
                ConnectionHelper helper = new ConnectionHelper();
                helper.createConnection(SharedDataSaveLoad.load(activities.get(0), activities.get(0).getResources().getString(R.string.shared_pref_api_url)), "POST");
                helper.addData(new Uri.Builder().appendQueryParameter("action", "user_logout")
                        .appendQueryParameter("user_id", SharedDataSaveLoad.load(activities.get(0), activities.get(0).getString(R.string.shared_pref_employee_id))));
                return helper.getResponse();
            }
            catch (Exception e)
            {
                return null;
            }

        }

        protected void onPostExecute(String paramString)
        {
            try
            {
                //Log.e("response logout", paramString);
                JSONObject json = new JSONObject(paramString);
                int success=json.getInt("success");
                if(success==1)
                {
                    makeLogout();
                }
            }
            catch (Exception e)
            {
                //Log.e("logout error: ","+e");
            }
        }

        protected void onPreExecute()
        {

        }
    }   */




}
