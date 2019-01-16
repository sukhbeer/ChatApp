package com.example.android.chatapp2.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.chatapp2.Fragment.ChatFragment;
import com.example.android.chatapp2.Fragment.RequestFragment;
import com.example.android.chatapp2.Fragment.UserFragment;

public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                RequestFragment requestFragment=new RequestFragment();
                return requestFragment;

            case 1:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;

            case 2:
                UserFragment userFragment=new UserFragment();
                return userFragment;

                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
