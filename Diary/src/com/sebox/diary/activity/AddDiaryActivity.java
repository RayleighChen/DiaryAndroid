package com.sebox.diary.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sebox.diary.R;
import com.sebox.diary.db.DiaryDao;
import com.sebox.diary.model.Diary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddDiaryActivity extends Activity {
	private TextView timeTextView = null;
	private TextView weekTextView = null;
	private Spinner weatherSpinner = null;
	private Calendar cal = Calendar.getInstance();
	private Date date = null;
	private SimpleDateFormat simpleDateFormat = null;
	public static final int WEEKDAYS = 7;
	private EditText diaryInfo = null;
	private EditText diaryTitle = null;
	public static String[] WEEK = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" };
	private ImageView back = null;
	private SharedPreferences preferences = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
	}
	@SuppressLint("SimpleDateFormat")
	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_diary);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		date = cal.getTime();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		timeTextView = (TextView) this.findViewById(R.id.time);
		timeTextView.setText(simpleDateFormat.format(date));
		weekTextView = (TextView) this.findViewById(R.id.week);
		weekTextView.setText(DateToWeek(date));
		weatherSpinner = (Spinner) this.findViewById(R.id.weather);
		diaryInfo = (EditText)this.findViewById(R.id.edit_diary_info);
		diaryTitle = (EditText)this.findViewById(R.id.edit_title);
		back = (ImageView)this.findViewById(R.id.back_add_diary);
		back.setOnClickListener(new BackListener());
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, 
				R.array.weather, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		weatherSpinner.setAdapter(adapter);
		weatherSpinner.setPrompt(getString(R.string.weather));
		setBackground();
	}
	
	private void setBackground() {
		// 得到当前布局
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.add_diary_layout);
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
	public static String DateToWeek(Date date){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {  
	        return null;  
	    }  
	    return WEEK[dayIndex - 1];  
	}
	
	class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			back();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void back(){
		if ((!diaryTitle.getText().toString().trim().equals("")) && 
				(!diaryInfo.getText().toString().trim().equals(""))) {
			DiaryDao diaryDao = new DiaryDao(AddDiaryActivity.this);
			Diary diary = new Diary();
			diary.setDate(timeTextView.getText().toString());
			diary.setWeek(weekTextView.getText().toString());
			diary.setWeather(weatherSpinner.getSelectedItem().toString());
			diary.setDiaryTitle(diaryTitle.getText().toString());
			diary.setDiaryInfo(diaryInfo.getText().toString());
			diaryDao.insert(diary);
			Intent intent = new Intent();
			intent.setClass(AddDiaryActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
			Toast.makeText(AddDiaryActivity.this, R.string.save_success, 0).show();
		}else {
			Toast.makeText(AddDiaryActivity.this, R.string.empty_info, 0).show();
			AddDiaryActivity.this.finish();
			overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
		}
	}
}
