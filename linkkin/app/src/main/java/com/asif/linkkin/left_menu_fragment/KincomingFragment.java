package com.asif.linkkin.left_menu_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.KincomingDetailsActivity;
import com.asif.linkkin.R;
import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;
import com.asif.linkkin.models.KincomingDetailsInfo;
import com.asif.linkkin.helper.CalenderRow;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.utils.Font;
import com.asif.linkkin.helper.NotificationCounter;
import com.asif.linkkin.utils.SharedDataSaveLoad;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.FlexibleCalendarView.OnDateClickListener;
import com.p_v.flexiblecalendar.FlexibleCalendarView.OnMonthChangeListener;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KincomingFragment extends Fragment
{
  Activity activity;
  CalenderRow calRow;
  FlexibleCalendarView calView;
  int cellHeight,calHeight;
  Context context;

  ArrayList<String> details = new ArrayList();
  ArrayList<String> titles = new ArrayList();
  ArrayList<String> cDays = new ArrayList();
  ArrayList<String> trainingNos = new ArrayList();
  ArrayList<String> startDats = new ArrayList();
  ArrayList<String> endDates = new ArrayList();
  ArrayList<String> durations = new ArrayList();
  ArrayList<String> startTimes = new ArrayList();
  ArrayList<String> endTimes = new ArrayList();
  ArrayList<String> totalTimes = new ArrayList();
  ArrayList<String> venues = new ArrayList();
  ArrayList<String> participants = new ArrayList();
  ArrayList<String> groups = new ArrayList();
  ArrayList<String> resources = new ArrayList();
  ArrayList<String> targets = new ArrayList();
  ArrayList<String> statuses = new ArrayList();

  ArrayList<String> tempDetails = new ArrayList();
  ArrayList<String> tempTitles = new ArrayList();
  ArrayList<String> tempDays = new ArrayList();
  ArrayList<String> tempTrainingNos = new ArrayList();
  ArrayList<String> tempStartDates = new ArrayList();
  ArrayList<String> tempEndDates = new ArrayList();
  ArrayList<String> tempDurations = new ArrayList();
  ArrayList<String> tempStartTimes = new ArrayList();
  ArrayList<String> tempEndTimes = new ArrayList();
  ArrayList<String> tempTotalTimes = new ArrayList();
  ArrayList<String> tempVenues = new ArrayList();
  ArrayList<String> tempParticipants = new ArrayList();
  ArrayList<String> tempGroups = new ArrayList();
  ArrayList<String> tempResources = new ArrayList();
  ArrayList<String> tempTargets = new ArrayList();
  ArrayList<String> tempStatuses = new ArrayList();

  int height,stHeigh,acHeight;
  ImageView imgLeft;
  ImageView imgRight;
  LinearLayout linearList;
  int monthSerial;
  String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
  ScrollView scroll;
  TextView txtMonth;
  int width;
  int yearSerial;
  int pxValue;
  

  
  public void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
  {
    return inflater.inflate(R.layout.fragment_kincoming, container, false);
  }
  
  public void onViewCreated(View view, Bundle bundle)
  {
    super.onViewCreated(view, bundle);
    this.activity = getActivity();
    this.context = view.getContext();
    this.scroll = ((ScrollView)view.findViewById(R.id.scrollView));
    this.calView = ((FlexibleCalendarView)view.findViewById(R.id.calendar_view));
    this.imgLeft = ((ImageView)view.findViewById(R.id.left_arrow));
    this.imgRight = ((ImageView)view.findViewById(R.id.right_arrow));
    this.txtMonth = ((TextView)view.findViewById(R.id.month_text_view));
    this.linearList = ((LinearLayout)view.findViewById(R.id.linearList));
    DisplayMetrics matrics = this.context.getResources().getDisplayMetrics();
    this.width = matrics.widthPixels;
    this.height = matrics.heightPixels;
    this.cellHeight = (this.width / 7);

    Font.LATO_Bold.apply(getActivity(), txtMonth);

    stHeigh=getStatusBarHeight();
    acHeight=getActionBarHeight();

    float scale = getResources().getDisplayMetrics().density;
    pxValue = (int) (44 * scale + 0.5f);


    Calendar cal = Calendar.getInstance(Locale.getDefault());
    monthSerial = cal.get(Calendar.MONTH);
    yearSerial = cal.get(Calendar.YEAR);

    String txt = this.months[this.monthSerial] + " " + this.yearSerial;
    this.txtMonth.setText(txt);
    this.calRow = new CalenderRow();
    if (this.calRow.getTotalRow(this.monthSerial, this.yearSerial) != 5) {
      calHeight=(this.cellHeight * 7 - this.cellHeight / 8);
    }
    else calHeight = (this.cellHeight * 6 - this.cellHeight / 8);
    this.calView.getLayoutParams().height = calHeight;

    saveLocalDate();
    int count= NotificationCounter.getNotificationCount();
    if(count<1) NotificationCounter.setNotificationText("");
    else NotificationCounter.setNotificationText(""+count);

    String url=SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_api_url));
    Log.e("url:",url);

    CheckConnectivity connectivity=new CheckConnectivity(getActivity());
    if(connectivity.isConnected()) new MyKincomingInfos().execute();
    else
    {
      getOfflineRes();
    }

      this.imgLeft.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          KincomingFragment.this.calView.moveToPreviousMonth();
        }
      });

      this.imgRight.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          KincomingFragment.this.calView.moveToNextMonth();
        }
      });

      this.calView.setOnMonthChangeListener(new OnMonthChangeListener() {
        public void onMonthChange(int yy, int mm, int dd) {
          calView.changeMonth();
          String str = KincomingFragment.this.months[mm] + " " + yy;
          KincomingFragment.this.txtMonth.setText(str);
          if (KincomingFragment.this.calRow.getTotalRow(mm, yy) == 5) {
            calHeight=(KincomingFragment.this.cellHeight * 6 - KincomingFragment.this.cellHeight / 8);
          } else {
            calHeight = (KincomingFragment.this.cellHeight * 7 - KincomingFragment.this.cellHeight / 8);
          }
          calView.getLayoutParams().height=calHeight;

          KincomingFragment.this.monthSerial = mm;
          KincomingFragment.this.yearSerial = yy;

          CheckConnectivity connectivity=new CheckConnectivity(getActivity());
          if(connectivity.isConnected()) new MyKincomingInfos().execute();
          else
          {
            String path_kincoming=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kincoming)+"_"+months[monthSerial]+"_"+yearSerial;
            File file = new File(path_kincoming);
            if(file.exists())
            {
              getOfflineRes();
            }
            else
            {
              Toast.makeText(getActivity(),"Internet connection required",Toast.LENGTH_SHORT).show();

              titles = new ArrayList();
              details = new ArrayList();
              cDays = new ArrayList();
              trainingNos = new ArrayList();
              startDats = new ArrayList();
              endDates = new ArrayList();
              durations = new ArrayList();
              startTimes = new ArrayList();
              endTimes = new ArrayList();
              totalTimes = new ArrayList();
              venues = new ArrayList();
              participants = new ArrayList();
              groups = new ArrayList();
              resources = new ArrayList();
              targets = new ArrayList();
              statuses = new ArrayList();

              tempDays=new ArrayList<String>();
              tempTitles=new ArrayList<String>();
              tempDetails=new ArrayList<String>();
              tempTrainingNos=new ArrayList<String>();
              tempStartDates=new ArrayList<String>();
              tempEndDates=new ArrayList<String>();
              tempDurations=new ArrayList<String>();
              tempStartTimes=new ArrayList<String>();
              tempEndTimes=new ArrayList<String>();
              tempTotalTimes=new ArrayList<String>();
              tempVenues=new ArrayList<String>();
              tempParticipants=new ArrayList<String>();
              tempGroups=new ArrayList<String>();
              tempResources=new ArrayList<String>();
              tempTargets=new ArrayList<String>();
              tempStatuses=new ArrayList<String>();

              setAdapter();
            }
          }

        }
      });



    calView.setOnDateClickListener(new OnDateClickListener() {
      @Override
      public void onDateClick(int year, int month, int day) {
        if(day==1)
        {
          FlexibleCalendarView.pressedFirst = true;
        }
        Calendar cal=Calendar.getInstance();
        if(cal.get(Calendar.YEAR)==year&&cal.get(Calendar.MONTH)==month&&cal.get(Calendar.DAY_OF_MONTH)==day)
        {
          FlexibleCalendarView.pressedToday = true;
        }

        tempDays=new ArrayList<String>();
        tempTitles=new ArrayList<String>();
        tempDetails=new ArrayList<String>();
        tempTrainingNos=new ArrayList<String>();
        tempStartDates=new ArrayList<String>();
        tempEndDates=new ArrayList<String>();
        tempDurations=new ArrayList<String>();
        tempStartTimes=new ArrayList<String>();
        tempEndTimes=new ArrayList<String>();
        tempTotalTimes=new ArrayList<String>();
        tempVenues=new ArrayList<String>();
        tempParticipants=new ArrayList<String>();
        tempGroups=new ArrayList<String>();
        tempResources=new ArrayList<String>();
        tempTargets=new ArrayList<String>();
        tempStatuses=new ArrayList<String>();

        for(int i=0;i<cDays.size();i++)
        {
          if(cDays.get(i).equals(String.valueOf(day)))
          {
            tempDays.add(cDays.get(i));
            tempTitles.add(titles.get(i));
            tempTrainingNos.add(trainingNos.get(i));
            tempStartDates.add(startDats.get(i));
            tempEndDates.add(endDates.get(i));
            tempDurations.add(durations.get(i));
            tempStartTimes.add(startTimes.get(i));
            tempEndTimes.add(endTimes.get(i));
            tempTotalTimes.add(totalTimes.get(i));
            tempGroups.add(groups.get(i));
            tempParticipants.add(participants.get(i));
            tempResources.add(resources.get(i));
            tempTargets.add(targets.get(i));
            tempVenues.add(venues.get(i));
            tempStatuses.add(statuses.get(i));
          }
        }
        setListItems();
      }
    });





  }


  public int getStatusBarHeight() {
    int result = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public int getActionBarHeight()
  {
    TypedValue tv = new TypedValue();
    if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
    {
      int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
      return actionBarHeight;
    }
    return 0;
  }

  
  public void setAdapter()
  {
    setListItems();
    setAllEvents();
    calView.refresh();
  }

  
  public void setAllEvents()
  {
    FlexibleCalendarView.pressedFirst = false;
    FlexibleCalendarView.pressedToday = false;
    ArrayList<SelectedDateItem> selItems = new ArrayList();
    int i = 0;
    while (i < this.cDays.size())
    {
      selItems.add(new SelectedDateItem(yearSerial, monthSerial, Integer.valueOf((String) this.cDays.get(i)).intValue()));
      i += 1;
    }
    this.calView.addSelectedItems(selItems);
  }



  private void setListItems()
  {
    this.linearList.removeAllViewsInLayout();
    linearList.setVisibility(View.INVISIBLE);
    if(tempDays.size()>0)
    {
      LayoutInflater mInflater = LayoutInflater.from(getActivity());
      View[] views = new View[this.tempDays.size()];
      TextView[] txtDates = new TextView[this.tempDays.size()];
      TextView[] txtTitles = new TextView[this.tempDays.size()];
      int i = 0;
      while (i < this.tempDays.size())
      {
        views[i] = mInflater.inflate(R.layout.row_date, null);
        txtDates[i] = ((TextView)views[i].findViewById(R.id.txtDate));
        txtTitles[i] = ((TextView)views[i].findViewById(R.id.txtEvent));
        Font.LATO_Regular.apply(getActivity(), txtDates[i]);
        Font.LATO_Regular.apply(getActivity(), txtTitles[i]);
        if (i == 0) {
          ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
        }
        String str = this.months[monthSerial] + " " + (String)this.tempDays.get(i) + ", " + yearSerial;
        txtDates[i].setText(str);
        txtTitles[i].setText((CharSequence)this.tempTitles.get(i));
        views[i].setOnClickListener(handleOnClick(i));
        this.linearList.addView(views[i]);
        i += 1;
      }
    }
    else
    {
      TextView tempView=new TextView(getActivity());
      Font.LATO_Bold.apply(getActivity(), tempView);
      tempView.setText("No Event Available");
      tempView.setTextSize(20);
      tempView.setTextColor(Color.parseColor("#555555"));
      int tempHeight=height-(calHeight+pxValue+stHeigh+acHeight);
      tempView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tempHeight));
      tempView.setGravity(Gravity.CENTER);
      linearList.addView(tempView);
    }
    linearList.setVisibility(View.VISIBLE);
  }



  View.OnClickListener handleOnClick(final int pos)
  {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Handler hh=new Handler();
        hh.postDelayed(new Runnable() {
          @Override
          public void run() {
            KincomingDetailsInfo info=new KincomingDetailsInfo();
            info.title=tempTitles.get(pos);
            info.trainingNo=tempTrainingNos.get(pos);
            info.startDate=tempStartDates.get(pos);
            info.endDate=tempEndDates.get(pos);
            info.duration=tempDurations.get(pos);
            info.startTime=tempStartTimes.get(pos);
            info.endTime=tempEndTimes.get(pos);
            info.totalTime=tempTotalTimes.get(pos);
            info.venue=tempVenues.get(pos);
            info.participant=tempParticipants.get(pos);
            info.group=tempGroups.get(pos);
            info.resource=tempResources.get(pos);
            info.target=tempTargets.get(pos);
            info.status=tempStatuses.get(pos);
            info.day=tempDays.get(pos);
            Intent in=new Intent(context, KincomingDetailsActivity.class);
            in.putExtra("info",info);
            context.startActivity(in);
          }
        },100);
      }
    };
  }



  public void saveLocalDate()
  {
    Calendar current=Calendar.getInstance(Locale.getDefault());
    int curDay=current.get(Calendar.DAY_OF_MONTH);
    int curMonth=current.get(Calendar.MONTH);
    int curYear=current.get(Calendar.YEAR);
    int curHour=current.get(Calendar.HOUR_OF_DAY);
    int curMinute=current.get(Calendar.MINUTE);
    int curSecond=current.get(Calendar.SECOND);

    try {
      DbHelper helper=new DbHelper(getActivity(), Constants.DATABASE_NAME,1);
      SQLiteDatabase db=helper.getEditableDB();
      String query="DELETE FROM seen_date WHERE type LIKE 'event';";
      db.execSQL(query);
      query="DELETE FROM event;";
      db.execSQL(query);

      LinkedHashMap<String, Integer> column2=new LinkedHashMap<String, Integer>();
      column2.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
      column2.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
      column2.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
      HashMap< Integer,ArrayList<String>> value2=new HashMap<Integer, ArrayList<String>>();
      ArrayList<String>data2= new ArrayList<String>();
      data2.add(String.valueOf(1));
      data2.add(curYear+"-"+formatedText(curMonth+1)+"-"+formatedText(curDay)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond));
      data2.add("event");
      value2.put(0,data2);
      boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column2, value2);
      db.close();
    }
    catch (Exception e) {
      Toast.makeText(getActivity(),""+e,Toast.LENGTH_SHORT).show();
    }
  }


  private String formatedText(int s)
  {
    if(s<10) return "0"+s;
    else return ""+s;
  }


  private int getMonthDifference(int selM, int selY)
  {
    Calendar current=Calendar.getInstance(Locale.getDefault());
    int curM=current.get(Calendar.MONTH)+1;
    int curY=current.get(Calendar.YEAR);
    int diff;
    if(selY==curY)
    {
      diff=Math.abs(selM-curM);
    }
    else if(Math.abs(selY-curY)==1)
    {
      if(selY>curY) diff=selM+12-curM;
      else diff=curM+12-selM;
    }
    else
    {
      diff=12;
    }
    return diff;
  }




  private void getOfflineRes()
  {
    String path_kincoming=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kincoming)+"_"+months[monthSerial]+"_"+yearSerial;
    File file = new File(path_kincoming);
    if(file.exists())
    {
      int length = (int) file.length();
      byte[] bytes = new byte[length];
      FileInputStream in = null;
      try {
        in = new FileInputStream(file);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      try {
        in.read(bytes);
        String contents = new String(bytes);
        showAllData(contents);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }




  private void showAllData(String response)
  {
    titles = new ArrayList();
    details = new ArrayList();
    cDays = new ArrayList();
    trainingNos = new ArrayList();
    startDats = new ArrayList();
    endDates = new ArrayList();
    durations = new ArrayList();
    startTimes = new ArrayList();
    endTimes = new ArrayList();
    totalTimes = new ArrayList();
    venues = new ArrayList();
    participants = new ArrayList();
    groups = new ArrayList();
    resources = new ArrayList();
    targets = new ArrayList();
    statuses = new ArrayList();
    try {
      JSONObject json = new JSONObject(response);
      if (json.getInt("no_of_event") > 0) {
        JSONArray jEvents = json.getJSONArray("all_events");
        int i = 0;
        while (i < jEvents.length()) {
          JSONObject jObj = jEvents.getJSONObject(i);
          KincomingFragment.this.cDays.add(jObj.getString("event_date"));
          KincomingFragment.this.titles.add(jObj.getString("title"));
          trainingNos.add(jObj.getString("event_training_no"));
          startDats.add(jObj.getString("event_date_from"));
          endDates.add(jObj.getString("event_date_to"));
          durations.add(jObj.getString("event_total_days"));
          startTimes.add(jObj.getString("event_time_from"));
          endTimes.add(jObj.getString("event_time_to"));
          totalTimes.add(jObj.getString("event_total_time"));
          venues.add(jObj.getString("event_venue"));
          participants.add(jObj.getString("event_no_of_participant"));
          groups.add(jObj.getString("event_group_concern"));
          resources.add(jObj.getString("event_resource_person"));
          targets.add(jObj.getString("event_target_people"));
          statuses.add(jObj.getString("event_status"));
          i += 1;
        }
      }

      tempDays = cDays;
      tempTitles = titles;
      tempDetails = details;
      tempTrainingNos = trainingNos;
      tempStartDates = startDats;
      tempEndDates = endDates;
      tempDurations = durations;
      tempStartTimes = startTimes;
      tempEndTimes = endTimes;
      tempTotalTimes = totalTimes;
      tempGroups = groups;
      tempParticipants = participants;
      tempVenues = venues;
      tempResources = resources;
      tempTargets = targets;
      tempStatuses = statuses;

      setAdapter();
    }
    catch (Exception e) {}
  }





  private class MyKincomingInfos extends AsyncTask<String, String, String>
  {
    SweetAlertDialog mDialog;


    protected String doInBackground(String... paramVarArgs)
    {
      try
      {
        ConnectionHelper helper = new ConnectionHelper();
        helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
        helper.addData(new Uri.Builder().appendQueryParameter("action", "get_kincoming")
                .appendQueryParameter("blog_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_web_id)))
                .appendQueryParameter("month", months[monthSerial])
                .appendQueryParameter("year", "" + yearSerial));
        return helper.getResponse();
      }
      catch (Exception e)
      {
        return null;
      }

    }

    protected void onPostExecute(String response)
    {
      try
      {
        if(response==null)
        {
          Log.e("response kincoming", "Response null");
          tempDays=cDays;
          tempTitles=titles;
          tempDetails=details;
          tempTrainingNos=trainingNos;
          tempStartDates=startDats;
          tempEndDates=endDates;
          tempDurations=durations;
          tempStartTimes=startTimes;
          tempEndTimes=endTimes;
          tempTotalTimes=totalTimes;
          tempGroups=groups;
          tempParticipants=participants;
          tempVenues=venues;
          tempResources=resources;
          tempTargets=targets;
          tempStatuses=statuses;
          if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
          setAdapter();
        }
        else
        {
          JSONObject json = new JSONObject(response);
          if (json.getInt("no_of_event")> 0)
          {
            JSONArray jEvents = json.getJSONArray("all_events");
            int i = 0;
            while (i < jEvents.length())
            {
              JSONObject localJSONObject = jEvents.getJSONObject(i);
              KincomingFragment.this.cDays.add(localJSONObject.getString("event_date"));
              KincomingFragment.this.titles.add(localJSONObject.getString("title"));
              trainingNos.add(localJSONObject.getString("event_training_no"));
              startDats.add(localJSONObject.getString("event_date_from"));
              endDates.add(localJSONObject.getString("event_date_to"));
              durations.add(localJSONObject.getString("event_total_days"));
              startTimes.add(localJSONObject.getString("event_time_from"));
              endTimes.add(localJSONObject.getString("event_time_to"));
              totalTimes.add(localJSONObject.getString("event_total_time"));
              venues.add(localJSONObject.getString("event_venue"));
              participants.add(localJSONObject.getString("event_no_of_participant"));
              groups.add(localJSONObject.getString("event_group_concern"));
              resources.add(localJSONObject.getString("event_resource_person"));
              targets.add(localJSONObject.getString("event_target_people"));
              statuses.add(localJSONObject.getString("event_status"));
              i += 1;
            }
          }

          tempDays=cDays;
          tempTitles=titles;
          tempDetails=details;
          tempTrainingNos=trainingNos;
          tempStartDates=startDats;
          tempEndDates=endDates;
          tempDurations=durations;
          tempStartTimes=startTimes;
          tempEndTimes=endTimes;
          tempTotalTimes=totalTimes;
          tempGroups=groups;
          tempParticipants=participants;
          tempVenues=venues;
          tempResources=resources;
          tempTargets=targets;
          tempStatuses=statuses;

          if(getMonthDifference(monthSerial+1,yearSerial)<=1)
          {
            String path_kincoming=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kincoming)+"_"+months[monthSerial]+"_"+yearSerial;
            File file = new File(path_kincoming);
            if(!file.exists())
            {
              file.createNewFile();
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
                  if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
                  setAdapter();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            }
            else
            {
              if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
              setAdapter();
            }
          }
          else
          {
            if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
            setAdapter();
          }

        }
      }
      catch (Exception e)
      {
        if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
        setAdapter();
      }
    }

    protected void onPreExecute()
    {

      titles = new ArrayList();
      details = new ArrayList();
      cDays = new ArrayList();
      trainingNos = new ArrayList();
      startDats = new ArrayList();
      endDates = new ArrayList();
      durations = new ArrayList();
      startTimes = new ArrayList();
      endTimes = new ArrayList();
      totalTimes = new ArrayList();
      venues = new ArrayList();
      participants = new ArrayList();
      groups = new ArrayList();
      resources = new ArrayList();
      targets = new ArrayList();
      statuses = new ArrayList();

      this.mDialog = new SweetAlertDialog(KincomingFragment.this.getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(KincomingFragment.this.getActivity().getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }
  }


}

