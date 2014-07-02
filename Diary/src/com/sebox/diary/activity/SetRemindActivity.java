package com.sebox.diary.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sebox.diary.R;
import com.sebox.diary.service.MyService;
import com.sebox.diary.utils.TimeFormatUtil;

public class SetRemindActivity extends Activity {

	private ImageView back = null;
	private Button set = null;
	private Button cancel = null;
	private SharedPreferences preferences = null;
	private Calendar calendar = null;
	private TextView currentTime = null;
	private TextView setTime = null;
	private TimeChangeReceiver receiver = null;
	private IntentFilter iFilter = null;
	private Intent mIntent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.remind);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		calendar = Calendar.getInstance();
		back = (ImageView) this.findViewById(R.id.back_remind);
		set = (Button) this.findViewById(R.id.remind_ok);
		cancel = (Button) this.findViewById(R.id.remind_cancel);
		currentTime = (TextView) this.findViewById(R.id.current_time);
		calendar.setTimeInMillis(System.currentTimeMillis());
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		currentTime.setText(getString(R.string.current_time) + " "
				+ TimeFormatUtil.format(h) + ":" + TimeFormatUtil.format(m));
		setTime = (TextView) this.findViewById(R.id.set_time);
		set.setOnClickListener(new MyListener());
		cancel.setOnClickListener(new MyListener());
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_below_in,
						R.anim.push_below_out);
			}
		});
		setBackground();
		receiver = new TimeChangeReceiver();
		iFilter = new IntentFilter();
		iFilter.addAction("com.android.set.remind.time");
		iFilter.setPriority(Integer.MAX_VALUE);
		// 注册广播接收器
		registerReceiver(receiver, iFilter);
	}

	class TimeChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) { // TODO
			if (intent.getAction().equals("com.android.set.remind.time")) {
				Log.d("SetRemindActivity", "-----set remind time-----");
				handler.sendEmptyMessage(0x001);
			}
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				SharedPreferences shared = getSharedPreferences("time", 0);
				setTime.setText(getString(R.string.remind_time) + " "
						+ TimeFormatUtil.format(shared.getInt("hour", 0)) + ":"
						+ TimeFormatUtil.format(shared.getInt("minute", 0)));
			}
		};
	};

	class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.remind_ok:
				calendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(SetRemindActivity.this,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								mIntent = new Intent(
										SetRemindActivity.this, MyService.class);
								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(SetRemindActivity.this,
												0, mIntent, 0);
								AlarmManager manager;
								// 获取闹钟管理的实例
								manager = (AlarmManager) getSystemService(ALARM_SERVICE);
								// 设置闹钟
								manager.set(AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										pendingIntent);
								// 设置周期闹钟
								manager.setRepeating(AlarmManager.RTC_WAKEUP,
										System.currentTimeMillis()
												+ (10 * 1000),
										(24 * 60 * 60 * 1000), pendingIntent);
								SharedPreferences shared = getSharedPreferences(
										"time", 0);
								Editor editor = shared.edit();
								editor.putInt("hour", hourOfDay);
								editor.putInt("minute", minute);
								editor.putBoolean("is_set", true);
								editor.putString("mytime",
										TimeFormatUtil.format(hourOfDay) + ""
												+ TimeFormatUtil.format(minute));
								editor.commit();
								SetRemindActivity.this.startService(mIntent);
							}
						}, mHour, mMinute, true).show();
				break;
			case R.id.remind_cancel:
				setTime.setText(getString(R.string.remind_time) + " ");
				SharedPreferences shared = getSharedPreferences(
						"time", 0);
				Editor editor = shared.edit();
				editor.putInt("hour", 0);
				editor.putInt("minute", 0);
				editor.putBoolean("is_set", false);
				editor.commit();
				if (mIntent != null) {
					SetRemindActivity.this.stopService(mIntent);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
		SharedPreferences shared = getSharedPreferences("time", 0);
		if (shared.getBoolean("is_set", false)) {
			setTime.setText(getString(R.string.remind_time) + " "
					+ TimeFormatUtil.format(shared.getInt("hour", 0)) + ":"
					+ TimeFormatUtil.format(shared.getInt("minute", 0)));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
	}

	private void setBackground() {
		// 得到当前布局
		RelativeLayout layout = (RelativeLayout) this
				.findViewById(R.id.remind_layout);
		// 得到id,此处id是在设置背景里面产生的，此处暂不解释
		int id = preferences.getInt("id", 0);
		if (id == 0) {// id=0说明是初始化时的背景
			// 设置背景方法
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 1) {// id=1说明用户选择了第一幅图片
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 2) {// id=2说明用户选择了第二幅图片
			layout.setBackgroundResource(R.drawable.spring);
		} else if (id == 3) {// id=3说明用户选择了第三幅图片
			layout.setBackgroundResource(R.drawable.summer);
		} else if (id == 4) {// id=4说明用户选择了第四幅图片
			layout.setBackgroundResource(R.drawable.autumn);
		} else if (id == 5) {// id=4说明用户选择了第四幅图片
			layout.setBackgroundResource(R.drawable.winter);
		}
	}
}