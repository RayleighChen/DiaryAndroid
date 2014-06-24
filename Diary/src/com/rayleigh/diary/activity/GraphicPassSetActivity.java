package com.rayleigh.diary.activity;

import java.util.List;

import com.rayleigh.diary.R;
import com.rayleigh.diary.view.LockPatternUtils;
import com.rayleigh.diary.view.LockPatternView;
import com.rayleigh.diary.view.LockPatternView.Cell;
import com.rayleigh.diary.view.LockPatternView.OnPatternListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GraphicPassSetActivity extends Activity {
	private static boolean isSet = false;
	private Button cancel;
	private Button next;
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private SharedPreferences preferences;
	public static String pass="";
	private int result;
	private SharedPreferences sharedPreferences = null;
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
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graphic_pass);
		sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
		lockPatternView = (LockPatternView) findViewById(R.id.lock);
		lockPatternUtils = new LockPatternUtils(this);
		cancel=(Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new CancelListener());
		next=(Button)findViewById(R.id.next);
		next.setOnClickListener(new NextListener());
		preferences=getSharedPreferences("pass", Context.MODE_PRIVATE);
		pass=preferences.getString("lock_pwd", "");
		lockPatternView.setOnPatternListener(new OnPatternListener() {
			
			public void onPatternStart() {
				// TODO Auto-generated method stub
				
			}
			
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				//result = lockPatternUtils.checkPattern(pattern);
				//lockPatternUtils.saveLockPattern(pattern);
				Editor editor=preferences.edit();
				editor.putString("lock_pwd", lockPatternUtils.patternToString(pattern));
				editor.commit();
				isSet=true;
			}
			
			public void onPatternCleared() {
				// TODO Auto-generated method stub
				
			}
			
			public void onPatternCellAdded(List<Cell> pattern) {
				// TODO Auto-generated method stub
				
			}
		});
		setBackground();
	}
	private void setBackground() {
		// 得到当前布局
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.graphic_pass_layout);
		// 得到id,此处id是在设置背景里面产生的，此处暂不解释
		int id = sharedPreferences.getInt("id", 0);
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
	class CancelListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			setPass();
			finish();
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	}
	
	class NextListener implements OnClickListener{
		public void onClick(View v) {
			if (isSet) {
				Intent intent=new Intent();
				intent.setClass(GraphicPassSetActivity.this, ConfirmPassActivity.class);
				startActivity(intent);
				GraphicPassSetActivity.this.finish();
				overridePendingTransition(R.anim.push_below_in,R.anim.push_below_out);
				isSet=false;
			}
			else {
				Toast.makeText(GraphicPassSetActivity.this, R.string.please_set_password, Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setPass();
		isSet=false;
	}
	
	public void setPass(){
		Editor editor = preferences.edit();
		if (pass!=null&&!pass.equals("")) {
	    	editor.putString("lock_pwd", pass);
	    	editor.commit();
		}else {
			editor.putString("lock_pwd", null);
		}
	}
}
