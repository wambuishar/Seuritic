package com.example.sha.security.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sha.security.activities.Splash;

public class MyReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;

    public MyReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (countPowerOff == 4) {
            countPowerOff = 0;
            context.startActivity(new Intent(context, Splash.class));
        } else {
            countPowerOff++;
        }
    }
}