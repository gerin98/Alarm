package com.example.gerin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver","Yay");

        Intent serviceIntent = new Intent(context, ringtonePlayingService.class);

        context.startService(serviceIntent);
    }


}
