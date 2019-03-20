package com.example.muhammet.sohbetuygulamasi.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.muhammet.sohbetuygulamasi.R;

public class ChangeFragment {
    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;
    }

    public void change(Fragment fragment)
    {
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentLayout,fragment,"fragnent")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                .commit();
    }

    public void changeWithParameter(Fragment fragment,String userid)
    {
        Bundle bundle=new Bundle();
        bundle.putString("userid",userid);
        fragment.setArguments(bundle);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentLayout,fragment,"fragnent")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null)
                .commit();
    }
}
