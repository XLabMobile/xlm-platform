package com.xlabm.tmservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * XLAB Mobile Version 0.1 Alpha Release
 * User: SID@XLABM
 * Date: 5/20/13
 * Time: 2:26 PM
 * Responsibility of Class: To launch the Trigger Manager Service on system
 * bootup.
 */
public class TMSBootUpBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context,
                TriggerManagerService.class);
        context.startService(startServiceIntent);
    }
}
