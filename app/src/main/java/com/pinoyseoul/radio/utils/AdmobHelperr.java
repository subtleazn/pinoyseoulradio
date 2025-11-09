package com.pinoyseoul.radio.utils;

import static com.pinoyseoul.radio.settings.Configs.ADS_OFF;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

import com.pinoyseoul.radio.R;


public class AdmobHelperr {

    AdRequest adRequest;
    AdView adView;
    Boolean isSubsctibe;




    public void setsAdmob(Activity context, LinearLayout ads_lay) {

        adView = new AdView(context);

        isSubsctibe = false;





        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("749A1BE6B04AEE9E5F40DDE69A6BDCDA","D9A44F8D6CD7C1D4A047D9A5971DD1CF"))
                        .build());


        adRequest = new AdRequest.Builder().build();
        adView.setAdUnitId(context.getString(R.string.banner_id));
        adView.setAdSize(AdSize.BANNER);
        ads_lay.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                System.out.println("ERROR = " + loadAdError.toString());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();


            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


        if (!ADS_OFF) {

            ViewGroup.LayoutParams params = ads_lay.getLayoutParams();
            params.height = 0;
            params.width = 100;
            ads_lay.setLayoutParams(params);

        } else {
            adView.loadAd(adRequest);


        }

    }




}


