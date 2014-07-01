package com.sebox.diary.receiver;

import java.util.Calendar;

import com.sebox.diary.R;
import com.sebox.diary.activity.AddDiaryActivity;
import com.sebox.diary.activity.MainActivity;
import com.sebox.diary.activity.SetRemindActivity;
import com.sebox.diary.service.MyService;
import com.sebox.diary.utils.TimeFormatUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyReceiver extends BroadcastReceiver{
	private Calendar calendar = Calendar.getInstance();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("com.android.system.time")) {
			calendar.setTimeInMillis(System.currentTimeMillis());
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			int m = calendar.get(Calendar.MINUTE);
			SharedPreferences preferences = MyService.service.getSharedPreferences("time", 0);
			if ((TimeFormatUtil.format(h)+TimeFormatUtil.format(m)).equals(
					preferences.getString("mytime", ""))) {
				myNotification(context);
				return;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void myNotification(Context con) {
		NotificationManager manager = (NotificationManager) MyService.service
				.getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = con.getString(R.string.notification_title);
		long when = System.currentTimeMillis();
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(icon, tickerText, when);
		Context context = MyService.service.getApplicationContext();
		CharSequence contentTitle = con.getString(R.string.notification_remind);
		CharSequence contentText = con.getString(R.string.notification_info);
		Intent notificationIntent = new Intent(MyService.service,
				MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(
				MyService.service, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		// 指定通知可以清除
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// 指定通知不能清除
		notification.flags |= Notification.FLAG_NO_CLEAR;
		// 通知显示的时候播放默认声音
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		//设置声音
		notification.defaults |=Notification.DEFAULT_SOUND;
		//设置LED灯
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		manager.notify(1, notification);
	}
}