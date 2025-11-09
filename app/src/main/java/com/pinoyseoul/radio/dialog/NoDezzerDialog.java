package com.pinoyseoul.radio.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.pinoyseoul.radio.R;


public class NoDezzerDialog extends AppCompatDialog implements
        View.OnClickListener {
    public Activity df;
    public Dialog dr;
    TextView text;
    Button ok;
    String alert;

    public NoDezzerDialog(Activity a, String apps) {
        super(a);
        this.df = a;
        this.alert = apps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nodeezer_dialog);
        text = (TextView) findViewById(R.id.errorText);
        ok = (Button) findViewById(R.id.btn_ook);
        text.setText(getContext().getResources().getString(R.string.we) + " " + alert + " " + getContext().getResources().getString(R.string.appl));
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ook:
                // c.finish();
                break;

            default:
                break;
        }
        dismiss();
    }
}

