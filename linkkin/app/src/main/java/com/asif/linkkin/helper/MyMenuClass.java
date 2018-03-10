package com.asif.linkkin.helper;

import android.support.v4.app.FragmentActivity;

import com.asif.linkkin.R;
import com.asif.linkkin.utils.SharedDataSaveLoad;

import java.util.ArrayList;

public class MyMenuClass
{
  public static FragmentActivity activity;
  public static String employeeId;
  public static String employeeName;

  public static String menuType;

  static ArrayList<String> allCategories = new ArrayList();
  static ArrayList<String> allLocations = new ArrayList();
  
  public MyMenuClass(FragmentActivity paramFragmentActivity)
  {
    activity = paramFragmentActivity;
  }
  
  public MyMenuClass(String paramString1, String paramString2, FragmentActivity paramFragmentActivity, ArrayList<String> allCategories, ArrayList<String> allLocations)
  {
    employeeId = paramString1;
    employeeName = paramString2;
    activity = paramFragmentActivity;
    this.allCategories=allCategories;
    this.allLocations=allLocations;
  }


  
  public static void setEmployee()
  {
    employeeName = SharedDataSaveLoad.load(activity, activity.getResources().getString(R.string.shared_pref_employee_name));
    employeeId = SharedDataSaveLoad.load(activity, activity.getResources().getString(R.string.shared_pref_employee_id));
  }
}

