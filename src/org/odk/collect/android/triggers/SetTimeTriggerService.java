package org.odk.collect.android.triggers;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class SetTimeTriggerService extends Service {
	public static final String TAG = "SETTIMETRIGGERSERVICE";
	private PowerManager.WakeLock wakeLock;
	private WifiManager.WifiLock wifiLock;
	
	@Override
	public void onCreate(){
		Log.i(TAG,"SetTimeServiceCalled");
		super.onCreate();
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SetTimeService");

	    WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	    wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL , "SetTimeSerive");
	    
	    wakeLock.acquire();
	    wifiLock.acquire();
	    
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Map <String,List<Calendar>> triggers = Utils.getTimeTrigger(this);
		
		if (triggers == null){
			Log.i(TAG,"SetTimeServiceRetry");
			Utils.retryLater(this,SetTimeTrigger.class,1);
			stopSelf();
			return;
		}
		
		Calendar now = Calendar.getInstance();
		for (Map.Entry<String, List<Calendar>> entry : triggers.entrySet()) {
		    String form = entry.getKey();
		    List<Calendar> calendars = entry.getValue();
			Intent intent = new Intent(this, ExecuteTimeTrigger.class);
			intent.putExtra("form", form);
			
			for (int i = 0; i<calendars.size(); i++)
				if (calendars.get(i).after(now)){
					Calendar calendar = calendars.get(i);
					Log.i(TAG,"Trigger set for form "+form+" at "+calendar.getTime());
					int id = form.length()*10000+calendar.get(Calendar.HOUR_OF_DAY)*100+calendar.get(Calendar.MINUTE);
					PendingIntent pi = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
				}
		}
		Log.i(TAG,"SetTimeServiceDone");
		stopSelf();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		releaseLocks();
	}
	public void releaseLocks(){
		wakeLock.release();
		wifiLock.release();
	}

}
