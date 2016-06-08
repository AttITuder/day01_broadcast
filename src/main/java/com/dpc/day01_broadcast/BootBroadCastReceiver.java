package com.dpc.day01_broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dupengcan on 16-6-8.
 */
public class BootBroadCastReceiver extends BroadcastReceiver {
    private String TAG = "BootBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive,intent.getAction=" + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent mainIntent = new Intent();
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.setClassName("com.dpc.day01_broadcast", "com.dpc.day01_broadcast.MainActivity");
            context.startActivity(mainIntent);
        }
    }
}
