package com.asif.linkkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.helper.MyMenuClass;
import com.asif.linkkin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ASUS on 10/25/2015.
 */
public class NewsFeedActivity extends AppCompatActivity{

    ImageView imgNext;
    LinearLayout linearKincoming,linearKinteract,linearKinformation,linearKintertainment;
    CardView cardKincoming,cardKinformation,cardKinteract,cardKintertainment;
    SweetAlertDialog mDialog;

    String kincomingDate,kinformationDate,kintertainmentDate,kinteractDate,employeeId;
    int successEvent,successInfo,successEntertainment,successInteract;

    String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    private int trimLength=60;

    ArrayList<String>eventDetails=new ArrayList<String>();
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

    DbHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_layout);
        DisconnectClass disconnectClass=new DisconnectClass(this);
        imgNext=(ImageView)findViewById(R.id.imgNext);
        linearKincoming=(LinearLayout)findViewById(R.id.linearKincoming);
        linearKinteract=(LinearLayout)findViewById(R.id.linearKinteract);
        linearKinformation=(LinearLayout)findViewById(R.id.linearKinformation);
        linearKintertainment=(LinearLayout)findViewById(R.id.linearKintertainment);
        cardKincoming=(CardView)findViewById(R.id.cardKincoming);
        cardKinformation=(CardView)findViewById(R.id.cardKinformation);
        cardKinteract=(CardView)findViewById(R.id.cardKinteract);
        cardKintertainment=(CardView)findViewById(R.id.cardKintertainment);

        try {
            helper=new DbHelper(NewsFeedActivity.this,Constants.DATABASE_NAME,1);
        }
        catch (Exception e) {}

      //  prepareAllData();
       // addAllItems();
        MyMenuClass tempClass=new MyMenuClass(NewsFeedActivity.this);
        tempClass.setEmployee();
        employeeId=MyMenuClass.employeeId;
        getSeenDate();
        new UpdateFinder().execute();

        imgNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CheckConnectivity connectivity=new CheckConnectivity(NewsFeedActivity.this);
                        if(connectivity.isConnected())
                        {
                            Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                            in.putExtra("type","");
                            in.putExtra("category", "");
                            startActivity(in);
                            NewsFeedActivity.this.finish();
                        }
                        else
                        {
                            SharedPreferences pref=getSharedPreferences("database_linkkin",0);
                            String kindom=pref.getString("kindom","no");
                            if(!kindom.equals("no"))
                            {
                                Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                                in.putExtra("type","");
                                in.putExtra("category", "");
                                startActivity(in);
                                NewsFeedActivity.this.finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please turn on internet connection of your device",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).start();
            }
        });


    }



    public void getSeenDate()
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
            HashMap<String,ArrayList<String>>data=helper.getAllRowByColumn(Constants.TABLE_SEEN_DATE,columns,Constants.SEEN_DATE_TYPE);
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
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(getApplicationContext(),kinteractDate,Toast.LENGTH_LONG).show();
    }


    public void getLocalIds()
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
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
    }


    public void removeRedundantInfos()
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

            retreiveUpdatedData();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }

    }


    public void retreiveUpdatedData()
    {
        eventDetails=new ArrayList<String>();
        eventDates=new ArrayList<String>();
        eventIds=new ArrayList<String>();

        infoDetails=new ArrayList<String>();
        infoDates=new ArrayList<String>();
        infoIds=new ArrayList<String>();
        infoTypes=new ArrayList<String>();
        infoImgLinks=new ArrayList<String>();

        enterIds=new ArrayList<String>();
        enterTitles=new ArrayList<String>();
        enterDetails=new ArrayList<String>();
        enterTypes=new ArrayList<String>();
        enterImgLinks=new ArrayList<String>();
        enterTimes=new ArrayList<String>();

        interactIds=new ArrayList<String>();
        interactPostIds=new ArrayList<String>();
        interactPostTitles=new ArrayList<String>();
        interactEmpIds=new ArrayList<String>();
        interactCommentors=new ArrayList<String>();
        interactDates=new ArrayList<String>();
        interactDescriptions=new ArrayList<String>();

        try
        {
            HashMap<String,ArrayList<String>>data1=helper.getAllRow(Constants.TABLE_EVENT,Constants.EVENT_DATE);
            eventIds=data1.get(Constants.EVENT_ID);
            eventDates=data1.get(Constants.EVENT_DATE);
            eventDetails=data1.get(Constants.EVENT_DETAIL);

            HashMap<String,ArrayList<String>>data2=helper.getAllRow(Constants.TABLE_INFORMATION,Constants.INFORMATION_DATE);
            infoIds=data2.get(Constants.INFORMATION_ID);
            infoDates=data2.get(Constants.INFORMATION_DATE);
            infoDetails=data2.get(Constants.INFORMATION_DETAILS);
            infoTypes=data2.get(Constants.INFORMATION_TYPE);
            infoImgLinks=data2.get(Constants.INFORMATION_IMAGE_LINK);

            HashMap<String,ArrayList<String>>data3=helper.getAllRow(Constants.TABLE_ENTERTAINMENT,Constants.ENTERTAINMENT_ID);
            enterIds=data3.get(Constants.ENTERTAINMENT_ID);
            enterTitles=data3.get(Constants.ENTERTAINMENT_TITLE);
            enterDetails=data3.get(Constants.ENTERTAINMENT_DETAIL);
            enterTypes=data3.get(Constants.ENTERTAINMENT_TYPE);
            enterImgLinks=data3.get(Constants.ENTERTAINMENT_IMAGE_LINK);
            enterTimes=data3.get(Constants.ENTERTAINMENT_TIME);

            HashMap<String,ArrayList<String>>data4=helper.getAllRow(Constants.TABLE_INTERACT_COMMENT,Constants.INTERACT_COMMENT_DATE);
            interactIds=data4.get(Constants.INTERACT_COMMENT_ID);
            interactPostIds=data4.get(Constants.INTERACT_COMMENT_POST_ID);
            interactEmpIds=data4.get(Constants.INTERACT_COMMENT_EMPLOYEE_ID);
            interactPostTitles=data4.get(Constants.INTERACT_COMMENT_POST);
            interactCommentors=data4.get(Constants.INTERACT_COMMENT_COMMENTOR);
            interactDates=data4.get(Constants.INTERACT_COMMENT_DATE);
            interactDescriptions=data4.get(Constants.INTERACT_COMMENT_DESCRIPTION);

            addAllItems();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
        }
    }



    private void prepareAllData()
    {
        infoDates=new ArrayList<String>();
        infoDetails=new ArrayList<String>();
        for(int i=1;i<=3;i++)
        {
            infoDates.add("October "+i+", 2015");
            infoDetails.add("This is details information of the item..");
        }
    }


    private void addAllItems()
    {
        if(infoIds.size()==0&&eventIds.size()==0&&interactIds.size()==0&&enterIds.size()==0)
        {
            if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
            Toast.makeText(getApplicationContext(),"No update available",Toast.LENGTH_SHORT).show();
            Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
            in.putExtra("type","");
            in.putExtra("category", "");
            startActivity(in);
            NewsFeedActivity.this.finish();
        }
        else
        {
            if(infoIds.size()>0) addInformationItems();
            else cardKinformation.setVisibility(View.GONE);
            if(eventIds.size()>0) addEventItems();
            else cardKincoming.setVisibility(View.GONE);
            if(interactIds.size()>0) addInteractItems();
            else cardKinteract.setVisibility(View.GONE);
            if(enterIds.size()>0) addEntertainmentItems();
            else cardKintertainment.setVisibility(View.GONE);
            if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
        }
    /*    if(infoIds.size()>0) addInformationItems();
        else cardKinformation.setVisibility(View.GONE);
        if(eventIds.size()>0) addEventItems();
        else cardKincoming.setVisibility(View.GONE);
        if(interactIds.size()>0) addInteractItems();
        else cardKinteract.setVisibility(View.GONE);
        if(enterIds.size()>0) addEntertainmentItems();
        else cardKintertainment.setVisibility(View.GONE);  */
    }



    private void addInformationItems()
    {
        linearKinformation.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(NewsFeedActivity.this);
        View[] views=new View[infoDates.size()];
        TextView[] txtDates=new TextView[infoDates.size()];
        TextView[] txtDetails=new TextView[infoDates.size()];
        TextView[] txtTypes=new TextView[infoDates.size()];
        LinearLayout[] linearUpdates=new LinearLayout[infoDates.size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<infoDates.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }

            Date sDate = null;
            try {
                sDate = format.parse(infoDates.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal=Calendar.getInstance();
            cal.setTime(sDate);
            int yy = cal.get(Calendar.YEAR);
            int mm = cal.get(Calendar.MONTH);
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            String sss = this.months[mm] + " " + dd + ", " + yy;

            txtDates[i].setText(sss);
            CharSequence ss=getTrimmedText(infoDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setText(infoTypes.get(i).toUpperCase());
            linearKinformation.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i,"information"));
        }
    }



    private void addEventItems()
    {
        linearKincoming.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(NewsFeedActivity.this);
        View[] views=new View[eventDates.size()];
        TextView[] txtDates=new TextView[eventDates.size()];
        TextView[] txtDetails=new TextView[eventDates.size()];
        TextView[] txtTypes=new TextView[eventDates.size()];
        LinearLayout[] linearUpdates=new LinearLayout[eventDates.size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<eventDates.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }

            Date sDate = null;
            try {
                sDate = format.parse(eventDates.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal=Calendar.getInstance();
            cal.setTime(sDate);
            int yy = cal.get(Calendar.YEAR);
            int mm = cal.get(Calendar.MONTH);
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            String sss = this.months[mm] + " " + dd + ", " + yy;

            txtDates[i].setText(sss);
            CharSequence ss=getTrimmedText(eventDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setVisibility(View.GONE);
           // txtTypes[i].setText(infoTypes.get(i));
            linearKincoming.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "event"));
        }
    }


    private void addInteractItems()
    {
        linearKinteract.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(NewsFeedActivity.this);
        View[] views=new View[interactIds.size()];
        TextView[] txtDates=new TextView[interactIds.size()];
        TextView[] txtDetails=new TextView[interactIds.size()];
        TextView[] txtTypes=new TextView[interactIds.size()];
        LinearLayout[] linearUpdates=new LinearLayout[interactIds.size()];
        for(int i=0;i<interactIds.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }
            txtDates[i].setText(interactPostTitles.get(i));
            CharSequence ss=getTrimmedText(interactDescriptions.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setVisibility(View.GONE);
            // txtTypes[i].setText(infoTypes.get(i));
            //txtDates[i].setTextColor(Color.parseColor("#282828"));
            linearKinteract.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "interact"));
        }
    }

    private void addEntertainmentItems()
    {
        linearKintertainment.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(NewsFeedActivity.this);
        View[] views=new View[enterIds.size()];
        TextView[] txtDates=new TextView[enterIds.size()];
        TextView[] txtDetails=new TextView[enterIds.size()];
        TextView[] txtTypes=new TextView[enterIds.size()];
        LinearLayout[] linearUpdates=new LinearLayout[enterIds.size()];
        for(int i=0;i<enterIds.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }
            txtDates[i].setText(enterTitles.get(i));
            CharSequence ss=getTrimmedText(enterDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setText(enterTypes.get(i).toUpperCase());
            txtDates[i].setTextColor(Color.parseColor("#282828"));
            linearKintertainment.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "entertainment"));
        }
    }



    private CharSequence getTrimmedText(String originalText)
    {
        if(originalText.length()<=trimLength) return originalText;
        else return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(".....");
    }


    View.OnClickListener handleOnClick(final LinearLayout linear, final int pos,final String tag) {
        return new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Clicked "+tag+": "+pos,Toast.LENGTH_SHORT).show();
                CheckConnectivity connectivity=new CheckConnectivity(NewsFeedActivity.this);
                if(connectivity.isConnected())
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            }
                            catch (Exception e) {}

                            if(tag.equals("information"))
                            {
                                Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                                in.putExtra("type","information");
                                in.putExtra("category",infoTypes.get(pos));
                                startActivity(in);
                                NewsFeedActivity.this.finish();
                            }
                            else if(tag.equals("event"))
                            {
                                Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                                in.putExtra("type","event");
                                in.putExtra("category", eventDates.get(pos));
                                startActivity(in);
                                NewsFeedActivity.this.finish();
                            }
                            else if(tag.equals("interact"))
                            {
                                Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                                in.putExtra("type","interact");
                                in.putExtra("category", "");
                                startActivity(in);
                                NewsFeedActivity.this.finish();
                            }
                            else if(tag.equals("entertainment"))
                            {
                                Intent in=new Intent(NewsFeedActivity.this,MainActivity.class);
                                in.putExtra("type","entertainment");
                                in.putExtra("category", enterTypes.get(pos));
                                startActivity(in);
                                NewsFeedActivity.this.finish();
                            }
                        }
                    }).start();
                }
                else Toast.makeText(getApplicationContext(),"Please turn on internet connection of your device",Toast.LENGTH_SHORT).show();
            }
        };
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


    private class UpdateFinder extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
        /*    Calendar cal=Calendar.getInstance(Locale.getDefault());
            int day=cal.get(Calendar.DAY_OF_MONTH);
            int month=cal.get(Calendar.MONTH);
            int year=cal.get(Calendar.YEAR);
            String sDate=year+"-"+(month+1)+"-"+day;   */
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
                Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onPreExecute() {
            mDialog = new SweetAlertDialog(NewsFeedActivity.this, SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            mDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.primary_color_dark));
            mDialog.setTitleText("");
            mDialog.setCancelable(false);
            mDialog.show();
        }
    }




}
