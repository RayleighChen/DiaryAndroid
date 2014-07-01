package com.sebox.diary.activity;

import com.sebox.diary.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailDiaryInfoActivity extends Activity {
	private TextView title = null;
	private TextView date = null;
	private TextView info = null;
	private ImageView back = null;
	private SharedPreferences preferences = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	private void init(){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail_diary_info);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		title = (TextView)this.findViewById(R.id.detail_view_title);
		date = (TextView)this.findViewById(R.id.detail_view_date);
		info = (TextView)this.findViewById(R.id.detail_view_info);
		back = (ImageView)this.findViewById(R.id.back_detail_diary);
		Bundle bundle = this.getIntent().getExtras();
		title.setText(bundle.getString("title"));
		date.setText(bundle.getString("date")+"  "+bundle.getString("week")+"  "+bundle.getString("weather"));
		info.setText(bundle.getString("info"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		});
		setBackground();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
	}
	private void setBackground() {
		// 得到当前布局
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.detail_diary_info_layout);
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}
}
