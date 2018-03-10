package com.asif.linkkin.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asif.linkkin.fragment.OfficialInformationFragment;
import com.asif.linkkin.fragment.PersonalInformationFragment;

/**
 * Created by kanchan on 11/24/2015.
 */
public class ProfileAdapter extends FragmentPagerAdapter {

    String[] tabs={"Personal","Work"};
    Bundle bundleFull,bundleOffice;

    public ProfileAdapter(FragmentManager fm, Bundle bundleFull,Bundle bundleOffice) {
        super(fm);
        this.bundleFull=bundleFull;
        this.bundleOffice=bundleOffice;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {
            return PersonalInformationFragment.newInstance(bundleFull);
        }
        else
        {
            return OfficialInformationFragment.newInstance(bundleOffice);
        }
    }

    @Override
    public int getCount() {
        return tabs.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
