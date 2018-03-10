package com.asif.linkkin.helper;

import android.app.Activity;
import android.widget.TextView;

import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kanchan on 11/9/2015.
 */
public class NotificationCounter {

    public static TextView txtCountNoti;
    public static Activity activity;

    public NotificationCounter(Activity activity)
    {
        this.activity=activity;
    }

    public void setTextView(TextView textView)
    {
        txtCountNoti=textView;
    }

    public static void setNotificationText(String text)
    {
        txtCountNoti.setText(text);
    }



    public static int getNotificationCount()
    {
        int count=0;
        try {
            DbHelper helper=new DbHelper(activity, Constants.DATABASE_NAME,1);
            ArrayList<String>columns1=new ArrayList<>();
            columns1.add(Constants.EVENT_ID);
            HashMap<String,ArrayList<String>> data1=helper.getAllRowByColumn(Constants.TABLE_EVENT,columns1,Constants.EVENT_ID);
            count+=data1.get(Constants.EVENT_ID).size();

            ArrayList<String>columns2=new ArrayList<>();
            columns2.add(Constants.INFORMATION_ID);
            HashMap<String,ArrayList<String>>data2=helper.getAllRowByColumn(Constants.TABLE_INFORMATION,columns2,Constants.INFORMATION_DATE);
            count+=data2.get(Constants.INFORMATION_ID).size();

            ArrayList<String>columns3=new ArrayList<>();
            columns3.add(Constants.ENTERTAINMENT_ID);
            HashMap<String,ArrayList<String>>data3=helper.getAllRowByColumn(Constants.TABLE_ENTERTAINMENT,columns3,Constants.ENTERTAINMENT_ID);
            count+=data3.get(Constants.ENTERTAINMENT_ID).size();

            ArrayList<String>columns4=new ArrayList<>();
            columns4.add(Constants.ENTERTAINMENT_ID);
            HashMap<String,ArrayList<String>>data4=helper.getAllRowByColumn(Constants.TABLE_INTERACT_COMMENT,columns4,Constants.INTERACT_COMMENT_DATE);
            count+=data4.get(Constants.INTERACT_COMMENT_ID).size();

            helper.close();
            return count;
        }
        catch (Exception e) {
            return count;
        }
    }


}
