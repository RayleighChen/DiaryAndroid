package com.sebox.diary.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sebox.diary.R;
import com.sebox.diary.adapter.DiaryAdapter;
import com.sebox.diary.db.DiaryDao;
import com.sebox.diary.model.Diary;
import com.sebox.diary.utils.FileOperate;
import com.sebox.diary.utils.TimeString;

public class LookDiaryActivity extends Activity {
	private ImageView back = null;
	private ListView diaryInfo = null;
	private DiaryDao diaryDao = null;
	private List<Diary> diaries = null;
	private BroadcastReceiver myReceiver = null;
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
	
	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diary_info);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		diaryDao = new DiaryDao(this);
		diaries = new ArrayList<Diary>();
		back = (ImageView) this.findViewById(R.id.back_look_diary);
		diaryInfo = (ListView) this.findViewById(R.id.diary_info_list);
		refresh();
		myDialog();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_below_in,
						R.anim.push_below_out);
			}
		});

		myReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				refresh();
			}
		};
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction("com.android.receivemsg");
		iFilter.setPriority(Integer.MAX_VALUE);
		// ע��㲥������
		registerReceiver(myReceiver, iFilter);
		setBackground();
	}
	
	private void setBackground() {
		// �õ���ǰ����
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.diary_info_layout);
		// �õ�id,�˴�id�������ñ�����������ģ��˴��ݲ�����
		int id = preferences.getInt("id", 0);
		if (id == 0) {// id=0˵���ǳ�ʼ��ʱ�ı���
			// ���ñ�������
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 1) {// id=1˵���û�ѡ���˵�һ��ͼƬ
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 2) {// id=2˵���û�ѡ���˵ڶ���ͼƬ
			layout.setBackgroundResource(R.drawable.spring);
		} else if (id == 3) {// id=3˵���û�ѡ���˵�����ͼƬ
			layout.setBackgroundResource(R.drawable.summer);
		} else if (id == 4) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.autumn);
		} else if (id == 5) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.winter);
		}
	}
	
	private void refresh() {
		diaryDao.query(diaries);
		DiaryAdapter adapter = new DiaryAdapter(this, diaries);
		diaryInfo.setAdapter(adapter);
		diaryInfo.setVerticalScrollBarEnabled(true);
		diaryInfo.setOnItemClickListener(new ItemClickListener());
		diaryInfo.setOnItemLongClickListener(new ItemLongPressListener());
		diaryInfo.setSelection(0);

	}

	private void myDialog() {
		if (diaries.isEmpty() || diaries.size() < 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.prompt));
			builder.setIcon(R.drawable.prompt);
			builder.setMessage(getString(R.string.is_add_diary));
			builder.setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setClass(LookDiaryActivity.this,
									AddDiaryActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.push_up_in,
									R.anim.push_up_out);
							finish();
							unregisterReceiver(myReceiver);
						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
							overridePendingTransition(R.anim.push_below_in,
									R.anim.push_below_out);
							unregisterReceiver(myReceiver);
						}
					});
			builder.setCancelable(false);
			builder.create().show();
		}
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(LookDiaryActivity.this,
					DetailDiaryInfoActivity.class);
			intent.putExtra("title", diaries.get(position).getDiaryTitle());
			intent.putExtra("info", diaries.get(position).getDiaryInfo());
			intent.putExtra("date", diaries.get(position).getDate());
			intent.putExtra("week", diaries.get(position).getWeek());
			intent.putExtra("weather", diaries.get(position).getWeather());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	class ItemLongPressListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v,
				final int position, long id) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LookDiaryActivity.this);
			builder.setTitle(getString(R.string.op));
			builder.setIcon(R.drawable.op);
			builder.setItems(new String[] { getString(R.string.share),
					getString(R.string.transmit), getString(R.string.delete),
					getString(R.string.backups), getString(R.string.empty) },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(final DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							if (which == 0) {
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.putExtra(Intent.EXTRA_SUBJECT,
										getString(R.string.share));
								intent.putExtra(Intent.EXTRA_TITLE, diaries
										.get(position).getDiaryTitle());
								intent.putExtra(Intent.EXTRA_TEXT,
										diaries.get(position).getDiaryInfo());
								intent.setType("text/plain");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								LookDiaryActivity.this.startActivity(Intent
										.createChooser(intent, getTitle()));
								dialog.dismiss();
							} else if (which == 1) {
								Uri smsToUri = Uri.parse("smsto:");
								Intent sendIntent = new Intent(
										Intent.ACTION_VIEW, smsToUri);
								sendIntent.putExtra("sms_body",
										diaries.get(position).getDiaryInfo());
								sendIntent.setType("vnd.android-dir/mms-sms");
								LookDiaryActivity.this
										.startActivity(sendIntent);
								dialog.dismiss();
							} else if (which == 2) {
								dialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(
										LookDiaryActivity.this);
								builder.setMessage(getString(R.string.delete_sure));
								builder.setTitle(getString(R.string.delete));
								builder.setIcon(getResources().getDrawable(
										R.drawable.delete));
								builder.setPositiveButton(
										getString(R.string.ok),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int which) {
												// TODO Auto-generated method
												// stub
												diaryDao.delete(diaries.get(
														position).getId());
												System.out.println("id ->"
														+ diaries.get(position)
																.getId());
												refresh();
												Toast.makeText(
														LookDiaryActivity.this,
														getString(R.string.delete_over),
														Toast.LENGTH_SHORT)
														.show();
												dialogInterface.cancel();
												Intent intent = new Intent();
												intent.setAction("com.android.info.delete");
												LookDiaryActivity.this
														.sendBroadcast(intent);
											}
										});
								builder.setNegativeButton(
										getString(R.string.cancel),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int which) {
												// TODO Auto-generated method
												// stub
												dialogInterface.dismiss();
											}
										});
								builder.create().show();
							} else if (which == 3) {
								dialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(
										LookDiaryActivity.this);
								builder.setTitle(getString(R.string.backups));
								builder.setMessage(getString(R.string.is_backups));
								builder.setIcon(getResources().getDrawable(
										R.drawable.backups));
								builder.setPositiveButton(
										getString(R.string.ok),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int which) {
												// TODO Auto-generated method
												// stub
												String fileName = TimeString
														.getTime();
												String info = diaries.get(
														position)
														.getDiaryTitle()
														+ "\n"
														+ diaries.get(position)
																.getDate()
														+ "  "
														+ diaries.get(position)
																.getWeek()
														+ " "
														+ diaries.get(position)
																.getWeather()
														+ "\n"
														+ diaries.get(position)
																.getDiaryInfo();
												if (FileOperate.wirteData(
														fileName, info)) {
													Toast.makeText(
															LookDiaryActivity.this,
															getString(R.string.save_success_sd),
															0).show();
												} else {
													Toast.makeText(
															LookDiaryActivity.this,
															getString(R.string.save_failed_sd),
															0).show();
												}

											}
										});
								builder.setNegativeButton(
										getString(R.string.cancel),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												dialog.dismiss();
											}
										});
								builder.create().show();
							} else if (which == 4) {
								dialog.dismiss();
								AlertDialog.Builder builder = new AlertDialog.Builder(
										LookDiaryActivity.this);
								builder.setMessage(getString(R.string.empty_all));
								builder.setTitle(getString(R.string.emptyall));
								builder.setIcon(getResources().getDrawable(
										R.drawable.delete));
								builder.setPositiveButton(
										getString(R.string.ok),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int which) {
												// TODO Auto-generated method
												// stub
												diaryDao.deleteAll();
												refresh();
												Toast.makeText(
														LookDiaryActivity.this,
														getString(R.string.emptyed),
														Toast.LENGTH_SHORT)
														.show();
												Intent intent = new Intent();
												intent.setAction("com.android.info.delete");
												LookDiaryActivity.this
														.sendBroadcast(intent);
											}
										});
								builder.setNegativeButton(
										getString(R.string.cancel),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialogInterface,
													int which) {
												// TODO Auto-generated method
												// stub
												dialogInterface.dismiss();
											}
										});
								builder.create().show();
							}
						}
					});
			builder.create().show();
			return true;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		unregisterReceiver(myReceiver);
		overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
	}
}
