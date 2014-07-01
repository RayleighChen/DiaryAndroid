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
		//���ô����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//�õ�����
		setContentView(R.layout.background);
		//ʵ�����ƶ�������ʵ�����ƻ���
		detector = new GestureDetector(this);
		//ͨ��findViewById�����õ�Flipper�ؼ�
		flipper = (ViewFlipper) this.findViewById(R.id.flipper);
		//��flipper������ķ�ͼƬ
		flipper.addView(getImageView(R.drawable.diary_view_bg));
		flipper.addView(getImageView(R.drawable.spring));
		flipper.addView(getImageView(R.drawable.summer));
		flipper.addView(getImageView(R.drawable.autumn));
		flipper.addView(getImageView(R.drawable.winter));
		//�õ����ð�ť
		setBackground = (Button) this.findViewById(R.id.backround_set);
		//�õ�ȡ��ť
		cancel = (Button) this.findViewById(R.id.backround_cancel);
		//���ð�ť������
		setBackground.setOnClickListener(new SetBackgroundListener());
		//ȡ��ť������
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
		if (e1.getX() - e2.getX() > 120) {// ���һ���
			if (flipper.getDisplayedChild() == 4) {
				//�������Ҳ���ѭ������
				flipper.stopFlipping();
				return false;
			} else {
				//���ý��붯��
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_in));
				//������ȥ����
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_left_out));
				//��ʾ��һ��ͼƬ
				flipper.showNext();
			}
			//���ð�ť���ɼ�
			findViewById(R.id.bg_button).setVisibility(View.GONE);
			//flag++ ��һ��Ĭ��Ϊ1 �˺����һ���һ�μ�+1
			flag++;

		} else if (e2.getX() - e1.getX() > 120) {// ���󻬶�
			if (flipper.getDisplayedChild() == 0) {
				//����������ѭ������
				flipper.stopFlipping();
				return false;
			} else {
				flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.push_right_out));
				//��ʾ��һ��ͼƬ
				flipper.showPrevious();
			}
			//���ð�ť���ɼ�
			findViewById(R.id.bg_button).setVisibility(View.GONE);
			//flag-- ��һ��Ĭ��Ϊ1 �˺����һ���һ�μ�-1
			flag--;
		}
		return false;
	}
	//����ͼƬ
		public void onLongPress(MotionEvent e) {
			//��������ť���ÿɼ�
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
				//����������ť
				findViewById(R.id.bg_button).setVisibility(View.GONE);
			}

		}

		class SetBackgroundListener implements OnClickListener {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�õ�һ��SharedPreferences�������Ա��浱ǰͼƬ��id
				SharedPreferences preferences = getSharedPreferences("image",
						MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putInt("id", flag);
				editor.commit();//�ύ�������ܱ���ɹ�
				//������ð�ť����תActivity��ʵ��
				Intent intent = new Intent();
				intent.setClass(SetBackgroundActivity.this,
						MainActivity.class);
				startActivity(intent);
				//����ǰActivity
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
