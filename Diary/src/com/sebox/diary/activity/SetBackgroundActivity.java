package com.sebox.diary.activity;

import com.sebox.diary.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class SetBackgroundActivity extends Activity implements OnGestureListener{
	private int flag = 1;
	private GestureDetector detector = null;
	private ViewFlipper flipper = null;
	private Button setBackground = null;
	private Button cancel = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	private void init() {
		//设置窗口无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//得到布局
		setContentView(R.layout.background);
		//实例化手势对象，用以实现手势滑动
		detector = new GestureDetector(this);
		//通过findViewById方法得到Flipper控件
		flipper = (ViewFlipper) this.findViewById(R.id.flipper);
		//在flipper中添加四幅图片
		flipper.addView(getImageView(R.drawable.diary_view_bg));
		flipper.addView(getImageView(R.drawable.spring));
		flipper.addView(getImageView(R.drawable.summer));
		flipper.addView(getImageView(R.drawable.autumn));
		flipper.addView(getImageView(R.drawable.winter));
		//得到设置按钮
		setBackground = (Button) this.findViewById(R.id.backround_set);
		//得到取消按钮
		cancel = (Button) this.findViewById(R.id.backround_cancel);
		//设置按钮监听器
		setBackground.setOnClickListener(new SetBackgroundListener());
		//取消按钮监听器
		cancel.setOnClickListener(new CancelListener());
	}
	private View getImageView(int id) {
		ImageView imgView = new ImageView(this);
		imgView.setImageResource(id);
		return imgView;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {// 向右滑动
			if (flipper.getDisplayedChild() == 4) {
				//设置往右不能循环滑动
				flipper.stopFlipping();
				return false;
			} else {
				//设置进入动画
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				//设置离去动画
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				//显示下一副图片
				flipper.showNext();
			}
			//设置按钮不可见
			findViewById(R.id.bg_button).setVisibility(View.GONE);
			//flag++ 第一张默认为1 此后往右滑动一次加+1
			flag++;

		} else if (e2.getX() - e1.getX() > 120) {// 向左滑动
			if (flipper.getDisplayedChild() == 0) {
				//设置往左不能循环滑动
				flipper.stopFlipping();
				return false;
			} else {
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				//显示上一副图片
				flipper.showPrevious();
			}
			//设置按钮不可见
			findViewById(R.id.bg_button).setVisibility(View.GONE);
			//flag-- 第一张默认为1 此后往右滑动一次加-1
			flag--;
		}
		return false;
	}
	//长按图片
		public void onLongPress(MotionEvent e) {
			//将两个按钮设置可见
			findViewById(R.id.bg_button).setVisibility(View.VISIBLE);
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}

		public void onShowPress(MotionEvent e) {

		}

		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		class CancelListener implements OnClickListener {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//隐藏两个按钮
				findViewById(R.id.bg_button).setVisibility(View.GONE);
			}

		}

		class SetBackgroundListener implements OnClickListener {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//得到一个SharedPreferences对象，用以保存当前图片的id
				SharedPreferences preferences = getSharedPreferences("image",
						MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putInt("id", flag);
				editor.commit();//提交，否则不能保存成功
				//点击设置按钮后跳转Activity的实现
				Intent intent = new Intent();
				intent.setClass(SetBackgroundActivity.this,
						MainActivity.class);
				startActivity(intent);
				//结束当前Activity
				SetBackgroundActivity.this.finish();
				overridePendingTransition(R.anim.push_below_in,R.anim.push_below_out);
			}
		}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.push_below_in,R.anim.push_below_out);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_below_in,R.anim.push_below_out);
			return true;
		}else if (keyCode == event.KEYCODE_MENU) {
			findViewById(R.id.bg_button).setVisibility(View.VISIBLE);
		}
		return super.onKeyDown(keyCode, event);
	}
}