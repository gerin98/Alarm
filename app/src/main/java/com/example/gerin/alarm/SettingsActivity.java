package com.example.gerin.alarm;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    Toolbar settings_toolbar;
    static Dialog d;
    Button snooze_length;
    TextView snooze_length_settings_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        settings_toolbar = (Toolbar) findViewById(R.id.settings_tolbar);
        settings_toolbar.setTitle("");
        setSupportActionBar(settings_toolbar);
        settings_toolbar.setTitle(R.string.settings_toolbar_title);
        settings_toolbar.setTitleTextColor(Color.WHITE);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //shared preferences
        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        int prefValue = preferences.getInt("snooze", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        if(prefValue == 0){
            editor.putInt("snooze", 1);
            editor.apply();
        }
        else{
            editor.putInt("snooze", prefValue);
            editor.apply();
            //keep previous preference
        }

        //snooze length text view
        snooze_length_settings_tv = (TextView) findViewById(R.id.snooze_length_settings_tv);

        //snooze settings button
        snooze_length = (Button) findViewById(R.id.snooze_length_settings_button);
        snooze_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });





    }

    public void show(){
        d = new Dialog(SettingsActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.snooze_length_dialog);
        Button set_button = (Button) d.findViewById(R.id.set_button);
        Button cancel_button = (Button) d.findViewById(R.id.cancel_button);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(30);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
//        np.setOnValueChangedListener(this);
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update textview
                String settext;
                if(np.getValue() == 1)
                    settext = String.valueOf(np.getValue()) + " minute";
                else
                    settext = String.valueOf(np.getValue()) + " minutes";
                snooze_length_settings_tv.setText(settext);

                //update shared preferences
                SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("snooze", np.getValue());
                editor.apply();

                d.dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
