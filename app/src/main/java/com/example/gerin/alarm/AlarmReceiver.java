package com.example.gerin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver","Yay");

        //need to send the extra to the ringtonePlayingService
        String getExtra = intent.getExtras().getString("extra");

        Intent serviceIntent = new Intent(context, ringtonePlayingService.class);

        serviceIntent.putExtra("extra", getExtra);

        //context.startForegroundService(serviceIntent);
        context.startService(serviceIntent);
    }


}
