package com.sebox.diary.activity;

import java.lang.reflect.Field;

import com.sebox.diary.R;
import com.sebox.diary.utils.MyAnimations;
import com.sebox.diary.view.MyWin8Button;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private MyWin8Button addDiary = null;
	private MyWin8Button lookDiary = null;
	private MyWin8Button searchDiary = null;
	private MyWin8Button passDiary = null;
	private MyWin8Button exitDiary = null;
	private RelativeLayout composerButtonsShowHideButton = null;
	private ImageView composerButtonsShowHideButtonIcon = null;
	private boolean areButtonsShowing = false;
	private RelativeLayout composerButtonsWrapper = null;
	private ImageButton btn_skin = null;
	private SharedPreferences sp_skin;
	private SharedPreferences sp = null;
	private boolean isSet = false;
	private String pass = null;
	private EditText checkPass = null;
	Class<?>[] classes = { AdviceActivity.class, SetRemindActivity.class,
			SetBackgroundActivity.class, HelpActivity.class,
			AboutActivity.class };
	private SharedPreferences preferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkPass();
	}

	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diary_view);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		addDiary = (MyWin8Button) this.findViewById(R.id.diary_add);
		lookDiary = (MyWin8Button) this.findViewById(R.id.diary_look);
		searchDiary = (MyWin8Button) this.findViewById(R.id.diary_search);
		passDiary = (MyWin8Button) this.findViewById(R.id.diary_pass);
		exitDiary = (MyWin8Button) this.findViewById(R.id.diary_exit);
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		composerButtonsShowHideButton = (RelativeLayout) this
				.findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) this
				.findViewById(R.id.composer_buttons_show_hide_button_icon);
		composerButtonsWrapper = (RelativeLayout) this
				.findViewById(R.id.composer_buttons_wrapper);
		btn_skin = (ImageButton) findViewById(R.id.composer_button_sleep);
		sp_skin = getSharedPreferences("skin", MODE_PRIVATE);
		btn_skin.setBackgroundResource(sp_skin.getBoolean("id", true) ? R.drawable.sleep
				: R.drawable.sun);

		addDiary.setOnClickListener(new MyListener());
		lookDiary.setOnClickListener(new MyListener());
		searchDiary.setOnClickListener(new MyListener());
		passDiary.setOnClickListener(new MyListener());
		exitDiary.setOnClickListener(new MyListener());
		
		composerButtonsShowHideButton.setOnClickListener(new MyListener());
		initCompserButton();
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
		RelativeLayout layout = (RelativeLayout) this
				.findViewById(R.id.diary_view_layout);
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

	class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.diary_add:
				Intent intent = new Intent(MainActivity.this,
						AddDiaryActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.diary_look:
				intent = new Intent(MainActivity.this, LookDiaryActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.diary_search:
				intent = new Intent(MainActivity.this,
						SearchDiaryActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.diary_pass:
				intent = new Intent(MainActivity.this,
						SetPasswordActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				break;
			case R.id.diary_exit:
				exitDialog();
				break;
			case R.id.composer_buttons_show_hide_button:
				if (!areButtonsShowing) {
					composerButtonsShowHideButtonIcon.setAnimation(MyAnimations
							.getRotateAnimation(0, -270, 300));
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
				} else {
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(
									-270, 0, 300));
					MyAnimations
							.startAnimationsOut(composerButtonsWrapper, 300);
				}
				areButtonsShowing = !areButtonsShowing;
				break;

			default:
				break;
			}
		}
	}

	private void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.quit);
		builder.setTitle(getString(R.string.quit));
		builder.setMessage(getString(R.string.is_quit));
		builder.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		builder.setCancelable(false);
		builder.create().show();
	}

	private void initCompserButton() {
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final int position = i;
			composerButtonsWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (position == 5) {
								sp_skin.edit()
										.putBoolean("id",
												!sp_skin.getBoolean("id", true))
										.commit();
								btn_skin.setBackgroundResource(sp_skin
										.getBoolean("id", true) ? R.drawable.sleep
										: R.drawable.sun);
								Toast.makeText(
										MainActivity.this,
										!sp_skin.getBoolean("id", true) ? getString(R.string.sleep_on)
												: getString(R.string.sleep_up),
										3000).show();
							} else {
								Intent intent = new Intent(MainActivity.this,
										classes[position]);
								startActivity(intent);
								overridePendingTransition(R.anim.push_up_in,
										R.anim.push_up_out);
							}
						}
					});
		}
		composerButtonsShowHideButton.startAnimation(MyAnimations
				.getRotateAnimation(0, 360, 200));
	}

	private void checkPass() {
		try {
			sp = getSharedPreferences("pass", Context.MODE_PRIVATE);
			String passWay = sp.getString("passway", null);
			if (passWay.equals("digitalpass")) {
				isSet = sp.getBoolean("isSet", false);
				pass = sp.getString("password", null);
				if (isSet) {
					LayoutInflater factory = LayoutInflater
							.from(MainActivity.this);
					final View textEntry = factory.inflate(
							R.layout.confirm_pass, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(this)
							.setTitle(getString(R.string.pass_con_title))
							.setIcon(
									getResources().getDrawable(
											android.R.drawable.ic_lock_lock))
							.setView(textEntry)
							.setCancelable(false)
							.setPositiveButton(getString(R.string.ok),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											checkPass = (EditText) textEntry
													.findViewById(R.id.check_pass);
											if (checkPass.getText().toString()
													.trim().equals(pass)) {
												try {
													Field field = dialog
															.getClass()
															.getSuperclass()
															.getDeclaredField(
																	"mShowing");
													field.setAccessible(true);
													field.set(dialog, true);
												} catch (Exception e) {
													e.printStackTrace();
												}
												dialog.dismiss();
											} else {
												try {
													Field field = dialog
															.getClass()
															.getSuperclass()
															.getDeclaredField(
																	"mShowing");
													field.setAccessible(true);
													field.set(dialog, false);
												} catch (Exception e) {
													e.printStackTrace();
												}
												Toast.makeText(
														MainActivity.this,
														R.string.wrong_pass,
														Toast.LENGTH_LONG)
														.show();
												checkPass.setText("");
											}
										}
									});
					builder.create().show();
				}
			}
		} catch (Exception e) {
			Log.e("MainActivity", e.toString());
		}
	}
}
