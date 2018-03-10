package com.asif.linkkin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asif.linkkin.fragment.MoviesFragment;

import java.util.ArrayList;

public class TabsPagerAdapter extends FragmentPagerAdapter
{

  ArrayList<String> allTypes = new ArrayList();
  ArrayList<String> typeIds = new ArrayList();
  ArrayList<String> typeIcons = new ArrayList();


  public TabsPagerAdapter(FragmentManager paramFragmentManager, ArrayList<String> allTypes, ArrayList<String> typeIds, ArrayList<String> typeIcons)
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
  
  public Fragment getItem(int paramInt)
  {

    return MoviesFragment.newInstance(allTypes.get(paramInt), typeIds.get(paramInt));
  }
  
  public CharSequence getPageTitle(int paramInt)
  {
    return allTypes.get(paramInt);
  }


}

