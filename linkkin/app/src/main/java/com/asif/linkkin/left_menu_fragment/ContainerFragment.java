package com.asif.linkkin.left_menu_fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.InteractPostActivity;
import com.asif.linkkin.R;
import com.asif.linkkin.adapter.InteractAdapter;
import com.asif.linkkin.db.Constants;
import com.asif.linkkin.db.DbHelper;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.helper.JsonParser;
import com.asif.linkkin.utils.MyUtils;
import com.asif.linkkin.helper.NotificationCounter;
import com.asif.linkkin.utils.SharedDataSaveLoad;

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

;

public class ContainerFragment extends Fragment
{
  String employeeId;
  String employeeName;

  InteractAdapter adapter;
  HashMap<String, ArrayList<String>> allCommentDates = new HashMap();
  HashMap<String, ArrayList<String>> allCommentDescs = new HashMap();
  HashMap<String, ArrayList<String>> allCommentors = new HashMap();
  ArrayList<String> categories = new ArrayList();
  ArrayList<String> commentDates = new ArrayList();
  ArrayList<String> commentDescs = new ArrayList();
  ArrayList<String> commentPostIds = new ArrayList();
  ArrayList<String> commentors = new ArrayList();
  ArrayList<String> dates = new ArrayList();
  ArrayList<String> shortdDescs = new ArrayList();
  ArrayList<String> descriptions = new ArrayList();
  ArrayList<String> noComments = new ArrayList();
  ArrayList<String> ids = new ArrayList();
  //ArrayList<String> imgLinks = new ArrayList();

  ArrayList<String> locations = new ArrayList();
  ArrayList<String> titles = new ArrayList();

  HashMap<Integer,ArrayList<String>>allImages=new HashMap<>();
  //HashMap<Integer,ArrayList<String>>allThumbs=new HashMap<>();

  RecyclerView recyclerList;
  FloatingActionButton fab;
  ProgressBar progress;
  Bundle savedBundle;

  private static final int HIDE_THRESHOLD = 20;
  private int scrolledDistance = 0;
  private boolean controlsVisible = true;

  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MyUtils.isCallable=false;
  }

  
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle args)
  {
    return inflater.inflate(R.layout.fragment_container, viewGroup, false);
  }


  public void onViewCreated(View view, Bundle bundle)
  {
    super.onViewCreated(view, bundle);
    recyclerList=(RecyclerView)view.findViewById(R.id.recyclerContainer);
    fab=(FloatingActionButton)view.findViewById(R.id.fab);
    progress=(ProgressBar)view.findViewById(R.id.progress);
    progress.setVisibility(View.GONE);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerList.setLayoutManager(layoutManager);

    saveLocalDate();
    int count= NotificationCounter.getNotificationCount();
    if(count<1) NotificationCounter.setNotificationText("");
    else NotificationCounter.setNotificationText(""+count);



    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CheckConnectivity connectivity=new CheckConnectivity(getActivity());
        if(connectivity.isConnected()) startActivity(new Intent(getActivity(), InteractPostActivity.class));
        else Toast.makeText(getActivity(),"Internet connection required",Toast.LENGTH_SHORT).show();
      }
    });



    recyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
          onHide();
          controlsVisible = false;
          scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
          onShow();
          controlsVisible = true;
          scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
          scrolledDistance += dy;
        }

      }
    });


    CheckConnectivity connectivity=new CheckConnectivity(getActivity());
    if(connectivity.isConnected()) new getAllPosts().execute();
    else
    {
      getOfflineRes();
    }


  }


  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    savedBundle=paramBundle;
  }


  @Override
  public void onPause() {
    super.onPause();
    MyUtils.isResumed=true;
  }


  @Override
  public void onResume() {
    super.onResume();
    MyUtils.isResumed=true;
    if(MyUtils.isCallable)
    {
      setBlankAdapter();
      new UploadImageTask().execute();
    }
    MyUtils.isCallable=false;
  }

  private void setListAdapter()
  {
    adapter = new InteractAdapter(getActivity(), this.employeeId, this.employeeName, this.ids, this.titles, this.dates, shortdDescs, this.descriptions, noComments, this.categories, this.locations, allImages);
    recyclerList.setAdapter(adapter);
  }



  private void setBlankAdapter()
  {
    ids = new ArrayList();
    titles = new ArrayList();
    dates = new ArrayList();
    locations = new ArrayList();
    shortdDescs = new ArrayList();
    descriptions = new ArrayList();
    noComments = new ArrayList();
    categories = new ArrayList();
    allImages = new HashMap<>();
    adapter = new InteractAdapter(getActivity(), this.employeeId, this.employeeName, this.ids, this.titles, this.dates, shortdDescs, this.descriptions, noComments, this.categories, this.locations, allImages);
    recyclerList.setAdapter(adapter);

  }




  private void onHide()
  {
    fab.setVisibility(View.INVISIBLE);
  }


  private void onShow()
  {
    fab.setVisibility(View.VISIBLE);
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
      String query="DELETE FROM seen_date WHERE type LIKE 'interact';";
      db.execSQL(query);
      query="DELETE FROM interact_comment;";
      db.execSQL(query);

      LinkedHashMap<String, Integer> column2=new LinkedHashMap<String, Integer>();
      column2.put(Constants.SEEN_DATE_ID,Constants.IS_NUMBER);
      column2.put(Constants.SEEN_DATE_DATE, Constants.IS_STRING);
      column2.put(Constants.SEEN_DATE_TYPE,Constants.IS_STRING);
      // column2.put(Constants.SEEN_DATE_CATEGORY, Constants.IS_STRING);
      HashMap< Integer,ArrayList<String>> value2=new HashMap<Integer, ArrayList<String>>();
      ArrayList<String>data2= new ArrayList<String>();
      data2.add(String.valueOf(3));
      data2.add(curYear+"-"+formatedText(curMonth+1)+"-"+formatedText(curDay)+" "+formatedText(curHour)+":"+formatedText(curMinute)+":"+formatedText(curSecond));
      data2.add("interact");
      value2.put(0,data2);
      boolean response2=helper.insertRowByColumn(Constants.TABLE_SEEN_DATE, column2, value2);
      db.close();
    }
    catch (Exception e) {
      Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
    }
  }


  private String formatedText(int s)
  {
    if(s<10) return "0"+s;
    else return ""+s;
  }




  private void getOfflineRes()
  {
    String path_kinteract=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinteract);
    File file = new File(path_kinteract);
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
    ids = new ArrayList();
    titles = new ArrayList();
    dates = new ArrayList();
    locations = new ArrayList();
    shortdDescs = new ArrayList();
    descriptions = new ArrayList();
    noComments = new ArrayList();
    categories = new ArrayList();
    allImages = new HashMap<>();
    try
    {
      JSONObject paramString = new JSONObject(response);
      int noKinteract= paramString.getInt("no_of_kinteract");
      if(noKinteract>0)
      {
        JSONArray localObject = paramString.getJSONArray("all_kinteract");
        int i = 0;
        while (i < localObject.length())
        {
          JSONObject localJSONObject = localObject.getJSONObject(i);
          ids.add(String.valueOf(localJSONObject.getInt("id")));
          titles.add(localJSONObject.getString("title"));
          dates.add(localJSONObject.getString("date"));
          shortdDescs.add(localJSONObject.getString("plain_description"));
          descriptions.add(localJSONObject.getString("description"));
          categories.add(localJSONObject.getString("cat_id"));
          locations.add(localJSONObject.getString("location_id"));
          JSONObject jComment=localJSONObject.getJSONObject("no_of_comment");
          noComments.add(String.valueOf(jComment.getInt("total_comments")));
          JSONArray jThumbs=localJSONObject.getJSONArray("image");
          ArrayList<String>tempThumbs=new ArrayList<>();
          for(int j=0;j<jThumbs.length();j++)
          {
            tempThumbs.add(jThumbs.getString(j));
          }
          allImages.put(i,tempThumbs);
          i += 1;
        }
      }
      setListAdapter();
    }
    catch (Exception e) {}
  }




  private class UploadImageTask extends AsyncTask<Void, Integer, String>
  {

    final StringBuilder response = new StringBuilder();

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress.setVisibility(View.VISIBLE);
      progress.setIndeterminate(true);
      progress.setProgress(0);
    }

    @Override
    protected String doInBackground(Void... params) {

      JsonParser jsonParser = new JsonParser();
      jsonParser.establishConnection("http://linkkin.co/sqgc/ajax-request/", "POST");
      try {
        jsonParser.writeParamData("action", "kinteract_image_upload");
        jsonParser.writeParamData("user_id", employeeId);
        jsonParser.writeParamData("post_id", MyUtils.postId);

        jsonParser.addFilePart("1", MyUtils.fileName, MyUtils.imagePath, "");

        response.append(jsonParser.getResponse());
        int period=(int)jsonParser.getContentLength();
        jsonParser.finishConnection();
        for(int i=0;i<period;i++)
        {
          publishProgress(i);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      return response.toString();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      if(MyUtils.isResumed) progress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String msg) {
      if(MyUtils.isResumed)
      {
        progress.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(msg)) // check response is null or not

          try
        {
          JSONObject jObj=new JSONObject(msg);
          int success=jObj.getInt("success");
          if(success==1)
          {
            new getAllPosts().execute();
          }
          else
          {
            Toast.makeText(getActivity(), "Image uploading failed", Toast.LENGTH_SHORT).show();
            new getAllPosts().execute();
          }
        }
        catch (Exception e)
        {
          Toast.makeText(getActivity(), "Image uploading failed", Toast.LENGTH_SHORT).show();
          new getAllPosts().execute();
        }
      }

    }


  }




  private class getAllPosts extends AsyncTask<String, Void, String>
  {
    SweetAlertDialog mDialog;

    protected String doInBackground(String... paramVarArgs)
    {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder()
              .appendQueryParameter("action", "get_kinteract")
              .appendQueryParameter("user_id", SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_employee_id))));
      return helper.getResponse();
    }

    protected void onPostExecute(String response)
    {
      super.onPostExecute(response);
      try
      {
        JSONObject paramString = new JSONObject(response);
        int noKinteract= paramString.getInt("no_of_kinteract");
        if(noKinteract>0)
        {
          JSONArray localObject = paramString.getJSONArray("all_kinteract");
          int i = 0;
          while (i < localObject.length())
          {
            JSONObject localJSONObject = localObject.getJSONObject(i);
            ids.add(String.valueOf(localJSONObject.getInt("id")));
            titles.add(localJSONObject.getString("title"));
            dates.add(localJSONObject.getString("date"));
            shortdDescs.add(localJSONObject.getString("plain_description"));
            descriptions.add(localJSONObject.getString("description"));
            categories.add(localJSONObject.getString("cat_id"));
            locations.add(localJSONObject.getString("location_id"));
            JSONObject jComment=localJSONObject.getJSONObject("no_of_comment");
            noComments.add(String.valueOf(jComment.getInt("total_comments")));
            JSONArray jThumbs=localJSONObject.getJSONArray("image");
            ArrayList<String>tempThumbs=new ArrayList<>();
            for(int j=0;j<jThumbs.length();j++)
            {
              tempThumbs.add(jThumbs.getString(j));
            }
            allImages.put(i,tempThumbs);
            i += 1;
          }
        }

        String path_kinteract=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinteract);
        File file = new File(path_kinteract);
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
              setListAdapter();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        else
        {
          if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
          setListAdapter();
        }

      }
      catch (Exception e)
      {
        if(mDialog!=null&&mDialog.isShowing()) mDialog.dismiss();
        Toast.makeText(getActivity(),""+e,Toast.LENGTH_SHORT).show();
      }
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      ids = new ArrayList();
      titles = new ArrayList();
      dates = new ArrayList();
      locations = new ArrayList();
      shortdDescs = new ArrayList();
      descriptions = new ArrayList();
      noComments = new ArrayList();
      categories = new ArrayList();
      allImages = new HashMap<>();

      ContainerFragment.this.employeeId = SharedDataSaveLoad.load(ContainerFragment.this.getActivity(), ContainerFragment.this.getResources().getString(R.string.shared_pref_employee_id));
      ContainerFragment.this.employeeName = SharedDataSaveLoad.load(ContainerFragment.this.getActivity(), ContainerFragment.this.getResources().getString(R.string.shared_pref_employee_name));

      this.mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }
  }




}

