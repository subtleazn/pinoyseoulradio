package com.pinoyseoul.radio.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pinoyseoul.radio.databinding.FragmentRadioBinding;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.pinoyseoul.radio.MainActivity;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.dialog.SearchPanel;
import com.pinoyseoul.radio.radiosHelper.PlaybackStatus;
import com.pinoyseoul.radio.radiosHelper.RadioService;
import com.pinoyseoul.radio.radiosHelper.Recorder;
import com.pinoyseoul.radio.settings.MessageCover;
import com.pinoyseoul.radio.settings.MessageEvent;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.settings.MessageTitle;
import com.pinoyseoul.radio.utils.Md5Class;
import com.pinoyseoul.radio.utils.ShareHelper;

public class RadioFragment extends Fragment implements Configs {
    private FragmentRadioBinding binding;
    String LOG_TAG = "Log";

    String urlRadio;
    private LinearLayoutManager layoutManager;
    Boolean iDle;
    Bitmap artImage;
    Recorder recorder;
    String song_name;
    Boolean ifVolumeOpen;
    private AudioManager audioManager = null;
    Boolean isSmall;
    Boolean isChanged;
    Md5Class md5Class;
    String deviceId;
    Intent radioServiseIntent;


    TextClicked mCallback;



    public RadioFragment() {
    }

    public interface TextClicked {
        public void sendText(String text);
    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TextClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRadioBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        binding.radioname.setText(Configs.APPNAME);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        ifVolumeOpen = false;
        isSmall = false;
        binding.txtQuality.setVisibility(View.GONE);

        isChanged = sharedPreferences.getBoolean("changed" , false);


        if (SINGLE) {

            binding.txtQuality.setVisibility(View.GONE);
            urlRadio = R_320;
        } else {

            int quality = sharedPreferences.getInt(SETTINGS_STREAM_QUALITY, 0);

            switch (quality) {

                case 0:
                    urlRadio = R_320;
                    binding.txtQuality.setText(R.string.a_kbs);

                    break;
                case 1:
                    urlRadio = R_192;
                    binding.txtQuality.setText(R.string.b_kbs);
                    break;
                case 2:
                    urlRadio = R_64;
                    binding.txtQuality.setText(R.string.c_kbs);
                    break;
                default:
                    break;
            }
        }

        iDle = false;
        binding.recordLabel.setVisibility(View.GONE);

        binding.recordLabel.setSelected(true);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setReverseLayout(false);





        binding.playTrigger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        radioServiseIntent = new Intent(requireActivity(), RadioService.class);

                        if (RadioService.getInstance() != null) {

                            if(RadioService.getInstance().isPlaying()){

                                Intent stop = new Intent(requireActivity(), RadioService.class);
                                stop.setAction(RadioService.ACTION_STOP);
                                requireActivity().startService(stop);
                            } else {
                                String playerCurrentRadio = RadioService.getInstance().getPlayingRadioStation();
                                if (playerCurrentRadio != null) {

                                    RadioService.getInstance().inits(requireActivity(), urlRadio);
                                    radioServiseIntent.setAction(RadioService.ACTION_PLAY);

                                } else {
                                    RadioService.getInstance().inits(requireActivity(), urlRadio);
                                    radioServiseIntent.setAction(RadioService.ACTION_PLAY);
                                }
                            }

                        } else {
                            RadioService.createInstance().inits(requireActivity(), urlRadio);
                            radioServiseIntent.setAction(RadioService.ACTION_PLAY);
                        }
                        requireActivity().startService(radioServiseIntent);


                    }
                });

       if(AUTOSTART){
            if (RadioService.getInstance() != null) {
                if(RadioService.getInstance().isPlaying()){
                  //  RadioService.getInstance().inits(requireActivity(), urlRadio);
                  //  requireActivity().startService(radioServiseIntent);
                }
            } else {

                radioServiseIntent = new Intent(requireActivity(), RadioService.class);
                RadioService.createInstance().inits(requireActivity(), urlRadio);
                radioServiseIntent.setAction(RadioService.ACTION_PLAY);
                requireActivity().startService(radioServiseIntent);
            }

        }

        binding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recorder != null) {

                    if (recorder.isRecording()) {
                        recorder.stopRecording();
                        binding.btnRecord.setBackground(getResources().getDrawable(R.drawable.rec_w));
                        recorder = null;
                        binding.btnRecord.clearAnimation();
                        binding.recordLabel.setVisibility(View.GONE);

                    }

                } else {

                    if (song_name == null) {

                    } else {

                        String tlt = song_name.replace(" ", "_");
                        String tld = tlt.replace("/", "_");
                        String currentTime = new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());
                        String rnd_title = currentTime + "-XYZ-" + tld + ".mp3";

                        recorder = new Recorder(requireActivity(), urlRadio, rnd_title);
                        recorder.record();
                        binding.btnRecord.setBackground(getResources().getDrawable(R.drawable.rec_r));
                        binding.recordLabel.setVisibility(View.VISIBLE);

                    }

                }

            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (song_name.matches("") || song_name == null) {

                } else {

                    Bundle args = new Bundle();
                    args.putString("song", song_name);
                    SearchPanel searchPanel = SearchPanel.newInstance();
                    searchPanel.setArguments(args);
                    searchPanel.show(requireActivity().getSupportFragmentManager(), "");

                }

            }
        });

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String shareText = "I'm listen Simple radio: " +song_name + " "
                        + getString(R.string.app_name) + " developed by  QODO :"
                        + "https://play.google.com/store/apps/details?id=" + requireActivity().getPackageName() + "";

                ShareHelper shareHelper = new ShareHelper();
                shareHelper.shareIt(artImage,shareText,requireContext());



            }
        });

        binding.buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ifVolumeOpen) {
                    ifVolumeOpen = false;

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.volumeLayout, "translationX", 0);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();

                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(binding.textCapsule, "translationX", 0);
                    objectAnimator1.setDuration(500);
                    objectAnimator1.start();

                } else {
                    ifVolumeOpen = true;

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.volumeLayout, "translationX", -200);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();

                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(binding.textCapsule, "translationX", -200);
                    objectAnimator1.setDuration(500);
                    objectAnimator1.start();

                }

            }
        });

        requireActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);
        binding.seekVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.seekVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        binding.seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ifVolumeOpen = false;

                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.volumeLayout, "translationX", 0);
                objectAnimator.setDuration(500);
                objectAnimator.start();

                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(binding.textCapsule, "translationX", 0);
                objectAnimator1.setDuration(500);
                objectAnimator1.start();
            }
        });



        md5Class = new Md5Class();

        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceId = md5Class.md5(android_id).toUpperCase();





        return rootView;
    }







    @Subscribe
    public void onEvent(MessageTitle event) {
        // makeScreenUpdate(event.message, event.bitmap, event.radioName);

        binding.txtSong.setText(event.message);
        song_name = event.message;

    }

    @Subscribe
    public void onEvent(MessageCover event) {
        // makeScreenUpdate(event.message, event.bitmap, event.radioName);

        binding.image.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
        artImage = event.bitmap;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Glide is removed, so we can't load the image here.
                // We can re-implement this later.
                binding.image.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            }
        }, 50);


    }



    @Subscribe
    public void onEvent(final String status) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (status) {

            case PlaybackStatus.LOADING:
                break;
            case PlaybackStatus.ERROR:

                editor.putBoolean("play", false);

                break;
            case PlaybackStatus.STOPPED:
                editor.putBoolean("play", false);

                break;
            case PlaybackStatus.IDLE:
                iDle = true;
                break;
            case PlaybackStatus.PAUSED:

                editor.putBoolean("play", false);
                break;
            case PlaybackStatus.PLAYING:
                editor.putBoolean("play", true);

                break;

        }
        editor.apply();

        binding.playTrigger.setBackground(status.equals(PlaybackStatus.PLAYING) ? ContextCompat.getDrawable(getActivity(), R.drawable.pause_w) : ContextCompat.getDrawable(getActivity(), R.drawable.play_w));

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);


        if(RadioService.getInstance() != null) {

            binding.txtSong.setText(sharedPreferences.getString(Configs.TITLE_RADIO,""));
            binding.playTrigger.setBackground(getResources().getDrawable(R.drawable.pause_w));

        } else {

            binding.txtSong.setText("TRANSITOR");
            binding.playTrigger.setBackground(getResources().getDrawable(R.drawable.play_w));
        }

        System.out.println("oOOO = = =");
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {

        //   EventBus.getDefault().unregister(this);

        super.onDestroy();

    }

    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Override
    public void onStart() {

        EventBus.getDefault().register(this);


        super.onStart();

    }

    public static Bitmap getBitmapsFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
