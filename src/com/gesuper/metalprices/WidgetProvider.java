package com.gesuper.metalprices;

import com.gesuper.metalprices.tools.FetchPrices;
import com.gesuper.metalprices.tools.FetchService;
import com.gesuper.metalprices.tools.MetalPrice;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {
	private static final String TAG = "WidgetProvider";
    private static final String CLICK_NAME_ACTION = "com.gesuper.metalprices.action.widget.click";
    
    private static RemoteViews mRemoteView;
    private static FetchPrices mFetchPrices;

    @Override
    public void onEnabled(Context context){
        Log.d(TAG, "onEnabled");
    	super.onEnabled(context);
    	if(mFetchPrices == null){
    		mFetchPrices = new FetchPrices();
    	}
    	mRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_main);
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
    	super.onDeleted(context, appWidgetIds);
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // TODO Auto-generated method stub
    	Log.d(TAG, "onUpdate");
    	FetchService.updateAppWidgetIds(appWidgetIds);  
        context.startService(new Intent(context,FetchService.class));  
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, "OnReceive");
        
        super.onReceive(context, intent);
        if (mRemoteView == null) {
			mRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_main);
		}
		if (intent.getAction().equals(CLICK_NAME_ACTION)) {
			Toast.makeText(context, "Refreshing", Toast.LENGTH_LONG).show();
			
			threadForUpdate(context);
		}
    }
    
    public static void threadForUpdate(final Context context){
    	Log.d(TAG, "threadForUpdate");
		new Thread(new Runnable() {  
			public void run(){
				mFetchPrices.updatePrices(mRemoteView);

				AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
				int[] appIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
				appWidgetManger.updateAppWidget(appIds, mRemoteView);
			}
    	}).start();
    }

    public static void updateAppWidget(Context context,
            AppWidgetManager appWidgeManger, int appWidgetId) {
    	if(mFetchPrices == null){
    		mFetchPrices = new FetchPrices();
    	}
    	mRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_main);
    	

    	Intent intentClick = new Intent(CLICK_NAME_ACTION);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
		mRemoteView.setOnClickPendingIntent(R.id.widget_gold, pendingIntent);
        appWidgeManger.updateAppWidget(appWidgetId, mRemoteView);
    }
}