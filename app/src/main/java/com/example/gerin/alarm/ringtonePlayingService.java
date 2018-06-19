package com.example.gerin.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;

public class ringtonePlayingService extends Service {

    MediaPlayer media_song;
    int startID;
    boolean isRunning;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        ringtonePlayingService getService() {
            // Return this instance of ringtonePlayingService so clients can call public methods
            return ringtonePlayingService.this;
        }
    }
        
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("ringtonePlayingService", "onStartCommand");

        /* create a notification */
        long date = System.currentTimeMillis();
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
        String timeString = "Alarm set at " + sdfTime.format(date);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.alarm_clock_time);
        builder.setContentText(timeString);
        builder.setContentTitle("Turn off alarm");
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        notificationManager.notify(1,notification);

        Log.e("Local Service", "Received start id " + startId + " : " + intent);

        String state = intent.getExtras().getString("extra");

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;

        }

        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        //if music IS NOT playing and user presses alarm ON, start playing
        if(!this.isRunning && startId == 1){

            media_song = MediaPlayer.create(this, preferences.getInt("tune",0));
            media_song.start();

            this.isRunning = true;
            this.startID = 0;

        }
        //if music IS playing and user presses alarm OFF, stop playing
        else if(this.isRunning && startId == 0){

            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startID = 1;

        }
        //if music IS NOT playing and user presses alarm OFF, do nothing
        else if(!this.isRunning && startId == 0){

            this.isRunning = false;
            this.startID = 0;

        }
        //if music IS playing and user presses alarm ON, do nothing
        else if(this.isRunning && startId == 1){

            this.isRunning = true;
            this.startID = 1;

        }
        else{

        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /* method for clients */
    public boolean showButtons(){
        if(this.isRunning == true)
            return true;
        else
            return false;
    }

}
