package com.rayleigh.diary.activity;

import com.rayleigh.diary.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity {

	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private LinearLayout animLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diary_welecome);
		init();
	}

	private void init() {
		animLayout = (LinearLayout) this.findViewById(R.id.animLayout);
		leftLayout = (LinearLayout) this.findViewById(R.id.leftLayout);
		rightLayout = (LinearLayout) this.findViewById(R.id.rightLayout);

		animLayout.setBackgroundResource(R.drawable.main_bg);
		// 加载开门动画
		Animation leftOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_right);
		// 左布局向左移动
		leftLayout.setAnimation(leftOutAnimation);
		// 右布局向右移动
		rightLayout.setAnimation(rightOutAnimation);
		// 设置动画监听器
		leftOutAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// 结束动画时，隐藏布局
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, AccessActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
				WelcomeActivity.this.finish();
			}
		});
	}
}
