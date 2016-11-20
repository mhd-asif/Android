package com.bscheme.linkkin.fragment;

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

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.adapter.PolicyAdapter;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.DividerItemDecoration;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PolicyFragment extends Fragment
{
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  LayoutManager layoutManager;
  private String mParam1;
  private String mParam2;
  RecyclerView mRecyclerView;
  PolicyAdapter policyAdapter;

  ArrayList<String> ids = new ArrayList();
  ArrayList<String> titles = new ArrayList();
  ArrayList<String> shortDescs = new ArrayList();
  ArrayList<String> descs = new ArrayList();

  String type,id;


  public static PolicyFragment newInstance(String type, String id)
  {
    PolicyFragment localPolicyFragment = new PolicyFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("type",type);
    localBundle.putString("id",id);
    localPolicyFragment.setArguments(localBundle);
    return localPolicyFragment;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getArguments() != null)
    {
      paramBundle=getArguments();
      this.type=paramBundle.getString("type");
      this.id=paramBundle.getString("id");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.fragment_policy, paramViewGroup, false);
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.mRecyclerView = ((RecyclerView)paramView.findViewById(R.id.rcl_policy));
    this.layoutManager = new LinearLayoutManager(getActivity());
    this.mRecyclerView.setLayoutManager(this.layoutManager);
    DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
    mRecyclerView.addItemDecoration(divider);

   // setAdapter();
    //new GetPolicyInfo().execute();
    String path_kinfo_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinformation_cat)+"_"+id;
    File file = new File(path_kinfo_details);
    if(file.exists())
    {
      getOfflineRes();
    }
    else
    {
      CheckConnectivity connectivity=new CheckConnectivity(getActivity());
      if(connectivity.isConnected()) new GetPolicyInfo().execute();
     // else Toast.makeText(getActivity(),"Internet connection required",Toast.LENGTH_SHORT).show();
    }
  }


  private void setAdapter()
  {
    //policyAdapter = new PolicyAdapter(getActivity(), this.policyDates, this.policyDetails,this.imgLinks,ids,likePollIds,likeEmployeeIds,type);
    policyAdapter = new PolicyAdapter(getActivity(), ids, titles, shortDescs,descs);
    this.mRecyclerView.setAdapter(this.policyAdapter);
  }



  private void getOfflineRes()
  {
    String path_kinfo_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinformation_cat)+"_"+id;
    File file = new File(path_kinfo_details);
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
    try
    {
      JSONObject json=new JSONObject(response);
      int policyNo=json.getInt("no_of_kinformation");
      JSONArray jPolicies=json.getJSONArray("all_events");
      for(int i=0;i<jPolicies.length();i++)
      {
        JSONObject jObj=jPolicies.getJSONObject(i);
        ids.add(jObj.getString("id"));
        titles.add(jObj.getString("title"));
        shortDescs.add(jObj.getString("short_description"));
        descs.add(jObj.getString("description"));
      }
      setAdapter();
    }
    catch (Exception e) {}

  }



  private class GetPolicyInfo extends AsyncTask<String, String, String>
  {

    SweetAlertDialog mDialog;

    @Override
    protected String doInBackground(String... params) {
      ConnectionHelper helper = new ConnectionHelper();
      helper.createConnection(SharedDataSaveLoad.load(getActivity(), getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
      helper.addData(new Uri.Builder()
              .appendQueryParameter("action", "get_kinformation")
              .appendQueryParameter("cat_id", id));
      return helper.getResponse();
    }


    @Override
    protected void onPreExecute() {
      ids=new ArrayList<>();
      titles=new ArrayList<>();
      shortDescs=new ArrayList<>();
      descs=new ArrayList<>();

      this.mDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.mDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
      this.mDialog.setTitleText("");
      this.mDialog.setCancelable(false);
      this.mDialog.show();
    }


    @Override
    protected void onPostExecute(String response) {
      mDialog.dismiss();
      if (!TextUtils.isEmpty(response)) // check response is null or not

        try {
        JSONObject json=new JSONObject(response);
        int policyNo=json.getInt("no_of_kinformation");
        JSONArray jPolicies=json.getJSONArray("all_events");
        for(int i=0;i<jPolicies.length();i++)
        {
          JSONObject jObj=jPolicies.getJSONObject(i);
          ids.add(jObj.getString("id"));
          titles.add(jObj.getString("title"));
          shortDescs.add(jObj.getString("short_description"));
          descs.add(jObj.getString("description"));
        }

        String path_kinfo_details=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kinformation_cat)+"_"+id;
        File file = new File(path_kinfo_details);
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
       // Toast.makeText(getActivity(),""+e,Toast.LENGTH_SHORT).show();
      }

    }


  }




}


