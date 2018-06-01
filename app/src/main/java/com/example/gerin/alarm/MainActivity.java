package com.example.gerin.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;

    //variable to sample alarm music
    AlarmSound sound = new AlarmSound(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set slide view pager
        mSlideViewPager = (ViewPager) findViewById(R.id.slidePager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        //set shared preferences
        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("tune", R.raw.down_stream);
        editor.apply();

        //new thread to update clock
        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //some code
                                TextView tDate = (TextView) findViewById(R.id.date);
                                TextView tTime = (TextView) findViewById(R.id.time);
//                                TextView tGreeting = (TextView) findViewById(R.id.greeting);
//                                LocalTime greetingtime1 = LocalTime.parse("12:00");
//                                LocalTime greetingtime2 = LocalTime.parse("17:00");
                                long date = System.currentTimeMillis();

                                SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd yyyy ");
                                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
//                                SimpleDateFormat sdfGreetingTime = new SimpleDateFormat("HH:mm");

                                String dateString = sdfDate.format(date);
                                String timeString = sdfTime.format(date);
//                                String greetingTimeString = sdfGreetingTime.format(date);

//                                LocalTime currTime = LocalTime.parse(greetingTimeString);

                                //this is in a try block since the reference returns null when on
                                //different pages
                                    try {
                                        tDate.setText(dateString);
                                        tTime.setText(timeString);
                                    }catch (NullPointerException e){}

//                                if(currTime.isBefore(greetingtime1))
//                                    tGreeting.setText("Good Morning");
//                                else if(currTime.isBefore(greetingtime2))
//                                    tGreeting.setText("Good Afternoon");
//                                else
//                                    tGreeting.setText("Good Evening");

                            }
                        });
                    }
                }catch (InterruptedException e){}
            }
        };
        t.start();

    }

    public void playSound(View view){
        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        sound.stopTune();
//        sound.chooseTrack(R.raw.down_stream);
        sound.chooseTrack(preferences.getInt("tune",0));
        sound.playTune();

    }

    public void playSound2(View view){
        sound.stopTune();
        sound.chooseTrack(R.raw.new_dawn);
        sound.playTune();
    }

    public void enter(View view){

        Intent gotoAlarmChooserActivity = new Intent();
        gotoAlarmChooserActivity.setClass(this, alarmChooserActivity.class);
        startActivity(gotoAlarmChooserActivity);

    }
}
