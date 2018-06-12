package com.example.gerin.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class ringtonePlayingService extends Service {

    MediaPlayer media_song;
    int startID;
    boolean isRunning;
        
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("ringtonePlayingService", "onStartCommand");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("my_channel", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//
//         NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"my_channel")
//                .setSmallIcon(R.mipmap.ic_launcher2)
//                .setContentTitle("Music Player")
//                .setContentText("Playing \"So Long - Massari\" ")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(false);

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, mBuilder.build());



//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher2)
//                .setContentTitle("My Awesome App")
//                .setContentText("Doing some work...")
//                .setContentIntent(pendingIntent).build();

        //notificationManager.notify(0, notification);

        //startForeground(1337, mBuilder.build());

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







//        sound.stopTune();
////        sound.chooseTrack(R.raw.down_stream);
//        sound.chooseTrack(preferences.getInt("tune",0));
//        sound.playTune();


        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
