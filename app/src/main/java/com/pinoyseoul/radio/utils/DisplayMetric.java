package com.pinoyseoul.radio.utils;

import android.app.Activity;
import android.util.DisplayMetrics;


public class DisplayMetric {

    public double screenInch(Activity context){

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        return screenInches;
    }
}
