package com.pinoyseoul.radio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pinoyseoul.radio.databinding.ActivityMainBinding;
import com.pinoyseoul.radio.fragments.NoInternetFragment;
import com.pinoyseoul.radio.fragments.RadioFragment;
import com.pinoyseoul.radio.fragments.RecordsFragment;
import com.pinoyseoul.radio.fragments.SettingsFragment;
import com.pinoyseoul.radio.fragments.SocialFragment;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.utils.AdmobHelperr;
import com.pinoyseoul.radio.utils.Md5Class;

public class MainActivity extends BaseActivity implements Configs, RadioFragment.TextClicked {

    private ActivityMainBinding binding;
    FragmentTransaction fTrans;
    RadioFragment radioFragment;
    NoInternetFragment noInternetFragment;
    RecordsFragment recordsFragment;
    SocialFragment socialFragment;
    SettingsFragment settingsFragment;

    String deviceId;
    Md5Class md5Class;
    Boolean isInternet;
    private static final int rID = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO

    };

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

    @Override
    public void sendText(String text){
        // Your text
        System.out.println("TTTT - - - - - - "+ text);
        binding.image.setImageBitmap(StringToBitMap(text));
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdmobHelperr admobHelperr = new AdmobHelperr();
        admobHelperr.setsAdmob(this, binding.ads);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        isInternet = sharedPreferences.getBoolean("internet", false);

        ActivityCompat.requestPermissions(this, PERMISSIONS, rID);

        binding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_radio:
                        goToRadio();
                        vibro();
                        return true;
                    case R.id.nav_record:
                        goToReord();
                        vibro();
                        return true;
                    case R.id.nav_social:
                        goWeb();
                        vibro();
                        return true;
                    case R.id.nav_settings:
                        goSettings();
                        vibro();
                        return true;
                }
                return false;
            }
        });


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setSupportActionBar(binding.toolbar);

        setTitle("");

        md5Class = new Md5Class();


        goToRadio();
    }

    private void goToRadio() {

        if (isInternet) {

            radioFragment = new RadioFragment();
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.content, radioFragment,"radio");
            fTrans.addToBackStack(null);
            fTrans.show(radioFragment);
            fTrans.commit();

        } else {

            noInternetFragment = new NoInternetFragment();
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.content, noInternetFragment);
            fTrans.addToBackStack(null);
            fTrans.show(noInternetFragment);
            fTrans.commit();

        }

    }

    private void goToReord() {

        recordsFragment = new RecordsFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.content, recordsFragment);
        fTrans.addToBackStack(null);
        fTrans.show(recordsFragment);
        fTrans.commit();
    }

    private void goWeb() {

        socialFragment = new SocialFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.content, socialFragment);
        fTrans.addToBackStack(null);
        fTrans.show(socialFragment);
        fTrans.commit();

    }

    private void goSettings() {

        settingsFragment = new SettingsFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.content, settingsFragment);
        fTrans.addToBackStack(null);
        fTrans.show(settingsFragment);
        fTrans.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("radio");
        if (currentFragment instanceof RadioFragment) {
           // currentFragment.onBackPressed();
        } else
            super.onBackPressed();
    }

    private void vibro(){

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(30);
        }
    }

    public void changeBack(int color){
        binding.main.setBackgroundColor(color);
    }
}
