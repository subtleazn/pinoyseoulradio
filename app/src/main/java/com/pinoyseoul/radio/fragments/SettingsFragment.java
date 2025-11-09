package com.pinoyseoul.radio.fragments;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.databinding.FragmentAboutBinding;
import com.pinoyseoul.radio.dialog.SetTimD;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.utils.Cms;
import com.pinoyseoul.radio.utils.STimersvc;
import com.pinoyseoul.radio.utils.Timerst;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements Configs {
    private FragmentAboutBinding binding;
    Timerst timerst;
    Cms cms;
    Handler handler = new Handler();

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        if (SINGLE) {
            binding.streamBox.setVisibility(View.GONE);
            binding.streamHead.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ((SeekBar) binding.rangeSeekbar).setProgress(sharedPreferences.getInt(Configs.SETTINGS_BLUR_RADIUS, 0));

        ((SeekBar) binding.rangeSeekbar).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt(Configs.SETTINGS_BLUR_RADIUS, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        binding.buttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] TO = {"tronix@icloud.com"};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                    Log.i("Finished ", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(),
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        timerst = new Timerst(getActivity());
        timerst.checkSleepTime();
        cms = new Cms();

        if (timerst.getIsSleepTimeOn()) {

            binding.buttonTimer.setText(getString(R.string.stop_timer));

        } else {

            binding.buttonTimer.setText(getString(R.string.start_timer));
        }

        binding.buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerst.getIsSleepTimeOn()) {

                    Intent i = new Intent(getActivity(), STimersvc.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), timerst.getSleepID(), i, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    pendingIntent.cancel();
                    alarmManager.cancel(pendingIntent);
                    timerst.sleepTime(false, 0, 0);
                    binding.timerText.setText(getString(R.string.timer));

                    binding.buttonTimer.setText(getString(R.string.start_timer));

                } else {
                    setTimeDialog();
                }
            }
        });

        binding.timerText.setText(getString(R.string.timer));
        updatez(binding.timerText, timerst.getSleepTime());

        return rootView;
    }

    private void setTimeDialog() {

        final SetTimD td = new SetTimD(getActivity());
        td.show();
        td.findViewById(R.id.btn_ook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                int min = sharedPreferences.getInt(COWNTDOWN, 0);
                String hours = String.valueOf(min / 60);
                String minute = String.valueOf(min % 60);

                if (hours.length() == 1) {
                    hours = "0" + hours;
                }

                if (minute.length() == 1) {
                    minute = "0" + minute;
                }

                String totalTime = hours + ":" + minute;
                long total_timer = cms.cToMs(totalTime) + System.currentTimeMillis();

                Random random = new Random();
                int id = random.nextInt(100);

                timerst.sleepTime(true, total_timer, id);

                Intent intent = new Intent(getActivity(), STimersvc.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, total_timer, pendingIntent);

                updatez(binding.timerText, timerst.getSleepTime());

                td.dismiss();

                binding.buttonTimer.setText(getString(R.string.stop_timer));

            }
        });
    }

    private void updatez(final android.widget.TextView textView, long time) {
        long timeleft = time - System.currentTimeMillis();
        if (timeleft > 0) {
            @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeleft),
                    TimeUnit.MILLISECONDS.toMinutes(timeleft) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(timeleft) % TimeUnit.MINUTES.toSeconds(1));

            udpt(hms);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (timerst.getIsSleepTimeOn()) {
                        updatez(textView, timerst.getSleepTime());
                    }
                }
            }, 1000);
        }
    }

    private void udpt(String t) {

        binding.timerText.setText(t);
        binding.timerText.invalidate();
        binding.timerText.requestLayout();
    }

}
