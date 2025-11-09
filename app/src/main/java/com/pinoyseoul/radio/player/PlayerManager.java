package com.pinoyseoul.radio.player;

import android.content.Context;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class PlayerManager {

    private static PlayerManager instance;
    private SimpleExoPlayer exoPlayer;

    private PlayerManager(Context context) {
        exoPlayer = new SimpleExoPlayer.Builder(context).build();
    }

    public static synchronized PlayerManager getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerManager(context);
        }
        return instance;
    }

    public SimpleExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    public void release() {
        exoPlayer.release();
        instance = null;
    }
}
