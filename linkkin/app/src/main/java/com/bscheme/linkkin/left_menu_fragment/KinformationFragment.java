package com.bscheme.linkkin.left_menu_fragment;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.adapter.InformationPagerAdapter;
import com.bscheme.linkkin.db.Constants;
import com.bscheme.linkkin.db.DbHelper;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.helper.MyMenuClass;
import com.bscheme.linkkin.helper.NotificationCounter;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

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

public class KinformationFragment
  extends Fragment
{
  Activity activity;
  ArrayList<String> allTypes = new ArrayList();
  ArrayList<String> typeIds = new ArrayList();
  ArrayList<String> typeIcons = new ArrayList();
  int noTypes;
  Context context;
  int curPos;
  InformationPagerAdapter mAdapter;
  private LinearLayout mTabsLinearLayout;
  PagerSlidingTabStrip tabsStrip;
  ViewPager viewPager;

  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
  {
    return inflater.inflate(R.layout.fragment_kinformation, container, false);
  }
  
  public void onSaveInstanceState(Bundle bundle)
  {
    super.onSaveInstanceState(bundle);
    bundle.putInt("curPos", this.curPos);
  }
  
  public void onViewCreated(View view, Bundle bundle)
  {
    super.onViewCreated(view, bundle);
    this.activity = getActivity();
    this.context = view.getContext();
    this.viewPager = ((ViewPager)view.findViewById(R.id.pager));
    this.tabsStrip = ((PagerSlidingTabStrip)view.findViewById(R.id.tabs));

    tabsStrip.setTypeface(Font.LATO_Bold.setFont(getActivity()), 0);

    if (bundle == null)
    {
      this.curPos=0;
    }
    else
    {
      this.curPos = bundle.getInt("curPos");
    }

    CheckConnectivity connectivity=new CheckConnectivity(getActivity());
    if(connectivity.isConnected()) new MyKinformationInfos().execute();
    else
    {
      getOfflineRes();
    }

    this.tabsStrip.setOnPageChangeListener(new OnPageChangeListener() {
      public void onPageScrollStateChanged(int position) {
      }

      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {
      }

      public void onPageSelected(int position) {
        KinformationFragment.this.curPos = position;
        KinformationFragment.this.setUpTabStrip(position);
        updateLocalDb(curPos);
      }
    });

  }
  
  public void setAdapter()
  {
    mAdapter=new InformationPagerAdapter(getChildFragmentManager(),allTypes,typeIds,typeIcons);
    this.viewPager.setAdapter(mAdapter);
    this.tabsStrip.setViewPager(this.viewPager);
    if(MyMenuClass.menuType!=null&&!MyMenuClass.menuType.equals(""))
    {
      for(int i=0;i<allTypes.size();i++)
      {
        if(MyMenuClass.menuType.toLowerCase().equals(allTypes.get(i).toLowerCase()))
        {
          curPos=i;
          MyMenuClass.menuType="";
          break;
        }
      }
    }
    updateLocalDb(curPos);
    setUpTabStrip(this.curPos);
    if(curPos!=0)
    {
      viewPager.setCurrentItem(curPos);
    }
  }

  
  public void setUpTabStrip(int position)
  {
    this.mTabsLinearLayout = ((LinearLayout)this.tabsStrip.getChildAt(0));
    for(int i=0;i<mTabsLinearLayout.getChildCount();i++)
    {
      TextView tempTextView = (TextView)this.mTabsLinearLayout.getChildAt(i);
      if (i == position) {
        tempTextView.setTextColor(Color.parseColor("#FFFFFF"));
      }
      else
      {
        tempTextView.setTextColor(Color.parseColor("#007F80"));
      }
    }
  }




  private void updateLocalDb(int position)
  {
    Calendar current=Calendar.getInstance(Locale.getDefault());
    int curDay=current.get(Calendar.DAY_OF_MONTH);
    int curMonth=current.get(Calendar.MONTH);
    int curYear=current.get(Calendar.YEAR);
    int curHour=current.get(Calendar.HOUR_OF_DAY);
    int curMinute=current.get(Calendar.MINUTE);
    int curSecond=current.get(Calendar.SECOND);

    String str = (allTypes.get(position)).toLowerCase();

    try {
      DbHelper helper=new DbHelper(activity, Constants.DATABASE_NAME,1);
      SQLiteDatabase db=helper.getEditableDB();
      String query="DELETE FROM seen_date WHERE type LIKE 'information';";
      db.execSQL(query);
      query="DELETE FROM information WHERE type LIKE '"+str+"';";
      db.execSQL(query);

      LinkedHashMap<String, Integer> column2=new LinkedHashMap<String, Integer>();
      column2.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
      column2.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
      column2.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
      HashMap< Integer,ArrayList<String>> value2=new HashMap<Integer, ArrayList<String>>();
      ArrayList<String>data2= new ArrayList<String>();
      data2.add(String.valueOf(2));
      data2.add(curYear+"-"+formatedText(curMonth+1)+"-"+formatedText(curDay)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond));
      data2.add("information");
      value2.put(0,data2);
      boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column2, value2);
      db.close();
    }
    catch (Exception e) {
      Toast.makeText(activity,""+e,Toast.LENGTH_SHORT).show();
    }

    int count= NotificationCounter.getNotificationCount();
    if(count<1) NotificationCounter.setNotificationText("");
    else NotificationCounter.setNotificationText(""+count);
  }



  private void getOfflineRes()
  {
    String path_kinfo_cat=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinformation_cat);
    File file = new File(path_kinfo_cat);
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
    allTypes = new ArrayList();
    typeIds = new ArrayList();
    typeIcons = new ArrayList();
    try
    {
      JSONObject json=new JSONObject(response);
      noTypes=json.getInt("no_of_options");
      JSONArray jOptions=json.getJSONArray("kinformation_options");
      for(int i=0;i<jOptions.length();i++)
      {
        JSONObject jOption=jOptions.getJSONObject(i);
        allTypes.add(jOption.getString("title"));
        typeIds.add(jOption.getString("id"));
        typeIcons.add(jOption.getString("icon"));
      }
      setAdapter();
    }
    catch (Exception e) {}
  }


  private String formatedText(int s)
  {
    if(s<10) return "0"+s;
    else return ""+s;
  }


  
  private class MyKinformationInfos extends AsyncTask<String, Integer, String>
  {
    SweetAlertDialog mDialog;
    
    private MyKinformationInfos() {}
    
    protected String doInBackground(String... paramVarArgs)
    {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder().appendQueryParameter("action", "get_kinformation_cat"));
      return helper.getResponse();
    }
    
    protected void onPostExecute(String response)
    {
      mDialog.dismiss();
      if (!TextUtils.isEmpty(response)) // check response is null or not

        try
      {
        JSONObject json=new JSONObject(response);
        noTypes=json.getInt("no_of_options");
        JSONArray jOptions=json.getJSONArray("kinformation_options");
        for(int i=0;i<jOptions.length();i++)
        {
          JSONObject jOption=jOptions.getJSONObject(i);
          allTypes.add(jOption.getString("title"));
          typeIds.add(jOption.getString("id"));
          typeIcons.add(jOption.getString("icon"));
        }
        String path_kinfo_cat=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinformation_cat);
        File file = new File(path_kinfo_cat);
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
      catch (Exception e)
      {
        if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
        Toast.makeText(KinformationFragment.this.getActivity(), "Error in fetching data: "+e, Toast.LENGTH_SHORT).show();
      }
    }
    
    protected void onPreExecute()
    {
      KinformationFragment.this.allTypes = new ArrayList();
      KinformationFragment.this.typeIds = new ArrayList();
      KinformationFragment.this.typeIcons = new ArrayList();
      this.mDialog = new SweetAlertDialog(KinformationFragment.this.getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(KinformationFragment.this.getActivity().getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }


  }




}

