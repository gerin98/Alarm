package com.example.gerin.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;

    //variable to sample alarm music
    AlarmSound sound = new AlarmSound(this);

    //Alarm variables
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    Context context;
    Calendar calendar;
    Button alarmOn;
    PendingIntent pendingIntent;
    Intent alarmReceiverIntent;
    boolean alarmActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        //initialize our alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our time picker
        //alarmTimePicker = (TimePicker) findViewById(R.id.time_picker);

        //create an instance of calendar
        calendar = Calendar.getInstance();

        //create an intent to the AlarmReceiver class
        alarmReceiverIntent = new Intent(this.context, AlarmReceiver.class);

        //initialize alarm ON button
        alarmOn = (Button) findViewById(R.id.alarm_button);



        //set slide view pager
        mSlideViewPager = (ViewPager) findViewById(R.id.slidePager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.setCurrentItem(sliderAdapter.getCount()-2);

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

    public void alarmToggle(View view){

        //setting calendar instance with hour and minute
//        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        //final boolean[] alarmActive = {false};
//        int hour = 0, minute = 0;
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                if(timePicker.isShown()) {  //???
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    Log.e("inside onTimeSet", "calendar updated");
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                alarmActive = true;
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
        Log.e("Set the calendar", "pending intent comes next");
        if(alarmActive)
            Log.e("inside if statement", "alarmActive: true");



        //have to check if time set before sending pending intent
        //if(alarmActive){
            //create a pending intent
//            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            //set the alarm manager
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //}


        //turn off the alarm
        //alarmManager.cancel(pendingIntent);

    }
}
