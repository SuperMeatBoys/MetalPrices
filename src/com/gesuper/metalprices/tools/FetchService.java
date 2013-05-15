package com.gesuper.metalprices.tools;

import java.util.LinkedList;
import java.util.Queue;

import com.gesuper.metalprices.WidgetProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

public class FetchService extends Service implements Runnable {
	private static final String TAG="FetchService";
	public static final String ACTION_FETCH_SERVICE = "com.gesuper.metalprices.action.fetch.service"; 
	
	private static Queue<Integer> sAppWidgetIds=new LinkedList<Integer>(); 
	private static boolean sThreadRunning = false; 
	
	private static Object sLock = new Object();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  
	
	public static void updateAppWidgetIds(int[] appWidgetIds){  
        synchronized (sLock) {  
            for (int appWidgetId : appWidgetIds) {  
                sAppWidgetIds.add(appWidgetId);  
            }  
        }  
    } 

    @Override  
    public void onCreate() {  
        super.onCreate();  
    }  
    
    
	@SuppressWarnings("deprecation")
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStart");	
        super.onStart(intent, startId);  
        if (null != intent) {  
            if (ACTION_FETCH_SERVICE.equals(intent.getAction())) {  
                AppWidgetManager widget = AppWidgetManager.getInstance(this);  
                updateAppWidgetIds(widget.getAppWidgetIds(new ComponentName(this, FetchService.class)));  
            }  
        }  
        synchronized (sLock) {  
            if (!sThreadRunning) {  
                sThreadRunning=true;  
                new Thread(this).start();  
            }  
        }
        return 0;
    }
    
    public void run(){
    	SharedPreferences setting = getSharedPreferences("com.gesuper.metalprices_preferences", 0);  
        String updateTime = setting.getString("update_time", "45000");  
        String updateUrl = setting.getString("update_url", "xxxxxxxxx");  
        
        WidgetProvider.threadForUpdate(this);
          
        Intent updateIntent=new Intent(ACTION_FETCH_SERVICE);  
        updateIntent.setClass(this, FetchService.class);  
        PendingIntent pending = PendingIntent.getService(this, 0, updateIntent, 0);  
          
        Time time = new Time();  
        long nowMillis = System.currentTimeMillis();  
        time.set(nowMillis+ 45000);  
        long updateTimes = time.toMillis(true);  
        Log.d(TAG, "request next update at "+updateTimes);  
          
        AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);  
        alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);  
        stopSelf();  
        sThreadRunning=false;
    }
}
