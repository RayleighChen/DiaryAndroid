package com.rayleigh.diary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	public static MyService service = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("MyService", "-----MyService onCreate-----");
		service = this;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("MyService", "-----MyService onStart-----");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("MyService", "-----MyService onStartCommand-----");
		sendBroadcast(new Intent("com.android.set.remind.time"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(60000);
						sendBroadcast(new Intent("com.android.system.time"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
}
