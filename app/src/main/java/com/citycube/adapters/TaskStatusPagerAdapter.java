package com.citycube.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.citycube.fragments.ActiveFragment;
import com.citycube.fragments.CancelFragment;
import com.citycube.fragments.CompletedFragment;

public class TaskStatusPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TaskStatusPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ActiveFragment activeFragment = new ActiveFragment();
                return activeFragment;
            case 1:
                CancelFragment cancelFragment = new CancelFragment();
                return cancelFragment;

            case 2:
                CompletedFragment completeFragment = new CompletedFragment();
                return completeFragment;

            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
