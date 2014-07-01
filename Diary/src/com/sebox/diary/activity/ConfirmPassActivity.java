package com.sebox.diary.activity;

import java.util.List;

import com.sebox.diary.R;
import com.sebox.diary.view.LockPatternUtils;
import com.sebox.diary.view.LockPatternView;
import com.sebox.diary.view.LockPatternView.Cell;
import com.sebox.diary.view.LockPatternView.OnPatternListener;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ConfirmPassActivity extends Activity {
	private Button last;
	private Button ok;
	private static boolean isSet = false;
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private SharedPreferences preferences;
	private String pass="";
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
		setContentView(R.layout.confim_graphic_pass);
		sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
		lockPatternView = (LockPatternView) findViewById(R.id.lock_confirm);
		last=(Button)findViewById(R.id.last);
		last.setOnClickListener(new LastListener());
		ok=(Button)findViewById(R.id.ok);
		ok.setOnClickListener(new OkListener());
		lockPatternUtils = new LockPatternUtils(this);
		preferences=getSharedPreferences("pass", Context.MODE_PRIVATE);
		pass=preferences.getString("lock_pwd", "");
		lockPatternView.setOnPatternListener(new OnPatternListener() {
			public void onPatternStart() {
				// TODO Auto-generated method stub
				
			}
			
			public void onPatternDetected(List<Cell> pattern) {
				if (pass.trim().equals(lockPatternUtils.patternToString(pattern))) {
					isSet=true;
				}
				else {
					Toast.makeText(ConfirmPassActivity.this, R.string.dif_pass, Toast.LENGTH_LONG).show();
				}
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
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.confirm_graphic_pass_layout);
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
	
	class LastListener implements OnClickListener{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(ConfirmPassActivity.this, GraphicPassSetActivity.class);
			startActivity(intent);
			setPass();
			ConfirmPassActivity.this.finish();
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	}
	class OkListener implements OnClickListener{
		public void onClick(View v) {
			if (isSet) {
				Editor editor=preferences.edit();
				editor.putString("passway", "graphicpass");
				editor.putBoolean("isSet", true);
				editor.commit();
				Intent intent=new Intent(ConfirmPassActivity.this,MainActivity.class);
				startActivity(intent);
				ConfirmPassActivity.this.finish();
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				isSet=false;
				Toast.makeText(ConfirmPassActivity.this, R.string.pass_set_success, Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(ConfirmPassActivity.this, R.string.please_confirm_pass, Toast.LENGTH_LONG).show();
			}
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setPass();
		//lockPatternView.clearPattern();
	}
	
	public void setPass(){
		Editor editor = preferences.edit();
		if (GraphicPassSetActivity.pass!=null&&!GraphicPassSetActivity.pass.equals("")) {
	    	editor.putString("lock_pwd", GraphicPassSetActivity.pass);
	    	editor.commit();
		}else {
			editor.putString("lock_pwd", null);
		}
	}
}
