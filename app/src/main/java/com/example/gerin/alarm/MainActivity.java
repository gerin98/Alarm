package com.example.gerin.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

//ToDo illegal state exception: not allowed to start service intent when app is closed

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;

    //variable to sample alarm music
    AlarmSound sound = new AlarmSound(this);

    //Alarm variables
    long snoozeTime;
    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    Context context;
    Calendar calendar;
    FloatingActionButton alarmOn;
    Button alarmOff;
    PendingIntent pendingIntent;
    Intent alarmReceiverIntent;
    boolean alarmActive = false;
    TextView alarm_textView;
    Switch alarm_switch;
    RelativeLayout alarm_relativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        //toolbars initialization
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);


        //initialize our alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our time picker
        //alarmTimePicker = (TimePicker) findViewById(R.id.time_picker);

        //create an instance of calendar
        calendar = Calendar.getInstance();

        //create an intent to the AlarmReceiver class
        alarmReceiverIntent = new Intent(this.context, AlarmReceiver.class);

        //initialize alarm ON/OFF buttons
        alarmOn = (FloatingActionButton) findViewById(R.id.alarm_button);
        alarmOff = (Button) findViewById(R.id.off_button);

        //set slide view pager
        mSlideViewPager = (ViewPager) findViewById(R.id.slidePager);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.setCurrentItem(sliderAdapter.getCount()-2);

        //set shared preferences
        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        int prefValue = preferences.getInt("tune", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        if(prefValue == 0){
            editor.putInt("tune", R.raw.down_stream);
            editor.apply();
        }
        else{
            editor.putInt("tune", prefValue);
            editor.apply();
            //keep previous preference
        }



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
                                TextView tGreeting = (TextView) findViewById(R.id.greeting);
                                TextView tampm = (TextView) findViewById(R.id.ampm);
//                                LocalTime greetingtime1 = LocalTime.parse("12:00");
//                                LocalTime greetingtime2 = LocalTime.parse("17:00");
                                long date = System.currentTimeMillis();


                                ////////////
//                                Calendar cal = Calendar.getInstance();
//                                cal.setTime();
//                                int hours = cal.get(Calendar.HOUR_OF_DAY);
                                ////////////

                                SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd yyyy ");
                                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
                                SimpleDateFormat sdfGreetingTime = new SimpleDateFormat("HH");

                                String dateString = sdfDate.format(date);
                                String timeString = sdfTime.format(date);
                                String greetingTimeString = sdfGreetingTime.format(date);

//                                LocalTime currTime = LocalTime.parse(greetingTimeString);

                                //this is in a try block since the reference returns null when on
                                //different pages
                                    try {
                                        tDate.setText(dateString);
                                        tTime.setText(timeString);
                                    }catch (NullPointerException e){}

                                if(Integer.valueOf(greetingTimeString) < 12) {
                                    tGreeting.setText("Good Morning");
                                    tampm.setText("AM");
                                }
                                else if(Integer.valueOf(greetingTimeString) < 17) {
                                    tGreeting.setText("Good Afternoon");
                                    tampm.setText("PM");
                                }
                                else {
                                    tGreeting.setText("Good Evening");
                                    tampm.setText("PM");
                                }
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
        alarm_textView = findViewById(R.id.alarm_textView);
        alarm_switch = findViewById(R.id.alarm_switch);

        alarm_relativeLayout =findViewById(R.id.alarm_relativeLayout);
        //setting calendar instance with hour and minute
//        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        //final boolean[] alarmActive = {false};
        //final int [] set_time = new int[2];
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                if(timePicker.isShown()) {  //???
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    Log.e("inside onTimeSet", "calendar updated");

                    if(selectedHour > 12) {
                        String alarm_text = String.valueOf(selectedHour - 12) + ":" + String.format("%02d", selectedMinute) + " PM";
                        alarm_textView.setText(alarm_text);
                    }
                    else {
                        String alarm_text = String.valueOf(selectedHour) + ":" + String.format("%02d", selectedMinute) + " AM";
                        alarm_textView.setText(alarm_text);
                    }
                    alarm_relativeLayout.setVisibility(View.VISIBLE);
                    alarm_switch.setVisibility(View.VISIBLE);
                    alarm_switch.setChecked(true);

                    snoozeTime = calendar.getTimeInMillis();

                    //put in extra string to say that you stopped the alarm
                    alarmReceiverIntent.putExtra("extra","alarm on");

                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                alarmActive = true;
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

//        System.out.println(String.valueOf(set_time[0]) + ":" + String.valueOf(set_time[1]));
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

    public void alarmToggleOff(View view){

        Log.e("inside alarmToggleOff", "turning off the alarm");

        alarmReceiverIntent.putExtra("extra","alarm off");

        //cancel the alarm
        alarmManager.cancel(pendingIntent);

        //stop the ringtone
        //sends a message to stop directly to the ringtonePlayingService
        sendBroadcast(alarmReceiverIntent);

        alarm_switch.setChecked(false);

    }

    public void alarmSnooze(View view){

        Log.e("inside alarmSnooze", "snoozing the alarm");

        alarmReceiverIntent.putExtra("extra","alarm off");

        //cancel the alarm
        alarmManager.cancel(pendingIntent);

        //stop the ringtone
        //sends a message to stop directly to the ringtonePlayingService
        sendBroadcast(alarmReceiverIntent);

        //put in extra string to say that you stopped the alarm
        alarmReceiverIntent.putExtra("extra","alarm on");

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //get snooze length
        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        int prefValue = preferences.getInt("snooze", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        if(prefValue == 0){
            editor.putInt("snooze", 1);
            editor.apply();

            //one minute snooze time
            snoozeTime += 60000;
        }
        else{
            editor.putInt("snooze", prefValue);
            editor.apply();

            //one minute snooze time
            snoozeTime += prefValue*60*1000;    //minutes*60seconds*1000milliseconds
        }





        alarmManager.set(AlarmManager.RTC_WAKEUP, snoozeTime, pendingIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //process your onClick here
            Intent gotoSettingsActivity = new Intent();
            gotoSettingsActivity.setClass(this, SettingsActivity.class);
            startActivity(gotoSettingsActivity);

        }
        else if(id == R.id.alarm_tune_settings){
            Intent gotoAlarmChooserActivity = new Intent();
            gotoAlarmChooserActivity.setClass(this, alarmChooserActivity.class);
            startActivity(gotoAlarmChooserActivity);
        }

        return super.onOptionsItemSelected(item);
    }
}

//public void alarmSnooze(View view){
//
//}