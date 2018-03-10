package com.asif.linkkin.helper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;
import com.asif.linkkin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by kanchan on 11/10/2015.
 */
public class ManageNotification {

    private Activity activity;
    DbHelper helper;

    String kincomingDate,kinformationDate,kintertainmentDate,kinteractDate,employeeId;
    int successEvent,successInfo,successEntertainment,successInteract;

    ArrayList<String> eventDetails=new ArrayList<String>();
    ArrayList<String>eventDates=new ArrayList<String>();
    ArrayList<String>eventIds=new ArrayList<String>();

    ArrayList<String>infoDetails=new ArrayList<String>();
    ArrayList<String>infoDates=new ArrayList<String>();
    ArrayList<String>infoIds=new ArrayList<String>();
    ArrayList<String>infoTypes=new ArrayList<String>();
    ArrayList<String>infoImgLinks=new ArrayList<String>();

    ArrayList<String>enterIds=new ArrayList<String>();
    ArrayList<String>enterTitles=new ArrayList<String>();
    ArrayList<String>enterDetails=new ArrayList<String>();
    ArrayList<String>enterTypes=new ArrayList<String>();
    ArrayList<String>enterImgLinks=new ArrayList<String>();
    ArrayList<String>enterTimes=new ArrayList<String>();

    ArrayList<String>interactIds=new ArrayList<String>();
    ArrayList<String>interactPostIds=new ArrayList<String>();
    ArrayList<String>interactPostTitles=new ArrayList<String>();
    ArrayList<String>interactEmpIds=new ArrayList<String>();
    ArrayList<String>interactCommentors=new ArrayList<String>();
    ArrayList<String>interactDates=new ArrayList<String>();
    ArrayList<String>interactDescriptions=new ArrayList<String>();

    ArrayList<String>localEventIds=new ArrayList<String>();
    ArrayList<String>localInfoIds=new ArrayList<String>();
    ArrayList<String>localEnterIds=new ArrayList<String>();
    ArrayList<String>localInterIds=new ArrayList<String>();


    public ManageNotification(Activity activity)
    {
        this.activity=activity;
    }


    public void manageAllNotifications()
    {
        try {
            helper=new DbHelper(activity, Constants.DATABASE_NAME,1);
        }
        catch (Exception e) {}

        MyMenuClass tempClass=new MyMenuClass((FragmentActivity)activity);
        tempClass.setEmployee();
        employeeId=MyMenuClass.employeeId;

        getSeenDate();
        if(kincomingDate.equals("")||kinformationDate.equals("")||kinteractDate.equals("")||kintertainmentDate.equals(""))
        {
            insertDate();
        }
        else
        {
            new UpdateFinder().execute();
        }

    }



    private void getSeenDate()
    {
        kincomingDate="";
        kinformationDate="";
        kintertainmentDate="";
        kinteractDate="";
        try
        {
            ArrayList<String>columns=new ArrayList<String>();
            columns.add(Constants.SEEN_DATE_DATE);
            columns.add(Constants.SEEN_DATE_TYPE);
            HashMap<String,ArrayList<String>> data=helper.getAllRowByColumn(Constants.TABLE_SEEN_DATE,columns,Constants.SEEN_DATE_TYPE);
            ArrayList<String>tempDates=data.get(Constants.SEEN_DATE_DATE);
            ArrayList<String>tempTypes=data.get(Constants.SEEN_DATE_TYPE);
            for(int i=0;i<tempTypes.size();i++)
            {
                String tempType=tempTypes.get(i);
                if(tempType.equals("event")) kincomingDate=tempDates.get(i);
                else if(tempType.equals("information")) kinformationDate=tempDates.get(i);
                else if(tempType.equals("entertainment")) kintertainmentDate=tempDates.get(i);
                else if(tempType.equals("interact")) kinteractDate=tempDates.get(i);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
        }
        // Toast.makeText(getApplicationContext(),kinteractDate,Toast.LENGTH_LONG).show();
    }






    private void getLocalIds()
    {
        try
        {
            ArrayList<String>columns1=new ArrayList<String>();
            columns1.add(Constants.EVENT_ID);
            HashMap<String,ArrayList<String>>data1=helper.getAllRowByColumn(Constants.TABLE_EVENT,columns1,Constants.EVENT_ID);
            localEventIds=data1.get(Constants.EVENT_ID);

            ArrayList<String>columns2=new ArrayList<String>();
            columns2.add(Constants.INFORMATION_ID);
            HashMap<String,ArrayList<String>>data2=helper.getAllRowByColumn(Constants.TABLE_INFORMATION,columns2,Constants.INFORMATION_ID);
            localInfoIds=data2.get(Constants.INFORMATION_ID);

            ArrayList<String>columns3=new ArrayList<String>();
            columns3.add(Constants.ENTERTAINMENT_ID);
            HashMap<String,ArrayList<String>>data3=helper.getAllRowByColumn(Constants.TABLE_ENTERTAINMENT,columns3,Constants.ENTERTAINMENT_ID);
            localEnterIds=data3.get(Constants.ENTERTAINMENT_ID);

            ArrayList<String>columns4=new ArrayList<String>();
            columns4.add(Constants.INTERACT_COMMENT_ID);
            HashMap<String,ArrayList<String>>data4=helper.getAllRowByColumn(Constants.TABLE_INTERACT_COMMENT, columns4, Constants.INTERACT_COMMENT_ID);
            localInterIds=data4.get(Constants.INTERACT_COMMENT_ID);

            removeRedundantInfos();
        }
        catch (Exception e)
        {
            Toast.makeText(activity,""+e,Toast.LENGTH_SHORT).show();
        }
    }


    private void removeRedundantInfos()
    {
        for(int i=0;i<eventIds.size();i++)
        {
            if(localEventIds.contains(eventIds.get(i)))
                eventIds.set(i,"-1");
        }
        for(int i=0;i<infoIds.size();i++)
        {
            if(localInfoIds.contains(infoIds.get(i)))
                infoIds.set(i,"-1");
        }
        for(int i=0;i<enterIds.size();i++)
        {
            if(localEnterIds.contains(enterIds.get(i)))
                enterIds.set(i,"-1");
        }
        for(int i=0;i<interactIds.size();i++)
        {
            if(localInterIds.contains(interactIds.get(i)))
                interactIds.set(i,"-1");
        }


        try
        {
            boolean response;

            LinkedHashMap<String, Integer> column1=new LinkedHashMap<String, Integer>();
            column1.put(Constants.EVENT_ID,Constants.IS_NUMBER);
            column1.put(Constants.EVENT_DATE,Constants.IS_STRING);
            column1.put(Constants.EVENT_DETAIL,Constants.IS_STRING);
            HashMap< Integer,ArrayList<String>> value1=new HashMap<Integer, ArrayList<String>>();
            int j=0;
            for(int i=0;i<eventIds.size();i++)
            {
                if(!eventIds.get(i).equals("-1"))
                {
                    ArrayList<String>data= new ArrayList<String>();
                    data.add(eventIds.get(i));
                    data.add(eventDates.get(i));
                    data.add(eventDetails.get(i));
                    value1.put(j, data);
                    j++;
                }
            }
            if(j>0) response=helper.insertRowByColumn(Constants.TABLE_EVENT, column1, value1);

            LinkedHashMap<String, Integer> column2=new LinkedHashMap<String, Integer>();
            column2.put(Constants.INFORMATION_ID,Constants.IS_NUMBER);
            column2.put(Constants.INFORMATION_DATE,Constants.IS_STRING);
            column2.put(Constants.INFORMATION_DETAILS,Constants.IS_STRING);
            column2.put(Constants.INFORMATION_TYPE,Constants.IS_STRING);
            column2.put(Constants.INFORMATION_IMAGE_LINK,Constants.IS_STRING);
            HashMap< Integer,ArrayList<String>> value2=new HashMap<Integer, ArrayList<String>>();
            j=0;
            for(int i=0;i<infoIds.size();i++)
            {
                if(!infoIds.get(i).equals("-1"))
                {
                    ArrayList<String>data= new ArrayList<String>();
                    data.add(infoIds.get(i));
                    data.add(infoDates.get(i));
                    data.add(infoDetails.get(i));
                    data.add(infoTypes.get(i));
                    data.add(infoImgLinks.get(i));
                    value2.put(j, data);
                    j++;
                }
            }
            if(j>0) response=helper.insertRowByColumn(Constants.TABLE_INFORMATION, column2, value2);

            LinkedHashMap<String, Integer> column3=new LinkedHashMap<String, Integer>();
            column3.put(Constants.ENTERTAINMENT_ID,Constants.IS_NUMBER);
            column3.put(Constants.ENTERTAINMENT_TITLE,Constants.IS_STRING);
            column3.put(Constants.ENTERTAINMENT_DETAIL,Constants.IS_STRING);
            column3.put(Constants.ENTERTAINMENT_TYPE,Constants.IS_STRING);
            column3.put(Constants.ENTERTAINMENT_IMAGE_LINK,Constants.IS_STRING);
            column3.put(Constants.ENTERTAINMENT_TIME,Constants.IS_STRING);

            HashMap< Integer,ArrayList<String>> value3=new HashMap<Integer, ArrayList<String>>();
            j=0;
            for(int i=0;i<enterIds.size();i++)
            {
                if(!enterIds.get(i).equals("-1"))
                {
                    ArrayList<String>data= new ArrayList<String>();
                    data.add(enterIds.get(i));
                    data.add(enterTitles.get(i));
                    data.add(enterDetails.get(i));
                    data.add(enterTypes.get(i));
                    data.add(enterImgLinks.get(i));
                    data.add(enterTimes.get(i));
                    value3.put(j, data);
                    j++;
                }
            }
            if(j>0) response=helper.insertRowByColumn(Constants.TABLE_ENTERTAINMENT, column3, value3);

            LinkedHashMap<String, Integer> column4=new LinkedHashMap<String, Integer>();
            column4.put(Constants.INTERACT_COMMENT_ID,Constants.IS_NUMBER);
            column4.put(Constants.INTERACT_COMMENT_POST_ID,Constants.IS_STRING);
            column4.put(Constants.INTERACT_COMMENT_EMPLOYEE_ID,Constants.IS_STRING);
            column4.put(Constants.INTERACT_COMMENT_POST,Constants.IS_STRING);
            column4.put(Constants.INTERACT_COMMENT_COMMENTOR,Constants.IS_STRING);
            column4.put(Constants.INTERACT_COMMENT_DATE,Constants.IS_STRING);
            column4.put(Constants.INTERACT_COMMENT_DESCRIPTION,Constants.IS_STRING);

            HashMap< Integer,ArrayList<String>> value4=new HashMap<Integer, ArrayList<String>>();
            j=0;
            for(int i=0;i<interactIds.size();i++)
            {
                if(!interactIds.get(i).equals("-1"))
                {
                    ArrayList<String>data= new ArrayList<String>();
                    data.add(interactIds.get(i));
                    data.add(interactPostIds.get(i));
                    data.add(interactEmpIds.get(i));
                    data.add(interactPostTitles.get(i));
                    data.add(interactCommentors.get(i));
                    data.add(interactDates.get(i));
                    data.add(interactDescriptions.get(i));
                    value4.put(j, data);
                    j++;
                }
            }
            if(j>0) response=helper.insertRowByColumn(Constants.TABLE_INTERACT_COMMENT, column4, value4);

            helper.close();

        }
        catch (Exception e)
        {
            Toast.makeText(activity,""+e,Toast.LENGTH_SHORT).show();
        }

    }





    private void insertDate()
    {
        Calendar current=Calendar.getInstance(Locale.getDefault());
        int curDay=current.get(Calendar.DAY_OF_MONTH);
        int curMonth=current.get(Calendar.MONTH);
        int curYear=current.get(Calendar.YEAR);
        int curHour=current.get(Calendar.HOUR_OF_DAY);
        int curMinute=current.get(Calendar.MINUTE);
        int curSecond=current.get(Calendar.SECOND);

        String curSDate=curYear+"-"+formatedText(curMonth+1)+"-"+formatedText(curDay)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond);
        //String curSDate=curYear+"-"+formatedText(curMonth+1)+"-"+formatedText(curDay)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond);

        try {
            SQLiteDatabase db=helper.getEditableDB();

            if(kincomingDate.equals(""))
            {
                String query="DELETE FROM seen_date WHERE type LIKE 'event';";
                db.execSQL(query);

                LinkedHashMap<String, Integer> column=new LinkedHashMap<String, Integer>();
                column.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
                column.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
                column.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
                // column2.put(Constants.SEEN_DATE_CATEGORY, Constants.IS_STRING);
                HashMap< Integer,ArrayList<String>> value=new HashMap<Integer, ArrayList<String>>();
                ArrayList<String>data= new ArrayList<String>();
                data.add(String.valueOf(1));
                data.add(curSDate);
                data.add("event");
                //  data2.add(str);
                value.put(0,data);
                boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column, value);
            }
            if(kinformationDate.equals(""))
            {
                String query="DELETE FROM seen_date WHERE type LIKE 'information';";
                db.execSQL(query);

                LinkedHashMap<String, Integer> column=new LinkedHashMap<String, Integer>();
                column.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
                column.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
                column.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
                // column2.put(Constants.SEEN_DATE_CATEGORY, Constants.IS_STRING);
                HashMap< Integer,ArrayList<String>> value=new HashMap<Integer, ArrayList<String>>();
                ArrayList<String>data= new ArrayList<String>();
                data.add(String.valueOf(2));
                data.add(curSDate);
                data.add("information");
                //  data2.add(str);
                value.put(0,data);
                boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column, value);
            }
            if(kinteractDate.equals(""))
            {
                String query="DELETE FROM seen_date WHERE type LIKE 'interact';";
                db.execSQL(query);

                LinkedHashMap<String, Integer> column=new LinkedHashMap<String, Integer>();
                column.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
                column.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
                column.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
                // column2.put(Constants.SEEN_DATE_CATEGORY, Constants.IS_STRING);
                HashMap< Integer,ArrayList<String>> value=new HashMap<Integer, ArrayList<String>>();
                ArrayList<String>data= new ArrayList<String>();
                data.add(String.valueOf(3));
                data.add(curSDate);
                data.add("interact");
                //  data2.add(str);
                value.put(0,data);
                boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column, value);
            }
            if(kintertainmentDate.equals(""))
            {
                String query="DELETE FROM seen_date WHERE type LIKE 'entertainment';";
                db.execSQL(query);

                LinkedHashMap<String, Integer> column=new LinkedHashMap<String, Integer>();
                column.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
                column.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
                column.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
                // column2.put(Constants.SEEN_DATE_CATEGORY, Constants.IS_STRING);
                HashMap< Integer,ArrayList<String>> value=new HashMap<Integer, ArrayList<String>>();
                ArrayList<String>data= new ArrayList<String>();
                data.add(String.valueOf(4));
                data.add(curSDate);
                data.add("entertainment");
                //  data2.add(str);
                value.put(0,data);
                boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column, value);
            }

        }
        catch (Exception e) {
            Toast.makeText(activity,""+e,Toast.LENGTH_SHORT).show();
        }
    }

    private String formatedText(int s)
    {
        if(s<10) return "0"+s;
        else return ""+s;
    }






    private class UpdateFinder extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            ConnectionHelper ch = new ConnectionHelper();
            ch.createConnection(Urls.GET_UPDATE, "POST");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("kincoming_date", kincomingDate)
                    .appendQueryParameter("kinformation_date", kinformationDate)
                    .appendQueryParameter("kinteract_date", kinteractDate)
                    .appendQueryParameter("kintertainment_date", kintertainmentDate)
                    .appendQueryParameter("employee_id", employeeId);
            ch.addData(builder);
            return ch.getResponse();
        }


        @Override
        protected void onPostExecute(String s) {
            // mDialog.dismiss();
            try {
                //  Log.i("update response:",s);
                JSONObject json=new JSONObject(s);

                successEvent=json.getInt("success_event");
                if(successEvent==1)
                {
                    JSONArray jEvents=json.getJSONArray("events");
                    for(int i=0;i<jEvents.length();i++)
                    {
                        JSONObject jEvent=jEvents.getJSONObject(i);
                        eventIds.add(String.valueOf(jEvent.getInt("id")));
                        eventDates.add(jEvent.getString("date"));
                        eventDetails.add(jEvent.getString("detail"));
                    }
                }

                successInfo=json.getInt("success_info");
                if(successInfo==1)
                {
                    JSONArray jInfos=json.getJSONArray("infos");
                    for(int i=0;i<jInfos.length();i++)
                    {
                        JSONObject jInfo=jInfos.getJSONObject(i);
                        infoIds.add(String.valueOf(jInfo.getInt("id")));
                        infoDates.add(jInfo.getString("date"));
                        infoDetails.add(jInfo.getString("detail"));
                        infoTypes.add(jInfo.getString("type"));
                        infoImgLinks.add(jInfo.getString("image_link"));
                    }
                }

                successEntertainment=json.getInt("success_entertainment");
                if(successEntertainment==1)
                {
                    JSONArray jEnters=json.getJSONArray("entertainments");
                    for(int i=0;i<jEnters.length();i++)
                    {
                        JSONObject jEnter=jEnters.getJSONObject(i);
                        enterIds.add(String.valueOf(jEnter.getInt("id")));
                        enterTitles.add(jEnter.getString("title"));
                        enterDetails.add(jEnter.getString("detail"));
                        enterTypes.add(jEnter.getString("type"));
                        enterImgLinks.add(jEnter.getString("image_link"));
                        enterTimes.add(jEnter.getString("time"));
                    }
                }

                successInteract=json.getInt("success_interact");
                if(successInteract==1)
                {
                    JSONArray jInters=json.getJSONArray("interacts");
                    for(int i=0;i<jInters.length();i++)
                    {
                        JSONObject jInter=jInters.getJSONObject(i);
                        interactIds.add(String.valueOf(jInter.getInt("id")));
                        interactPostIds.add(jInter.getString("post_id"));
                        interactPostTitles.add(jInter.getString("post_title"));
                        interactEmpIds.add(jInter.getString("employee_id"));
                        interactCommentors.add(jInter.getString("commentor"));
                        interactDescriptions.add(jInter.getString("description"));
                        interactDates.add(jInter.getString("date"));
                    }
                }

                // addAllItems();
                getLocalIds();
                // Toast.makeText(getApplicationContext(),""+ccc,Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(activity,""+e,Toast.LENGTH_SHORT).show();
            }
        }


    }






}
