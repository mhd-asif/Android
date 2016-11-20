/**
 * Copyright (C) 2015 ogaclejapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ogaclejapan.smarttablayout.utils.v4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentPagerItemAdapter extends FragmentPagerAdapter {

  private final FragmentPagerItems pages;
  private final SparseArrayCompat<WeakReference<Fragment>> holder;


  ArrayList<String> policyDetails=new ArrayList<String>();
  ArrayList<String>policyDates=new ArrayList<String>();
  ArrayList<String>pollDetails=new ArrayList<String>();
  ArrayList<String>pollDates=new ArrayList<String>();
  ArrayList<String>alertDetails=new ArrayList<String>();
  ArrayList<String>alertDates=new ArrayList<String>();
  boolean fromInfo;


  public FragmentPagerItemAdapter(FragmentManager fm,FragmentPagerItems pages,boolean fromInfo,ArrayList<String> policyDetails,ArrayList<String>policyDates,ArrayList<String>pollDetails,ArrayList<String>pollDates,ArrayList<String>alertDetails,ArrayList<String>alertDates) {
    super(fm);
    this.pages = pages;
    this.holder = new SparseArrayCompat<>(pages.size());
    this.fromInfo=fromInfo;
    this.policyDates=policyDates;
    this.policyDetails=policyDetails;
    this.pollDates=pollDates;
    this.pollDetails=pollDetails;
    this.alertDates=alertDates;
    this.alertDetails=alertDetails;
  }

  public FragmentPagerItemAdapter(FragmentManager fm, FragmentPagerItems pages, boolean fromInfo) {
    super(fm);
    this.pages = pages;
    this.holder = new SparseArrayCompat<>(pages.size());
    this.fromInfo=fromInfo;
  }

  @Override
  public int getCount() {
    return pages.size();
  }

  @Override
  public Fragment getItem(int position) {
    if(fromInfo)
    {
      Bundle bundle=new Bundle();
      if(position==0)
      {
        bundle.putSerializable("policyDates",policyDates);
        bundle.putSerializable("policyDetails",policyDetails);
      }
      else if(position==1)
      {
        bundle.putSerializable("pollDates",pollDates);
        bundle.putSerializable("pollDetails",pollDetails);
      }
      else if(position==2)
      {
        bundle.putSerializable("alertDates",alertDates);
        bundle.putSerializable("alertDetails",alertDetails);
      }
      Fragment myFrag= getPagerItem(position).instantiate(pages.getContext(), position);
      myFrag.setArguments(bundle);
      return myFrag;
    }
    else return getPagerItem(position).instantiate(pages.getContext(), position);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Object item = super.instantiateItem(container, position);
    if (item instanceof Fragment) {
      holder.put(position, new WeakReference<Fragment>((Fragment) item));
    }
    return item;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    holder.remove(position);
    super.destroyItem(container, position, object);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return getPagerItem(position).getTitle();
  }

  @Override
  public float getPageWidth(int position) {
    return super.getPageWidth(position);
  }

  public Fragment getPage(int position) {
    final WeakReference<Fragment> weakRefItem = holder.get(position);
    return (weakRefItem != null) ? weakRefItem.get() : null;
  }

  protected FragmentPagerItem getPagerItem(int position) {
    return pages.get(position);
  }

}
