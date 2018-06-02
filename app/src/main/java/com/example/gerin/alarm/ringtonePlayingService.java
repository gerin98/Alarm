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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Local Service", "Received start id " + startId + " : " + intent);


        SharedPreferences preferences = getSharedPreferences("alarm_tune", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        media_song = MediaPlayer.create(this, preferences.getInt("tune",0));
        media_song.start();

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
