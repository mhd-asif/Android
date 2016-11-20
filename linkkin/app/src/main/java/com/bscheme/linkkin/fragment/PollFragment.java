package com.bscheme.linkkin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bscheme.linkkin.R;
import com.bscheme.linkkin.adapter.PolicyAdapter;
import com.bscheme.linkkin.utils.DividerItemDecoration;

import java.util.ArrayList;

public class PollFragment
  extends Fragment
{
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  LayoutManager layoutManager;
  private String mParam1;
  private String mParam2;
  RecyclerView mRecyclerView;
  PolicyAdapter policyAdapter;
  ArrayList<String> pollDates = new ArrayList();
  ArrayList<String> pollDetails = new ArrayList();
  
  public static PollFragment newInstance(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2)
  {
    PollFragment localPollFragment = new PollFragment();
    Bundle localBundle = new Bundle();
    localBundle.putSerializable("pollDates", paramArrayList1);
    localBundle.putSerializable("pollDetails", paramArrayList2);
    localPollFragment.setArguments(localBundle);
    return localPollFragment;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getArguments() != null)
    {
      paramBundle = getArguments();
      this.pollDates = ((ArrayList)paramBundle.getSerializable("pollDates"));
      this.pollDetails = ((ArrayList)paramBundle.getSerializable("pollDetails"));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.fragment_poll, paramViewGroup, false);
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.mRecyclerView = ((RecyclerView)paramView.findViewById(R.id.rcl_poll));
    this.layoutManager = new LinearLayoutManager(getActivity());
    this.mRecyclerView.setLayoutManager(this.layoutManager);
    DividerItemDecoration divider = new DividerItemDecoration(getActivity(), 1);
    mRecyclerView.addItemDecoration(divider);
   // this.policyAdapter = new PolicyAdapter(getActivity(), this.pollDates, this.pollDetails);
   // this.mRecyclerView.setAdapter(this.policyAdapter);
  }
}

