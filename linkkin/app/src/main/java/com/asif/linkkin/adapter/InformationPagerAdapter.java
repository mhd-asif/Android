package com.asif.linkkin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asif.linkkin.fragment.PolicyFragment;

import java.util.ArrayList;

public class InformationPagerAdapter
  extends FragmentPagerAdapter
{

  ArrayList<String> allTypes = new ArrayList();
  ArrayList<String> typeIds = new ArrayList();
  ArrayList<String> typeIcons = new ArrayList();


  public InformationPagerAdapter(FragmentManager paramFragmentManager, ArrayList<String> allTypes, ArrayList<String> typeIds, ArrayList<String> typeIcons)
  {
    super(paramFragmentManager);
    this.allTypes = allTypes;
    this.typeIds=typeIds;
    this.typeIcons=typeIcons;

  }

  
  public int getCount()
  {
    return this.allTypes.size();
  }
  
  public Fragment getItem(int position)
  {
    return PolicyFragment.newInstance(allTypes.get(position),typeIds.get(position));
  }

  
  public CharSequence getPageTitle(int position)
  {
    return allTypes.get(position);
  }


}

