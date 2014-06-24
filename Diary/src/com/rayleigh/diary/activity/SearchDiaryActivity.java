package com.rayleigh.diary.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rayleigh.diary.R;
import com.rayleigh.diary.adapter.DiaryAdapter;
import com.rayleigh.diary.db.DiaryDao;
import com.rayleigh.diary.model.Diary;
import com.rayleigh.diary.utils.FileOperate;
import com.rayleigh.diary.utils.TimeString;

public class SearchDiaryActivity extends Activity {
	private ImageView back = null;
	private EditText search = null;
	private ListView searchInfo = null;
	private DiaryDao diaryDao = null;
	private List<Diary> diaries = null;
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
	private void init(){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_diary_info);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		diaryDao = new DiaryDao(this);
		diaries = new ArrayList<Diary>();
		search = (EditText)this.findViewById(R.id.search_edit);
		back = (ImageView)this.findViewById(R.id.back_search_diary);
		searchInfo = (ListView)this.findViewById(R.id.search_diary_info_list);
		search.addTextChangedListener(new SearchInfoListener());
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
			}
		});
		setBackground();
	}
	private void setBackground() {
		// 得到当前布局
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.search_diary_layout);
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
		overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
	}
	
	class SearchInfoListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (!search.getText().toString().trim().equals("")) {
				refresh();
			}else {
				diaries.clear();
				// �������뷨 
				InputMethodManager manager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
				// ��ʾ�����������뷨 
				manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	private void refresh(){
		diaryDao.dimSearch(search,diaries);
		DiaryAdapter adapter = new DiaryAdapter(this, diaries);
		searchInfo.setAdapter(adapter);
		searchInfo.setVerticalScrollBarEnabled(true);
		searchInfo.setOnItemClickListener(new ItemClickListener());
		searchInfo.setOnItemLongClickListener(new ItemLongPressListener());
		searchInfo.setSelection(0);
	}
	
	class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(SearchDiaryActivity.this, DetailDiaryInfoActivity.class);
			intent.putExtra("title", diaries.get(position).getDiaryTitle());
			intent.putExtra("info", diaries.get(position).getDiaryInfo());
			intent.putExtra("date", diaries.get(position).getDate());
			intent.putExtra("week", diaries.get(position).getWeek());
			intent.putExtra("weather", diaries.get(position).getWeather());
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}
	}
	
	class ItemLongPressListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, final int position,
				long id) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(SearchDiaryActivity.this);
			builder.setTitle(getString(R.string.op));
			builder.setIcon(R.drawable.op);
builder.setItems(new String[]{getString(R.string.share),getString(R.string.transmit),
		getString(R.string.delete),getString(R.string.backups),getString(R.string.empty)}, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(final DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (which == 0) {
						Intent intent=new Intent(Intent.ACTION_SEND);
						intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
						intent.putExtra(Intent.EXTRA_TITLE, diaries.get(position).getDiaryTitle());
						intent.putExtra(Intent.EXTRA_TEXT, diaries.get(position).getDiaryInfo());
						intent.setType("text/plain");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						SearchDiaryActivity.this.startActivity(Intent.createChooser(intent, getTitle()));
				        dialog.dismiss();
					}else if (which == 1) {
						Uri smsToUri = Uri.parse("smsto:"); 
						Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri); 
						sendIntent.putExtra("sms_body",diaries.get(position).getDiaryInfo()); 
						sendIntent.setType("vnd.android-dir/mms-sms");
						SearchDiaryActivity.this.startActivity(sendIntent);
						dialog.dismiss();
					}else if (which == 2) {
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(SearchDiaryActivity.this);
						builder.setMessage(getString(R.string.delete_sure));
						builder.setTitle(getString(R.string.delete));
						builder.setIcon(getResources().getDrawable(R.drawable.delete));
						builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO Auto-generated method stub
								diaryDao.delete(diaries.get(position).getId());
								System.out.println("id ->"+diaries.get(position).getId());
								refresh();
								Toast.makeText(SearchDiaryActivity.this, getString(R.string.delete_over), Toast.LENGTH_SHORT).show();
								dialogInterface.cancel();
								Intent intent = new Intent();
								intent.setAction("com.android.info.delete");
								SearchDiaryActivity.this.sendBroadcast(intent);
							}
						});
						builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO Auto-generated method stub
								dialogInterface.dismiss();
							}
						});
						builder.create().show();
					}
					else if (which == 3) {
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(SearchDiaryActivity.this);
						builder.setTitle(getString(R.string.backups));
						builder.setMessage(getString(R.string.is_backups));
						builder.setIcon(getResources().getDrawable(R.drawable.backups));
						builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO Auto-generated method stub
								String fileName = TimeString.getTime();
								String info = diaries.get(position).getDiaryTitle()+"\n"+
								diaries.get(position).getDate()+"  "+diaries.get(position).getWeek()+" "+
										diaries.get(position).getWeather()+"\n"+
								diaries.get(position).getDiaryInfo();
								if (FileOperate.wirteData(
										fileName, info)) {
									Toast.makeText(
											SearchDiaryActivity.this,
											getString(R.string.save_success_sd),
											0).show();
								} else {
									Toast.makeText(
											SearchDiaryActivity.this,
											getString(R.string.save_failed_sd),
											0).show();
								}

							}
						});
						builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
					else if (which == 4) {
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(SearchDiaryActivity.this);
						builder.setMessage(getString(R.string.empty_all));
						builder.setTitle(getString(R.string.emptyall));
						builder.setIcon(getResources().getDrawable(R.drawable.delete));
						builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO Auto-generated method stub
								diaryDao.deleteAll();
								refresh();
								Toast.makeText(SearchDiaryActivity.this, "��Ϣ�����", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent();
								intent.setAction("com.android.info.delete");
								SearchDiaryActivity.this.sendBroadcast(intent);
							}
						});
						builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int which) {
								// TODO Auto-generated method stub
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
}
