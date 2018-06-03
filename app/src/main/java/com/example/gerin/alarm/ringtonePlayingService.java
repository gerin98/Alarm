package com.example.gerin.alarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ringtonePlayingService extends Service {

    MediaPlayer media_song;
    int startID;
    boolean isRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
        //super.onDestroy();
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
