package com.pinoyseoul.radio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.pinoyseoul.radio.databinding.ActivityStartBinding;
import com.pinoyseoul.radio.settings.Configs;

public class StartActivity extends BaseActivity implements Configs {

    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isOnline()) {
            gotoMain(); // if not demo
            editor.putBoolean("internet", true);
        } else {
            editor.putBoolean("internet", false);
            gotomainWithoutInternet();
        }
        editor.apply();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void gotoMain() {

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                binding.animationView.setVisibility(View.INVISIBLE);

            }
        }, 6000);

    }

    private void gotomainWithoutInternet() {

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                binding.animationView.setVisibility(View.INVISIBLE);

            }
        }, 5000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // This method is called when the  permissions are given

        }

    }

}
