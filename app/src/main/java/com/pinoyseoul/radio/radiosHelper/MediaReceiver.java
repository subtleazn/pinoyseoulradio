package com.pinoyseoul.radio.radiosHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaReceiver extends BroadcastReceiver {

    public MediaReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Received!", Toast.LENGTH_SHORT).show();
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            // do something
            if(RadioService.getInstance() != null) {
                Intent intent_pause = new Intent(context, RadioService.class);
                intent_pause.setAction(RadioService.ACTION_TOGGLE);
                context.startService(intent_pause);
            }
        }
        abortBroadcast();
    }
}