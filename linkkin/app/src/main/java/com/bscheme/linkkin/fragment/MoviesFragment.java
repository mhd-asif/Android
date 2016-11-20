package com.bscheme.linkkin.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.adapter.RecyclerAdapter;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MoviesFragment
  extends Fragment
{
  Activity activity;
  RecyclerAdapter adapter;
  Context context;
  LayoutManager layoutManager;
  RecyclerView recycler;

  String type,id;
  ArrayList<String> ids = new ArrayList();
  ArrayList<String> titles = new ArrayList();
  ArrayList<String> shortDescs = new ArrayList();
  ArrayList<String> descs = new ArrayList();
  ArrayList<String> imgLinks = new ArrayList();
  ArrayList<String> times = new ArrayList();
  ArrayList<String> youtubes = new ArrayList();
  ArrayList<String> tvs = new ArrayList();
  HashMap<Integer, ArrayList<String>> allImageLinks = new HashMap();



  public static MoviesFragment newInstance(String type, String id)
  {
    MoviesFragment frag = new MoviesFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("type", type);
    localBundle.putString("id", id);
    frag.setArguments(localBundle);
    return frag;
  }


  
  public void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    if (getArguments() != null)
    {
      bundle=getArguments();
      this.type=bundle.getString("type");
      this.id=bundle.getString("id");
    }
  }
  
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
  {
    return inflater.inflate(R.layout.movies_fragment, container, false);
  }
  
  public void onViewCreated(View view, Bundle bundle)
  {
    super.onViewCreated(view, bundle);
    this.recycler = ((RecyclerView)view.findViewById(R.id.recycler));
    this.context = view.getContext();
    this.activity = getActivity();
    this.layoutManager = new LinearLayoutManager(this.activity);
    this.recycler.setLayoutManager(this.layoutManager);
    String path_kinfo_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kintertainment_cat)+"_"+id;
    File file = new File(path_kinfo_details);
    if(file.exists())
    {
      getOfflineRes();
    }
    else
    {
      CheckConnectivity connectivity=new CheckConnectivity(getActivity());
      if(connectivity.isConnected()) new GetEntertainInfo().execute();
    }
  }
  
  public void setAdapter()
  {
    this.adapter = new RecyclerAdapter(getActivity(),ids, titles, shortDescs, descs, imgLinks, times, youtubes, tvs, allImageLinks);
    this.recycler.setAdapter(this.adapter);
  }




  private void getOfflineRes()
  {
    String path_kintertain_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kintertainment_cat)+"_"+id;
    File file = new File(path_kintertain_details);
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
    ids=new ArrayList<>();
    titles=new ArrayList<>();
    shortDescs=new ArrayList<>();
    descs=new ArrayList<>();
    imgLinks = new ArrayList();
    times = new ArrayList();
    youtubes = new ArrayList();
    tvs = new ArrayList();
    allImageLinks = new HashMap();
    try
    {
      JSONObject json=new JSONObject(response);
      int entertainNo=json.getInt("no_of_kintertainment");
      JSONArray jEntertains=json.getJSONArray("all_kintertainment");
      for(int i=0;i<jEntertains.length();i++)
      {
        JSONObject jObj=jEntertains.getJSONObject(i);
        ids.add(jObj.getString("id"));
        titles.add(jObj.getString("title"));
        shortDescs.add(jObj.getString("short_description"));
        descs.add(jObj.getString("description"));
        imgLinks.add(jObj.getString("image"));
        times.add(jObj.getString("date"));
        youtubes.add(jObj.getString("youtube"));
        tvs.add(jObj.getString("tv"));
        JSONArray jThumbs=jObj.getJSONArray("thumbs");
        ArrayList<String>tempLinks=new ArrayList<>();
        for(int j=0;j<jThumbs.length();j++)
        {
          tempLinks.add(jThumbs.getString(j));
        }
        allImageLinks.put(i, tempLinks);
      }
      setAdapter();
    }
    catch (Exception e) {}

  }




  private class GetEntertainInfo extends AsyncTask<String, String, String>
  {

    SweetAlertDialog mDialog;

    @Override
    protected String doInBackground(String... params) {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder()
              .appendQueryParameter("action", "get_kintertainment")
              .appendQueryParameter("cat_id", id));
      return helper.getResponse();
    }


    @Override
    protected void onPreExecute() {
      ids=new ArrayList<>();
      titles=new ArrayList<>();
      shortDescs=new ArrayList<>();
      descs=new ArrayList<>();
      imgLinks = new ArrayList();
      times = new ArrayList();
      youtubes = new ArrayList();
      tvs = new ArrayList();
      allImageLinks = new HashMap();

      this.mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }


    @Override
    protected void onPostExecute(String response) {
//      mDialog.dismiss();

      if (!TextUtils.isEmpty(response)) // check response is null or not

        try {
        JSONObject json=new JSONObject(response);
        int entertainNo=json.getInt("no_of_kintertainment");
        JSONArray jEntertains=json.getJSONArray("all_kintertainment");
        for(int i=0;i<jEntertains.length();i++)
        {
          JSONObject jObj=jEntertains.getJSONObject(i);
          ids.add(jObj.getString("id"));
          titles.add(jObj.getString("title"));
          shortDescs.add(jObj.getString("short_description"));
          descs.add(jObj.getString("description"));
          imgLinks.add(jObj.getString("image"));
          times.add(jObj.getString("date"));
          youtubes.add(jObj.getString("youtube"));
          tvs.add(jObj.getString("tv"));
          JSONArray jThumbs=jObj.getJSONArray("thumbs");
          ArrayList<String>tempLinks=new ArrayList<>();
          for(int j=0;j<jThumbs.length();j++)
          {
            tempLinks.add(jThumbs.getString(j));
          }
          allImageLinks.put(i,tempLinks);
        }

        String path_kintertain_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kintertainment_cat)+"_"+id;
        File file = new File(path_kintertain_details);
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
        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
      }

    }


  }





}

