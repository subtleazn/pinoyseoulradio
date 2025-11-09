package com.pinoyseoul.radio.radiosHelper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.session.MediaButtonReceiver;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pinoyseoul.radio.MainActivity;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.player.PlayerManager;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.settings.Constant;
import com.pinoyseoul.radio.settings.MessageCover;
import com.pinoyseoul.radio.settings.MessageTitle;

@SuppressWarnings("deprecation")
public class RadioService extends Service {

    static private final int NOTIFICATION_ID = 1;
    @SuppressLint("StaticFieldLeak")
    static private RadioService service;
    @SuppressLint("StaticFieldLeak")
    static private Context context;
    static NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    static String radio;
    private Boolean isCanceled = false;
    RemoteViews bigViews, smallViews;

    Bitmap bitmap;
    ComponentName componentName;
    AudioManager mAudioManager;
    PowerManager.WakeLock mWakeLock;
    MediaSessionCompat mMediaSession;
    private String status;
    private SimpleExoPlayer exoPlayer;

    public static final String ACTION_STOP = "action.STOP";
    public static final String ACTION_PLAY = "action.PLAY";
    public static final String ACTION_TOGGLE = "action.TOGGLE_PLAYPAUSE";


    public void inits(Context context, String station) {
        RadioService.context = context;
        radio = station;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }



    public static RadioService getInstance() {
        return service;
    }

    public static RadioService createInstance() {
        if (service == null) {
            service = new RadioService();
        }
        return service;
    }

    public Boolean isPlaying() {
        if (service == null) {
            return false;
        } else {
            if (exoPlayer != null) {
                return exoPlayer.getPlayWhenReady();
            } else {
                return false;
            }
        }
    }

    @Override
    public void onCreate() {

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (mAudioManager != null) {
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        componentName = new ComponentName(getPackageName(), MediaReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(componentName);

        LocalBroadcastManager.getInstance(this).registerReceiver(onCallIncome, new IntentFilter("android.intent.action.PHONE_STATE"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onHeadPhoneDetect, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        exoPlayer = PlayerManager.getInstance(context).getExoPlayer();
        exoPlayer.addListener(listener);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        System.out.println("AAA = = " + action);
        if (action != null)
            try {
                switch (action) {
                    case ACTION_STOP:
                        stop(intent);

                        status = PlaybackStatus.STOPPED;
                        EventBus.getDefault().post(status);

                        break;
                    case ACTION_PLAY:
                        play(radio);
                        status = PlaybackStatus.PLAYING;
                        EventBus.getDefault().post(status);
                        break;
                    case ACTION_TOGGLE:
                        togglePlayPause();

                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return START_NOT_STICKY;
    }

    Player.Listener listener = new Player.Listener() {
        @Override
        public void onCues(@NonNull List<Cue> cues) {

        }

        @Override
        public void onMetadata(@NonNull Metadata metadata) {
            getMetadata(metadata);
        }

        @Override
        public void onTimelineChanged(@NonNull Timeline timeline, int reason) {

        }

        @Override
        public void onMediaMetadataChanged(@NonNull MediaMetadata mediaMetadata) {
            //getMediaMetadata(mediaMetadata);
        }

        @Override
        public void onIsLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {



            if (playbackState == Player.STATE_ENDED) {


            } else if (playbackState == Player.STATE_READY) {
                if (!isCanceled) {
//                    ((MainActivity) context).seekBarUpdate();

                    if (mBuilder == null) {
                        createNotification();
                    } else {
                        updateNotification();
                    }
                    changePlayPause(true);
                } else {
                    isCanceled = false;
                    stopExoPlayer();
                }
            }
        }

        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
            if (playWhenReady) {
                if (!mWakeLock.isHeld()) {
                    mWakeLock.acquire(60000);
                }
            } else {
                if (mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        }

        @Override
        public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }


    };

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void getMetadata(Metadata metadata) {
        if (!metadata.get(0).toString().equals("")) {
            String data = metadata.get(0).toString().replace("ICY: ", "");
            System.out.println("MMM = = = " + metadata);
            ArrayList<String> arrayList = new ArrayList(Arrays.asList(data.split(",")));
            String[] mediaMetadata = arrayList.get(0).split("=");
            String title = mediaMetadata[1].replace("\"", "");
            if ("".equalsIgnoreCase(title)) {

                EventBus.getDefault().post(new MessageTitle(title));
            } else {

                EventBus.getDefault().post(new MessageTitle(title));

                if (mBuilder == null) {
                    createNotification();
                } else {
                    updateNotification();
                }
            }
        }
    }

    private void updateNotificationMetadata(String radio_name, String metadata) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mMediaSession = new MediaSessionCompat(context, getString(R.string.app_name));
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMetadata(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, radio_name)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, metadata)
                    .build());
            mBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mMediaSession.getSessionToken())
                    .setShowCancelButton(true)
                    .setShowActionsInCompactView(0, 1)
                    .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP)));
        }
    }

    private String getUserAgent() {

        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version"));
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE;
        result.append(version.length() > 0 ? version : "1.0");

        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }

        String id = Build.ID;

        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }

        result.append(")");
        return result.toString();
    }

    private void changePlayPause(Boolean play) {
       // ((MainActivity) context).changePlayPause(play);
    }

    private void togglePlayPause() {
        if (exoPlayer.getPlayWhenReady()) {
            pause();
            status = PlaybackStatus.PAUSED;
            EventBus.getDefault().post(status);
        } else {
            play(radio);
            status = PlaybackStatus.PLAYING;
            EventBus.getDefault().post(status);

        }
    }

    private void pause() {
        exoPlayer.setPlayWhenReady(false);
        changePlayPause(false);
        updateNotificationPlay(false);
    }

    private void play(String url) {
        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent(getUserAgent()).setAllowCrossProtocolRedirects(true);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), httpDataSourceFactory);
        MediaItem mMediaItem = MediaItem.fromUri(Uri.parse(url));
        MediaSource mediaSource;
        if (url.contains(".m3u8") || url.contains(".M3U8")) {
            mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                    .setAllowChunklessPreparation(false)
                    .createMediaSource(mMediaItem);
        } else {
            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mMediaItem);
        }
        exoPlayer.setMediaSource(mediaSource);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
        changePlayPause(true);
        updateNotificationPlay(true);
    }

    public void stop(Intent intent) {
        if (exoPlayer != null) {
            try {
                mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(onCallIncome);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(onHeadPhoneDetect);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            changePlayPause(false);
            stopExoPlayer();
            service = null;
            stopService(intent);
            stopForeground(true);
        }
    }

    public void stopExoPlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.removeListener(listener);
        }
    }

    private void createNotification() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent playIntent = new Intent(this, RadioService.class);
        playIntent.setAction(ACTION_TOGGLE);
        PendingIntent pendingIntentPlay = PendingIntent.getService(this, 0, playIntent, 0 | PendingIntent.FLAG_IMMUTABLE);

        Intent closeIntent = new Intent(this, RadioService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pendingIntentClose = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        String NOTIFICATION_CHANNEL_ID = "app_channel";
        mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setTicker(Configs.APPNAME)
                .setContentTitle(Configs.APPNAME)
                .setContentText(Constant.metadata)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_radio_notif)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setOnlyAlertOnce(true);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(mChannel);

            mMediaSession = new MediaSessionCompat(context, getString(R.string.app_name));
            mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            mBuilder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mMediaSession.getSessionToken())
                    .setShowCancelButton(true)
                    .setShowActionsInCompactView(0, 1)
                    .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP)))
                    .addAction(new NotificationCompat.Action(
                            android.R.drawable.ic_media_pause, "Pause",
                            pendingIntentPlay))
                    .addAction(new NotificationCompat.Action(
                            R.drawable.stops, "Close",
                            pendingIntentClose));
        } else {
            bigViews = new RemoteViews(getPackageName(), R.layout.lyt_notification_large);
            smallViews = new RemoteViews(getPackageName(), R.layout.lyt_notification_small);
            bigViews.setOnClickPendingIntent(R.id.img_notification_play, pendingIntentPlay);
            smallViews.setOnClickPendingIntent(R.id.status_bar_play, pendingIntentPlay);

            bigViews.setOnClickPendingIntent(R.id.img_notification_close, pendingIntentClose);
            smallViews.setOnClickPendingIntent(R.id.status_bar_collapse, pendingIntentClose);

            bigViews.setImageViewResource(R.id.img_notification_play, android.R.drawable.ic_media_pause);
            smallViews.setImageViewResource(R.id.status_bar_play, android.R.drawable.ic_media_pause);

            bigViews.setTextViewText(R.id.txt_notification_name, "TST = R_NAME");
            bigViews.setTextViewText(R.id.txt_notification_category, Constant.metadata);
            smallViews.setTextViewText(R.id.status_bar_track_name, "TST = R-NAME");
            smallViews.setTextViewText(R.id.status_bar_artist_name, Constant.metadata);

            bigViews.setImageViewResource(R.id.img_notification, R.mipmap.ic_launcher);
            smallViews.setImageViewResource(R.id.status_bar_album_art, R.mipmap.ic_launcher);

            mBuilder.setCustomContentView(smallViews).setCustomBigContentView(bigViews);
        }

        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    private void updateNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setContentTitle(Configs.APPNAME);
            mBuilder.setContentText(Constant.metadata);
        } else {
            bigViews.setTextViewText(R.id.txt_notification_name, "TST - R_NAME");
            bigViews.setTextViewText(R.id.txt_notification_category, Constant.metadata);
            smallViews.setTextViewText(R.id.status_bar_track_name, "TST = R-NAME");
            smallViews.setTextViewText(R.id.status_bar_artist_name, Constant.metadata);
        }
        updateNotificationPlay(exoPlayer.getPlayWhenReady());
    }

    @SuppressLint("RestrictedApi")
    private void updateNotificationPlay(Boolean isPlay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            mBuilder.mActions.remove(0);
            Intent playIntent = new Intent(this, RadioService.class);
            playIntent.setAction(ACTION_TOGGLE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE);
            if (isPlay) {
                mBuilder.mActions.add(0, new NotificationCompat.Action(R.drawable.pauses, "Pause", pendingIntent));
            } else {
                mBuilder.mActions.add(0, new NotificationCompat.Action(R.drawable.plays, "Play", pendingIntent));
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mBuilder.mActions.remove(0);
            Intent playIntent = new Intent(this, RadioService.class);
            playIntent.setAction(ACTION_TOGGLE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, playIntent, 0 | PendingIntent.FLAG_IMMUTABLE);
            if (isPlay) {
                mBuilder.mActions.add(0, new NotificationCompat.Action(R.drawable.pauses, "Pause", pendingIntent));
            } else {
                mBuilder.mActions.add(0, new NotificationCompat.Action(R.drawable.plays, "Play", pendingIntent));
            }
        } else {
            if (isPlay) {
                bigViews.setImageViewResource(R.id.img_notification_play, android.R.drawable.ic_media_pause);
                smallViews.setImageViewResource(R.id.status_bar_play, android.R.drawable.ic_media_pause);
            } else {
                bigViews.setImageViewResource(R.id.img_notification_play, android.R.drawable.ic_media_play);
                smallViews.setImageViewResource(R.id.status_bar_play, android.R.drawable.ic_media_play);
            }
        }
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public String getPlayingRadioStation() {
        return radio;
    }

    private void getBitmapFromURL(String src) {
        try {
            URL url = new URL(src.replace(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
            Log.d("getBitmap", "load bitmap url : " + src);
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            Log.d("getBitmap", "error : " + src);
        }
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

    BroadcastReceiver onCallIncome = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (isPlaying()) {
                if (state != null) {
                    if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        Intent intent_stop = new Intent(context, RadioService.class);
                        intent_stop.setAction(ACTION_TOGGLE);
                        startService(intent_stop);
                        Toast.makeText(context, "there is an call!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "whoops!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    BroadcastReceiver onHeadPhoneDetect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.is_playing) {
                togglePlayPause();
            }
        }
    };

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = focusChange -> {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Resume your media player here
                if (Configs.RESUME_RADIO_ON_PHONE_CALL) {
                    togglePlayPause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (isPlaying()) {
                    togglePlayPause();
                }
                break;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer.removeListener(listener);
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            try {
                mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(onCallIncome);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(onHeadPhoneDetect);
                mAudioManager.unregisterMediaButtonEventReceiver(componentName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}