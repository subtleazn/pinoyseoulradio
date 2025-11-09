package com.pinoyseoul.radio.dialog;

import static android.content.Context.MODE_PRIVATE;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.settings.Configs;

public class SetTimD extends AppCompatDialog implements
        View.OnClickListener, Configs {
    public Activity df;
    public Dialog dr;
    TextView text;
    Button ok;
    SeekBar seek;

    public SetTimD(Activity a) {
        super(a);
        this.df = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.timer_set_dialog);
        text = (TextView) findViewById(R.id.errorText);
        ok = (Button) findViewById(R.id.btn_ook);
        seek = (SeekBar) findViewById(R.id.seek);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        SharedPreferences sharedPreferences = df.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt(COWNTDOWN, progress).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ook:
                break;
            case R.id.seek:
                break;
            default:
                break;
        }
        dismiss();
    }

}
