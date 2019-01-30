package com.example.android.chatapp2.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.chatapp2.Fragment.ChatFragment;
import com.example.android.chatapp2.Fragment.UserFragment;

public class SectionPageAdapter extends FragmentPagerAdapter {

private String tabTitle[]=new String[]{"Chats","Users"};
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                return new ChatFragment();

            case 1:
                return new UserFragment();

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
